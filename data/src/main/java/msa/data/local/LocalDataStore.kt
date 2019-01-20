package msa.data.local

import io.reactivex.Observable
import msa.data.inmemory.InMemoryDataStore
import msa.domain.entities.GetMessagesParams
import msa.domain.entities.Message
import msa.domain.entities.MessageStatus
import msa.domain.entities.SendMessageParams
import java.util.*

/**
 * Created by Abhi Muktheeswarar.
 */

class LocalDataStore(private val messageDao: MessageDao) {

    fun getMessages(getMessagesParams: GetMessagesParams): Observable<List<Message>> {
        return messageDao.getMessages(getMessagesParams.chatBotId).map { TODO() }
    }

    fun insertMessage(sendMessageParams: SendMessageParams, messageStatus: MessageStatus) {

        val messageEntity = MessageEntity(
            chatBotId = sendMessageParams.chatBotId,
            senderId = InMemoryDataStore.USER_ID,
            receiverId = sendMessageParams.chatBotId,
            message = sendMessageParams.message,
            emotion = "",
            date = Date(),
            messageStatus = messageStatus
        )

        messageDao.insertMessage(messageEntity)
    }

    fun insertMessage(message: Message) {

        val messageEntity = MessageEntity(
            chatBotId = message.senderId,
            senderId = message.senderId,
            receiverId = message.receiverId,
            message = message.message,
            emotion = "",
            date = Date(),
            messageStatus = message.messageStatus
        )

        messageDao.insertMessage(messageEntity)
    }
}