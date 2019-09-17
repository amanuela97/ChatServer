package chatServer


import kotlinx.serialization.Serializable


@Serializable
data class ChatMessage (private val message: String, private val userName:String){

    //message structure
    override fun toString(): String {
        return "$message from $userName"
    }
}

