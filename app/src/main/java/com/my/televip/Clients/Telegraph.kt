package com.my.televip.Clients

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.struct.ClassInfo
import com.my.televip.obfuscate.struct.FieldInfo
import com.my.televip.obfuscate.struct.MethodInfo
import de.robv.android.xposed.XC_MethodReplacement

class Telegraph {
    companion object {
        private val classList = ArrayList<ClassInfo>()
        private val fieldList = ArrayList<FieldInfo>()
        private val methodList = ArrayList<MethodInfo>()
        private val methodAlias = HashMap<String, String>()

        @JvmStatic
        fun resolveMethodName(className: String, name: String): String {
            return methodAlias[className + "#" + name] ?: name
        }

        @JvmStatic
        fun loadParameter() {
            // Class
            classList.add(ClassInfo("org.telegram.messenger.AndroidUtilities", "org.telegram.messenger.COm4"))
            classList.add(ClassInfo("org.telegram.messenger.ApplicationLoader", "org.telegram.messenger.COm5"))
            classList.add(ClassInfo("org.telegram.messenger.LocaleController", "org.telegram.messenger.o9"))
            classList.add(ClassInfo("org.telegram.messenger.MessageObject", "org.telegram.messenger.vh"))
            classList.add(ClassInfo("org.telegram.messenger.MessagesController", "org.telegram.messenger.Jr"))
            classList.add(ClassInfo("org.telegram.messenger.MessagesStorage", "org.telegram.messenger.px"))
            classList.add(ClassInfo("org.telegram.messenger.NotificationCenter", "org.telegram.messenger.Ix"))
            classList.add(ClassInfo("org.telegram.messenger.NotificationsController", "org.telegram.messenger.Vy"))
            classList.add(ClassInfo("org.telegram.messenger.SharedConfig", "org.telegram.messenger.WD"))
            classList.add(ClassInfo("org.telegram.messenger.UserConfig", "org.telegram.messenger.qG"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBar", "org.telegram.ui.ActionBar.cOn"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBar\$ActionBarMenuOnItemClick", "org.telegram.ui.ActionBar.cOn\$coN"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBarMenuItem", "org.telegram.ui.ActionBar.CoM2"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.AlertDialog\$OnButtonClickListener", "org.telegram.ui.ActionBar.AlertDialog\$coN"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.Theme", "org.telegram.ui.ActionBar.A"))
            classList.add(ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "org.telegram.ui.Cells.coM7"))
            classList.add(ClassInfo("org.telegram.ui.Cells.HeaderCell", "org.telegram.ui.Cells.LPT6"))
            classList.add(ClassInfo("org.telegram.ui.Cells.ShadowSectionCell", "org.telegram.ui.Cells.T"))
            classList.add(ClassInfo("org.telegram.ui.Cells.TextCheckCell", "org.telegram.ui.Cells.P0"))
            classList.add(ClassInfo("org.telegram.ui.Cells.TextSettingsCell", "org.telegram.ui.Cells.r1"))
            classList.add(ClassInfo("org.telegram.ui.ChatActivity", "org.telegram.ui.Oh"))
            classList.add(ClassInfo("org.telegram.ui.ChatActivity\$ChatMessageCellDelegate", "org.telegram.ui.Oh\$Lpt7"))
            classList.add(ClassInfo("org.telegram.ui.Components.UniversalAdapter", "org.telegram.ui.Components.NR"))
            classList.add(ClassInfo("org.telegram.ui.PhotoViewer\$PhotoViewerProvider", "org.telegram.ui.PhotoViewer\$lPt1"))
            classList.add(ClassInfo("org.telegram.ui.PhotoViewer\$PlaceProviderObject", "org.telegram.ui.PhotoViewer\$LPt1"))
            classList.add(ClassInfo("org.telegram.ui.Stories.PeerStoriesView\$StoryItemHolder", "org.telegram.ui.Stories.n\$coM2"))
            classList.add(ClassInfo("org.telegram.ui.Stories.StoriesController", "org.telegram.ui.Stories.A2"))

            // Field
            fieldList.add(FieldInfo("ApplicationLoader", "applicationContext", "b"))
            fieldList.add(FieldInfo("LocaleController", "currentLocale", "y"))
            fieldList.add(FieldInfo("LocaleController", "isRTL", "U"))
            fieldList.add(FieldInfo("MessagesController", "dialogMessage", "C"))
            fieldList.add(FieldInfo("MessagesController", "dialogMessagesByIds", "F"))
            fieldList.add(FieldInfo("NotificationCenter", "messagesDeleted", "c0"))
            fieldList.add(FieldInfo("NotificationCenter", "tlSchemeParseException", "P4"))
            fieldList.add(FieldInfo("UserConfig", "clientUserId", "i"))
            fieldList.add(FieldInfo("UserConfig", "selectedAccount", "k0"))
            fieldList.add(FieldInfo("Theme", "chat_timePaint", "x3"))
            fieldList.add(FieldInfo("ChatMessageCell", "currentTimeString", "Mb"))
            fieldList.add(FieldInfo("ChatMessageCell", "timeTextWidth", "b0"))
            fieldList.add(FieldInfo("ChatMessageCell", "timeWidth", "Lb"))
            fieldList.add(FieldInfo("UItem", "id", "d"))
            fieldList.add(FieldInfo("UItem", "text", "l"))
            fieldList.add(FieldInfo("UItem", "subtext", "m"))
            fieldList.add(FieldInfo("LaunchActivity", "frameLayout", "L"))
            fieldList.add(FieldInfo("PhotoViewer", "galleryButton", "h0"))
            fieldList.add(FieldInfo("PhotoViewer\$PlaceProviderObject", "imageReceiver", "a"))
            fieldList.add(FieldInfo("SettingsActivity\$SettingCell", "iconView", "d"))

            // Method
            methodList.add(MethodInfo("AndroidUtilities", "isTabletInternal", "z4"))
            methodList.add(MethodInfo("LocaleController", "formatShortNumber", "D0"))
            methodList.add(MethodInfo("LocaleController", "formatYearMont", "W0"))
            methodList.add(MethodInfo("LocaleController", "getInstance", "F1"))
            methodList.add(MethodInfo("MessagesController", "checkPromoInfoInternal", "Q9"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJIZI", "pa"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJIZIZ", "qa"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJZIZJOI", "ra"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJZIZJOIZI", "sa"))
            methodList.add(MethodInfo("MessagesController", "getGlobalMainSettings", "Ub"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelJ", "Yb"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelO", "Zb"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelO2", "ac"))
            methodList.add(MethodInfo("MessagesController", "getInstance", "mc"))
            methodList.add(MethodInfo("MessagesController", "isChatNoForwardsJ", "Ed"))
            methodList.add(MethodInfo("MessagesController", "isChatNoForwardsO", "Fd"))
            methodList.add(MethodInfo("MessagesController", "processNewDifferenceParams", "Yf"))
            methodList.add(MethodInfo("MessagesController", "removePromoDialog", "Hg"))
            methodList.add(MethodInfo("MessagesController", "storiesEnabled", "Mh"))
            methodList.add(MethodInfo("MessagesController", "storyEntitiesAllowed", "Nh"))
            methodList.add(MethodInfo("MessagesController", "storyEntitiesAllowedO", "Oh"))
            methodList.add(MethodInfo("MessagesStorage", "getDatabase", "G5"))
            methodList.add(MethodInfo("MessagesStorage", "getInstance", "U5"))
            methodList.add(MethodInfo("MessagesStorage", "getStorageQueue", "n6"))
            methodList.add(MethodInfo("MessagesStorage", "markMessagesAsDeletedJAZZII", "u7"))
            methodList.add(MethodInfo("MessagesStorage", "markMessagesAsDeletedJIZZ", "t7"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesAZZZIIJ", "X7"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesAZZZIZIJ", "Y7"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesOJIIZIJ", "Z7"))
            methodList.add(MethodInfo("NotificationCenter", "postNotificationName", "C"))
            methodList.add(MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "s1"))
            methodList.add(MethodInfo("SharedConfig", "isAppUpdateAvailable", "p"))
            methodList.add(MethodInfo("UserConfig", "getClientUserId", "v"))
            methodList.add(MethodInfo("UserConfig", "getCurrentUser", "w"))
            methodList.add(MethodInfo("UserConfig", "isPremium", "N"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIC", "a0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIC", "d0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIICO", "e0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIICZ", "f0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIDCZZ", "b0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIDCZZO", "c0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIV", "Z"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIVII", "g0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemVII", "h0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIDC", "N0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIIC", "M0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIIDCZZ", "L0"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "create", "c"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "getDismissRunnable", "f"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setMessage", "x"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setNegativeButton", "z"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setNeutralButton", "A"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setPositiveButton", "F"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setTitle", "H"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setViewV", "O"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setViewVI", "P"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "show", "R"))
            methodList.add(MethodInfo("AlertDialog\$OnButtonClickListener", "onClick", "a"))
            methodList.add(MethodInfo("Theme", "isCurrentThemeDark", "V3"))
            methodList.add(MethodInfo("ChatMessageCell", "measureTime", "V6"))
            methodList.add(MethodInfo("HeaderCell", "setTextC", "setText"))
            methodList.add(MethodInfo("HeaderCell", "setTextCZ", "d"))
            methodList.add(MethodInfo("TextCheckCell", "isChecked", "d"))
            methodList.add(MethodInfo("TextCheckCell", "setTextAndCheck", "i"))
            methodList.add(MethodInfo("TextCheckCell", "setTextAndValueAndCheck", "j"))
            methodList.add(MethodInfo("TextSettingsCell", "setText", "c"))
            methodList.add(MethodInfo("TextSettingsCell", "setTextAndValueCCZ", "g"))
            methodList.add(MethodInfo("TextSettingsCell", "setTextAndValueCCZZ", "h"))
            methodList.add(MethodInfo("ChatActivity", "createPinnedMessageView", "Db"))
            methodList.add(MethodInfo("ChatActivity", "hasSelectedNoforwardsMessage", "Oc"))
            methodList.add(MethodInfo("ChatActivity", "processSelectedOption", "we"))
            methodList.add(MethodInfo("ChatActivity", "sendSecretMediaDelete", "Se"))
            methodList.add(MethodInfo("ChatActivity", "sendSecretMessageRead", "Te"))
            methodList.add(MethodInfo("ChatActivity", "updatePinnedMessageViewZ", "rg"))
            methodList.add(MethodInfo("ChatActivity", "updatePinnedMessageViewZI", "sg"))
            methodList.add(MethodInfo("ChatActivity\$ChatMessageCellDelegate", "didPressImage", "F"))
            methodList.add(MethodInfo("PhotoViewer", "getInstance", "Kc"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoAIJJJO", "de"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoAIO", "ee"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoIOO", "ce"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOIOJJJO", "fe"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOJJJOZ", "ge"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOO", "oe"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOJJJO", "ie"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOO", "je"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOOOAAAIOOJJJZOI", "he"))
            methodList.add(MethodInfo("PhotoViewer", "setIsAboutToSwitchToIndexIZZ", "Ye"))
            methodList.add(MethodInfo("PhotoViewer", "setIsAboutToSwitchToIndexIZZZ", "ff"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityA", "mf"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityAO", "of"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityAOO", "nf"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityO", "pf"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityOO", "qf"))
            methodList.add(MethodInfo("SettingsActivity", "fillItems", "r0"))
            methodList.add(MethodInfo("SettingsActivity", "onClick", "v0"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell", "set", "a"))
            methodList.add(MethodInfo("PeerStoriesView\$StoryItemHolder", "allowScreenshots", "d"))
            methodList.add(MethodInfo("StoriesController", "hasStories", "h1"))
            methodList.add(MethodInfo("StoriesController", "hasStoriesJ", "i1"))

            methodAlias["MessagesController#storyEntitiesAllowed2"] = "storyEntitiesAllowedO"
            methodAlias["StoriesController#hasStories2"] = "hasStoriesJ"
            methodAlias["PhotoViewer#setIsAboutToSwitchToIndex"] = "setIsAboutToSwitchToIndexIZZZ"
            methodAlias["MessagesStorage#markMessagesAsDeleted"] = "markMessagesAsDeletedJAZZII"
            methodAlias["MessagesController#deleteMessages"] = "deleteMessagesAAOJZIZJOIZI"
            methodAlias["TextSettingsCell#setTextAndValue"] = "setTextAndValueCCZZ"
            methodAlias["MessagesStorage#putMessages"] = "putMessagesOJIIZIJ"
            methodAlias["MessagesController#isChatNoForwards"] = "isChatNoForwardsO"
            methodAlias["ChatActivity#updatePinnedMessageView"] = "updatePinnedMessageViewZI"
            methodAlias["PhotoViewer#openPhoto"] = "openPhotoOJJJOZ"
            methodAlias["HeaderCell#setText"] = "setTextC"
            methodAlias["AlertDialog\$Builder#setView"] = "setViewV"

            val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
            if (msgObjClass != null) {
                ParameterResolver.register(
                    "fillMessageMenu",
                    arrayOf(
                        msgObjClass,
                        msgObjClass,
                        ArrayList::class.java,
                        ArrayList::class.java,
                        ArrayList::class.java
                    )
                )
            }
        }

        @JvmStatic
        fun removeAd() {
            try {
                val connectionsManager = ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER)
                if (connectionsManager != null) {
                    HMethod.hookMethod(
                        connectionsManager,
                        "native_expireFile",
                        Long::class.javaPrimitiveType!!,
                        object : XC_MethodReplacement() {
                            override fun replaceHookedMethod(param: MethodHookParam): Any {
                                return false
                            }
                        }
                    )
                    HMethod.hookMethod(
                        connectionsManager,
                        "native_daysFile",
                        Long::class.javaPrimitiveType!!,
                        object : XC_MethodReplacement() {
                            override fun replaceHookedMethod(param: MethodHookParam): Any {
                                return 999
                            }
                        }
                    )
                    HMethod.hookMethod(
                        connectionsManager,
                        "native_checkLicense",
                        Long::class.javaPrimitiveType!!,
                        object : XC_MethodReplacement() {
                            override fun replaceHookedMethod(param: MethodHookParam): Any {
                                return true
                            }
                        }
                    )
                    HMethod.hookMethod(
                        connectionsManager,
                        "native_removeInstance",
                        Int::class.javaPrimitiveType!!,
                        Boolean::class.javaPrimitiveType!!,
                        object : XC_MethodReplacement() {
                            override fun replaceHookedMethod(param: MethodHookParam): Any {
                                return true
                            }
                        }
                    )
                }
            } catch (t: Throwable) {
                Logger.e(t)
            }
        }
    }

    object ClassResolver {
        @JvmStatic
        fun resolve(name: String): String? = classList.firstOrNull { it.original == name }?.resolved

        @JvmStatic
        fun has(name: String): Boolean = classList.any { it.original == name }
    }

    object FieldResolver {
        @JvmStatic
        fun resolve(className: String, name: String): String? =
            fieldList.firstOrNull { it.className == className && it.original == name }?.resolved

        @JvmStatic
        fun has(className: String, name: String): Boolean =
            fieldList.any { it.className == className && it.original == name }
    }

    object MethodResolver {
        @JvmStatic
        fun resolve(className: String, name: String): String? =
            methodList.firstOrNull { it.className == className && it.original == name }?.resolved

        @JvmStatic
        fun has(className: String, name: String): Boolean =
            methodList.any { it.className == className && it.original == name }
    }

    object ParameterResolver {
        private val objectList = HashMap<String, Array<Class<*>>>()

        @JvmStatic
        fun register(name: String, classes: Array<Class<*>>) {
            objectList[name] = classes
        }

        @JvmStatic
        fun resolve(name: String): Array<Class<*>>? = objectList[name]

        @JvmStatic
        fun has(name: String): Boolean = objectList.containsKey(name)
    }
}
