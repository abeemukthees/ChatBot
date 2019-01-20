package msa.domain.usecase

import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable
import io.reactivex.Scheduler
import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.interactor.UseCase
import msa.domain.repository.Repository
import msa.domain.statemachine.ChatAction
import msa.domain.statemachine.ChatState

/**
 * Created by Abhi Muktheeswarar.
 */

class GetChatBots(private val repository: Repository, ioScheduler: Scheduler, computationScheduler: Scheduler) :
    UseCase(ioScheduler, computationScheduler) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        return repository.getChatBots().toObservable().map { ChatAction.ChatBotsLoadedAction(it) }
    }

    fun getChatBotsSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(ChatAction.GetChatBotsAction::class.java)
            .filter { (state() as ChatState).chatBots.isNullOrEmpty() }
            .switchMap { execute(it, state()).startWith(ChatAction.LoadingChatBots) }
}