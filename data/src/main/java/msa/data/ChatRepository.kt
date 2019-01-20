package msa.data

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.Single
import msa.domain.entities.*
import msa.domain.repository.Repository

/**
 * Created by Abhi Muktheeswarar.
 */

class ChatRepository(private val dataStoreFactory: DataStoreFactory) : Repository {

    override fun getChatBots(): Single<List<ChatBot>> {
        return dataStoreFactory.inMemoryDataStore.getChatBots()
    }

    override fun getMessages(getMessagesParams: GetMessagesParams): Observable<List<Message>> {
        return dataStoreFactory.localDataStore.getMessages(getMessagesParams)
    }

    override fun sendMessage(sendMessageParams: SendMessageParams): Observable<Result<Message, Exception>> {
        return dataStoreFactory.remoteDataStore.sendMessage(sendMessageParams).doOnNext {
            it.fold({ message ->
                dataStoreFactory.localDataStore.insertMessage(sendMessageParams, MessageStatus.SUCCESS)
                dataStoreFactory.localDataStore.insertMessage(message)
            },
                { dataStoreFactory.localDataStore.insertMessage(sendMessageParams, MessageStatus.FAILURE) })
        }.doOnError {

            dataStoreFactory.localDataStore.insertMessage(sendMessageParams, MessageStatus.FAILURE)
        }
    }
}