package msa.data.remote

import io.reactivex.Observable
import msa.domain.entities.poko.ChatMessagePoko
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Abhi Muktheeswarar.
 */

interface ChatBotApi {

    @GET("chat")
    fun sendMessage(
        @Query("apiKey") apiKey: String, @Query("message") message: String, @Query("chatBotID") chatBotId: Int, @Query(
            "externalID"
        ) externalId: String
    ): Observable<ChatMessagePoko>
}