package com.my.televip.utils

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import com.my.televip.logging.Logger

object TeleVipScope : CoroutineScope {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.e(throwable)
    }

    override val coroutineContext = SupervisorJob() + Dispatchers.IO + exceptionHandler
}
