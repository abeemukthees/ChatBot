package msa.chatbot

import android.app.Application
import timber.log.Timber

/**
 * Created by Abhi Muktheeswarar.
 */

class ChatBotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}