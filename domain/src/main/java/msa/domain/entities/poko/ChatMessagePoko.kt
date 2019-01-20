package msa.domain.entities.poko

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessagePoko(
    val `data`: List<Any>,
    val errorMessage: String,
    val messagePoko: MessagePoko,
    val success: Int
)

@Serializable
data class MessagePoko(
    val chatBotID: Int,
    val chatBotName: String,
    val emotion: String,
    val message: String
)