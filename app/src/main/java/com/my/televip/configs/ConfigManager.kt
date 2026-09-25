package com.my.televip.configs

import com.my.televip.clients.ClientManager
import com.my.televip.clients.Telegraph
import com.my.televip.features.connections.DownloadSpeed
import com.my.televip.features.ghostMode.GhostMode
import com.my.televip.features.ghostMode.HidePhone
import com.my.televip.features.media.EnableSavingStories
import com.my.televip.features.media.PreventMedia
import com.my.televip.features.media.SecretMediaSave
import com.my.televip.features.media.VoiceToMusicHook
import com.my.televip.features.messages.MessageTimeModifier
import com.my.televip.features.messages.SaveEditsHistory
import com.my.televip.features.messages.ShowDeletedMessages
import com.my.televip.features.other.FixTLError
import com.my.televip.features.other.HideUpdateApp
import com.my.televip.features.other.RemovesContentSaving
import com.my.televip.features.other.TelePremium
import com.my.televip.features.otherfeatures.AlwaysSaveMedia
import com.my.televip.features.otherfeatures.CopyNameHook
import com.my.televip.features.otherfeatures.EditOnlineTextView
import com.my.televip.features.otherfeatures.FeatureInitializer
import com.my.televip.features.stories.DisableStories
import com.my.televip.features.ui.DisableChannelSwipeBack
import com.my.televip.features.ui.DisableNumberRounding
import com.my.televip.features.ui.DisableProfileSwipeBack
import com.my.televip.features.ui.HidePinnedMessages
import com.my.televip.features.ui.HideProxySponsor
import com.my.televip.features.ui.HijriDate
import com.my.televip.language.Keys
import com.my.televip.logging.Logger
import java.util.ArrayList

object ConfigManager {

    @JvmField
    val items: MutableList<ConfigItem> = ArrayList()

    @JvmStatic
    fun getItems(): List<ConfigItem> = items

    @JvmField
    var ghostModeSettings: ConfigItem? = null
    @JvmField
    var hideSeenPrivateChat: ConfigItem? = null
    @JvmField
    var hideSeenChannel: ConfigItem? = null
    @JvmField
    var hideSeen: ConfigItem? = null
    @JvmField
    var markReadAfterSend: ConfigItem? = null
    @JvmField
    var hideTyping: ConfigItem? = null
    @JvmField
    var hideStoryView: ConfigItem? = null
    @JvmField
    var hidePhone: ConfigItem? = null
    @JvmField
    var hideOnline: ConfigItem? = null
    @JvmField
    var onlineInfo: ConfigItem? = null

    @JvmField
    var shadows: ConfigItem? = null

    @JvmField
    var stories: ConfigItem? = null
    @JvmField
    var disableStories: ConfigItem? = null

    @JvmField
    var messages: ConfigItem? = null
    @JvmField
    var showDeletedMessages: ConfigItem? = null
    @JvmField
    var showMessageId: ConfigItem? = null
    @JvmField
    var saveEditsHistory: ConfigItem? = null

    @JvmField
    var connections: ConfigItem? = null
    @JvmField
    var downloadSpeed: ConfigItem? = null

    @JvmField
    var media: ConfigItem? = null
    @JvmField
    var secretMediaSave: ConfigItem? = null
    @JvmField
    var preventMedia: ConfigItem? = null
    @JvmField
    var enableSavingStories: ConfigItem? = null
    @JvmField
    var enableVoiceMessageSaving: ConfigItem? = null

    @JvmField
    var ui: ConfigItem? = null
    @JvmField
    var hidePinnedMessages: ConfigItem? = null
    @JvmField
    var disableChannelSwipeBack: ConfigItem? = null
    @JvmField
    var disableProfileSwipeBack: ConfigItem? = null
    @JvmField
    var hideProxySponsor: ConfigItem? = null
    @JvmField
    var showUserID: ConfigItem? = null
    @JvmField
    var customCalendar: ConfigItem? = null

