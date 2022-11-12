package com.example.tucon.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

object AlertDialogBuilder {

    fun build(context: Context, message: String, positive: String, negative: String,
              dialogInterface: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton(positive, dialogInterface)
        builder.setNegativeButton(negative) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}