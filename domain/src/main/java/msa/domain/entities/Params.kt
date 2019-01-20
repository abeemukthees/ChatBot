package msa.domain.entities

/**
 * Created by Abhi Muktheeswarar.
 */
data class GetChatBotsParams(val getFromCache: Boolean = true)

data class GetMessagesParams(val chatBotId: Int)

data class SendMessageParams(val chatBotId: Int, val message: String)

