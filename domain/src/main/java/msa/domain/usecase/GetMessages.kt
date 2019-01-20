package msa.domain.usecase

import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable
import io.reactivex.Scheduler
import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.interactor.UseCase
import msa.domain.repository.Repository
import msa.domain.statemachine.ChatAction

/**
 * Created by Abhi Muktheeswarar.
 */

class GetMessages(private val repository: Repository, ioScheduler: Scheduler, computationScheduler: Scheduler) :
    UseCase(ioScheduler, computationScheduler) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        val getMessagesParams = (action as ChatAction.GetMessagesAction).getMessagesParams
        return repository.getMessages(getMessagesParams).map { ChatAction.MessagesLoadedAction(it) }
    }

    fun getMessagesSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(ChatAction.GetMessagesAction::class.java)
            .switchMap { execute(it, state()) }
}