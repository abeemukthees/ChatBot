package msa.domain.statemachine

import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import msa.domain.core.Action
import msa.domain.core.BaseStateMachine
import msa.domain.core.State
import msa.domain.entities.ChatBot
import msa.domain.entities.Message
import msa.domain.entities.SendMessageParams
import msa.domain.usecase.GetChatBots
import msa.domain.usecase.GetMessages
import msa.domain.usecase.SendMessage

/**
 * Created by Abhi Muktheeswarar.
 */

sealed class ChatAction : Action {

    object GetChatBotsAction : ChatAction()

    object LoadingChatBots : ChatAction()

    data class ChatBotsLoadedAction(val chatBots: List<ChatBot>) : ChatAction()

    data class GetMessagesAction(val chatBot: ChatBot) : ChatAction()

    object LoadingMessages : ChatAction()

    data class MessagesLoadedAction(val messages: List<Message>) : ChatAction()

    data class SendMessageAction(val sendMessageParams: SendMessageParams) : ChatAction()

    object MessageSentAction : ChatAction()

    data class ErrorSendingMessageAction(val exception: Exception) : ChatAction()

}

data class ChatState(
    val loading: Boolean = false,
    val chatBots: List<ChatBot>? = null,
    val chatBot: ChatBot? = null,
    val messages: List<Message>? = null,
    val exception: Exception? = null
) : State

class AppStateMachine(getChatBots: GetChatBots, getMessages: GetMessages, sendMessage: SendMessage) :
    BaseStateMachine<ChatState> {

    override val input: Relay<Action> = PublishRelay.create()

    override val state: Observable<ChatState> = input
        .doOnNext { println("Input Action ${it.javaClass.simpleName}") }
        .reduxStore(
            initialState = ChatState(),
            sideEffects = listOf(
                getChatBots::getChatBotsSideEffect,
                getMessages::getMessagesSideEffect,
                sendMessage::sendMessageSideEffect
            ),
            reducer = ::reducer
        )
        .distinctUntilChanged()
        .doOnNext { println("RxStore state ${it.javaClass.simpleName}") }


    override fun reducer(state: ChatState, action: Action): ChatState {
        return when (action) {

            is ChatAction.GetChatBotsAction -> state.copy(chatBot = null)

            is ChatAction.LoadingChatBots -> state.copy(loading = true)

            is ChatAction.ChatBotsLoadedAction -> state.copy(
                loading = false,
                chatBots = action.chatBots,
                exception = null
            )

            is ChatAction.GetMessagesAction -> state.copy(
                loading = true,
                chatBot = action.chatBot,
                messages = null,
                exception = null
            )

            is ChatAction.LoadingMessages -> state.copy(loading = true)

            is ChatAction.MessagesLoadedAction -> state.copy(
                loading = false,
                messages = action.messages,
                exception = null
            )

            is ChatAction.SendMessageAction -> state.copy(loading = true)

            is ChatAction.MessageSentAction -> state.copy(loading = false)

            is ChatAction.ErrorSendingMessageAction -> state.copy(loading = false, exception = action.exception)

            else -> state
        }
    }
}