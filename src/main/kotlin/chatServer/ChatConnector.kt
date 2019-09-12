package chatServer

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.util.*

class ChatConnector(input: InputStream, output: OutputStream): Runnable, ChatHistoryObserver {


    private val ins = Scanner(input)
    private val out = PrintWriter(output,true)
    @UnstableDefault
    override fun run(){
        while(true) {
            val userInput = ins.nextLine()
            val parsedMessage = Json.parse(ChatMessage.serializer(),userInput)
            addMessageToChatHistory(parsedMessage)
            ChatHistory.notifyObservers(parsedMessage)
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