    @JvmField
    var otherFeatures: ConfigItem? = null
    @JvmField
    var removesContentSaving: ConfigItem? = null
    @JvmField
    var telegramPremium: ConfigItem? = null
    @JvmField
    var disableNumberRounding: ConfigItem? = null
    @JvmField
    var hideUpdateApp: ConfigItem? = null
    @JvmField
    var fixTLError: ConfigItem? = null

    @JvmField
    var btnChannel: ConfigItem? = null
    @JvmField
    var btnRestartApp: ConfigItem? = null

    @JvmStatic
    fun loadAndRead() {
        ConfigPreferences.init()
        load()
    }

    @JvmStatic
    fun load() {
        items.clear()

        ghostModeSettings = ConfigItem(ConfigItem.HEADER, Keys.GhostModeSettings)
        items.add(ghostModeSettings!!)

        val childrenHideSeen = ArrayList<ConfigItem>()

        hideSeenPrivateChat = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HideSeenPrivateChat,
            ConfigPreferences.getBoolean(Keys.HideSeenPrivateChat),
            Runnable { GhostMode.init() }
        )
        childrenHideSeen.add(hideSeenPrivateChat!!)

        hideSeenChannel = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HideSeenChannel,
            ConfigPreferences.getBoolean(Keys.HideSeenChannel),
            Runnable { GhostMode.init() }
        )
        childrenHideSeen.add(hideSeenChannel!!)

        hideSeen = ConfigItem(ConfigItem.EXPANDABLE_SWITCH, Keys.HideSeen, childrenHideSeen)
        items.add(hideSeen!!)

        markReadAfterSend = ConfigItem(
            ConfigItem.SWITCH,
            Keys.MarkReadAfterSend,
            ConfigPreferences.getBoolean(Keys.MarkReadAfterSend),
            Runnable { GhostMode.init() }
        )
        items.add(markReadAfterSend!!)

        hideTyping = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HideTyping,
            ConfigPreferences.getBoolean(Keys.HideTyping),
            Runnable { GhostMode.init() }
        )
        items.add(hideTyping!!)

        hideStoryView = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HideStoryView,
            ConfigPreferences.getBoolean(Keys.HideStoryView),
            Runnable { GhostMode.init() }
        )
        items.add(hideStoryView!!)

        hidePhone = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HidePhone,
            true,
            ConfigPreferences.getBoolean(Keys.HidePhone),
            Runnable { HidePhone.init() }
        )
        items.add(hidePhone!!)

        hideOnline = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HideOnline,
            true,
            ConfigPreferences.getBoolean(Keys.HideOnline),
            Runnable { GhostMode.init() }
        )
        items.add(hideOnline!!)

        onlineInfo = ConfigItem(ConfigItem.INFO, Keys.OfflineVisibilityInfo)
        items.add(onlineInfo!!)

        shadows = ConfigItem(ConfigItem.DIVIDER)
        items.add(shadows!!)

        stories = ConfigItem(ConfigItem.HEADER, Keys.StoriesSettings)
        items.add(stories!!)

        disableStories = ConfigItem(
            ConfigItem.SWITCH,
            Keys.DisableStories,
            true,
            ConfigPreferences.getBoolean(Keys.DisableStories),
            Runnable { DisableStories.init() }
        )
        items.add(disableStories!!)

        items.add(shadows!!)

        messages = ConfigItem(ConfigItem.HEADER, Keys.MessagesSettings)
        items.add(messages!!)

        showDeletedMessages = ConfigItem(
            ConfigItem.SWITCH,
            Keys.ShowDeletedMessages,
            ConfigPreferences.getBoolean(Keys.ShowDeletedMessages),
            Runnable { ShowDeletedMessages.init() }
        )
        items.add(showDeletedMessages!!)

        if (!ClientManager.`is`(ClientManager.Client.NagramX)) {
            showMessageId = ConfigItem(
                ConfigItem.SWITCH,
                Keys.ShowMessageID,
                ConfigPreferences.getBoolean(Keys.ShowMessageID),
                Runnable { MessageTimeModifier.init() }
            )
            items.add(showMessageId!!)
        }

        saveEditsHistory = ConfigItem(
            ConfigItem.SWITCH,
            Keys.SaveEditsHistory,
            ConfigPreferences.getBoolean(Keys.SaveEditsHistory),
            Runnable { SaveEditsHistory.init() }
        )
        items.add(saveEditsHistory!!)

        items.add(shadows!!)

        connections = ConfigItem(ConfigItem.HEADER, Keys.ConnectionsSettings)
        items.add(connections!!)

        downloadSpeed = ConfigItem(
            ConfigItem.SWITCH,
            Keys.DownloadSpeed,
            ConfigPreferences.getBoolean(Keys.DownloadSpeed),
            Runnable { DownloadSpeed.init() }
        )
        items.add(downloadSpeed!!)

        items.add(shadows!!)

        media = ConfigItem(ConfigItem.HEADER, Keys.MediaSettings)
        items.add(media!!)

        if (!ClientManager.`is`(ClientManager.Client.Nekogram) && !ClientManager.`is`(ClientManager.Client.Cherrygram)) {
            secretMediaSave = ConfigItem(
                ConfigItem.SWITCH,
                Keys.SecretMediaSave,
                ConfigPreferences.getBoolean(Keys.SecretMediaSave),
                Runnable { SecretMediaSave.init() }
            )
            items.add(secretMediaSave!!)
        }

        preventMedia = ConfigItem(
            ConfigItem.SWITCH,
            Keys.PreventMedia,
            ConfigPreferences.getBoolean(Keys.PreventMedia),
            Runnable { PreventMedia.init() }
        )
        items.add(preventMedia!!)

        enableSavingStories = ConfigItem(
            ConfigItem.SWITCH,
            Keys.EnableSavingStories,
            ConfigPreferences.getBoolean(Keys.EnableSavingStories),
            Runnable { EnableSavingStories.init() }
        )
        items.add(enableSavingStories!!)

        enableVoiceMessageSaving = ConfigItem(
            ConfigItem.SWITCH,
            Keys.EnableVoiceMessageSaving,
            ConfigPreferences.getBoolean(Keys.EnableVoiceMessageSaving),
            Runnable { VoiceToMusicHook.init() }
        )
        items.add(enableVoiceMessageSaving!!)

        items.add(shadows!!)

        ui = ConfigItem(ConfigItem.HEADER, Keys.UiSettings)
        items.add(ui!!)

        hidePinnedMessages = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HidePinnedMessages,
            ConfigPreferences.getBoolean(Keys.HidePinnedMessages),
            Runnable { HidePinnedMessages.init() }
        )
        items.add(hidePinnedMessages!!)

        disableChannelSwipeBack = ConfigItem(
            ConfigItem.SWITCH,
            Keys.DisableChannelSwipeBack,
            ConfigPreferences.getBoolean(Keys.DisableChannelSwipeBack),
            Runnable { DisableChannelSwipeBack.init() }
        )
        items.add(disableChannelSwipeBack!!)

        disableProfileSwipeBack = ConfigItem(
            ConfigItem.SWITCH,
            Keys.DisableProfileSwipeBack,
            ConfigPreferences.getBoolean(Keys.DisableProfileSwipeBack),
            Runnable { DisableProfileSwipeBack.init() }
        )
        items.add(disableProfileSwipeBack!!)

        hideProxySponsor = ConfigItem(
            ConfigItem.SWITCH,
            Keys.HideProxySponsor,
            true,
            ConfigPreferences.getBoolean(Keys.HideProxySponsor),
            Runnable { HideProxySponsor.init() }
        )
        items.add(hideProxySponsor!!)

        if (!ClientManager.`is`(ClientManager.Client.Telegraph) &&
            !ClientManager.`is`(ClientManager.Client.Nekogram) &&
            !ClientManager.`is`(ClientManager.Client.Cherrygram)
        ) {
            showUserID = ConfigItem(
                ConfigItem.SWITCH,
                Keys.ShowUserID,
                ConfigPreferences.getBoolean(Keys.ShowUserID),
                Runnable { EditOnlineTextView.init() }
            )
            items.add(showUserID!!)

            customCalendar = ConfigItem(
                ConfigItem.TEXT,
                Keys.Calendar,
                true,
                Runnable { HijriDate.init() }
            )
            items.add(customCalendar!!)
        }

        items.add(shadows!!)

        otherFeatures = ConfigItem(ConfigItem.HEADER, Keys.OtherFeaturesSettings)
        items.add(otherFeatures!!)

        removesContentSaving = ConfigItem(
            ConfigItem.SWITCH,
            Keys.RemovesContentSaving,
            ConfigPreferences.getBoolean(Keys.RemovesContentSaving),
            Runnable { RemovesContentSaving.init() }
        )
        items.add(removesContentSaving!!)

        telegramPremium = ConfigItem(
            ConfigItem.SWITCH,
            Keys.TelegramPremium,
            ConfigPreferences.getBoolean(Keys.TelegramPremium),
            Runnable { TelePremium.init() }
        )
        items.add(telegramPremium!!)

        if (!ClientManager.`is`(ClientManager.Client.Telegraph)) {
            disableNumberRounding = ConfigItem(
                ConfigItem.SWITCH,
                Keys.DisableNumberRounding,
                "5.3K -> 5300",
                ConfigPreferences.getBoolean(Keys.DisableNumberRounding),
                Runnable { DisableNumberRounding.init() }
            )
            hideUpdateApp = ConfigItem(
                ConfigItem.SWITCH,
                Keys.HideUpdateApp,
                true,
                ConfigPreferences.getBoolean(Keys.HideUpdateApp),
                Runnable { HideUpdateApp.init() }
            )
            fixTLError = ConfigItem(
                ConfigItem.SWITCH,
                Keys.FixTLError,
                ConfigPreferences.getBoolean(Keys.FixTLError),
                Runnable { FixTLError.init() }
            )
            items.add(disableNumberRounding!!)
            items.add(hideUpdateApp!!)
            items.add(fixTLError!!)
        }

        items.add(shadows!!)

        btnChannel = ConfigItem(ConfigItem.TEXT, Keys.DeveloperChannel)
        items.add(btnChannel!!)

        items.add(shadows!!)

        btnRestartApp = ConfigItem(ConfigItem.TEXT, Keys.RestartApp)
        items.add(btnRestartApp!!)

        items.add(shadows!!)

        readFeature()
    }

    @JvmStatic
    fun readFeature() {
        try {
            for (item in items) {
                if (item.type != ConfigItem.SWITCH && item.getCustomCalendar() == 0) continue
                if (item.isEnable) item.run()
            }

            if (!ClientManager.`is`(ClientManager.Client.Telegraph) &&
                !ClientManager.`is`(ClientManager.Client.Nekogram) &&
                !ClientManager.`is`(ClientManager.Client.Cherrygram)
            ) {
                FeatureInitializer.init()
                CopyNameHook.init()
                EditOnlineTextView.init()
            }
            AlwaysSaveMedia.init()

            if (ClientManager.`is`(ClientManager.Client.Telegraph)) {
                Telegraph.removeAd()
            }

        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun isGhostMode(): Boolean {
        return (hideSeen?.isEnable == true) ||
                (hideStoryView?.isEnable == true) ||
                (hideTyping?.isEnable == true) ||
                (hideOnline?.isEnable == true) ||
                (markReadAfterSend?.isEnable == true)
    }
}
