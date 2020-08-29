package com.sungbin.hyunnie.server.widget

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.sungbin.hyunnie.server.R

class LoadingDialog constructor(context: Context){
    private val ctx = context
    private var dialog: AlertDialog.Builder = AlertDialog.Builder(ctx)
    private var layout: View
    private var alert: AlertDialog? = null
    private var listener: DialogInterface.OnClickListener? = null
    private var btnName: String? = null

    init {
        dialog.setCancelable(false)
        layout = getView()
    }

    @SuppressLint("InflateParams")
    private fun getView(): View {
        return LayoutInflater.from(ctx).inflate(R.layout.dialog_loading_layout, null, false)
    }

    fun getLayout(): View{
        return layout
    }

    fun setLayout(view: View){
        dialog.setView(view)
    }

    fun setOnClick(obj : DialogInterface.OnClickListener){
        listener = obj
    }

    fun setBtnName(string: String){
        btnName = string
    }

    fun getDialog(): AlertDialog.Builder{
        return dialog
    }

    fun show(){
        alert = getDialog().create()
        alert!!.setView(layout)
        alert!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if(btnName != null) alert!!.setButton(AlertDialog.BUTTON_POSITIVE, btnName, listener)
        alert!!.show()
    }

    fun cancel(){
        alert!!.cancel()
    }
}