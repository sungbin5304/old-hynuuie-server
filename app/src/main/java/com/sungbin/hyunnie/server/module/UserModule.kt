package com.sungbin.hyunnie.server.module

import com.sungbin.hyunnie.server.dto.UserData

object UserModule {
    var imei: String? = null
    var level: Int? = null //0: 무료 계정, 1: H, 2: 애니, 3: H, 애니
    var useCoupon: String? = null

    fun setData(userData: UserData){
        imei = userData.imei
        level = userData.level
        useCoupon = userData.useCoupon
    }
}