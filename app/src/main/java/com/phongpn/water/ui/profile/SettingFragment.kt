package com.phongpn.water.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.opencsv.CSVWriter
import com.phongpn.water.R
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.ui.dialog.base.BasePaddingBottomSheetDialogFragment
import com.phongpn.water.util.profileparams.AppSetting
import com.phongpn.water.viewmodel.LogDrinkViewModel
import kotlinx.android.synthetic.main.clear_all_data_dialog.*
import kotlinx.android.synthetic.main.setting_fragment.*
import java.io.File
import java.io.FileWriter

class SettingFragment(title: String) : BaseDetailProfileFragment(title) {
    private val logDrinkViewModel : LogDrinkViewModel by activityViewModels()
    private val appSetting = AppSetting.getInstance()
    private lateinit var listLog : List<LogDrink>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logDrinkViewModel.getLogs()
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDrinkViewModel.allLog.observe(viewLifecycleOwner) {
            listLog = it
        }
        sound_frame.apply {
            if (!appSetting.sound) sound_iv.setImageResource(R.drawable.icon_mute)
            setOnClickListener {
                appSetting.sound = !appSetting.sound
                if (!appSetting.sound) sound_iv.setImageResource(R.drawable.icon_mute)
                else sound_iv.setImageResource(R.drawable.icon_volume)
            }
        }

        clear_all_data_frame.setOnClickListener {
            ClearAllDataDialog().show(childFragmentManager, null)
        }

        export_data_frame.setOnClickListener {
            val csvFile = File.createTempFile("water", ".csv")
            val writer = FileWriter(csvFile)
            var csvWriter =    CSVWriter(writer)
            listLog.forEach {
                    csvWriter.writeNext(arrayOf(it.cal.time.toString(), it.type, it.amount.toString()))
            }
            writer.close()
            csvWriter.close()
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/*"
                putExtra(Intent.EXTRA_TEXT, csvFile.name)
                putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context!!, "${context!!.applicationContext.packageName}.provider",csvFile))
            }, null))
        }
        about_frame.setOnClickListener {
            Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
    }
}
class ClearAllDataDialog : BasePaddingBottomSheetDialogFragment(){
    private val logDrinkViewModel : LogDrinkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.clear_all_data_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancel_bt.setOnClickListener { dismiss() }
        delete_bt.setOnClickListener {
            logDrinkViewModel.deleteALlLogs()
            dismiss()
            Toast.makeText(context, getString(R.string.all_data_was_removed), Toast.LENGTH_SHORT).show()
        }
    }
}