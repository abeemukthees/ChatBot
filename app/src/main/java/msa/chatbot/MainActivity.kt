package msa.chatbot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import msa.domain.statemachine.ChatState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val chatViewModel by viewModel<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
        chatViewModel.state.observe(this, Observer {
            setupViews(it as ChatState)
        })
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private fun setupViews(state: ChatState) {

        if (navController.currentDestination?.id == R.id.chatFragment) {

            state.chatBot?.let {
                supportActionBar?.title = it.name
            }
        }
    }
}
