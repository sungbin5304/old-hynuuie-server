package com.sungbin.hyunnie.server.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.sungbin.hyunnie.server.R
import com.sungbin.hyunnie.server.utils.IntroUtils
import com.sungbin.sungbintool.ToastUtils

@Suppress("DEPRECATION")
class IntroActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: SlideViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private var dots: Array<TextView?>? = null
    private var layouts: IntArray? = null
    private var btnNext: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!getInternetConnected(applicationContext)) {
            ToastUtils.show(applicationContext,
            getString(R.string.need_internet), ToastUtils.LONG, ToastUtils.INFO)
            finish()
        }
        else {
            val introUtils = IntroUtils(this)
            if (!introUtils.isFirstTimeLaunch) {
                launchHomeScreen()
            }

            if (Build.VERSION.SDK_INT >= 21) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }

            setContentView(R.layout.content_intro)

            viewPager = findViewById<View>(R.id.view_pager) as ViewPager
            dotsLayout = findViewById<View>(R.id.il_dots) as LinearLayout
            btnNext = findViewById<View>(R.id.btn_next) as Button
            layouts = intArrayOf(
                R.layout.welcome_slide,    //0
                R.layout.permission_slide, //1
                R.layout.start_slide       //2
            )

            addBottomDots(0)
            changeStatusBarColor()

            myViewPagerAdapter = SlideViewPagerAdapter(this)
            viewPager!!.adapter = myViewPagerAdapter
            viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)

            btnNext!!.setOnClickListener {
                val current = getItem(+1)
                if (current < layouts!!.size) {
                    viewPager!!.currentItem = current
                } else { //마지막 슬라이드 일 때
                    if (ContextCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) { //모든 권한 허용
                        launchHomeScreen()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.need_permission_agree))
                            .setMessage(getString(R.string.need_permission_to_start))
                            .setPositiveButton(R.string.string_ok) { _, _ ->
                                viewPager!!.currentItem = 1
                            }
                            .show()
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts!!.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout!!.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i]!!.text = Html.fromHtml("&#8226;")
            dots!![i]!!.textSize = 35f
            dots!![i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots!![i])
        }
        if (dots!!.isNotEmpty()) dots!![currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        startActivity(Intent(this, LoadActivity::class.java))
        finish()
    }

    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            if (position == layouts!!.size - 1) {
                btnNext!!.text = getString(R.string.string_start)
            } else {
                btnNext!!.text = getString(R.string.string_next)
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    inner class SlideViewPagerAdapter(private val activity: Activity) : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view =
                layoutInflater!!.inflate(layouts!![position], container, false)

            if(position == 1){ //권한 요청 슬라이드
                view.findViewById<Button>(R.id.btn_request_storage).setOnClickListener {
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 500)
                }
            }

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(
            view: View,
            obj: Any
        ): Boolean {
            return view === obj
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            obj: Any
        ) {
            val view = obj as View
            container.removeView(view)
        }
    }

    fun getInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

}