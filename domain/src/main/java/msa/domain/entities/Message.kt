package msa.domain.entities

import java.util.*

/**
 * Created by Abhi Muktheeswarar.
 */

data class Message(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val message: String,
    val date: Date,
    val messageStatus: MessageStatus
)