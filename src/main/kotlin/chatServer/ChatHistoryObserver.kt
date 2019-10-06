package chatServer

interface ChatHistoryObserver{
    fun newMessage(message:ChatMessage)
    //fun getUserName():String
}