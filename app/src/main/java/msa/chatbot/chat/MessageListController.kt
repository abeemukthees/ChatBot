package msa.chatbot.chat

import android.text.format.DateUtils
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.TypedEpoxyController
import msa.chatbot.R
import msa.chatbot.base.BaseEpoxyHolder
import msa.domain.entities.MessageStatus
import msa.domain.statemachine.ChatState
import java.util.*

/**
 * Created by Abhi Muktheeswarar.
 */

class MessageListController : TypedEpoxyController<ChatState>() {

    fun setChatState(data: ChatState) {
        setData(data)
    }

    override fun buildModels(data: ChatState) {

        data.messages?.forEach { message ->

            messageItem {
                id(message.id)
                messageId(message.id)
                message(message.message)
                date(message.date)
                status(message.messageStatus)
            }
        }
    }
}

@EpoxyModelClass(layout = R.layout.item_message)
abstract class MessageItemModel : EpoxyModelWithHolder<MessageItemModel.MessageItemViewHolder>() {

    @EpoxyAttribute
    open var messageId: Int = -1
    @EpoxyAttribute
    lateinit var message: String
    @EpoxyAttribute
    lateinit var date: Date
    @EpoxyAttribute
    lateinit var status: MessageStatus

    override fun bind(holder: MessageItemViewHolder) {
        super.bind(holder)
        holder.messageTextView.text = message
        holder.timeStampTextView.text = DateUtils.getRelativeTimeSpanString(date.time)
        holder.statusTextView.text = status.name
    }


    class MessageItemViewHolder : BaseEpoxyHolder() {

        val rootItemView by bind<ViewGroup>(R.id.constraintLayout_item_chat)
        val messageTextView by bind<TextView>(R.id.text_message)
        val timeStampTextView by bind<TextView>(R.id.text_timeStamp)
        val statusTextView by bind<TextView>(R.id.text_status)

    }
}



