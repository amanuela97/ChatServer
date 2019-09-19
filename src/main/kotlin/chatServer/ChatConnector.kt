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
    private val json = Json(JsonConfiguration.Stable)
    private var message:ChatMessage = ChatMessage("","","")


    @ImplicitReflectionSerializer
    override fun run(){
        ChatHistory.registerObserver(this)
        ChatConsole.register()
        out.println("Welcome to chat messenger")
        commandInstructions()
        while(connected) {
            if (inn.hasNextLine()) inp = inn.nextLine() else closeClientConnection()
            if (isJsonString(inp) && inp.isNotEmpty()) {
                message = json.parse(inp)
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
        if (message.command == ":user"){
            userName = message.userName
            if (Users.checkIfUserExist(userName)){
                out.println("User name $userName is taken: Try a different user name: ")
            }
            else{
                Users.addUser(userName)
                out.println("User set to $userName.")
                ChatHistory.insert(message)
                signedIn = true
                TopChatter.activeUsers(this)
                println("User $userName has signed in")
            }
        }
        else {nothingHappens()}
    }
    @ImplicitReflectionSerializer
    private fun ifUserHasSignedIn(message:ChatMessage){
        when{
            message.command.startsWith(":")-> handleCommand(message.command)
            else -> ChatHistory.insert(message)
            }

    }
    private fun handleCommand(command:String) {
        when(command) {
            ":users" -> out.println(Users.toString())
            ":messages" -> out.println(ChatHistory.toString())
            ":exit" -> closeClientConnection()
            ":user" -> out.println("user already set to $userName")
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



