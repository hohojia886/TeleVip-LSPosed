package com.my.televip.Clients

import com.my.televip.obfuscate.struct.ClassInfo
import com.my.televip.obfuscate.struct.FieldInfo
import com.my.televip.obfuscate.struct.MethodInfo

class Nekogram {
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
            classList.add(ClassInfo("androidx.collection.LongSparseArray", "bd9"))
            classList.add(ClassInfo("org.telegram.messenger.AndroidUtilities", "org.telegram.messenger.b"))
            classList.add(ClassInfo("org.telegram.messenger.ApplicationLoader", "org.telegram.messenger.d"))
            classList.add(ClassInfo("org.telegram.messenger.DispatchQueue", "of5"))
            classList.add(ClassInfo("org.telegram.messenger.FileLoadOperation", "org.telegram.messenger.u"))
            classList.add(ClassInfo("org.telegram.messenger.FileLoader", "org.telegram.messenger.v"))
            classList.add(ClassInfo("org.telegram.messenger.LocaleController", "org.telegram.messenger.h0"))
            classList.add(ClassInfo("org.telegram.messenger.MessageObject", "org.telegram.messenger.l0"))
            classList.add(ClassInfo("org.telegram.messenger.MessagesController", "org.telegram.messenger.n0"))
            classList.add(ClassInfo("org.telegram.messenger.MessagesStorage", "org.telegram.messenger.o0"))
            classList.add(ClassInfo("org.telegram.messenger.NotificationCenter", "org.telegram.messenger.p0"))
            classList.add(ClassInfo("org.telegram.messenger.NotificationsController", "org.telegram.messenger.q0"))
            classList.add(ClassInfo("org.telegram.messenger.SharedConfig", "org.telegram.messenger.y0"))
            classList.add(ClassInfo("org.telegram.messenger.UserConfig", "org.telegram.messenger.f1"))
            classList.add(ClassInfo("org.telegram.messenger.browser.Browser", "m71"))
            classList.add(ClassInfo("org.telegram.messenger.time.FastDateFormat", "u76"))
            classList.add(ClassInfo("org.telegram.tgnet.QuickAckDelegate", "bzd"))
            classList.add(ClassInfo("org.telegram.tgnet.RequestDelegateTimestamp", "org.telegram.tgnet.a"))
            classList.add(ClassInfo("org.telegram.tgnet.TLObject", "hjh"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$Chat", "org.telegram.tgnet.TLRPC\$o"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$EncryptedChat", "org.telegram.tgnet.TLRPC\$p0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$InputPeer", "org.telegram.tgnet.TLRPC\$s1"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$Message", "org.telegram.tgnet.TLRPC\$k2"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$Peer", "org.telegram.tgnet.TLRPC\$c3"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_channels_readHistory", "org.telegram.tgnet.TLRPC\$pf"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_channels_readMessageContents", "org.telegram.tgnet.TLRPC\$qf"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_inputPeerChannel", "org.telegram.tgnet.TLRPC\$xx"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_affectedMessages", "org.telegram.tgnet.TLRPC\$fe0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_readDiscussion", "org.telegram.tgnet.TLRPC\$rl0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_readEncryptedHistory", "org.telegram.tgnet.TLRPC\$sl0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_readHistory", "org.telegram.tgnet.TLRPC\$ul0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_readMessageContents", "org.telegram.tgnet.TLRPC\$wl0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_sendMedia", "org.telegram.tgnet.TLRPC\$un0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_sendMessage", "org.telegram.tgnet.TLRPC\$vn0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_sendMultiMedia", "org.telegram.tgnet.TLRPC\$wn0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_sendPaidReaction", "org.telegram.tgnet.TLRPC\$xn0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_sendReaction", "org.telegram.tgnet.TLRPC\$zn0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_setEncryptedTyping", "org.telegram.tgnet.TLRPC\$ko0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$TL_messages_setTyping", "org.telegram.tgnet.TLRPC\$mo0"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$User", "org.telegram.tgnet.TLRPC\$y81"))
            classList.add(ClassInfo("org.telegram.tgnet.TLRPC\$messages_Messages", "org.telegram.tgnet.TLRPC\$sa1"))
            classList.add(ClassInfo("org.telegram.tgnet.WriteToSocketDelegate", "nqk"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_account\$updateStatus", "fvh"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_stories\$TL_stories_incrementStoryViews", "hji"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_stories\$TL_stories_readStories", "jji"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_update\$TL_updateDeleteChannelMessages", "zli"))
            classList.add(ClassInfo("org.telegram.tgnet.tl.TL_update\$TL_updateDeleteMessages", "bmi"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBar", "org.telegram.ui.ActionBar.a"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBar\$ActionBarMenuOnItemClick", "org.telegram.ui.ActionBar.a\$m"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.ActionBarMenuItem", "org.telegram.ui.ActionBar.d"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.AlertDialog\$OnButtonClickListener", "org.telegram.ui.ActionBar.AlertDialog\$l"))
            classList.add(ClassInfo("org.telegram.ui.ActionBar.Theme", "org.telegram.ui.ActionBar.r"))
            classList.add(ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "nk3"))
            classList.add(ClassInfo("org.telegram.ui.Cells.HeaderCell", "ek7"))
            classList.add(ClassInfo("org.telegram.ui.Cells.ShadowSectionCell", "uif"))
            classList.add(ClassInfo("org.telegram.ui.Cells.TextCheckCell", "iui"))
            classList.add(ClassInfo("org.telegram.ui.Cells.TextSettingsCell", "kwi"))
            classList.add(ClassInfo("org.telegram.ui.Components.UItem", "org.telegram.ui.Components.a5"))
            classList.add(ClassInfo("org.telegram.ui.Components.UniversalAdapter", "org.telegram.ui.Components.i5"))
            classList.add(ClassInfo("org.telegram.ui.PhotoViewer\$PhotoViewerProvider", "org.telegram.ui.PhotoViewer\$y2"))
            classList.add(ClassInfo("org.telegram.ui.PhotoViewer\$PlaceProviderObject", "org.telegram.ui.PhotoViewer\$z2"))
            classList.add(ClassInfo("org.telegram.ui.SettingsActivity\$SettingCell", "org.telegram.ui.SettingsActivity\$k"))
            classList.add(ClassInfo("org.telegram.ui.SettingsActivity\$SettingCell\$Factory", "org.telegram.ui.SettingsActivity\$k\$b"))
            classList.add(ClassInfo("org.telegram.ui.Stories.PeerStoriesView\$StoryItemHolder", "org.telegram.ui.Stories.PeerStoriesView\$q0"))
            classList.add(ClassInfo("org.telegram.ui.Stories.StoriesController", "org.telegram.ui.Stories.g"))

            // Field
            fieldList.add(FieldInfo("ApplicationLoader", "applicationContext", "b"))
            fieldList.add(FieldInfo("FileLoadOperation", "downloadChunkSizeBig", "l"))
            fieldList.add(FieldInfo("FileLoadOperation", "maxCdnParts", "q"))
            fieldList.add(FieldInfo("FileLoadOperation", "maxDownloadRequests", "n"))
            fieldList.add(FieldInfo("FileLoadOperation", "maxDownloadRequestsBig", "o"))
            fieldList.add(FieldInfo("LocaleController", "currentLocale", "y"))
            fieldList.add(FieldInfo("LocaleController", "isRTL", "S"))
            fieldList.add(FieldInfo("MessagesController", "dialogMessagesByIds", "T"))
            fieldList.add(FieldInfo("NotificationCenter", "messagesDeleted", "z"))
            fieldList.add(FieldInfo("NotificationCenter", "tlSchemeParseException", "s"))
            fieldList.add(FieldInfo("UserConfig", "clientUserId", "p"))
            fieldList.add(FieldInfo("UserConfig", "selectedAccount", "r0"))
            fieldList.add(FieldInfo("Utilities", "stageQueue", "d"))
            fieldList.add(FieldInfo("TLRPC\$InputPeer", "channel_id", "d"))
            fieldList.add(FieldInfo("TLRPC\$InputPeer", "chat_id", "e"))
            fieldList.add(FieldInfo("TLRPC\$InputPeer", "user_id", "c"))
            fieldList.add(FieldInfo("TLRPC\$Message", "flags", "k"))
            fieldList.add(FieldInfo("TLRPC\$Message", "from_id", "b"))
            fieldList.add(FieldInfo("TLRPC\$Message", "id", "a"))
            fieldList.add(FieldInfo("TLRPC\$Message", "message", "i"))
            fieldList.add(FieldInfo("TLRPC\$Message", "ttl", "m0"))
            fieldList.add(FieldInfo("TLRPC\$Peer", "channel_id", "c"))
            fieldList.add(FieldInfo("TLRPC\$Peer", "chat_id", "b"))
            fieldList.add(FieldInfo("TLRPC\$Peer", "user_id", "a"))
            fieldList.add(FieldInfo("TLRPC\$TL_channels_readHistory", "channel", "a"))
            fieldList.add(FieldInfo("TLRPC\$TL_channels_readHistory", "max_id", "b"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_affectedMessages", "pts", "a"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_affectedMessages", "pts_count", "b"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_readHistory", "max_id", "b"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_readHistory", "peer", "a"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_readDiscussion", "peer", "a"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_sendMedia", "peer", "h"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_sendMessage", "peer", "j"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_sendMultiMedia", "peer", "h"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_sendPaidReaction", "peer", "c"))
            fieldList.add(FieldInfo("TLRPC\$TL_messages_sendReaction", "peer", "d"))
            fieldList.add(FieldInfo("TLRPC\$User", "phone", "f"))
            fieldList.add(FieldInfo("TLRPC\$messages_Messages", "messages", "a"))
            fieldList.add(FieldInfo("TL_account\$updateStatus", "offline", "a"))
            fieldList.add(FieldInfo("TL_update\$TL_updateDeleteChannelMessages", "channel_id", "a"))
            fieldList.add(FieldInfo("TL_update\$TL_updateDeleteChannelMessages", "messages", "b"))
            fieldList.add(FieldInfo("TL_update\$TL_updateDeleteMessages", "messages", "a"))
            fieldList.add(FieldInfo("Theme", "chat_timePaint", "U2"))
            fieldList.add(FieldInfo("MessagesController", "dialogMessage", "Q"))

            // Method
            methodList.add(MethodInfo("LongSparseArray", "getJ", "h"))
            methodList.add(MethodInfo("LongSparseArray", "getJO", "i"))
            methodList.add(MethodInfo("SQLiteCursor", "byteBufferValue", "b"))
            methodList.add(MethodInfo("SQLiteCursor", "dispose", "d"))
            methodList.add(MethodInfo("SQLiteCursor", "intValue", "g"))
            methodList.add(MethodInfo("SQLiteCursor", "longValue", "i"))
            methodList.add(MethodInfo("SQLiteCursor", "next", "j"))
            methodList.add(MethodInfo("SQLiteDatabase", "executeFast", "e"))
            methodList.add(MethodInfo("SQLiteDatabase", "queryFinalized", "h"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindByteBufferIB", "b"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindByteBufferIO", "a"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindByteBufferJIBI", "bindByteBuffer"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindInteger", "c"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindLongIJ", "d"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "bindLongJIJ", "bindLong"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "dispose", "i"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "requery", "m"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "step", "n"))
            methodList.add(MethodInfo("SQLitePreparedStatement", "stepJ", "step"))
            methodList.add(MethodInfo("AndroidUtilities", "isTabletInternal", "B3"))
            methodList.add(MethodInfo("DispatchQueue", "postRunnableR", "k"))
            methodList.add(MethodInfo("DispatchQueue", "postRunnableRJ", "l"))
            methodList.add(MethodInfo("FileLoadOperation", "updateParams", "b1"))
            methodList.add(MethodInfo("FileLoader", "getInstance", "K0"))
            methodList.add(MethodInfo("FileLoader", "getPathToMessageO", "U0"))
            methodList.add(MethodInfo("FileLoader", "getPathToMessageOZ", "V0"))
            methodList.add(MethodInfo("FileLoader", "getPathToMessageOZZ", "W0"))
            methodList.add(MethodInfo("ImageReceiver", "getImageLocation", "K"))
            methodList.add(MethodInfo("LocaleController", "formatShortNumber", "E0"))
            methodList.add(MethodInfo("LocaleController", "formatYearMont", "X0"))
            methodList.add(MethodInfo("LocaleController", "getInstance", "D1"))
            methodList.add(MethodInfo("MessageObject", "canForwardMessage", "O"))
            methodList.add(MethodInfo("MessageObject", "getDialogId", "i1"))
            methodList.add(MethodInfo("MessageObject", "getDialogIdO", "j1"))
            methodList.add(MethodInfo("MessageObject", "isMusic", "y5"))
            methodList.add(MethodInfo("MessageObject", "isSecret", "v4"))
            methodList.add(MethodInfo("MessageObject", "isVoice", "T6"))
            methodList.add(MethodInfo("MessagesController", "checkPromoInfoInternal", "x9"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJIZI", "W9"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJIZIZ", "X9"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJZIZJOI", "Y9"))
            methodList.add(MethodInfo("MessagesController", "deleteMessagesAAOJZIZJOIZI", "Z9"))
            methodList.add(MethodInfo("MessagesController", "getGlobalMainSettings", "wb"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelJ", "Ab"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelO", "Bb"))
            methodList.add(MethodInfo("MessagesController", "getInputChannelO2", "Cb"))
            methodList.add(MethodInfo("MessagesController", "getInstance", "Nb"))
            methodList.add(MethodInfo("MessagesController", "isChatNoForwardsJ", "ed"))
            methodList.add(MethodInfo("MessagesController", "isChatNoForwardsO", "fd"))
            methodList.add(MethodInfo("MessagesController", "processNewDifferenceParams", "zn"))
            methodList.add(MethodInfo("MessagesController", "removePromoDialog", "io"))
            methodList.add(MethodInfo("MessagesController", "storiesEnabled", "lp"))
            methodList.add(MethodInfo("MessagesController", "storyEntitiesAllowed", "mp"))
            methodList.add(MethodInfo("MessagesController", "storyEntitiesAllowedO", "np"))
            methodList.add(MethodInfo("MessagesStorage", "getDatabase", "u5"))
            methodList.add(MethodInfo("MessagesStorage", "getInstance", "I5"))
            methodList.add(MethodInfo("MessagesStorage", "getStorageQueue", "b6"))
            methodList.add(MethodInfo("MessagesStorage", "markMessagesAsDeletedJAZZII", "Hb"))
            methodList.add(MethodInfo("MessagesStorage", "markMessagesAsDeletedJIZZ", "Gb"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesAZZZIIJ", "ic"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesAZZZIZIJ", "jc"))
            methodList.add(MethodInfo("MessagesStorage", "putMessagesOJIIZIJ", "kc"))
            methodList.add(MethodInfo("NotificationCenter", "postNotificationName", "G"))
            methodList.add(MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "Y1"))
            methodList.add(MethodInfo("SharedConfig", "isAppUpdateAvailable", "N"))
            methodList.add(MethodInfo("SharedConfig", "setNewAppVersionAvailable", "q0"))
            methodList.add(MethodInfo("UserConfig", "getClientUserId", "n"))
            methodList.add(MethodInfo("UserConfig", "getCurrentUser", "o"))
            methodList.add(MethodInfo("UserConfig", "isPremium", "C"))
            methodList.add(MethodInfo("Browser", "openUrlCS", "H"))
            methodList.add(MethodInfo("Browser", "openUrlCSZ", "I"))
            methodList.add(MethodInfo("Browser", "openUrlCSZZ", "J"))
            methodList.add(MethodInfo("Browser", "openUrlCU", "C"))
            methodList.add(MethodInfo("Browser", "openUrlCUZ", "D"))
            methodList.add(MethodInfo("Browser", "openUrlCUZZ", "E"))
            methodList.add(MethodInfo("Browser", "openUrlCUZZO", "F"))
            methodList.add(MethodInfo("Browser", "openUrlCUZZZOSZZZ", "G"))
            methodList.add(MethodInfo("FastDateFormat", "formatD", "c"))
            methodList.add(MethodInfo("FastDateFormat", "formatJ", "a"))
            methodList.add(MethodInfo("FastDateFormat", "formatOSF", "format"))
            methodList.add(MethodInfo("TLRPC\$Message", "TLdeserialize", "c"))
            methodList.add(MethodInfo("TLRPC\$Message", "readAttachPath", "d"))
            methodList.add(MethodInfo("ActionBar", "setActionBarMenuOnItemClick", "G0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIC", "b0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIC", "e0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIICO", "f0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIICZ", "g0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIDCZZ", "c0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIIDCZZO", "d0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIV", "a0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemIVII", "i0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "addSubItemVII", "j0"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIDC", "h1"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIIC", "g1"))
            methodList.add(MethodInfo("ActionBarMenuItem", "lazilyAddSubItemIIDCZZ", "f1"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "create", "c"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "getDismissRunnable", "f"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setMessage", "t"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setNegativeButton", "v"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setNeutralButton", "w"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setPositiveButton", "B"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setTitle", "D"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setViewV", "K"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "setViewVI", "L"))
            methodList.add(MethodInfo("AlertDialog\$Builder", "show", "N"))
            methodList.add(MethodInfo("AlertDialog\$OnButtonClickListener", "onClick", "a"))
            methodList.add(MethodInfo("Theme", "isCurrentThemeDark", "Y2"))
            methodList.add(MethodInfo("ChatMessageCell", "getMessageObject", "q"))
            methodList.add(MethodInfo("ChatMessageCell", "measureTime", "x8"))
            methodList.add(MethodInfo("HeaderCell", "setTextC", "j"))
            methodList.add(MethodInfo("HeaderCell", "setTextCZ", "k"))
            methodList.add(MethodInfo("TextCheckCell", "isChecked", "f"))
            methodList.add(MethodInfo("TextCheckCell", "setChecked", "k"))
            methodList.add(MethodInfo("TextCheckCell", "setTextAndCheck", "r"))
            methodList.add(MethodInfo("TextCheckCell", "setTextAndValueAndCheck", "s"))
            methodList.add(MethodInfo("TextSettingsCell", "setText", "j"))
            methodList.add(MethodInfo("TextSettingsCell", "setTextAndValueCCZ", "k"))
            methodList.add(MethodInfo("TextSettingsCell", "setTextAndValueCCZZ", "l"))
            methodList.add(MethodInfo("ChatActivity", "createPinnedMessageView", "Ku"))
            methodList.add(MethodInfo("ChatActivity", "createView", "C0"))
            methodList.add(MethodInfo("ChatActivity", "fillMessageMenu", "vv"))
            methodList.add(MethodInfo("ChatActivity", "hasSelectedNoforwardsMessage", "Yw"))
            methodList.add(MethodInfo("ChatActivity", "isSwipeBackEnabled", "S1"))
            methodList.add(MethodInfo("ChatActivity", "processSelectedOption", "RH"))
            methodList.add(MethodInfo("ChatActivity", "scrollToMessageIdIIZIZI", "L"))
            methodList.add(MethodInfo("ChatActivity", "scrollToMessageIdIIZIZIIABR", "xI"))
            methodList.add(MethodInfo("ChatActivity", "scrollToMessageIdIIZIZIIR", "wI"))
            methodList.add(MethodInfo("ChatActivity", "sendSecretMediaDelete", "JI"))
            methodList.add(MethodInfo("ChatActivity", "sendSecretMessageRead", "KI"))
            methodList.add(MethodInfo("ChatActivity", "updatePinnedMessageViewZ", "YK"))
            methodList.add(MethodInfo("ChatActivity", "updatePinnedMessageViewZI", "ZK"))
            methodList.add(MethodInfo("ChatActivity\$ChatMessageCellDelegate", "didPressImage", "j0"))
            methodList.add(MethodInfo("PhotoViewer", "getInstance", "Fc"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoAIJJJO", "bh"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoAIO", "ch"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoIOO", "ah"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOIOJJJO", "dh"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOJJJOZ", "eh"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOO", "ih"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOJJJO", "gh"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOO", "hh"))
            methodList.add(MethodInfo("PhotoViewer", "openPhotoOOOOAAAIOOJJJZOI", "fh"))
            methodList.add(MethodInfo("PhotoViewer", "setIsAboutToSwitchToIndexIZZ", "Wh"))
            methodList.add(MethodInfo("PhotoViewer", "setIsAboutToSwitchToIndexIZZZ", "ci"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityA", "hi"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityAO", "ji"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityAOO", "ii"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityO", "ki"))
            methodList.add(MethodInfo("PhotoViewer", "setParentActivityOO", "li"))
            methodList.add(MethodInfo("PhotoViewer\$PhotoViewerProvider", "getPlaceForPhoto", "n"))
            methodList.add(MethodInfo("ProfileActivity", "isSwipeBackEnabled", "S1"))
            methodList.add(MethodInfo("SecretMediaViewer", "closePhoto", "o0"))
            methodList.add(MethodInfo("SecretMediaViewer", "openMedia", "N0"))
            methodList.add(MethodInfo("SettingsActivity", "fillItems", "m4"))
            methodList.add(MethodInfo("SettingsActivity", "onClick", "M4"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell", "set", "a"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell\$Factory", "ofIIIIC", "k"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell\$Factory", "ofIIIICC", "l"))
            methodList.add(MethodInfo("SettingsActivity\$SettingCell\$Factory", "ofIIIICCC", "m"))
            methodList.add(MethodInfo("PeerStoriesView\$StoryItemHolder", "allowScreenshots", "d"))
            methodList.add(MethodInfo("StoriesController", "hasStories", "h1"))
            methodList.add(MethodInfo("StoriesController", "hasStoriesJ", "i1"))

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
