package msa.chatbot

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import msa.data.ChatRepository
import msa.data.DataStoreFactory
import msa.domain.repository.Repository
import msa.domain.statemachine.AppStateMachine
import msa.domain.usecase.GetChatBots
import msa.domain.usecase.GetMessages
import msa.domain.usecase.SendMessage
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by Abhi Muktheeswarar.
 */

val appModule = module {

    single { DataStoreFactory(get()) }
    single<Repository> { ChatRepository(get()) }
    single(name = "ioScheduler") { Schedulers.io() }
    single(name = "computationScheduler") { Schedulers.computation() }
    single(name = "postExecutionScheduler") { AndroidSchedulers.mainThread() }
}

val stateMachineModule = module {

    factory { AppStateMachine(get(), get(), get()) }
}

val useCaseModule = module {

    factory { GetChatBots(get(), get(name = "ioScheduler"), get(name = "computationScheduler")) }
    factory { GetMessages(get(), get(name = "ioScheduler"), get(name = "computationScheduler")) }
    factory { SendMessage(get(), get(name = "ioScheduler"), get(name = "computationScheduler")) }
}

val viewModelModule = module {

    viewModel { ChatViewModel(get(), get(name = "postExecutionScheduler")) }

}