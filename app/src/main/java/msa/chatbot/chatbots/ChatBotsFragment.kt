package msa.chatbot.chatbots

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.android.synthetic.main.fragment_chatbots.*
import msa.chatbot.ChatViewModel
import msa.chatbot.R
import msa.chatbot.base.BaseEpoxyHolder
import msa.chatbot.base.BaseFragment
import msa.chatbot.common.loadingItem
import msa.chatbot.withModels
import msa.domain.core.Action
import msa.domain.entities.ChatBot
import msa.domain.statemachine.ChatAction
import msa.domain.statemachine.ChatState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by Abhi Muktheeswarar.
 */

class ChatBotsFragment : BaseFragment() {

    private val chatViewModel by sharedViewModel<ChatViewModel>()

    private val itemActionListener: (action: Action) -> Unit = {

        chatViewModel.input.accept(it)
        findNavController().navigate(R.id.chatFragment)
    }

    override fun getLayoutId(): Int = R.layout.fragment_chatbots

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyRecyclerView_chatBots.setItemSpacingDp(8)
    }

    override fun onStart() {
        super.onStart()
        chatViewModel.state.observe(this, Observer {
            setupViews(it as ChatState)
        })

        chatViewModel.input.accept(ChatAction.GetChatBotsAction)
    }

    private fun setupViews(state: ChatState) {

        epoxyRecyclerView_chatBots.withModels {

            if (state.loading) {


                loadingItem { id("loading") }

            }

            state.chatBots?.forEach { chatBot ->

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



