package chatServer

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.time.LocalDateTime
import java.util.*

class ChatConnector(input: InputStream, output: OutputStream): Runnable, ChatHistoryObserver {


    private val ins = Scanner(input)
    private val out = PrintWriter(output,true)
    override fun run(){
        while(true) {
            val userInput = ins.nextLine()
            val message = ChatMessage(userInput, "Amabel", LocalDateTime.now())
            addMessageToChatHistory(message)
            ChatHistory.notifyObservers(message)
            if(userInput.contains("bye")) break
        }
    }

    override fun newMessage(message: ChatMessage) {
        out.println("$message")
    }

    private fun addMessageToChatHistory(message:ChatMessage){
        ChatHistory.insert(message)
    }

}