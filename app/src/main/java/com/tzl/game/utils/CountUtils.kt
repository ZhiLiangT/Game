package com.tzl.game.utils

/**
 * 科学计算工具类
 * Create by 田志亮 on 2018/8/24
 */
object CountUtils {

    /**
     * 对数计算
     */
    fun log(value:Double,base:Double): Double {
        return Math.log(value)/Math.log(base)
    }
    /**
     * 对数计算
     * 重载底数为2
     */
    fun log2(value:Double):Double{
        return log(value,2.0)
    }
    /**
     * 对数计算
     * 重载底数为10
     */
    fun log10(value:Double):Double{
        return log(value,10.0)
    }

}