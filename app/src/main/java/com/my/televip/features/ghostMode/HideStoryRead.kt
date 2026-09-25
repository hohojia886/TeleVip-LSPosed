package com.my.televip.features.ghostMode

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.obfuscate.Obfuscate

object HideStoryRead {

    @JvmStatic
    fun isReadStoriesRequest(objectParam: Any): Boolean {
        return if (!ClientManager.isTgnetObfuscated()) {
            val className = objectParam.javaClass.name
            className.contains("TL_stories_readStories") ||
                    className.contains("TL_stories_incrementStoryViews")
        } else {
            val objectClass = objectParam.javaClass
            objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_STORIES_READ_STORIES)) ||
                    objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_STORIES_INCREMENT_STORY_VIEWS))
        }
    }
}
