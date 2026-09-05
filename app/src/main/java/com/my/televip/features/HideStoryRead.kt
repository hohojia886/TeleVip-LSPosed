package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.obfuscate.AutomationResolver

object HideStoryRead {
    @JvmStatic
    fun isReadStoriesRequest(obj: Any): Boolean {
        return if (!ClientChecker.isTgnetObfuscated()) {
            val className = obj.javaClass.name
            className.contains("TL_stories_readStories") || className.contains("TL_stories_incrementStoryViews")
        } else {
            val objectClass = obj.javaClass
            objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_STORIES_READ_STORIES)) ||
                    objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_STORIES_INCREMENT_STORY_VIEWS))
        }
    }
}
