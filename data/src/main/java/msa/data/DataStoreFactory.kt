package msa.data

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.serializationConverterFactory
import kotlinx.serialization.json.JSON
import msa.data.inmemory.InMemoryDataStore
import msa.data.local.LocalDataStore
import msa.data.local.MessageDatabase
import msa.data.remote.ChatBotApi
import msa.data.remote.RemoteDataStore
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Created by Abhi Muktheeswarar.
 */

class DataStoreFactory(
    applicationContext: Context,
    baseUrl: String = "http://www.personalityforge.com/api/"
) {

    val inMemoryDataStore: InMemoryDataStore = InMemoryDataStore()
    val localDataStore: LocalDataStore
    val remoteDataStore: RemoteDataStore

    init {

        val messageDatabase = Room.databaseBuilder(
            applicationContext,
            MessageDatabase::class.java, "messageDb"
        ).build()

        localDataStore = LocalDataStore(messageDatabase.messageDao())

        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BASIC
            )
        ).build()

        val chatBotApi = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(serializationConverterFactory(MediaType.get("application/json"), JSON))
            .baseUrl(baseUrl)
            .build()
            .create(ChatBotApi::class.java)

        remoteDataStore = RemoteDataStore(chatBotApi)
    }
}