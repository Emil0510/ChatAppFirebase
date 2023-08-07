package com.emilabdurahmanli.chatapprealtime.data.response.model

import java.io.Serializable

data class GroupModel(
    val roomName: String? = "",
    val roomKey: String? = "",
) : Serializable
