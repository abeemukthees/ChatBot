package msa.chatbot.chatbots

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.*
import msa.chatbot.R
import msa.chatbot.base.BaseEpoxyHolder
import msa.chatbot.common.LoadingItemModel_
import msa.chatbot.loadUrl
import msa.domain.core.Action
import msa.domain.entities.ChatBot
import msa.domain.statemachine.ChatAction
import msa.domain.statemachine.ChatState

/**
 * Created by Abhi Muktheeswarar.
 */

class ChatBotsListController(private val itemActionListener: (action: Action) -> Unit) :
    TypedEpoxyController<ChatState>() {

    @AutoModel
    lateinit var loadingItemModel: LoadingItemModel_

    fun setChatState(data: ChatState) {
        setData(data)
    }

    override fun buildModels(data: ChatState) {

        data.chatBots?.forEach { chatBot ->

            chatBotItem {
                id(chatBot.id)
                chatBotId(chatBot.id)
                name(chatBot.name)
                imageUrl(chatBot.imageUrl)
                itemActionListener(itemActionListener)
            }
        }
    }
}


@EpoxyModelClass(layout = R.layout.item_chatbot)
abstract class ChatBotItemModel : EpoxyModelWithHolder<ChatBotItemModel.ChatBotItemViewHolder>() {

    @EpoxyAttribute
    open var chatBotId: Int = -1
    @EpoxyAttribute
    lateinit var name: String
    @EpoxyAttribute
    lateinit var imageUrl: String
    @EpoxyAttribute(hash = false)
    lateinit var itemActionListener: (action: Action) -> Unit

    override fun bind(holder: ChatBotItemViewHolder) {
        super.bind(holder)
        holder.imageView.loadUrl(imageUrl)
        holder.nameTextView.text = name

        holder.rootItemView.setOnClickListener {
            itemActionListener(
                ChatAction.GetMessagesAction(
                    ChatBot(
                        chatBotId, name, imageUrl
                    )
                )
            )
        }
    }

    class ChatBotItemViewHolder : BaseEpoxyHolder() {

        val rootItemView by bind<ViewGroup>(R.id.constraintLayout_item_chatBot)
        val nameTextView by bind<TextView>(R.id.text_chatBotName)
        val imageView by bind<ImageView>(R.id.image_chatBot)
    }
}






