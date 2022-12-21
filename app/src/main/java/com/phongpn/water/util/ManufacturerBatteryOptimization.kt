package com.phongpn.water.util

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AlertDialog
import java.util.*


object ManufacturerBatteryOptimization {
    //
    private var sharedPreferences: SharedPreferences? = null

    //
    private const val MANUFACTURER = "MANUFACTURER"
    private const val PROTECTED = "PROTECTED"
    private val POWERMANAGER_INTENTS = Arrays.asList(
        Intent().setComponent(ComponentName("com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity")),
        Intent().setComponent(ComponentName("com.letv.android.letvsafe",
            "com.letv.android.letvsafe.AutobootManageActivity")),
        Intent().setComponent(ComponentName("com.huawei.systemmanager",
            "com.huawei.systemmanager.optimize.process.ProtectActivity")),
        Intent().setComponent(ComponentName("com.huawei.systemmanager",
            "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
        Intent().setComponent(ComponentName("com.coloros.safecenter",
            "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.coloros.safecenter",
            "com.coloros.safecenter.startupapp.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.oppo.safe",
            "com.oppo.safe.permission.startup.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.iqoo.secure",
            "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
        Intent().setComponent(ComponentName("com.iqoo.secure",
            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
        Intent().setComponent(ComponentName("com.vivo.permissionmanager",
            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.lool",
            "com.samsung.android.sm.ui.battery.BatteryActivity")),
        Intent().setComponent(ComponentName("com.htc.pitroad",
            "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
        Intent().setComponent(ComponentName("com.asus.mobilemanager",
            "com.asus.mobilemanager.MainActivity")),
        Intent().setComponent(ComponentName("com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity")),
        Intent().setComponent(ComponentName("com.letv.android.letvsafe",
            "com.letv.android.letvsafe.AutobootManageActivity")),
        Intent().setComponent(ComponentName("com.huawei.systemmanager",
            "com.huawei.systemmanager.optimize.process.ProtectActivity")),
        Intent().setComponent(ComponentName("com.coloros.safecenter",
            "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.coloros.safecenter",
            "com.coloros.safecenter.startupapp.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.oppo.safe",
            "com.oppo.safe.permission.startup.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.iqoo.secure",
            "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
        Intent().setComponent(ComponentName("com.iqoo.secure",
            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
        Intent().setComponent(ComponentName("com.vivo.permissionmanager",
            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
        Intent().setComponent(ComponentName("com.asus.mobilemanager",
            "com.asus.mobilemanager.entry.FunctionActivity")).setData(
            Uri.parse("mobilemanager://function/entry/AutoStart"))
    )

    private fun getAvailableIntents(context: Context): ArrayList<Intent> {
        val availableIntents = ArrayList<Intent>()
        for (intent in POWERMANAGER_INTENTS) {
            if (context.packageManager.resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY) != null
            ) {
                availableIntents.add(intent)
            }
        }
        return availableIntents
    }

    fun     showAddToWhitelistDialog(context: Context) {
        if (getAvailableIntents(context).size > 0) {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(MANUFACTURER, Context.MODE_PRIVATE)
            }
            if (!sharedPreferences!!.getBoolean(PROTECTED, false)) {
                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("ATTENTION PLEASE")
                    .setMessage("""Dear ${Build.MANUFACTURER} user, you need to add ${
                        getApplicationName(context)
                    } in your Task Killer's whitelist for ${
                        getApplicationName(context)
                    } to work properly.
Click OK to proceed...""")
                    .setPositiveButton("OK"
                    ) { dialog, which ->
                        for (intent in getAvailableIntents(context)) {
                            context.startActivity(intent)
                        }
                        sharedPreferences!!.edit().putBoolean(PROTECTED, true).apply()
                    }
                    .setCancelable(false)
                    .create()
                dialog.show()
            }
        }
    }

    private fun getApplicationName(context: Context): String {
        return context.applicationInfo.loadLabel(context.packageManager).toString()
    }
}