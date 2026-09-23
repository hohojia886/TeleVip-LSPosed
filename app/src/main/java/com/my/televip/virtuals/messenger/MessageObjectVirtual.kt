package com.my.televip.virtuals.messenger

import com.my.televip.virtuals.VirtualField
import com.my.televip.virtuals.VirtualMethod

interface MessageObjectVirtual {
    @VirtualField(name = "messageOwner", className = "MessageObject")
    fun getMessageOwnerRaw(): Any?

    @VirtualMethod(name = "getDialogId", className = "MessageObject")
    fun getDialogId(): Long

    @VirtualMethod(name = "isVoice", className = "MessageObject")
    fun isVoice(): Boolean
}
