package com.sungbin.hyunnie.server.dto

class UserData {
    var imei: String? = null
    var level: Int? = null //0: 무료 계정, 1: H, 2: 애니, 3: H, 애니
    var useCoupon: String? = null

    constructor() {}
    constructor(imei: String?, level: Int?, useCoupon: String?) {
        this.imei = imei
        this.level = level
        this.useCoupon = useCoupon
    }
}