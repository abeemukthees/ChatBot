package msa.data.inmemory

import androidx.collection.SparseArrayCompat
import io.reactivex.Single
import msa.domain.entities.ChatBot
import msa.domain.entities.poko.MessagePoko

/**
 * Created by Abhi Muktheeswarar.
 */

class InMemoryDataStore {

    private val messages = SparseArrayCompat<MessagePoko>()

    companion object {

        const val USER_ID = -1
    }


    fun getChatBots(): Single<List<ChatBot>> {

        return Single.fromCallable {

            val listOfChatBots = arrayListOf<ChatBot>()

            listOfChatBots.add(
                ChatBot(
                    id = 63906,
                    name = "Cyber Ty",
                    imageUrl = "https://www.personalityforge.com/images/faces/cyberty.jpg"
                )
            )

            listOfChatBots.add(
                ChatBot(
                    id = 71367,
                    name = "Laurel Sweet",
                    imageUrl = "https://www.personalityforge.com/images/faces/Gal.gif"
                )
            )

            listOfChatBots.add(
                ChatBot(
                    id = 6526,
                    name = "Jake Thompson",
                    imageUrl = "https://www.personalityforge.com/images/faces/jakethompson.jpg"
                )
            )

            listOfChatBots
        }
    }
}