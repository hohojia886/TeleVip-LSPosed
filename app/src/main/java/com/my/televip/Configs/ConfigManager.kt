package com.my.televip.Configs

import android.content.Context
import com.my.televip.ClientChecker
import com.my.televip.Clients.Telegraph
import com.my.televip.features.DisableChannelSwipeBack
import com.my.televip.features.DisableNumberRounding
import com.my.televip.features.DisableProfileSwipeBack
import com.my.televip.features.DisableStories
import com.my.televip.features.DownloadSpeed
import com.my.televip.features.EnableSavingStories
import com.my.televip.features.FixTLError
import com.my.televip.features.GhostMode
import com.my.televip.features.HidePhone
import com.my.televip.features.HidePinnedMessages
import com.my.televip.features.HideProxySponsor
import com.my.televip.features.HideUpdateApp
import com.my.televip.features.HijriDate
import com.my.televip.features.PreventMedia
import com.my.televip.features.RemovesContentSaving
import com.my.televip.features.SaveEditsHistory
import com.my.televip.features.SecretMediaSave
import com.my.televip.features.ShowDeletedMessages
import com.my.televip.features.TelePremium
import com.my.televip.features.VoiceToMusicHook
import com.my.televip.features.base.FeatureRegistry
import com.my.televip.features.otherFeatures.AlwaysSaveMedia
import com.my.televip.features.otherFeatures.CopyNameHook
import com.my.televip.features.otherFeatures.EditOnlineTextView
import com.my.televip.features.otherFeatures.FeatureInitializer
import com.my.televip.language.Keys
import com.my.televip.logging.Logger
import com.my.televip.virtuals.ui.Cells.ChatMessageCell

object ConfigManager {

    @JvmStatic
    val items: ArrayList<ConfigItem> = ArrayList()

    private val dummyItem = ConfigItem(ConfigItem.DIVIDER)

    // GhostMode
    @JvmField var ghostModeSettings: ConfigItem = dummyItem
    @JvmField var hideSeenPrivateChat: ConfigItem = dummyItem
    @JvmField var hideSeenChannel: ConfigItem = dummyItem
    @JvmField var hideSeen: ConfigItem = dummyItem
    @JvmField var markReadAfterSend: ConfigItem = dummyItem
    @JvmField var hideTyping: ConfigItem = dummyItem
    @JvmField var hideStoryView: ConfigItem = dummyItem
    @JvmField var hidePhone: ConfigItem = dummyItem
    @JvmField var hideOnline: ConfigItem = dummyItem
    @JvmField var onlineInfo: ConfigItem = dummyItem

    @JvmField var shadows: ConfigItem = dummyItem

    // Stories
    @JvmField var stories: ConfigItem = dummyItem
    @JvmField var disableStories: ConfigItem = dummyItem

    // Messages
    @JvmField var messages: ConfigItem = dummyItem
    @JvmField var showDeletedMessages: ConfigItem = dummyItem
    @JvmField var showMessageId: ConfigItem = dummyItem
    @JvmField var saveEditsHistory: ConfigItem = dummyItem

    // Connections
    @JvmField var connections: ConfigItem = dummyItem
    @JvmField var downloadSpeed: ConfigItem = dummyItem

    // Media
    @JvmField var media: ConfigItem = dummyItem
    @JvmField var secretMediaSave: ConfigItem = dummyItem
    @JvmField var preventMedia: ConfigItem = dummyItem
    @JvmField var enableSavingStories: ConfigItem = dummyItem
    @JvmField var enableVoiceMessageSaving: ConfigItem = dummyItem

    // UI
    @JvmField var ui: ConfigItem = dummyItem
    @JvmField var hidePinnedMessages: ConfigItem = dummyItem
    @JvmField var disableChannelSwipeBack: ConfigItem = dummyItem
    @JvmField var disableProfileSwipeBack: ConfigItem = dummyItem
    @JvmField var hideProxySponsor: ConfigItem = dummyItem
    @JvmField var showUserID: ConfigItem = dummyItem
    @JvmField var customCalendar: ConfigItem = dummyItem

    // Other Features
    @JvmField var otherFeatures: ConfigItem = dummyItem
    @JvmField var removesContentSaving: ConfigItem = dummyItem
    @JvmField var telegramPremium: ConfigItem = dummyItem
    @JvmField var disableNumberRounding: ConfigItem = dummyItem
    @JvmField var hideUpdateApp: ConfigItem = dummyItem
    @JvmField var fixTLError: ConfigItem = dummyItem

    // Button
    @JvmField var btnChannel: ConfigItem = dummyItem
    @JvmField var btnRestartApp: ConfigItem = dummyItem

    @JvmStatic
    fun loadAndRead(context: Context) {
        ConfigPreferences.init()
        load(context)
        readFeature(context)
    }

