package com.example.chatapp.dataclass

//if we are using ordinary room db we can convert this to a data class but firebaseDb
// requires that we must use an empty constructor to work with
class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var selectedPhotoUri: String? = null

    constructor()

    constructor(name: String?, email: String?, uid: String?, selectedPhotoUri: String){
        this.name = name
        this.email = email
        this.uid = uid
        this.selectedPhotoUri = selectedPhotoUri
    }
}