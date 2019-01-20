package msa.chatbot

import io.reactivex.Scheduler
import msa.chatbot.base.BaseViewModel
import msa.domain.statemachine.AppStateMachine
import timber.log.Timber

/**
 * Created by Abhi Muktheeswarar.
 */

class ChatViewModel(appStateMachine: AppStateMachine, postExecutionScheduler: Scheduler) : BaseViewModel() {

    init {

        Timber.d("Init...")

        addDisposable(inputRelay.subscribe(appStateMachine.input))
        addDisposable(appStateMachine.state.observeOn(postExecutionScheduler).subscribe { state ->
            mutableState.value = state
        })
    }
}