package com.example.timer.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.timer.R
import com.example.timer.model.CountdownTimer
import java.util.Date

class CountdownTimerDialog(private val observer: CountdownTimerDialogObserver) : DialogFragment() {
    interface CountdownTimerDialogObserver{
        fun onAdded(timer: CountdownTimer)
        fun getId():Int
    }

    private fun addTimer(view: View){
        var minutes : Long
        var secconds : Long
        try {
             minutes = view.findViewById<EditText>(R.id.editAddCountdownMinutes).text.toString().toLong()
        }catch (e: NumberFormatException){
            minutes = 0
        }
        try {
            secconds = view.findViewById<EditText>(R.id.editAddCountdownSecconds).text.toString().toLong()
        }catch (e: NumberFormatException){
            secconds  =0
        }
        observer.onAdded(CountdownTimer(secconds+minutes*60 ,observer.getId(), Date()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Добавяне на обратен брояч")

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.add_countdown_dialog_layout, null)


        builder.setView(dialogView)

        builder.setPositiveButton("Потвърди"){ dialog, _ ->
            addTimer(dialogView)
            dialog.dismiss()
        }

        builder.setNegativeButton("Отказ") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }
}