package chatServer

object TopChatter: ChatHistoryObserver {

    private var chatters = mutableMapOf<String,Int>()
    private var count = 0
    var chattersList = ""

    fun register(){
        ChatHistory.registerObserver(this)
    }

    override fun newMessage(message: ChatMessage) {}

    //print 4 top chatters to the console every time a user is added to the users list
    fun activeUsers(){
        println("Top Chatters:")
        Users.userNames.forEach { username -> ChatHistory.messages.forEach {message ->
            if ( message.userName == username)count++
        }
            if (chatters.containsKey(username))chatters.replace(username, count) else chatters[username] = count
            count = 0
        }
        val sortedChatters = chatters.toList().sortedByDescending{ (_, value) -> value }.take(4).toMap()
        chattersList = ""
        for (chatter in sortedChatters){
            chattersList += "$chatter\r\n"
            println("${chatter.key} sent ${chatter.value} message")
        }
    }








}