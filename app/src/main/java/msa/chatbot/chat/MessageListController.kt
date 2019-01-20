package msa.chatbot.chat

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.TypedEpoxyController
import msa.chatbot.R
import msa.chatbot.base.BaseEpoxyHolder
import msa.domain.entities.Message
import msa.domain.entities.MessageStatus
import msa.domain.statemachine.ChatState
import java.text.DateFormat
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
                messageType(message.messageType)
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
    lateinit var messageType: Message.MessageType
    @EpoxyAttribute
    lateinit var status: MessageStatus

    override fun onViewAttachedToWindow(holder: MessageItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        val params = holder.rootItemView.layoutParams as FrameLayout.LayoutParams
        val large = holder.rootItemView.context.resources.getDimensionPixelSize(R.dimen.chat_margin_large)
        val small = holder.rootItemView.context.resources.getDimensionPixelSize(R.dimen.chat_margin_small)

        when (messageType) {

            Message.MessageType.SENT -> {

                params.gravity = Gravity.END
                params.setMargins(large, 0, small, 0)
                holder.rootItemView.background =
                        ContextCompat.getDrawable(holder.rootItemView.context, R.drawable.bg_sent_message)

            }

            Message.MessageType.RECEIVED -> {

                params.gravity = Gravity.START
                params.setMargins(small, 0, large, 0)
                holder.rootItemView.background =
                        ContextCompat.getDrawable(holder.rootItemView.context, R.drawable.bg_receive_message)
            }
        }

        holder.rootItemView.layoutParams = params


    }

    override fun bind(holder: MessageItemViewHolder) {
        super.bind(holder)
        holder.messageTextView.text = message
        holder.timeStampTextView.text = DateFormat.getDateTimeInstance().format(date)
        holder.statusTextView.text = status.name
    }


    class MessageItemViewHolder : BaseEpoxyHolder() {

        val rootItemView by bind<ViewGroup>(R.id.constraintLayout_item_chat)
        val messageTextView by bind<TextView>(R.id.text_message)
        val timeStampTextView by bind<TextView>(R.id.text_timeStamp)
        val statusTextView by bind<TextView>(R.id.text_status)

    }
}



