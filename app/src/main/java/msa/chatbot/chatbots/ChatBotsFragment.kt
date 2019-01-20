package msa.chatbot.chatbots

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_chatbots.*
import msa.chatbot.ChatViewModel
import msa.chatbot.R
import msa.chatbot.base.BaseFragment
import msa.domain.statemachine.ChatAction
import msa.domain.statemachine.ChatState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by Abhi Muktheeswarar.
 */

class ChatBotsFragment : BaseFragment() {

    private val chatViewModel by sharedViewModel<ChatViewModel>()

    private val chatBotsListController by lazy {
        ChatBotsListController {
            chatViewModel.input.accept(it)
            findNavController().navigate(R.id.chatFragment)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_chatbots

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyRecyclerView_chatBots.setItemSpacingDp(8)
        epoxyRecyclerView_chatBots.setController(chatBotsListController)

    }

    override fun onStart() {
        super.onStart()
        chatViewModel.state.observe(this, Observer {
            setupViews(it as ChatState)
        })

        chatViewModel.input.accept(ChatAction.GetChatBotsAction)
    }

    private fun setupViews(state: ChatState) {

        chatBotsListController.setChatState(state)
    }
}
