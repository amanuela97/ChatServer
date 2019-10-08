package chatServer

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.*
import kotlinx.serialization.parse
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import java.util.*

class ChatConnector(input: InputStream, output: OutputStream, private var socket: Socket): Runnable, ChatHistoryObserver {

    private val inn = Scanner(input)
    private val out = PrintWriter(output,true)
    private var userName:String = ""
    private var connected = true
    private var signedIn = false
    private var inp: String = " "
    private val json = Json(JsonConfiguration.Stable)


    @ImplicitReflectionSerializer
    override fun run(){
        ChatHistory.registerObserver(this)
        ChatConsole.register()
        TopChatter.register()
        while(connected) {
            try {
                if (inn.hasNextLine()) inp = inn.nextLine() else closeClientConnection()
                if (isJsonString(inp) && inp.isNotEmpty() && connected) {
                    val message = json.parse<ChatMessage>(inp)
                    interpretInput(message)
                } else nothingHappens()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun nothingHappens(){}

    @ImplicitReflectionSerializer
    fun isJsonString(str:String): Boolean {
        try {
            json.parse<ChatMessage>(str)
        } catch (e:JsonException) {
            return false
        }
        return true
    }

    override fun newMessage(message: ChatMessage) {
        if(Users.checkIfUserExist(message.userName)) out.println("@ $message")

    }

    @ImplicitReflectionSerializer
    private fun interpretInput(message: ChatMessage){
        if (message.command == ":user"){
            askUserToSignIn(message)
        }
        else{
            ifUserHasSignedIn(message)
        }

    }

    private fun askUserToSignIn(message: ChatMessage){
        if (message.command == ":user"){
            userName = message.userName
            if (Users.checkIfUserExist(userName)){
                nothingHappens()
            }
            else{
                signedIn = true
                println("User $userName has signed in")
                out.println(":User set to $userName.")
                Users.addUser(userName)
            }
        }
    }
    @ImplicitReflectionSerializer
    private fun ifUserHasSignedIn(message:ChatMessage){
        if (message.command.isNotEmpty() && message.userName == this.userName){
            handleCommand(message.command)
        }
        else if (message.userName == this.userName && message.command.isEmpty())
            ChatHistory.insert(message)

    }
    private fun handleCommand(command:String) {
        when(command) {
            "users" -> out.println(Users.toString())
            "messages" -> out.println(ChatHistory)
            "exit" -> closeClientConnection()
            "topChatter" -> out.println(TopChatter.chattersList)
            else -> nothingHappens()
        }
    }

    private fun closeClientConnection(){
        if(userName.isNotEmpty()){println("lost client connection to $userName")}
        else {println("lost client connection")}
        Users.removeUser(userName)
        connected = false
        out.close()
        socket.close()
    }

}



