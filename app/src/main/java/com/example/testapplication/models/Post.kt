package com.example.testapplication.models

import com.google.gson.annotations.SerializedName

class Post constructor() {
    var userId: Int? = null
    var id: Int? = null
    var title: String? = null
    @SerializedName("body")
    var text: String? = null


}