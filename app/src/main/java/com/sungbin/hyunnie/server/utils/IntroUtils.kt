package com.sungbin.hyunnie.server.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

@SuppressLint("CommitPrefEdits")
class IntroUtils(context: Context) {
    private var pref: SharedPreferences
    private var editor: Editor

    private var PRIVATE_MODE = 0

    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    companion object {
        private const val PREF_NAME = "intro-manager"
        private const val IS_FIRST_TIME_LAUNCH = "isFirstLaunch"
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}