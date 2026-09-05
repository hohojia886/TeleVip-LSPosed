package com.my.televip.Clients

import com.my.televip.obfuscate.struct.ClassInfo
import com.my.televip.obfuscate.struct.FieldInfo
import com.my.televip.obfuscate.struct.MethodInfo

class Cherrygram {
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
            classList.add(ClassInfo("androidx.collection.LongSparseArray", "tx3"))
            classList.add(ClassInfo("org.telegram.messenger.AndroidUtilities", "org.telegram.messenger.ᵎ"))
            classList.add(ClassInfo("org.telegram.messenger.ApplicationLoader", "org.telegram.messenger.㨤"))
            classList.add(ClassInfo("org.telegram.messenger.DispatchQueue", "ee"))
            classList.add(ClassInfo("org.telegram.messenger.FileLoadOperation", "org.telegram.messenger.ₛ"))
            classList.add(ClassInfo("org.telegram.messenger.FileLoader", "org.telegram.messenger.ᶎ"))
            classList.add(ClassInfo("org.telegram.messenger.LocaleController", "org.telegram.messenger.ဪ"))
            classList.add(ClassInfo("org.telegram.messenger.MessageObject", "org.telegram.messenger.㕽"))
            classList.add(ClassInfo("org.telegram.messenger.MessagesController", "org.telegram.messenger.ឌ"))
            classList.add(ClassInfo("org.telegram.messenger.MessagesStorage", "org.telegram.messenger.㦴"))
            classList.add(ClassInfo("org.telegram.messenger.NotificationCenter", "org.telegram.messenger.㝊"))
            classList.add(ClassInfo("org.telegram.messenger.NotificationsController", "org.telegram.messenger.ፀ"))
            classList.add(ClassInfo("org.telegram.messenger.SharedConfig", "org.telegram.messenger.ᣃ"))
            classList.add(ClassInfo("org.telegram.messenger.UserConfig", "org.telegram.messenger.ϰ"))
            classList.add(ClassInfo("org.telegram.messenger.browser.Browser", "㕅"))
            classList.add(ClassInfo("org.telegram.messenger.time.FastDateFormat", "ha0"))
            classList.add(ClassInfo("org.telegram.tgnet.QuickAckDelegate", "iw8"))
            classList.add(ClassInfo("org.telegram.tgnet.RequestDelegateTimestamp", "org.telegram.tgnet.ㄙ"))
            classList.add(ClassInfo("org.telegram.tgnet.TLObject", "iuc"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$Chat", "org.telegram.tgnet.TLRPC\$ᑃ"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$EncryptedChat", "org.telegram.tgnet.TLRPC\$ឌ"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$InputPeer", "org.telegram.tgnet.TLRPC\$㓄"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$Message", "org.telegram.tgnet.TLRPC\$ⱂ"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$Peer", "org.telegram.tgnet.TLRPC\$ᳶ"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$User", "org.telegram.tgnet.TLRPC\$Ḕ"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$messages_Messages", "org.telegram.tgnet.TLRPC\$㒸"))
            classList.add(ClassInfo("org.telegram.tgnet.WriteToSocketDelegate", "vag"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_account\$updateStatus", "p6d"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_stories\$TL_stories_incrementStoryViews", "tud"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_stories\$TL_stories_readStories", "vud"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_update\$TL_updateDeleteChannelMessages", "lxd"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_update\$TL_updateDeleteMessages", "nxd"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBar", "org.telegram.ui.ActionBar.ㄙ"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBar\$ActionBarMenuOnItemClick", "org.telegram.ui.ActionBar.ㄙ\$ㆁ"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBarMenuItem", "org.telegram.ui.ActionBar.㨤"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.AlertDialog\$OnButtonClickListener", "org.telegram.ui.ActionBar.AlertDialog\$ᅥ"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.Theme", "org.telegram.ui.ActionBar.ॷ"))
            classList.add(ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "㢬"))
            classList.add(ClassInfo("org.telegram.ui.Cells.HeaderCell", "mu1"))
            classList.add(ClassInfo("org.telegram.ui.Cells.ShadowSectionCell", "toa"))
            classList.add(ClassInfo("org.telegram.ui.Cells.TextCheckCell", "w6e"))
            classList.add(ClassInfo("org.telegram.ui.Cells.TextSettingsCell", "d9e"))
            classList.add(ClassInfo("org.telegram.ui.Components.UItem", "org.telegram.ui.Components.ⱒ"))
            classList.add(ClassInfo("org.telegram.ui.Components.UniversalAdapter", "org.telegram.ui.Components.щ"))
            classList.add(ClassInfo("org.telegram.ui.PhotoViewer\$PhotoViewerProvider", "org.telegram.ui.PhotoViewer\$Ⲅ"))
            classList.add(ClassInfo("org.telegram.ui.PhotoViewer\$PlaceProviderObject", "org.telegram.ui.PhotoViewer\$㧶"))
            classList.add(ClassInfo("org.telegram.ui.SettingsActivity\$SettingCell", "org.telegram.ui.SettingsActivity\$ᚏ"))
            classList.add(ClassInfo("org.telegram.ui.SettingsActivity\$SettingCell\$Factory", "org.telegram.ui.SettingsActivity\$ᚏ\$ᵎ"))
            classList.add(ClassInfo("org.telegram.ui.Stories.PeerStoriesView\$StoryItemHolder", "org.telegram.ui.Stories.PeerStoriesView\$㦴"))
            classList.add(ClassInfo("org.telegram.ui.Stories.StoriesController", "org.telegram.ui.Stories.ࠔ"))

            // Field
            fieldList.add(FieldInfo("ApplicationLoader", "applicationContext", "ᣒ"))
            fieldList.add(FieldInfo("FileLoadOperation", "downloadChunkSizeBig", "থ"))
            fieldList.add(FieldInfo("FileLoadOperation", "maxCdnParts", "ゞ"))
            fieldList.add(FieldInfo("FileLoadOperation", "maxDownloadRequests", "ӆ"))
            fieldList.add(FieldInfo("FileLoadOperation", "maxDownloadRequestsBig", "ڌ"))
            fieldList.add(FieldInfo("LocaleController", "currentLocale", "㩂"))
            fieldList.add(FieldInfo("LocaleController", "isRTL", "ᅀ"))
            fieldList.add(FieldInfo("MessagesController", "dialogMessage", "㜁"))
            fieldList.add(FieldInfo("MessagesController", "dialogMessagesByIds", "ᱳ"))
            fieldList.add(FieldInfo("NotificationCenter", "messagesDeleted", "㩂"))
            fieldList.add(FieldInfo("NotificationCenter", "tlSchemeParseException", "ᣎ"))
            fieldList.add(FieldInfo("UserConfig", "clientUserId", "㜜"))
            fieldList.add(FieldInfo("UserConfig", "selectedAccount", "ⳡ"))
            fieldList.add(FieldInfo("Utilities", "stageQueue", "㟹"))
            fieldList.add(FieldInfo("Theme", "chat_timePaint", "㠧"))

            // Method
            methodList.add(MethodInfo("LongSparseArray", "getJ", "ম"))
            methodList.add(MethodInfo("LongSparseArray", "getJO", "㞬"))
            methodList.add(MethodInfo("SQLiteCursor", "byteBufferValue", "Ӵ"))
            methodList.add(MethodInfo("SQLiteCursor", "dispose", "㟹"))
            methodList.add(MethodInfo("SQLiteCursor", "intValue", "ᡶ"))
            methodList.add(MethodInfo("SQLiteCursor", "longValue", "ম"))
            methodList.add(MethodInfo("SQLiteCursor", "next", "㞬"))
            methodList.add(MethodInfo("SQLiteDatabase", "executeFast", "<ctrl42>"))
            methodList.add(MethodInfo("SQLiteDatabase", "queryFinalized", "ㄆ"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindByteBufferIB", "Ӵ"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindByteBufferIO", "ạ"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindByteBufferJIBI", "bindByteBuffer"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindInteger", "ᄅ"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindLongIJ", "㟹"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindLongJIJ", "bindLong"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "dispose", "ম"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "requery", "ぎ"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "step", "ӆ"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "stepJ", "step"))
            methodList.add(MethodInfo("AndroidUtilities", "isTabletInternal", "ᡎ"))
            methodList.add(MethodInfo("DispatchQueue", "postRunnableR", "থ"))
            methodList.add(MethodInfo("DispatchQueue", "postRunnableRJ", "ぎ"))
            methodList.add(MethodInfo("FileLoadOperation", "updateParams", "ὲ"))
            methodList.add(MethodInfo("FileLoader", "getInstance", "㧸"))
            methodList.add(MethodInfo("FileLoader", "getPathToMessageO", "ඍ"))
            methodList.add(MethodInfo("FileLoader", "getPathToMessageOZ", "㠼"))
            methodList.add(MethodInfo("FileLoader", "getPathToMessageOZZ", "㐞"))
            methodList.add(MethodInfo("ImageReceiver", "getImageLocation", "ⳑ"))
            methodList.add(MethodInfo("LocaleController", "formatShortNumber", "ሲ"))
            methodList.add(MethodInfo("LocaleController", "formatYearMont", "ⴗ"))
            methodList.add(MethodInfo("LocaleController", "getInstance", "㠶"))
            methodList.add(MethodInfo("MessageObject", "canForwardMessage", "ᆈ"))
            methodList.add(MethodInfo("MessageObject", "getDialogId", "ᴢ"))
            methodList.add(MethodInfo("MessageObject", "getDialogIdO", "㘥"))
            methodList.add(MethodInfo("MessageObject", "isMusic", "㥊"))
            methodList.add(MethodInfo("MessageObject", "isSecret", "ᢊ"))
            methodList.add(MethodInfo("MessageObject", "isVoice", "㔂"))
            methodList.add(MethodInfo("MessagesController", "checkPromoInfoInternal", "ᅱ"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJIZI", "㛿"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJIZIZ", "㐚"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJZIZJOI", "ᇟ"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJZIZJOIZI", "ↇ"))
            methodList.add(MethodInfo("MessagesController", "getGlobalMainSettings", "㝨"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelJ", "ᘉ"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelO", "㤖"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelO2", "ඨ"))
            methodList.add(MethodInfo("MessagesController", "getInstance", "㫈"))
            methodList.add(MethodInfo("MessagesController", "isChatNoForwardsJ", "ఴ"))
            methodList.add(MethodInfo("MessagesController", "isChatNoForwardsO", "ઢ"))
            methodList.add(MethodInfo("MessagesController", "processNewDifferenceParams", "Ꮏ"))
            methodList.add(MethodInfo("MessagesController", "removePromoDialog", "ب"))
            methodList.add(MethodInfo("MessagesController", "storiesEnabled", "π"))
            methodList.add(MethodInfo("MessagesController", "storyEntitiesAllowed", "ܭ"))
            methodList.add(MethodInfo("MessagesController", "storyEntitiesAllowedO", "Δ"))
            methodList.add(MethodInfo("MessagesStorage", "getDatabase", "ϲ"))
            methodList.add(MethodInfo("MessagesStorage", "getInstance", "χ"))
            methodList.add(MethodInfo("MessagesStorage", "getStorageQueue", "Ꮸ"))
            methodList.add(MethodInfo("MessagesStorage", "markMessagesAsDeletedJAZZII", "㤖"))
            methodList.add(MethodInfo("MessagesStorage", "markMessagesAsDeletedJIZZ", "ᘉ"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesAZZZIIJ", "ᇞ"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesAZZZIZIJ", "〲"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesOJIIZIJ", "ᬛ"))
            methodList.add(MethodInfo("NotificationCenter", "postNotificationName", "ᇅ"))
            methodList.add(MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "㗦"))
            methodList.add(MethodInfo("SharedConfig", "isAppUpdateAvailable", "㘿"))
            methodList.add(MethodInfo("SharedConfig", "setNewAppVersionAvailable", "ᬑ"))
            methodList.add(MethodInfo("UserConfig", "getClientUserId", "ӆ"))
            methodList.add(MethodInfo("UserConfig", "getCurrentUser", "ڌ"))
            methodList.add(MethodInfo("UserConfig", "isPremium", "㐬"))
            methodList.add(MethodInfo("Browser", "openUrlCS", "ཡ"))
            methodList.add(MethodInfo("Browser", "openUrlCSZ", "ຫ"))
            methodList.add(MethodInfo("Browser", "openUrlCSZZ", "㘿"))
            methodList.add(MethodInfo("Browser", "openUrlCU", "ⲗ"))
            methodList.add(MethodInfo("Browser", "openUrlCUZ", "<ctrl42>"))
            methodList.add(MethodInfo("Browser", "openUrlCUZZ", "ᇅ"))
            methodList.add(MethodInfo("Browser", "openUrlCUZZO", "Ⴙ"))
            methodList.add(MethodInfo("Browser", "openUrlCUZZZOSZZZ", "ណ"))
            methodList.add(MethodInfo("FastDateFormat", "formatD", "ᄅ"))
            methodList.add(MethodInfo("FastDateFormat", "formatJ", "ạ"))
            methodList.add(MethodInfo("FastDateFormat", "formatOSF", "format"))
            methodList.add(MethodInfo("TLRPC\$Message", "TLdeserialize", "ᄅ"))
            methodList.add(MethodInfo("TLRPC\$Message", "readAttachPath", "㟹"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIC", "ᛋ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIC", "ᦱ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIICO", "ⲹ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIICZ", "㪃"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIDCZZ", "ᔅ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIDCZZO", "Ⱑ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIV", "Ặ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIVII", "ڠ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemVII", "ㅽ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIDC", "ῦ"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIIC", "㐞"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIIDCZZ", "㠼"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "create", "ᄅ"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "getDismissRunnable", "ㅌ"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setMessage", "ʛ"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setNegativeButton", "ᅠ"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setNeutralButton", "㜜"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setPositiveButton", "ቕ"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setTitle", "<ctrl42>"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setViewV", "ຫ"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setViewVI", "㘿"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "show", "ᆈ"))
            methodList.add(MethodInfo("AlertDialog\$OnButtonClickListener", "onClick", "ạ"))
            methodList.add(MethodInfo("Theme", "isCurrentThemeDark", "ᖴ"))
            methodList.add(MethodInfo("ChatMessageCell", "measureTime", "ڢ"))
            methodList.add(MethodInfo("HeaderCell", "setTextC", "setText"))
            methodList.add(MethodInfo("HeaderCell", "setTextCZ", "㟹"))
            methodList.add(MethodInfo("TextCheckCell", "isChecked", "㟹"))
            methodList.add(MethodInfo("TextCheckCell", "setTextAndCheck", "ম"))
            methodList.add(MethodInfo("TextCheckCell", "setTextAndValueAndCheck", "㞬"))
            methodList.add(MethodInfo("TextSettingsCell", "setText", "ᄅ"))
            methodList.add(MethodInfo("TextSettingsCell", "setTextAndValueCCZ", "㟹"))
            methodList.add(MethodInfo("TextSettingsCell", "setTextAndValueCCZZ", "<ctrl42>"))
            methodList.add(MethodInfo("ChatActivity", "createPinnedMessageView", "ມ"))
            methodList.add(MethodInfo("ChatActivity", "fillMessageMenu", "ԧ"))
            methodList.add(MethodInfo("ChatActivity", "hasSelectedNoforwardsMessage", "た"))
            methodList.add(MethodInfo("ChatActivity", "processSelectedOption", "<ctrl42>"))
            methodList.add(MethodInfo("ChatActivity", "scrollToMessageIdIIZIZI", "<ctrl42>"))
            methodList.add(MethodInfo("ChatActivity", "scrollToMessageIdIIZIZIIABR", "ⴀ"))
            methodList.add(MethodInfo("ChatActivity", "scrollToMessageIdIIZIZIIR", "Ә"))
            methodList.add(MethodInfo("ChatActivity", "sendSecretMediaDelete", "ᐞ"))
            methodList.add(MethodInfo("ChatActivity", "sendSecretMessageRead", "㓖"))
            methodList.add(MethodInfo("ChatActivity", "updatePinnedMessageViewZ", "ፖ"))
            methodList.add(MethodInfo("ChatActivity", "updatePinnedMessageViewZI", "㙎"))
            methodList.add(MethodInfo("ChatActivity\$ChatMessageCellDelegate", "didPressImage", "㦃"))
            methodList.add(MethodInfo("PhotoViewer", "getInstance", "ኣ"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoAIJJJO", "ᡖ"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoAIO", "㧝"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoIOO", "<ctrl42>"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOIOJJJO", "㘺"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOJJJOZ", "㫐"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOO", "ߚ"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOJJJO", "ⅱ"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOO", "ᘐ"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOOOAAAIOOJJJZOI", "ܧ"))
            methodList.add(MethodInfo("PhotoViewer", "setIsAboutToSwitchToIndexIZZ", "㦲"))
            methodList.add(MethodInfo("PhotoViewer", "setIsAboutToSwitchToIndexIZZZ", "ԛ"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityA", "व"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityAO", "ᦲ"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityAOO", "ஒ"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityO", "㐽"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityOO", "㐖"))
            methodList.add(MethodInfo("PhotoViewer\$PhotoViewerProvider", "getPlaceForPhoto", "ӆ"))
            methodList.add(MethodInfo("SecretMediaViewer", "closePhoto", "㓁"))
            methodList.add(MethodInfo("SecretMediaViewer", "openMedia", "㒡"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell", "set", "ㅌ"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell\$Factory", "ofIIIIC", "থ"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell\$Factory", "ofIIIICC", "ぎ"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell\$Factory", "ofIIIICCC", "ӆ"))
            methodList.add(MethodInfo("PeerStoriesView\$StoryItemHolder", "allowScreenshots", "㟹"))
            methodList.add(MethodInfo("StoriesController", "hasStories", "ϼ"))
            methodList.add(MethodInfo("StoriesController", "hasStoriesJ", "ஏ"))
            methodList.add(MethodInfo("ProfileActivity", "updateProfileData", "ࢣ"))

            methodAlias["Browser#openUrl"] = "openUrlCS"
            methodAlias["MessagesController#storyEntitiesAllowed2"] = "storyEntitiesAllowedO"
            methodAlias["StoriesController#hasStories2"] = "hasStoriesJ"
            methodAlias["PhotoViewer#setIsAboutToSwitchToIndex"] = "setIsAboutToSwitchToIndexIZZZ"
            methodAlias["MessagesStorage#markMessagesAsDeleted"] = "markMessagesAsDeletedJAZZII"
            methodAlias["MessagesController#deleteMessages"] = "deleteMessagesAAOJZIZJOIZI"
            methodAlias["MessageObject#getDialogId"] = "getDialogIdO"
            methodAlias["TextSettingsCell#setTextAndValue"] = "setTextAndValueCCZZ"
            methodAlias["MessagesStorage#putMessages"] = "putMessagesOJIIZIJ"
            methodAlias["MessagesController#isChatNoForwards"] = "isChatNoForwardsO"
            methodAlias["ChatActivity#updatePinnedMessageView"] = "updatePinnedMessageViewZI"
            methodAlias["PhotoViewer#openPhoto"] = "openPhotoOJJJOZ"
            methodAlias["SettingsActivity\$SettingCell\$Factory#of"] = "ofIIIICC"
            methodAlias["HeaderCell#setText"] = "setTextC"
            methodAlias["DispatchQueue#postRunnable"] = "postRunnableR"
            methodAlias["SQLitePreparedStatement#bindByteBuffer"] = "bindByteBufferIO"
            methodAlias["SQLitePreparedStatement#bindLong"] = "bindLongIJ"
            methodAlias["LongSparseArray#get"] = "getJ"
            methodAlias["AlertDialog\$Builder#setView"] = "setViewV"
            methodAlias["FileLoader#getPathToMessage"] = "getPathToMessageO"
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
