package chatServer

import java.lang.Exception
import java.net.ServerSocket

class ChatServer {
    private var serverSocket = ServerSocket(30000,2)

    fun server(){
        println("ChatServer is listening on port 30000")
        println("accepting")
        while(true) {
            try {
                val s = serverSocket.accept()
                println("accepted")
                val ob = ChatConnector(s.getInputStream(), s.getOutputStream())
                // add observer to observers collection in chat history
                ChatHistory.registerObserver(ob)
                // start thread of chatConnector
                val t = Thread(ob)
                t.start()
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                println("Done!")
            }

        }

    }
}
