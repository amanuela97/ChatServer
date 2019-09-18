package chatServer

object ChatConsole: ChatHistoryObserver {
    override fun getUserName(): String {
        return "ChatConsole"
    }

    fun register(){
        ChatHistory.registerObserver(this)
    }
    override fun newMessage(message: ChatMessage) {
        println(message)
    }


}