package msa.chatbot.chat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.DiffResult
import kotlinx.android.synthetic.main.fragment_chat.*
import msa.chatbot.ChatViewModel
import msa.chatbot.base.BaseFragment
import msa.domain.entities.SendMessageParams
import msa.domain.statemachine.ChatAction
import msa.domain.statemachine.ChatState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * Created by Abhi Muktheeswarar.
 */

class ChatFragment : BaseFragment() {

    private val chatViewModel by sharedViewModel<ChatViewModel>()

    private val messageListController by lazy { MessageListController() }


    override fun getLayoutId(): Int = msa.chatbot.R.layout.fragment_chat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        linearLayoutManager.stackFromEnd = true
        epoxyRecyclerView_messages.layoutManager = linearLayoutManager
        epoxyRecyclerView_messages.setItemSpacingDp(8)
        messageListController.addModelBuildListener { result: DiffResult ->

            epoxyRecyclerView_messages.smoothScrollToPosition(0)


        }
        epoxyRecyclerView_messages.setController(messageListController)
        fab_send.setOnClickListener {

            val message = edit_message.text.toString()
            val sendMessageParams = SendMessageParams(message = message)
            chatViewModel.input.accept(ChatAction.SendMessageAction(sendMessageParams))
            edit_message.text.clear()
            edit_message.clearFocus()
        }


    }

    override fun onStart() {
        super.onStart()
        chatViewModel.state.observe(this, Observer {
            setupViews(it as ChatState)
        })
    }

    private fun setupViews(state: ChatState) {

        messageListController.setChatState(state)

    }
}