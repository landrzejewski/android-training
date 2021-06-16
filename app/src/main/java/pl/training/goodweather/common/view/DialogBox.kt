package pl.training.goodweather.common.view

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import pl.training.goodweather.R

class DialogBox {

    fun show(context: Context, text: String, parent: View) {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_box, null)
        val popup = PopupWindow(view, 800, 400, /*MATCH_PARENT*/ true)
        view.findViewById<TextView>(R.id.messageTextView).text = text
        view.findViewById<Button>(R.id.okButton).setOnClickListener { popup.dismiss() }
        popup.showAtLocation(parent, Gravity.CENTER, 0, 0)
    }

}