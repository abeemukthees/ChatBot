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
        return messageDao.getMessages(getMessagesParams.chatBotId).map { messages -> messages.map { transform(it) } }
    }

    fun insertMessage(sendMessageParams: SendMessageParams, messageStatus: MessageStatus): Long {

        val messageEntity = MessageEntity(
            chatBotId = sendMessageParams.chatBotId,
            senderId = InMemoryDataStore.USER_ID,
            receiverId = sendMessageParams.chatBotId,
            message = sendMessageParams.message,
            emotion = "",
            date = Date(),
            messageStatus = messageStatus
        )

        return messageDao.insertMessage(messageEntity)
    }

    fun updateMessage(sendMessageParams: SendMessageParams, messageId: Int, messageStatus: MessageStatus) {

        val messageEntity = MessageEntity(
            id = messageId,
            chatBotId = sendMessageParams.chatBotId,
            senderId = InMemoryDataStore.USER_ID,
            receiverId = sendMessageParams.chatBotId,
            message = sendMessageParams.message,
            emotion = "",
            date = Date(),
            messageStatus = messageStatus
        )

        return messageDao.updateMessage(messageEntity)
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

    private fun transform(messageEntity: MessageEntity): Message {

        return Message(
            messageEntity.id!!,
            receiverId = messageEntity.receiverId,
            senderId = messageEntity.senderId,
            message = messageEntity.message,
            date = messageEntity.date,
            messageType = if (messageEntity.senderId == messageEntity.chatBotId) Message.MessageType.RECEIVED else Message.MessageType.SENT,
            messageStatus = messageEntity.messageStatus
        )
    }
}