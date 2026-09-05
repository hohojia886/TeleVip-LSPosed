package com.my.televip.settings.controller

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.my.televip.Configs.ConfigPreferences
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.settings.ui.SettingsActivity
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.messenger.browser.Browser
import com.my.televip.virtuals.ui.LaunchActivity

class SettingsController(private val context: Context) {

    @JvmField var settingsView: FrameLayout? = null
    @JvmField var settingsActivity: SettingsActivity? = null

    fun getContext(): Context = context

    fun openView() {
        try {
            if (settingsView == null) {
                settingsView = FrameLayout(context)
            }
            val view = settingsView!!
            view.removeAllViews()
            val activity = SettingsActivity(context)
            settingsActivity = activity
            showJoinTeleVip()

            view.addView(activity.createView(this))
            show(view)
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun showJoinTeleVip() {
        try {
            if (!ConfigPreferences.getBoolean("JTV")) {
                val alertDialog = AlertDialog(context)
                alertDialog.setTitle(Translator.get(Keys.GhostMode))
                alertDialog.setMessage(Translator.get(Keys.JoinTeleVip))
                alertDialog.setPositiveButton(Translator.get(Keys.Join), AlertDialog.click {
                    Browser.openUrl(context, "https://t.me/t_l0_e")
                    hide()
                })
                alertDialog.setNegativeButton(Translator.get(Keys.Cancel), null)
                alertDialog.setNeutralButton(Translator.get(Keys.DontShowAgain), AlertDialog.click {
                    ConfigPreferences.putBoolean("JTV", true)
                })
                alertDialog.show()
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    fun show(target: View) {
        val launchActivity = LaunchActivity(context)
        if (target.parent == null) {
            launchActivity.frameLayout.addView(target)
        }

        val childCount = launchActivity.frameLayout.childCount
        for (i in 0 until childCount) {
            val child = launchActivity.frameLayout.getChildAt(i)
            child.visibility = if (child == target) View.VISIBLE else View.GONE
        }

        target.bringToFront()
    }

    fun hide() {
        val currentSettingsView = settingsView ?: return
        val launchActivity = LaunchActivity(currentSettingsView.context)
        val childCount = launchActivity.frameLayout.childCount
        for (i in 0 until childCount) {
            val child = launchActivity.frameLayout.getChildAt(i)
            child.visibility = if (child == currentSettingsView) View.GONE else View.VISIBLE
        }

        if (currentSettingsView.parent != null) {
            launchActivity.frameLayout.removeView(currentSettingsView)
        }
        SettingsActivity.isSettings = false
    }
}
