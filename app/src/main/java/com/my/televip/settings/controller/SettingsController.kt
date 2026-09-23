package com.my.televip.settings.controller

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.my.televip.Configs.ConfigPreferences
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.settings.ui.SettingsActivity
import com.my.televip.utils.Utils
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.messenger.browser.Browser
import com.my.televip.virtuals.ui.LaunchActivity

class SettingsController {

    @JvmField
    var settingsView: FrameLayout? = null

    @JvmField
    var settingsActivity: SettingsActivity? = null

    val context: Context?
        get() = Utils.getCurrentActivity()

    fun openView() {
        try {
            val ctx = context ?: return
            if (settingsView == null) {
                settingsView = FrameLayout(ctx)
            }

            settingsView?.removeAllViews()
            val activity = SettingsActivity(ctx)
            settingsActivity = activity
            showJoinTeleVip()

            val createdView = activity.createView(this)
            settingsView?.addView(createdView)

            settingsView?.let { show(it) }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun showJoinTeleVip() {
        try {
            val ctx = context ?: return
            if (!ConfigPreferences.getBoolean("JTV")) {
                val alertDialog = AlertDialog(ctx)
                alertDialog.setTitle(Translator.get(Keys.GhostMode))
                alertDialog.setMessage(Translator.get(Keys.JoinTeleVip))

                alertDialog.setPositiveButton(Translator.get(Keys.Join), AlertDialog.click {
                    Browser.openUrl(ctx, "https://t.me/t_l0_e")
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
        val launchActivity = LaunchActivity(Utils.getCurrentActivity())
        val frameLayout = launchActivity.frameLayout
        if (target.parent == null) {
            frameLayout.addView(target)
        }

        for (i in 0 until frameLayout.childCount) {
            val child = frameLayout.getChildAt(i)
            child.visibility = if (child == target) View.VISIBLE else View.GONE
        }

        target.bringToFront()
    }

    fun hide() {
        val view = settingsView ?: return
        val launchActivity = LaunchActivity(view.context)
        val frameLayout = launchActivity.frameLayout

        for (i in 0 until frameLayout.childCount) {
            val child = frameLayout.getChildAt(i)
            child.visibility = if (child == view) View.GONE else View.VISIBLE
        }

        if (view.parent != null) {
            frameLayout.removeView(view)
        }
        SettingsActivity.isSettings = false
    }
}