    @JvmStatic
    fun load(context: Context) {
        items.clear()

        ghostModeSettings = ConfigItem(ConfigItem.HEADER, Keys.GhostModeSettings)
        items.add(ghostModeSettings)

        val childrenHideSeen = ArrayList<ConfigItem>()

        hideSeenPrivateChat = ConfigItem(ConfigItem.SWITCH, Keys.HideSeenPrivateChat, ConfigPreferences.getBoolean(Keys.HideSeenPrivateChat)) { GhostMode.init() }
        childrenHideSeen.add(hideSeenPrivateChat)

        hideSeenChannel = ConfigItem(ConfigItem.SWITCH, Keys.HideSeenChannel, ConfigPreferences.getBoolean(Keys.HideSeenChannel)) { GhostMode.init() }
        childrenHideSeen.add(hideSeenChannel)

        hideSeen = ConfigItem(ConfigItem.EXPANDABLE_SWITCH, Keys.HideSeen, childrenHideSeen)
        items.add(hideSeen)

        markReadAfterSend = ConfigItem(ConfigItem.SWITCH, Keys.MarkReadAfterSend, ConfigPreferences.getBoolean(Keys.MarkReadAfterSend)) { GhostMode.init() }
        items.add(markReadAfterSend)

        hideTyping = ConfigItem(ConfigItem.SWITCH, Keys.HideTyping, ConfigPreferences.getBoolean(Keys.HideTyping)) { GhostMode.init() }
        items.add(hideTyping)

        hideStoryView = ConfigItem(ConfigItem.SWITCH, Keys.HideStoryView, ConfigPreferences.getBoolean(Keys.HideStoryView)) { GhostMode.init() }
        items.add(hideStoryView)

        hidePhone = ConfigItem(ConfigItem.SWITCH, Keys.HidePhone, true, ConfigPreferences.getBoolean(Keys.HidePhone)) { HidePhone.init() }
        items.add(hidePhone)

        hideOnline = ConfigItem(ConfigItem.SWITCH, Keys.HideOnline, true, ConfigPreferences.getBoolean(Keys.HideOnline)) { GhostMode.init() }
        items.add(hideOnline)

        onlineInfo = ConfigItem(ConfigItem.INFO, Keys.OfflineVisibilityInfo)
        items.add(onlineInfo)

        shadows = ConfigItem(ConfigItem.DIVIDER)
        items.add(shadows)

        // Stories
        stories = ConfigItem(ConfigItem.HEADER, Keys.StoriesSettings)
        items.add(stories)

        disableStories = ConfigItem(ConfigItem.SWITCH, Keys.DisableStories, true, ConfigPreferences.getBoolean(Keys.DisableStories)) { DisableStories.init() }
        items.add(disableStories)

        items.add(shadows)

        // Messages
        messages = ConfigItem(ConfigItem.HEADER, Keys.MessagesSettings)
        items.add(messages)

        showDeletedMessages = ConfigItem(ConfigItem.SWITCH, Keys.ShowDeletedMessages, ConfigPreferences.getBoolean(Keys.ShowDeletedMessages)) { ShowDeletedMessages.initProcessing() }
        items.add(showDeletedMessages)

        if (!ClientChecker.check(ClientChecker.ClientType.NagramX)) {
            showMessageId = ConfigItem(ConfigItem.SWITCH, Keys.ShowMessageID, ConfigPreferences.getBoolean(Keys.ShowMessageID)) { ChatMessageCell.init() }
            items.add(showMessageId)
        }

        saveEditsHistory = ConfigItem(ConfigItem.SWITCH, Keys.SaveEditsHistory, ConfigPreferences.getBoolean(Keys.SaveEditsHistory)) { SaveEditsHistory.init(context) }
        items.add(saveEditsHistory)

        items.add(shadows)

        // Connections
        connections = ConfigItem(ConfigItem.HEADER, Keys.ConnectionsSettings)
        items.add(connections)

        downloadSpeed = ConfigItem(ConfigItem.SWITCH, Keys.DownloadSpeed, ConfigPreferences.getBoolean(Keys.DownloadSpeed)) { DownloadSpeed.init() }
        items.add(downloadSpeed)

        items.add(shadows)

        // Media
        media = ConfigItem(ConfigItem.HEADER, Keys.MediaSettings)
        items.add(media)

        if (!ClientChecker.check(ClientChecker.ClientType.Nekogram) && !ClientChecker.check(ClientChecker.ClientType.Cherrygram)) {
            secretMediaSave = ConfigItem(ConfigItem.SWITCH, Keys.SecretMediaSave, ConfigPreferences.getBoolean(Keys.SecretMediaSave)) { SecretMediaSave.init() }
            items.add(secretMediaSave)
        }

        preventMedia = ConfigItem(ConfigItem.SWITCH, Keys.PreventMedia, ConfigPreferences.getBoolean(Keys.PreventMedia)) { PreventMedia.init() }
        items.add(preventMedia)

        enableSavingStories = ConfigItem(ConfigItem.SWITCH, Keys.EnableSavingStories, ConfigPreferences.getBoolean(Keys.EnableSavingStories)) { EnableSavingStories.init() }
        items.add(enableSavingStories)

        enableVoiceMessageSaving = ConfigItem(ConfigItem.SWITCH, Keys.EnableVoiceMessageSaving, ConfigPreferences.getBoolean(Keys.EnableVoiceMessageSaving)) { VoiceToMusicHook.init() }
        items.add(enableVoiceMessageSaving)

        items.add(shadows)

        // UI
        ui = ConfigItem(ConfigItem.HEADER, Keys.UiSettings)
        items.add(ui)

        hidePinnedMessages = ConfigItem(ConfigItem.SWITCH, Keys.HidePinnedMessages, ConfigPreferences.getBoolean(Keys.HidePinnedMessages)) { HidePinnedMessages.init() }
        items.add(hidePinnedMessages)

        disableChannelSwipeBack = ConfigItem(ConfigItem.SWITCH, Keys.DisableChannelSwipeBack, ConfigPreferences.getBoolean(Keys.DisableChannelSwipeBack)) { DisableChannelSwipeBack.init() }
        items.add(disableChannelSwipeBack)

        disableProfileSwipeBack = ConfigItem(ConfigItem.SWITCH, Keys.DisableProfileSwipeBack, ConfigPreferences.getBoolean(Keys.DisableProfileSwipeBack)) { DisableProfileSwipeBack.init() }
        items.add(disableProfileSwipeBack)

        hideProxySponsor = ConfigItem(ConfigItem.SWITCH, Keys.HideProxySponsor, true, ConfigPreferences.getBoolean(Keys.HideProxySponsor)) { HideProxySponsor.init() }
        items.add(hideProxySponsor)

        if (!ClientChecker.check(ClientChecker.ClientType.Telegraph) && !ClientChecker.check(ClientChecker.ClientType.Nekogram) && !ClientChecker.check(ClientChecker.ClientType.Cherrygram)) {
            showUserID = ConfigItem(ConfigItem.SWITCH, Keys.ShowUserID, ConfigPreferences.getBoolean(Keys.ShowUserID)) { EditOnlineTextView.init(context) }
            items.add(showUserID)
            customCalendar = ConfigItem(ConfigItem.TEXT, Keys.Calendar, true) { HijriDate.init() }
            items.add(customCalendar)
        }

        items.add(shadows)

        // Other Features
        otherFeatures = ConfigItem(ConfigItem.HEADER, Keys.OtherFeaturesSettings)
        items.add(otherFeatures)

        removesContentSaving = ConfigItem(ConfigItem.SWITCH, Keys.RemovesContentSaving, ConfigPreferences.getBoolean(Keys.RemovesContentSaving)) { RemovesContentSaving.init() }
        items.add(removesContentSaving)

        telegramPremium = ConfigItem(ConfigItem.SWITCH, Keys.TelegramPremium, ConfigPreferences.getBoolean(Keys.TelegramPremium)) { TelePremium.init() }
        items.add(telegramPremium)

        if (!ClientChecker.check(ClientChecker.ClientType.Telegraph)) {
            disableNumberRounding = ConfigItem(ConfigItem.SWITCH, Keys.DisableNumberRounding, "5.3K -> 5300", ConfigPreferences.getBoolean(Keys.DisableNumberRounding)) { DisableNumberRounding.init() }
            hideUpdateApp = ConfigItem(ConfigItem.SWITCH, Keys.HideUpdateApp, true, ConfigPreferences.getBoolean(Keys.HideUpdateApp)) { HideUpdateApp.init() }
            fixTLError = ConfigItem(ConfigItem.SWITCH, Keys.FixTLError, ConfigPreferences.getBoolean(Keys.FixTLError)) { FixTLError.init() }
            items.add(disableNumberRounding)
            items.add(hideUpdateApp)
            items.add(fixTLError)
        }

        items.add(shadows)

        val customItems = FeatureRegistry.getConfigItems(context)
        if (customItems.isNotEmpty()) {
            items.addAll(customItems)
            items.add(shadows)
        }

        btnChannel = ConfigItem(ConfigItem.TEXT, Keys.DeveloperChannel)
        items.add(btnChannel)

        items.add(shadows)

        btnRestartApp = ConfigItem(ConfigItem.TEXT, Keys.RestartApp)
        items.add(btnRestartApp)

        items.add(shadows)
    }

    @JvmStatic
    fun readFeature(context: Context) {
        try {
            for (item in items) {
                if (item.type != ConfigItem.SWITCH && item.getCustomCalendar() == 0) continue
                if (item.isEnable) item.run()
            }

            if (!ClientChecker.check(ClientChecker.ClientType.Telegraph) && !ClientChecker.check(ClientChecker.ClientType.Nekogram) && !ClientChecker.check(ClientChecker.ClientType.Cherrygram)) {
                FeatureInitializer.init(context)
                CopyNameHook.init(context)
                EditOnlineTextView.init(context)
            }
            AlwaysSaveMedia.init()

            if (ClientChecker.check(ClientChecker.ClientType.Telegraph)) Telegraph.removeAd()

            FeatureRegistry.initAll(context)

        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun isGhostMode(): Boolean {
        return hideSeen.isEnable ||
                hideStoryView.isEnable ||
                hideTyping.isEnable ||
                hideOnline.isEnable ||
                markReadAfterSend.isEnable
    }
}
