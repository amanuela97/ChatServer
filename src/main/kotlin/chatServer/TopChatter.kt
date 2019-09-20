package chatServer

object TopChatter: ChatHistoryObserver {

    private var chatters = mutableMapOf<String,Int>()
    private var count = 0

    fun register(){
        ChatHistory.registerObserver(this)
    }

    override fun getUserName(): String {
        return "TopChatter"
    }


    override fun newMessage(message: ChatMessage) {
    }

    fun activeUsers(){
        println("Top Chatters:")
        Users.userNames.forEach { username -> ChatHistory.messages.forEach {message ->
            if ( message.userName == username)count++
        }
            if (chatters.containsKey(username))chatters.replace(username, count) else chatters[username] = count
            count = 0
        }
        val sortedChatters = chatters.toList().sortedByDescending{ (_, value) -> value }.take(4).toMap()
        for (chatter in sortedChatters){
            println("${chatter.key} sent ${chatter.value} message")
        }
    }








}