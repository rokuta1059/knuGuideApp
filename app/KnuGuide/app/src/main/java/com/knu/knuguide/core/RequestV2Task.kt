package com.knu.knuguide.core

import android.app.Activity
import android.os.AsyncTask
import com.google.cloud.dialogflow.v2beta1.*
import com.knu.knuguide.view.main.MainActivity

class RequestV2Task: AsyncTask<Void, Void, DetectIntentResponse> {

    private val activity: Activity
    private val session: SessionName
    private val sessionsClient: SessionsClient
    private val queryInput: QueryInput

    constructor(activity: Activity, sessionName: SessionName, sessionsClient: SessionsClient, queryInput: QueryInput) {
        this.activity = activity
        this.session = sessionName
        this.sessionsClient = sessionsClient
        this.queryInput = queryInput
    }

    override fun doInBackground(vararg params: Void?): DetectIntentResponse? {
        try {
            val detectIntentRequest =
                DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build()
            return sessionsClient.detectIntent(detectIntentRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(response: DetectIntentResponse?) {
        (activity as MainActivity).callbackV2(response)
    }
}