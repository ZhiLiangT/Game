package com.tzl.game.utils

/**
 * Create by 田志亮 on 2018/8/24
 */
object CountUtils {

    fun log(value:Double,base:Double): Double {
        return Math.log(value)/Math.log(base)
    }

    fun log2(value:Double):Double{
        return log(value,2.0)
    }

    fun log10(value:Double):Double{
        return log(value,10.0)
    }
}