package chatServer

import java.lang.Exception
import java.net.ServerSocket

class ChatServer {

    fun server(){
        val serverSocket = ServerSocket(30000,2)
        println("ChatServer is listening on port " + serverSocket.localPort)
        println("accepting")
        while(true) {
            try {
                val s = serverSocket.accept()
                println("new connection " + s.inetAddress.hostAddress + " " + s.port)
                val t = Thread(ChatConnector(s.getInputStream(), s.getOutputStream()))
                t.start()
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                println("accepted")
            }

        }

    }
}
