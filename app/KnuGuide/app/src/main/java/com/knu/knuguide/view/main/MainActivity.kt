package com.knu.knuguide.view.main

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2beta1.*
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.core.PrefService
import com.knu.knuguide.core.RequestV2Task
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.support.FastClickPreventer
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.WebViewActivity
import com.knu.knuguide.view.adapter.AnnouncementAdapter
import com.knu.knuguide.view.adapter.decor.PreviewAnnouncementDecor
import com.knu.knuguide.view.announcement.AnnouncementActivity
import com.knu.knuguide.view.bus.BusActivity
import com.knu.knuguide.view.calendar.CalendarActivity
import com.knu.knuguide.view.department.DepartmentActivity
import com.knu.knuguide.view.search.SearchActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.knu_appbar.*
import kotlinx.android.synthetic.main.preview_announcement.*
import kotlinx.android.synthetic.main.preview_announcement.recyclerView
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : KNUActivity(), KNUAdapterListener, PrefService.PrefChangeListener, TextToSpeech.OnInitListener {
    private val compositeDisposable = CompositeDisposable()
    private val fastClickPreventer = FastClickPreventer()
    private var items = ArrayList<KNUData>()

    private lateinit var favoriteId: String

    private lateinit var textToSpeech: TextToSpeech

    private val uuid = UUID.randomUUID().toString()

    // Dialogflow V2
    private lateinit var sessionsClient: SessionsClient
    private lateinit var session: SessionName

    // TODO : 음성 인식 활성화
    //  -> initDialogFlow() Annotation 해제
    //  -> fab_speech onClickListenr Annotation 해제
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textToSpeech = TextToSpeech(this, this)

        //initDialogFlow()

        // menu 클릭 시 Navigation View 열기
        appbar_menu.setOnClickListener {
            if (fastClickPreventer.isClickable())
                drawerLayout.openDrawer(nav_view)
        }

        nav_view.setNavigationItemSelectedListener { m ->
            drawerLayout.closeDrawers()
            when (m.itemId) {
                R.id.announcement -> {
                    navigateTo(AnnouncementActivity::class.java, null)
                }
                R.id.calender -> {
                    navigateTo(CalendarActivity::class.java, null)
                }
                R.id.department -> {
                    val bundle = Bundle()
                    bundle.putBoolean(DepartmentActivity.KEY_DEPARTMENT_INFO, true)
                    navigateTo(SearchActivity::class.java, bundle)
                }
                R.id.bus -> {
                    navigateTo(BusActivity::class.java, null)
                }
            }
            false
        }

        more.setOnClickListener {
            if (fastClickPreventer.isClickable())
                navigateTo(AnnouncementActivity::class.java, null)
        }

//        fab_speech.setOnClickListener {
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
//            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해주세요")
//
//            try {
//                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
//            } catch (e: ActivityNotFoundException) {
//                Toast.makeText(applicationContext, "지원할 수 있는 기능이 없습니다.", Toast.LENGTH_SHORT).show();
//            }
//        }

        favoriteId = PrefService.instance()!!.getFavoriteId()

        // 공지사항 아이템 추가
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AnnouncementAdapter(this, items, this)
        recyclerView.addItemDecoration(PreviewAnnouncementDecor(this, 2f))

        PrefService.instance()!!.register(this.javaClass.simpleName, this)

        getNotice()
    }

//    private fun initDialogFlow() {
//        val stream: InputStream = resources.openRawResource(R.raw.credential_file)
//        val credentials = GoogleCredentials.fromStream(stream)
//        val projectId = (credentials as ServiceAccountCredentials).projectId
//
//        val settingsBuilder: SessionsSettings.Builder = SessionsSettings.newBuilder()
//        val sessionsSettings: SessionsSettings =
//            settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials))
//                .build()
//        sessionsClient = SessionsClient.create(sessionsSettings)
//        session = SessionName.of(projectId, uuid)
//    }

    private fun getNotice() {
        if (favoriteId.isEmpty()) {
            empty.visibility = View.VISIBLE
            items.clear()

            recyclerView.adapter!!.notifyDataSetChanged()
            return
        }
        empty.visibility = View.GONE

        // 공지사항 데이터 불러오기
        compositeDisposable.add(KNUService.instance()!!.getNotice(favoriteId).subscribeWith(object : DisposableSingleObserver<List<Announcement>>() {
            override fun onSuccess(list: List<Announcement>) {
                var cnt = 0
                items.clear()
                for (item in list) {
                    item.type = Announcement.Type.PREVIEW // 미리 보기 형식 지정
                    items.add(item)

                    cnt++
                    if (cnt > 4)
                        break
                }
                recyclerView.adapter!!.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Log.d("Error", e.message)
            }
        }))
    }

    override fun onAnnouncementClick(item: Announcement) {
        val args = Bundle()
        args.putSerializable(WebViewActivity.KEY_DATA, item.link)
        navigateTo(WebViewActivity::class.java, args)
    }

    override fun onChangePref(key: String, value: Any) {
        if (key == PrefService.DEPARTMENT_FAVORITE_KEY) {
            favoriteId = value as String

            getNotice()
        }
    }

    override fun onInit(status: Int) {
        if (status === TextToSpeech.SUCCESS) {
            // 작업 성공
            val language: Int = textToSpeech.setLanguage(Locale.KOREAN) // 언어 설정
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어 데이터가 없거나, 지원하지 않는경우
                fab_speech.isEnabled = false
                Toast.makeText(this, "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 준비 완료
                fab_speech.isEnabled = true
            }
        } else {
            // 작업 실패
            Toast.makeText(this, "TTS 작업에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage(msg: String) {
        // Java V2
        val queryInput: QueryInput = QueryInput.newBuilder()
            .setText(TextInput.newBuilder().setText(msg).setLanguageCode("ko-KR")).build()

        RequestV2Task(this@MainActivity, session, sessionsClient, queryInput).execute()
    }

    fun callbackV2(response: DetectIntentResponse?) {
        if (response != null) {
            // process aiResponse here
            val botReply: String = response.queryResult.fulfillmentText

            val st = StringTokenizer(botReply, " ")

            if (st.hasMoreTokens()) {
                val cmd = st.nextToken()

                when (cmd) {
                    "notice" -> navigateTo(AnnouncementActivity::class.java, null)
                    "bus" -> navigateTo(BusActivity::class.java, null)
                    "schedule" -> navigateTo(CalendarActivity::class.java, null)
                    "department" -> {
                        val bundle = Bundle().apply {
                            putString(AnnouncementActivity.KEY_DEPARTMENT, st.nextToken())
                        }
                        navigateTo(DepartmentActivity::class.java, bundle)
                    }
                    "search" -> {
                        val bundle = Bundle()
                        bundle.putBoolean(DepartmentActivity.KEY_DEPARTMENT_INFO, true)
                        navigateTo(SearchActivity::class.java, bundle)
                    }
                    else -> Snackbar.make(rootLayout, botReply, Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            Snackbar.make(rootLayout, "There was some communication issue.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
        textToSpeech?.apply {
            stop()
            shutdown()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    if (result.isNotEmpty()) {
                        sendMessage(result[0])
                    }
                }
            }
        }
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "MainActivity"

        const val REQ_CODE_SPEECH_INPUT = 100
    }
}
