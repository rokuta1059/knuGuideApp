package com.knu.knuguide.view.speech

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2beta1.*
import com.knu.knuguide.R
import com.knu.knuguide.core.RequestV2Task
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_speech_test.*
import java.io.InputStream
import java.util.*

// todo : 1. TTS로 음성 읽기 -> 읽은 Text를 DialogFlow에 전달 -> 받은 Query에 대해서 명령 실행
class SpeechTestActivity : KNUActivity(), TextToSpeech.OnInitListener {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var textToSpeech: TextToSpeech

    private val uuid = UUID.randomUUID().toString()

    // Dialogflow V2
    private lateinit var sessionsClient: SessionsClient
    private lateinit var session: SessionName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_test)

        if (Build.VERSION.SDK_INT >= 23)
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO), CODE_PERMISSION)

        textToSpeech = TextToSpeech(this, this)

        /* 음성 인식 */
        bt_speech.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해주세요")

            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(applicationContext, "지원할 수 있는 기능이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        initV2Chatbot()
        sendMessage("컴공 알려줘")
    }

    private fun initV2Chatbot() {
        val stream: InputStream = resources.openRawResource(R.raw.credential_file)
        val credentials = GoogleCredentials.fromStream(stream)
        val projectId = (credentials as ServiceAccountCredentials).projectId

        val settingsBuilder: SessionsSettings.Builder = SessionsSettings.newBuilder()
        val sessionsSettings: SessionsSettings =
            settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build()
        sessionsClient = SessionsClient.create(sessionsSettings)
        session = SessionName.of(projectId, uuid)
    }

    private fun sendMessage(msg: String) {
        // Java V2
        val queryInput: QueryInput = QueryInput.newBuilder()
                    .setText(TextInput.newBuilder().setText(msg).setLanguageCode("ko-KR")).build()
        RequestV2Task(this@SpeechTestActivity, session, sessionsClient, queryInput).execute()
    }

    fun callbackV2(response: DetectIntentResponse?) {
        if (response != null) {
            // process aiResponse here
            val botReply: String = response.queryResult.fulfillmentText
            println(botReply)
        } else {
            println("There was some communication issue. Please Try again!")
        }
    }

    /* 음성 출력 */
    private fun speakOut(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    override fun onInit(status: Int) {
        if (status === TextToSpeech.SUCCESS) {
            // 작업 성공
            val language: Int = textToSpeech.setLanguage(Locale.KOREAN) // 언어 설정
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어 데이터가 없거나, 지원하지 않는경우
                bt_speech.isEnabled = false
                Toast.makeText(this, "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 준비 완료
                bt_speech.isEnabled = true
            }
        } else {
            // 작업 실패
            Toast.makeText(this, "TTS 작업에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    tv_result.text = result[0]

                    if (result[0] == "학사 일정")
                        navigateTo(CalendarActivity::class.java, null)
                }
            }
        }
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "SpeechTestActivity"

        const val CODE_PERMISSION = 1
        const val REQ_CODE_SPEECH_INPUT = 100
    }
}
