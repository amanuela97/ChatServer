package chatServer

interface ChatHistoryObservable {
    fun insert(message: ChatMessage)
    fun registerObserver(observer:ChatHistoryObserver)
    fun deregisterObserver(observer:ChatHistoryObserver)
    fun notifyObservers (message:ChatMessage)
}