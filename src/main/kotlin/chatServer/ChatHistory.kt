package chatServer


object ChatHistory: ChatHistoryObservable {

    private val messages = mutableListOf<ChatMessage>()
    private val chatObservers = mutableListOf<ChatHistoryObserver>()
    private var messageHistory:String = ""

    override fun insert(message: ChatMessage){
        messages.add(message)
    }

    override fun registerObserver(observer: ChatHistoryObserver) {
        chatObservers.add(observer)
    }

    override fun deregisterObserver(observer: ChatHistoryObserver) {
        chatObservers.remove(observer)
    }

    override fun notifyObservers(message: ChatMessage) {
        chatObservers.forEach{it.newMessage(message)}
    }

    override fun toString(): String{
        //nicely formatting string
        messages.forEach { messageHistory  += "\n $it" }
        return messageHistory
    }
}