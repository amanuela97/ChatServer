package chatServer

object ChatConsole: ChatHistoryObserver {

    fun register(){
        ChatHistory.registerObserver(this)
    }
    override fun newMessage(message: ChatMessage) {
        println(message)
    }


}