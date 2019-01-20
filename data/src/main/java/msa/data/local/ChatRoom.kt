package msa.data.local

import androidx.room.*
import io.reactivex.Observable
import msa.domain.entities.MessageStatus
import java.util.*

/**
 * Created by Abhi Muktheeswarar.
 */

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val chatBotId: Int,
    var senderId: Int,
    var receiverId: Int,
    var message: String,
    var emotion: String,
    var date: Date,
    var messageStatus: MessageStatus
)

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromMessageStatus(value: String?): MessageStatus? {
        return value?.let { MessageStatus.valueOf(it) }
    }

    @TypeConverter
    fun messageStatusToString(messageStatus: MessageStatus?): String? {
        return messageStatus?.name
    }
}

@Dao
interface MessageDao {

    @Query("SELECT * FROM messageentity WHERE chatBotId == :chatBotId")
    fun getMessages(chatBotId: Int): Observable<List<MessageEntity>>

    @Insert
    fun insertMessage(messageEntity: MessageEntity): Long

    @Update
    fun updateMessage(messageEntity: MessageEntity)

    @Delete
    fun deleteMessage(messageEntity: MessageEntity)
}

@Database(entities = [MessageEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MessageDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
}
