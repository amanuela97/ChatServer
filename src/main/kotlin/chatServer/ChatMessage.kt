package chatServer


import kotlinx.serialization.Serializable


@Serializable
class ChatMessage (private val message: String, val userName:String){

    //message structure
    override fun toString(): String {
        return "$message from $userName"
    }
}

