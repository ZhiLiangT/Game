package com.tzl.game

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tzl.game.widget.GameOverDialog
import com.tzl.game.widget.GameView2048

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var gameView: GameView2048
    private lateinit var tvScore: TextView
    private lateinit var tvMostScore:TextView
    private lateinit var btBack:Button
    private lateinit var btRestart:Button
    private lateinit var bt3:Button
    private lateinit var bt4:Button
    private lateinit var bt5:Button
    private lateinit var bt6:Button
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
        bt3=findViewById(R.id.bt_3)
        bt4=findViewById(R.id.bt_4)
        bt5=findViewById(R.id.bt_5)
        bt6=findViewById(R.id.bt_6)
        btBack.setOnClickListener(this)
        btRestart.setOnClickListener(this)
        bt3.setOnClickListener(this)
        bt4.setOnClickListener(this)
        bt5.setOnClickListener(this)
        bt6.setOnClickListener(this)
        init()
        gameView.setOnScoreChangeListener(object :GameView2048.OnScoreChangeListener{
            override fun onFinish() {
                val dialog= GameOverDialog()
                dialog.setOnConfirmListener(object :GameOverDialog.OnConfirmClickListener{
                    override fun onConfirm() {
                        setCurrScore(0)
                        gameView.reStart()
                    }
                })
                dialog.show(supportFragmentManager,"")
            }
            override fun onChange(total: Int) {
                currScore=total
                setCurrScore(currScore)
                if (total>mostScore){
                    mostScore=total
                    tvMostScore.text="最高分数为:\n$total"
                }
            }
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
        gameView.setData(data)
        tvMostScore.text="最高分数为:\n$mostScore"
        setCurrScore(currScore)
        gameView.setScore(currScore)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onPause() {
        super.onPause()
        sp=getSharedPreferences("data",0)
        val editor = sp!!.edit()
        val gson:Gson=GsonBuilder().create()
        val str=gson.toJson(gameView.getData())
        editor.putString("attr",str)
        editor.putInt("score",mostScore)
        editor.putInt("curr_score",currScore)
        editor.apply()
    }
    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bt_back    -> {}
            R.id.bt_restart -> {
                gameView.reStart()
                setCurrScore(0)
            }
            R.id.bt_3   ->{
                gameView.setCoordinateSize(3)
                setCurrScore(0)
            }
            R.id.bt_4   ->{
                gameView.setCoordinateSize(4)
                setCurrScore(0)
            }
            R.id.bt_5   ->{
                gameView.setCoordinateSize(5)
                setCurrScore(0)
            }
            R.id.bt_6   ->{
                gameView.setCoordinateSize(6)
                setCurrScore(0)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setCurrScore(score:Int){
        currScore=score
        tvScore.text=resources.getText(R.string.curr_score).toString()+"\n$currScore"
    }

}
