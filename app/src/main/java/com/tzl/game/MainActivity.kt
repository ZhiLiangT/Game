package com.tzl.game

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.tzl.game.widget.GameView2048

class MainActivity : AppCompatActivity() {

    private var gameView: GameView2048?=null
    private var tvScore: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView=findViewById(R.id.game_view)
        tvScore=findViewById(R.id.tv_score)
        gameView!!.setOnScoreChangeListener(object :GameView2048.OnScoreChangeListener{
            override fun onChange(total: Int) {
                tvScore!!.text="分数为：$total"
            }
        })
    }
}
