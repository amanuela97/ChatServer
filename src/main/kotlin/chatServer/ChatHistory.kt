package chatServer


object ChatHistory: ChatHistoryObservable {

    val messages = mutableListOf<ChatMessage>()
    private val chatObservers = mutableSetOf<ChatHistoryObserver>()
    private var messageHistory:String = ""

    override fun insert(message: ChatMessage){
        messages.add(message)
        notifyObservers(message)
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
        messages.forEach { messageHistory  += "$it\r\n" }
        return messageHistory
    }
}