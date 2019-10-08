package chatServer

object Users {

    var userNames = HashSet<String>()

    fun addUser(name: String){
        if(!userNames.contains(name)){
            userNames.add(name)
            TopChatter.activeUsers()
        }

    }

    fun removeUser(name: String){
        if (checkIfUserExist(name)) userNames.remove(name)
    }

    fun checkIfUserExist(name:String): Boolean{
        return userNames.contains(name)
    }

    override fun toString(): String{
        //nicely formatting string
        var userList = ""
        userNames.forEach { userList += "$it\r\n" }
        return userList
    }
}