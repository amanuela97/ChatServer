package chatServer


object ChatHistory: ChatHistoryObservable {

    private val Messages = mutableListOf<ChatMessage>()
    private val chatObservers = mutableListOf<ChatHistoryObserver>()

    override fun insert(message: ChatMessage){
        Messages.add(message)
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
        return Messages.toString()
    }
}