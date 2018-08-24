package com.tzl.game.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tzl.game.R

/**
 * Create by 田志亮 on 2018/8/22
 */
class GameOverDialog:DialogFragment(),View.OnClickListener{


    private lateinit var btConfirm: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.dialog_game_over,container,false)
        btConfirm=view.findViewById(R.id.bt_confirm)
        btConfirm.setOnClickListener(this)
        return view
    }

    interface OnConfirmClickListener{
        fun onConfirm()
    }

    private var listener:OnConfirmClickListener ?=null

    fun setOnConfirmListener(listener:OnConfirmClickListener ){
        this.listener=listener
    }

    override fun onClick(v: View?) {
        if (listener!=null){
            dismiss()
            listener!!.onConfirm()
        }
    }
}