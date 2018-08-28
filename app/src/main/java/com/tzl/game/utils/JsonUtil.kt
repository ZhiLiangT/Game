package com.tzl.game.utils


import com.google.gson.Gson
import com.google.gson.JsonParser

import org.json.JSONArray
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.HashMap

/**
 * Description：GSON单例类，提供对象到JSON数据格式转换
 * Copyright：Copyright (c) 2013
 * @author gujc
 */
object JsonUtil {


    private var gson: Gson? = null

    private fun gson(): Gson? {
        if (gson == null) {
            synchronized(JsonUtil::class.java) {
                if (gson == null) {
                    gson = Gson()
                }
            }
        }
        return gson
    }

    /**
     * 目标对象到JSON数据格式转换
     *
     * @param src 目标对象
     */
    fun toJson(src: Any): String {
        return gson()!!.toJson(src)
    }

    /**
     * JSON格式数据到目标对象转换
     *
     * @param js    JSON格式数据
     * @param clazz 目标对象類型
     * @return 目标对象
     * @throws "JsonConvertException"
     */
    fun <T> fromJson(js: String, clazz: Class<T>): T {
        return gson()!!.fromJson(js, clazz)
    }

    fun <T> getObjectList(jsonString: String, cls: Class<T>): List<T> {
        val list = ArrayList<T>()
        try {
            val gson = Gson()
            val arry = JsonParser().parse(jsonString).asJsonArray
            for (jsonElement in arry) {
                list.add(gson.fromJson(jsonElement, cls))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

    fun <M> getList(jsonString: String, cls: Class<M>): List<M> {
        val list = ArrayList<M>()
        try {
            val gson = Gson()
            val arry = JsonParser().parse(jsonString).asJsonArray
            for (jsonElement in arry) {
                list.add(gson.fromJson(jsonElement, cls))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

    /**
     * Json 转成 Map<>
     * @param jsonStr
     * @return
     */
    fun getMapForJson(jsonStr: String): Map<String, Any>? {
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(jsonStr)

            val keyIter = jsonObject.keys()
            var key: String
            var value: Any
            val valueMap = HashMap<String, Any>()
            while (keyIter.hasNext()) {
                key = keyIter.next()
                value = jsonObject.get(key)
                valueMap[key] = value
            }
            return valueMap
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

        return null
    }

    /**
     * Json 转成 Map<>
     * @param jsonStr
     * @return
     */
    fun getMapStringJson(jsonStr: String): Map<String, String>? {
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(jsonStr)

            val keyIter = jsonObject.keys()
            var key: String
            var value: Any
            val valueMap = HashMap<String, String>()
            while (keyIter.hasNext()) {
                key = keyIter.next()
                value = jsonObject.get(key)
                valueMap[key] = value as String
            }
            return valueMap
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

        return null
    }

    /**
     * Json 转成 List<Map></Map><>>
     * @param jsonStr
     * @return
     */
    fun getlistForJson(jsonStr: String): List<Map<String, Any>>? {
        var list: MutableList<Map<String, Any>>? = null
        try {
            val jsonArray = JSONArray(jsonStr)
            var jsonObj: JSONObject
            list = ArrayList()
            for (i in 0 until jsonArray.length()) {
                jsonObj = jsonArray.get(i) as JSONObject
                list.add(getMapForJson(jsonObj.toString())!!)
            }
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

        return list
    }

    fun <T> isEmpty(sourceList: List<T>?): Boolean {
        return sourceList == null || sourceList.size == 0
    }

    /**
     * 输入流转字符串
     * @param is
     * @return
     */
    fun convertStreamToString(`is`: InputStream): String {

        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        val line: String? = null
        try {
            while ((line == reader.readLine()) != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return sb.toString()

    }
}