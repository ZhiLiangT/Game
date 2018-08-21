package com.tzl.game

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tzl.game.widget.GameOverDialog
import com.tzl.game.widget.GameView2048

class MainActivity : AppCompatActivity() {

    private var gameView: GameView2048?=null
    private var tvScore: TextView?=null
    private var tvMostScore:TextView?=null
    private var btBack:Button ?=null
    private var btRestart:Button ?=null
    private var sp: SharedPreferences ?=null
    private var mostScore:Int=0
    private var data:Array<IntArray> ?=null
    private var currScore:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView=findViewById(R.id.game_view)
        tvScore=findViewById(R.id.tv_score)
        tvMostScore=findViewById(R.id.tv_most_score)
        btBack=findViewById(R.id.bt_back)
        btRestart=findViewById(R.id.bt_restart)
        init()
        gameView!!.setOnScoreChangeListener(object :GameView2048.OnScoreChangeListener{

            override fun onFinish() {
                val dialog= GameOverDialog()
                dialog.setOnConfirmListener(object :GameOverDialog.OnConfirmClickListener{
                    override fun onConfirm() {
                        gameView!!.reStart()
                    }
                })
                dialog.show(supportFragmentManager,"")
            }

            override fun onChange(total: Int) {
                currScore=total
                tvScore!!.text="当前分数为:\n$currScore"
                if (total>mostScore){
                    mostScore=total
                    tvMostScore!!.text="最高分数为:\n$total"
                }
            }
        })
        btBack!!.setOnClickListener({

        })
        btRestart!!.setOnClickListener({
            gameView!!.reStart()
            currScore=0
            tvScore!!.text="当前分数为:\n$currScore"
        })
    }

    @SuppressLint("CommitPrefEdits")
    private fun init() {
        sp=getSharedPreferences("data",0)
        mostScore=sp!!.getInt("score",0)
        currScore=sp!!.getInt("curr_score",0)
        val attrStr=sp!!.getString("attr","")
        val gson:Gson=GsonBuilder().create()
        data =gson.fromJson<Array<IntArray>>(attrStr, Array<IntArray>::class.java)
        gameView!!.setData(data)
        tvMostScore!!.text="最高分数为:\n$mostScore"
        tvScore!!.text="当前分数为:\n$currScore"
        gameView!!.setScore(currScore)
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
        editor.putInt("curr_score",currScore)
        editor.apply()
    }
}
