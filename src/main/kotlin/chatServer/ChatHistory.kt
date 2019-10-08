package chatServer


object ChatHistory: ChatHistoryObservable {

    val messages = mutableListOf<ChatMessage>()
    private val chatObservers = mutableSetOf<ChatHistoryObserver>()

    override fun insert(message: ChatMessage){
        messages.add(message)
        notifyObservers(message)
        TopChatter.activeUsers()
    }

    override fun registerObserver(observer: ChatHistoryObserver) {
        chatObservers.add(observer)
    }

    override fun deregisterObserver(observer: ChatHistoryObserver) {
        chatObservers.remove(observer)
    }

    override fun notifyObservers(message: ChatMessage) {
        // notify all except the person sending the message
        chatObservers.forEach{it.newMessage(message)}
    }

    override fun toString(): String{
        //nicely formatting string
        var messageHistory = ""
        messages.forEach { messageHistory  += "$it\r\n" }
        return messageHistory
    }
}