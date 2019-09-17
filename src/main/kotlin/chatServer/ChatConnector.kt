package chatServer



import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.*
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.net.Socket
import java.time.LocalDateTime
import java.util.*

class ChatConnector(input: InputStream, output: OutputStream, private var socket: Socket): Runnable, ChatHistoryObserver {

    private val inn = Scanner(input)
    private val out = PrintWriter(output,true)
    private var userName:String = ""
    private var signedIn = false
    private var connected = true
    private var inp:String = " "
    private var userCommand:List<String> = listOf()

    @ImplicitReflectionSerializer
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
        out.println("$message at ${LocalDateTime.now()}")
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

    @ImplicitReflectionSerializer
    private fun interpretInput(input: String){
        if (!signedIn){
            askUserToSignIn(input)
        }
        else{
            ifUserHasSignedIn(input)
        }


    }
    private fun askUserToSignIn(input: String){
        userCommand = input.split(" ")
        if (userCommand.size == 2 && userCommand[0] == ":user"){
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
        }
        else {
            out.println("User name not set. Use command :user [username] to set it")
        }
    }
    @ImplicitReflectionSerializer
    private fun ifUserHasSignedIn(input:String){
        when{
            input.startsWith(":")-> handleCommand(input)
            else -> sendMessage(input)
            }

    }
    private fun handleCommand(input:String) {
        userCommand = input.split(" ")
        when {
            userCommand[0] == ":users" -> out.println(Users.toString())
            userCommand[0] == ":messages" -> out.println(ChatHistory.toString())
            userCommand[0] == ":exit" -> userExit()
            userCommand[0] == ":user" -> out.println("user already set to $userName")
            else -> out.println("Unknown command: $userCommand")
        }
    }

    @ImplicitReflectionSerializer
    private fun sendMessage(input:String){
        val json = Json(JsonConfiguration.Stable)

        //serialize/stringify incoming message for now
        val jsonData = json.stringify(ChatMessage(input,userName))
        //parse incoming message into object of ChatMessage
        val messageObject = json.parse<ChatMessage>(jsonData)

        //add message to message list
        ChatHistory.insert(messageObject)
        //broadcast to all observers that are logged in
        if (this.userName != "")ChatHistory.notifyObservers(messageObject)

    }

    private fun userExit(){
        println("$userName exited chat")
        Users.removeUser(userName)
        connected = false
        socket.close()
        println("$userName exited")
    }


}



