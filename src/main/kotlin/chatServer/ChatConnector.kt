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
    private var signedIn = false
    private var connected = true
    private var inp: String = " "
    private var userCommand:List<String> = listOf()


    @ImplicitReflectionSerializer
    override fun run(){
        ChatHistory.registerObserver(this)
        ChatConsole.register()
        out.println("Welcome to chat messenger")
        commandInstructions()
        while(connected) {
            if (inn.hasNextLine()) inp = inn.nextLine() else closeClientConnection()
            val json = Json(JsonConfiguration.Stable)
            val message = if (isJsonString(inp)) json.parse(inp) else ChatMessage("InvalidJSON","Fail")
            when{
                message.toString().isEmpty()  ->  nothingHappens()
                message.toString().split(" ")[0] == "InvalidJSON" -> nothingHappens()
                else -> interpretInput(message)
            }
        }
    }

    private fun nothingHappens(){}

    override fun getUserName() = userName

    @ImplicitReflectionSerializer
    fun isJsonString(str:String): Boolean {
        val json = Json(JsonConfiguration.Stable)
        try {
            json.parse<ChatMessage>(str)
        } catch (e:JsonException) {
            return false
        }
        return true
    }

    override fun newMessage(message: ChatMessage) {
        //if the user has not set a user name or signed in he/she will not receive a message
        if(signedIn) out.println("$message") else nothingHappens()
    }

    private fun closeClientConnection(){
        if(userName.isNotEmpty()){println("lost client connection to $userName")}
        else {println("lost client connection")}
        connected = false
        Users.removeUser(userName)
        socket.close()
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
        userCommand = message.toString().split(" ")
        if (userCommand[0] == ":user" && userCommand[1] == "from"){
            userName = userCommand[2]
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
        else {nothingHappens()}
    }
    @ImplicitReflectionSerializer
    private fun ifUserHasSignedIn(message:ChatMessage){
        val command = message.toString()
        when{
            command.startsWith(":")-> handleCommand(command)
            else -> ChatHistory.insert(message)
            }

    }
    private fun handleCommand(input:String) {
        userCommand = input.split(" ")
        when {
            userCommand[0] == ":users" -> out.println(Users.toString())
            userCommand[0] == ":messages" -> out.println(ChatHistory.toString())
            userCommand[0] == ":exit" -> closeClientConnection()
            userCommand[0] == ":user" -> out.println("user already set to $userName")
            else -> out.println("Unknown command: $userCommand")
        }
    }

    private fun commandInstructions() {
        val message =
            "To set user name, use command :user username\r\n" +
                    "To see users, use command :users\r\n" +
                    "To see history messages, use command :messages\r\n" +
                    "To exit, use command :exit\r\n"

        out.println(message)
    }



}



