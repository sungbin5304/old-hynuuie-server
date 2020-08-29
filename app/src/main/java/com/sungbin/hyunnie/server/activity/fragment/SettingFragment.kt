package com.sungbin.hyunnie.server.activity.fragment

import android.R.color
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.sungbin.hyunnie.server.R
import com.sungbin.sungbintool.DataUtils
import com.sungbin.sungbintool.ToastUtils


class SettingFragment : Fragment() {

    var setThemeBtn: ImageView? = null
    var colorLens: ImageView? = null
    var tvAppTheme: TextView? = null
    var numberImage: ImageView? = null
    var tvFileCount: TextView? = null
    var tbCheckFileCount: ToggleButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        setThemeBtn = view.findViewById<ImageView>(R.id.iv_circle)
        colorLens = view.findViewById<ImageView>(R.id.iv_color_lens)
        tvAppTheme = view.findViewById<TextView>(R.id.tv_app_theme)
        numberImage = view.findViewById<ImageView>(R.id.iv_number)
        tvFileCount = view.findViewById<TextView>(R.id.tv_file_count)
        tbCheckFileCount = view.findViewById<ToggleButton>(R.id.tb_off_file_count)
        setThemeBtn!!.setOnClickListener {
            showThemeDialog()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val primary = Color.parseColor(DataUtils.readData(context!!, "primary", "#2195f2"))
        val primaryDark = Color.parseColor(DataUtils.readData(context!!, "primaryDark", "#0068bf"))
        val accent = Color.parseColor(DataUtils.readData(context!!, "accent", "#6ec5ff"))
        val isThemeChange = DataUtils.readData(context!!, "theme change", "false").toBoolean()

        if(isThemeChange){
            setThemeBtn!!.setColorFilter(primary, PorterDuff.Mode.SRC_IN)
            colorLens!!.setColorFilter(primary, PorterDuff.Mode.SRC_IN)
            numberImage!!.setColorFilter(primary, PorterDuff.Mode.SRC_IN)
            tvAppTheme!!.setTextColor(primary)
            tvFileCount!!.setTextColor(primary)
        }
    }

    fun showThemeDialog() {
        try {
            var alert: AlertDialog? = null
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("테마 설정")
            val grid = GridLayout(context)
            grid.columnCount = 4
            grid.rowCount = 5
            grid.orientation = GridLayout.HORIZONTAL
            val colorName =
                "RED\n,PINK\n,PURPLE\n,DEEP\nPURPLE,INDIGO\n,BLUE\n,LIGHT\nBLUE,CYAN\n,TEAL\n,GREEN\n,LIGHT\nGREEN,LIME\n,YELLOW\n,AMBER\n,ORANGE\n,DEEP\nORANGE,BROWN\n,GREY\n,LIGHT\nGREY,DEFAULT\n"
                    .split(",").toTypedArray()
            val colorCode =
                "e84e40,f48fb1,ce93d8,9575cd,9fa8da,738ffe,81d4fa,4dd0e1,4db6ac,42bd41,9ccc65,cddc39,ffeb3b,ffc107,ffa726,ff7043,795548,9e9e9e,cfd8dc,2195f2"
                    .split(",").toTypedArray()
            val buttons =
                arrayOfNulls<Button>(20)
            var color = 0
            while (color < 20) {
                buttons[color] = Button(context)
                buttons[color]!!.id = color
                buttons[color]!!.setBackgroundColor(Color.parseColor("#" + colorCode[color]))
                buttons[color]!!.text = colorName[color]
                buttons[color]!!.setOnClickListener { view ->
                    setThemeColor(view.id)
                    alert!!.cancel()
                }
                grid.addView(buttons[color])
                color++
            }
            val scroll = HorizontalScrollView(context)
            scroll.addView(grid)
            scroll.overScrollMode = View.OVER_SCROLL_NEVER
            scroll.setPadding(16, 30, 16, 16)
            dialog.setView(scroll)

            alert = dialog.create()
            alert!!.show()
        } catch (e: Exception) {
            Log.d("TTT", e.toString())
        }
    }

    fun setThemeColor(tag: Int) {
        when (tag) {
            0 -> setColor("e84e3c", "af1113", "ff8168")
            1 -> setColor("f48fb1", "bf5f82", "ffc1e3")
            2 -> setColor("ce93d8", "9c64a6", "ffc4ff")
            3 -> setColor("9575cd", "65499c", "c7a4ff")
            4 -> setColor("9fa8da", "6f79a8", "d1d9ff")
            5 -> setColor("738ffe", "3862ca", "a9bfff")
            6 -> setColor("81d4fa", "4ba3c7", "b6ffff")
            7 -> setColor("4dd0e1", "009faf", "88ffff")
            8 -> setColor("4db6ac", "00867d", "82e9de")
            9 -> setColor("42bd41", "008c09", "7af071")
            10 -> setColor("9ccc65", "6b9b37", "cfff95")
            11 -> setColor("cddc39", "99aa00", "ffff6e")
            12 -> setColor("ffeb3b", "c8b900", "ffff72")
            13 -> setColor("ffc107", "c79100", "fff350")
            14 -> setColor("ffa726", "c77800", "ffd95b")
            15 -> setColor("ff7043", "c63f17", "ffa270")
            16 -> setColor("775447", "4a2b20", "a78172")
            17 -> setColor("9e9e9e", "707070", "cfcfcf")
            18 -> setColor("ced7db", "9da6a9", "ffffff")
            19 -> setColor("42A5F5", "0077C2", "80D6FF")
        }
    }

    fun setColor(
        primary: String,
        primaryDark: String,
        accent: String
    ) {
        DataUtils.saveData(context!!, "primary", "#$primary")
        DataUtils.saveData(context!!, "primaryDark", "#$primaryDark")
        DataUtils.saveData(context!!, "accent", "#$accent")
        DataUtils.saveData(context!!, "theme change", (primary != "null").toString())
        ToastUtils.show(context!!, getString(R.string.restart_apply_theme), ToastUtils.SHORT, ToastUtils.SUCCESS)
    }

}