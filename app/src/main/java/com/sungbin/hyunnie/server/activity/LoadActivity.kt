package com.sungbin.hyunnie.server.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.sungbin.hyunnie.server.R
import com.sungbin.hyunnie.server.dto.UserData
import com.sungbin.hyunnie.server.module.UserModule
import com.sungbin.hyunnie.server.utils.IntroUtils
import com.sungbin.sungbintool.StorageUtils
import com.sungbin.sungbintool.ToastUtils
import java.lang.Exception

@Suppress("DEPRECATION", "MissingPermission", "HardwareIds")
class LoadActivity : AppCompatActivity() {

    private val reference = FirebaseDatabase.getInstance().reference
    private var introUtils: IntroUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            introUtils = IntroUtils(this)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            setContentView(R.layout.content_load)

            val imei = Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID
            );

            if (introUtils!!.isFirstTimeLaunch) {
                val userData = UserData(imei, 0, null)
                reference.child("UserData").child(imei).setValue(userData)
                UserModule.setData(userData)
                ToastUtils.show(
                    applicationContext,
                    getString(R.string.welcome_login),
                    ToastUtils.SHORT, ToastUtils.SUCCESS
                )
                introUtils!!.isFirstTimeLaunch = false
            } else {
                reference.child("UserData").child(imei)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val userData: UserData = dataSnapshot.getValue(UserData::class.java)!!
                            UserModule.setData(userData)
                            ToastUtils.show(
                                applicationContext,
                                getString(R.string.success_load_user),
                                ToastUtils.SHORT, ToastUtils.SUCCESS
                            )
                        }

                    })
            }

            Handler().postDelayed({
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }, 3000) //3초 후에 실행
        }
        catch (e: Exception){
            StorageUtils.save("버그 리포드.log", e.toString())
        }
    }

}
