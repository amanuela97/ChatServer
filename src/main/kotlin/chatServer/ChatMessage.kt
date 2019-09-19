package chatServer


import kotlinx.serialization.Serializable
import java.util.*


@Serializable
class ChatMessage (val command:String,
                   private val message: String,
                   val userName:String,
                   private val dateAndTime:Long = System.currentTimeMillis()
){

    //message structure
    override fun toString(): String {
        val date = Date(dateAndTime)
        return "$message from $userName on $date"
    }
}

