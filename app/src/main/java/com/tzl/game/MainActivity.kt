package com.tzl.game

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tzl.game.widget.GameView2048

class MainActivity : AppCompatActivity() {

    private var gameView: GameView2048?=null
    private var tvScore: TextView?=null
    private var tvMostScore:TextView?=null
    private var sp: SharedPreferences ?=null
    private var mostScore:Int=0
    private var data:Array<IntArray> ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView=findViewById(R.id.game_view)
        tvScore=findViewById(R.id.tv_score)
        tvMostScore=findViewById(R.id.tv_most_score)
        init()
        gameView!!.setOnScoreChangeListener(object :GameView2048.OnScoreChangeListener{
            override fun onChange(total: Int) {
                tvScore!!.text="当前分数为:\n$total"
                if (total>mostScore){
                    mostScore=total
                    tvMostScore!!.text="最高分数为:\n$total"
                }
            }
        })
    }

    @SuppressLint("CommitPrefEdits")
    private fun init() {
        sp=getSharedPreferences("data",0)
        mostScore=sp!!.getInt("score",0)
        val attrStr=sp!!.getString("attr","")
        val gson:Gson=GsonBuilder().create()
        data =gson.fromJson<Array<IntArray>>(attrStr, Array<IntArray>::class.java)
        gameView!!.setData(data)
        tvMostScore!!.text="最高分数为:\n$mostScore"
    }

    @SuppressLint("CommitPrefEdits")
    override fun onPause() {
        super.onPause()
        sp=getSharedPreferences("data",0)
        val editor = sp!!.edit()
        val gson:Gson=GsonBuilder().create()
        val str=gson.toJson(gameView!!.getData())
        editor.putString("attr",str)
        editor.putInt("score",mostScore)
        editor.apply()
    }
}
