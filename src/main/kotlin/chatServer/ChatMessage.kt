package chatServer

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class ChatMessage (private val message: String, private val userName: String, private val timeDate: LocalDateTime) {

    //message structure
    override fun toString(): String {
        return "$message from $userName at $timeDate"
    }
}

