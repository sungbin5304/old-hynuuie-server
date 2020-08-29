package com.sungbin.hyunnie.server.activity

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.sungbin.hyunnie.server.R
import com.sungbin.hyunnie.server.activity.fragment.DownloadFragment
import com.sungbin.hyunnie.server.activity.fragment.MainFragment
import com.sungbin.hyunnie.server.activity.fragment.SettingFragment
import com.sungbin.sungbintool.ColorUtils
import com.sungbin.sungbintool.DataUtils
import com.sungbin.sungbintool.DialogUtils
import com.sungbin.sungbintool.NotificationUtils
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        val primary = Color.parseColor(DataUtils.readData(applicationContext, "primary", "#2195f2"))
        val primaryDark = Color.parseColor(DataUtils.readData(applicationContext, "primaryDark", "#0068bf"))
        val accent = Color.parseColor(DataUtils.readData(applicationContext, "accent", "#6ec5ff"))
        val isThemeChange = DataUtils.readData(applicationContext, "theme change", "false").toBoolean()

        if(isThemeChange){
            ColorUtils.setStatusBarColor(this, primaryDark)
            toolbar.setBackgroundColor(primary)
        }

        NotificationUtils.setGroupName(getString(R.string.app_name))
        NotificationUtils.createChannel(applicationContext,
            getString(R.string.app_name), getString(R.string.app_name))

        fragmentManager.beginTransaction().add(R.id.framelayout, MainFragment()).commit()
        bottombar.onItemSelected = {
            val fragmentTransaction = fragmentManager.beginTransaction()
            when(it){
                0 -> { //메인
                    fragmentTransaction.replace(R.id.framelayout, MainFragment()).commit()
                }
                1 -> { //다운로드 항목
                    fragmentTransaction.replace(R.id.framelayout, DownloadFragment()).commit()
                }
                2 -> { //설정
                    fragmentTransaction.replace(R.id.framelayout, SettingFragment()).commit()
                }
            }
        }
    }

    override fun onBackPressed() {
        DialogUtils.show(this,
        getString(R.string.string_exit),
        getString(R.string.exit_app_ask), DialogInterface.OnClickListener {
                    _: DialogInterface, _: Int ->
                super.onBackPressed()
            })
    }
}
