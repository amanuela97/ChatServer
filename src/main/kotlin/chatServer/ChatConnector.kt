package chatServer


import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.net.Socket
import java.util.*

class ChatConnector(input: InputStream, output: OutputStream, private var socket: Socket): Runnable, ChatHistoryObserver {

    private val inn = Scanner(input)
    private val out = PrintWriter(output,true)
    private var userName:String = ""
    private var signedIn = false
    private var connected = true
    var inp:String = " "

    override fun run(){
        ChatHistory.registerObserver(this)
        out.println("Welcome to chat messenger")
        commandInstructions()
        while(connected) {
            if (inn.hasNextLine()) inp = inn.nextLine() else closeClientConnection()
            when(inp){
                " "  ->  nothingHappens()
                else -> interpretInput(inp)
            }
        }
    }

    private fun nothingHappens(){}

    override fun newMessage(message: ChatMessage) {
        out.println("$message")
    }

    private fun commandInstructions() {
        val message =
            "To set user name, use command :user username\r\n" +
                    "To see users, use command :users\r\n" +
                    "To see history messages, use command :messages\r\n" +
                    "To exit, use command :exit\r\n"

        out.println(message)
    }

    private fun closeClientConnection(){
        if(userName.isNotEmpty()){println("lost client connection to $userName")}
        else {println("lost client connection")}
        connected = false
        Users.removeUser(userName)
        socket.close()
        println("Connection Closed")
    }

    private fun interpretInput(input: String){
        if (!signedIn){askUserToSignIn(input)}
        else{ifUserHasSignedIn(input)}

    }
    private fun askUserToSignIn(input: String){
        val userCommand = input.split(" ")
        if (userCommand[0].startsWith(":") && userCommand.size == 2 && userCommand[0] == ":user"){
            userName = userCommand[1]
            if (Users.checkIfUserExist(userName)){
                out.println("User name $userName is taken: Try a different user name: ")
            }
            else{
                Users.addUser(userName)
                out.println("User set to $userName.")
                signedIn = true
                println("User $userName has signed in")
            }
        }else{
            out.println("User name not set. Use command :user [username] to set it")
        }
    }
    private fun ifUserHasSignedIn(input:String){
        when{
            input.startsWith(":")-> handleCommand(input)
            else -> sendMessage(input)
            }

    }
    private fun handleCommand(input:String) {
        val userCommand = input.split(" ")
        when {
            userCommand[0] == "users" -> out.println(Users.toString())
            userCommand[0] == "messages" -> out.println(ChatHistory.toString())
            userCommand[0] == "exit" -> userExit()

        }
    }

    private fun sendMessage(input:String){
        val message = ChatMessage(input,userName)
        //val mm = Json.parse(message.serializer(),input)
        ChatHistory.insert(message)
        ChatHistory.notifyObservers(message)
    }

    private fun userExit(){
        println("$userName exited chat")
        Users.removeUser(userName)
        connected = false
    }


}