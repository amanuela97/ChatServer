package chatServer

import java.lang.Exception
import java.net.ServerSocket

class ChatServer {
    private val serverSocket = ServerSocket(30000,2)

    fun server(){
        println("ChatServer is now listening on port " + serverSocket.localPort)
        while(true) {
            try {
                println("accepting")
                val s = serverSocket.accept()
                println("new connection " + s.inetAddress.hostAddress + " " + s.port)
                val t = Thread(ChatConnector(s.getInputStream(), s.getOutputStream(), s))
                t.start()
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                println("accepted")
            }

        }

    }
}
