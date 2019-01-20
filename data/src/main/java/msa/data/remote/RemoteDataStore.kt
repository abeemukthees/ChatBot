package msa.data.remote

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import msa.data.BuildConfig
import msa.data.inmemory.InMemoryDataStore
import msa.domain.entities.Message
import msa.domain.entities.MessageStatus
import msa.domain.entities.SendMessageParams
import java.util.*

/**
 * Created by Abhi Muktheeswarar.
 */

class RemoteDataStore(private val chatBotApi: ChatBotApi) {

    private val EXTERNAL_ID = "mSaChAt"

    fun sendMessage(sendMessageParams: SendMessageParams): Observable<Result<Message, Exception>> {
        return chatBotApi.sendMessage(
            apiKey = BuildConfig.API_KEY,
            message = sendMessageParams.message,
            chatBotId = sendMessageParams.chatBotId,
            externalId = EXTERNAL_ID
        ).map {

            if (it.success == 1) {

                val message = Message(
                    id = -1,
                    message = it.message.message,
                    senderId = sendMessageParams.chatBotId,
                    receiverId = InMemoryDataStore.USER_ID,
                    date = Date(),
                    messageStatus = MessageStatus.SUCCESS
                )

                Result.of(message)
            } else Result.error(Exception(it.errorMessage))
        }
    }
}