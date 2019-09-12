package chatServer

import java.time.LocalDateTime

class ChatMessage (private val message: String, private val userName: String, private val timeDate: LocalDateTime) {

    //message structure
    override fun toString(): String {
        return "$message from $userName at $timeDate"
    }
}

