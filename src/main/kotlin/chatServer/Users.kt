package chatServer

object Users {

    private val userNames = HashSet<String>()
    private var userList:String = ""

    fun addUser(name: String): Boolean{
        if(!checkIfUserExist(name)) {
            userNames.add(name)
            return true
        }
        return false
    }

    fun removeUser(name: String){
        if (checkIfUserExist(name)) userNames.remove(name)
    }

    fun checkIfUserExist(name:String): Boolean{
        return userNames.contains(name)
    }

    override fun toString(): String{
        //nicely formatting string
        userNames.forEach { userList += "$it\r\n" }
        return userList
    }
}