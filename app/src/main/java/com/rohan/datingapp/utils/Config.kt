package com.rohan.datingapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.rohan.datingapp.R
import java.util.Objects

object Config {

//    private var dialog: AlertDialog? = null
    private var dialog: Dialog? = null

    fun showDialog(context: Context){
//        dialog = MaterialAlertDialogBuilder(context)
//            .setView(R.layout.loading_layout)
//            .setCancelable(false)
//            .create()

        dialog = Dialog(context)
        dialog!!.setContentView(R.layout.loading_layout)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun hideDialog(){
        if(Objects.nonNull(dialog)){
            dialog?.dismiss()
        }
    }

}