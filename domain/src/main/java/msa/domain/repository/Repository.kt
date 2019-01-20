package msa.domain.repository

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.Single
import msa.domain.entities.ChatBot
import msa.domain.entities.GetMessagesParams
import msa.domain.entities.Message
import msa.domain.entities.SendMessageParams

/**
 * Created by Abhi Muktheeswarar.
 */

interface Repository {

    fun getChatBots(): Single<List<ChatBot>>

    fun getMessages(getMessagesParams: GetMessagesParams): Observable<List<Message>>

    fun sendMessage(sendMessageParams: SendMessageParams): Observable<Result<Message, Exception>>
}