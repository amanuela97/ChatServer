package chatServer



import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.util.*

class ChatConnector(input: InputStream, output: OutputStream): Runnable, ChatHistoryObserver {

    private var stateOfConnection = true
    private val ins = Scanner(input)
    private val out = PrintWriter(output,true)

    override fun run(){
        ChatHistory.registerObserver(this)
        out.println("Welcome to chat messenger")
        while(stateOfConnection) {
            val userInput = ins.nextLine()
            val message = ChatMessage(userInput)
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