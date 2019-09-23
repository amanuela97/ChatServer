package chatServer

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.*
import kotlinx.serialization.parse
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
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
        commandInstructions()
        while(connected) {
            if (inn.hasNextLine()) inp = inn.nextLine() else closeClientConnection()
            if (isJsonString(inp) && inp.isNotEmpty() && connected) {
                val message = json.parse<ChatMessage>(inp)
                interpretInput(message)
            } else nothingHappens()
        }
    }

    private fun nothingHappens(){}

    override fun getUserName() = userName

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
        if(Users.checkIfUserExist(message.userName)) out.println("$message")
    }

    @ImplicitReflectionSerializer
    private fun interpretInput(message: ChatMessage){
        if (!signedIn){
            askUserToSignIn(message)
        }
        else{
            ifUserHasSignedIn(message)
        }
    }

    private fun askUserToSignIn(message: ChatMessage){
        if (message.command == ":user"){
            userName = message.userName
            if (Users.checkIfUserExist(userName)){nothingHappens()}
            else{
                signedIn = true
                println("User $userName has signed in")
                ChatHistory.insert(message)
                out.println("User set to $userName.")
                Users.addUser(userName)
            }
        }
        else {nothingHappens()}
    }
    @ImplicitReflectionSerializer
    private fun ifUserHasSignedIn(message:ChatMessage){
        if (message.command.startsWith(":") && message.userName == this.userName){
            ChatHistory.insert(message)
            handleCommand(message.command)
        }
        else if (message.userName == this.userName && !message.command.startsWith(":"))
            ChatHistory.insert(message)

    }
    private fun handleCommand(command:String) {
        when(command) {
            ":users" -> out.println(Users.toString())
            ":messages" -> out.println(ChatHistory.toString())
            ":exit" -> closeClientConnection()
            ":user" -> out.println("user already set to $userName")
            else -> out.println("Unknown command: $command")
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

    private fun commandInstructions() {
        val message =
            "Welcome to chat messenger\r\n" +
            "To set user name, use command :user username\r\n" +
                    "To see users, use command :users\r\n" +
                    "To see history messages, use command :messages\r\n" +
                    "To exit, use command :exit\r\n"

        out.println(message)
    }



}



