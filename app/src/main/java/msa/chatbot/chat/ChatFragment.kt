package msa.chatbot.chat

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_chat.*
import msa.chatbot.ChatViewModel
import msa.chatbot.base.BaseFragment
import msa.domain.entities.SendMessageParams
import msa.domain.statemachine.ChatAction
import msa.domain.statemachine.ChatState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit


/**
 * Created by Abhi Muktheeswarar.
 */

class ChatFragment : BaseFragment() {

    private val chatViewModel by sharedViewModel<ChatViewModel>()

    private val messageListController by lazy { MessageListController() }

    override fun getLayoutId(): Int = msa.chatbot.R.layout.fragment_chat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_send.isEnabled = false
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        linearLayoutManager.stackFromEnd = true
        epoxyRecyclerView_messages.layoutManager = linearLayoutManager
        epoxyRecyclerView_messages.setItemSpacingDp(12)
        messageListController.addModelBuildListener {

            epoxyRecyclerView_messages.smoothScrollToPosition(0)

        }
        epoxyRecyclerView_messages.setController(messageListController)
        addDisposable(
            edit_message.textChanges().debounce(300, TimeUnit.MILLISECONDS).map { it.length }.observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe {
                button_send.isEnabled = it > 0
            })
        button_send.setOnClickListener {

            val message = edit_message.text.toString()
            val sendMessageParams = SendMessageParams(message = message)
            chatViewModel.input.accept(ChatAction.SendMessageAction(sendMessageParams))
            edit_message.text.clear()
            edit_message.clearFocus()
            getView()?.let {

                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
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