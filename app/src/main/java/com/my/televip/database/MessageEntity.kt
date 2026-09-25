package com.my.televip.database

data class MessageEntity(
    @JvmField val id: Long,
    @JvmField val msgId: Int,
    @JvmField val msgCount: Int,
    @JvmField val message: String?,
    @JvmField val messageDate: Long
)
