package chatServer

object Users {

    private val userNames = HashSet<String>()

    fun addUser(name: String){
        userNames.add(name)
    }

    fun removeUser(name: String){
        userNames.remove(name)
    }

    fun checkIfUserExist(name:String): Boolean{
        return userNames.contains(name)
    }

    override fun toString(): String{
        return userNames.toString()
    }
}