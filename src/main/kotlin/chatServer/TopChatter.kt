package chatServer

object TopChatter: ChatHistoryObserver {

    private val activeUserList = mutableListOf<ChatHistoryObserver>()
    private val activeMessages = mutableListOf<ChatMessage>()
    private var activeUsers:String = ""
    private var count:Int = 0

    override fun newMessage(message: ChatMessage) {}

    override fun getUserName(): String{
        return ""
    }

    fun activeUsers(Observer: ChatHistoryObserver){
        activeUserList.add(Observer)
        println("Active Users:")
        for (u in activeUserList){
            for (m in activeMessages){
                if(u.getUserName() == m.userName ){
                   if(m.userName == this.getUserName()) count++
                    activeUsers += "${u.getUserName()} sent $count number of messages\r\n"
                }
            }
        }

        println(activeUsers)

    }
    fun getMessage(message: ChatMessage){
        activeMessages.add(message)
    }






}