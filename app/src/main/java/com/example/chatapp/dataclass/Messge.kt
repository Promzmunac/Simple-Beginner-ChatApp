package com.example.chatapp.dataclass

class Messge {

    var message: String? = null
    var senderId: String? = null

    constructor()

    constructor(message: String?, senderId: String?){
        this.message = message
        this.senderId = senderId
    }
}
