package com.tzl.game.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

import com.tzl.game.R
import com.tzl.game.utils.CountUtils
import com.tzl.game.utils.JsonUtil


/**
 * 2048Game 自定义View
 * Create by 田志亮 on 2018/8/18
 */
class GameView2048 :View{

    constructor(context: Context):this(context,null)

    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)

    constructor( context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context,attrs,defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    /**横向数量 */
    private var horizontalNum = 5
    /**纵向数量 */
    private var verticalNum = 5
    /**View的宽度 */
    private var viewWidth: Float = 0f
    /**View的高度 */
    private var viewHeight: Float = 0f
    /**每个小方块的宽 */
    private var itemWidht: Float = 0f
    /**每个小方块的高 */
    private var itemHeight: Float = 0f
    /**背景颜色 */
    private var bgColor = Color.parseColor("#C9BDB3")
    /**分割线颜色 */
    private var dividerColor = Color.parseColor("#464C62")
    /**分割线的宽度 */
    private var dividerSize = 5f
    /**数字的颜色 */
    private var textColor = Color.BLACK
    /**数字的大小 */
    private var textSize = 30f
    /**数据集合 */
    private var attr: Array<IntArray> = Array(verticalNum){IntArray(horizontalNum)}
    /**总分数 */
    private var totalScore: Int = 0
    /**分数比例 */
    private var scale = 1
    /**是否开启Test模式 */
    private val isTest = false
    /**画笔 */
    private lateinit var paint: Paint
    /**文字画笔 */
    private lateinit var textPaint: Paint
    /**文字颜色集合*/
    private lateinit var colorAttr:IntArray
    /**按下的开始坐标*/
    private var startX: Int = 0
    private var startY: Int = 0
    /**是否滑动*/
    private var isMove = false
    /**判断数据是否发生了变化 */
    private var isChange = false
    /**状态监听，分数监听和游戏失败监听*/
    private var listener: OnScoreChangeListener? = null
    /**回退集合*/
    private var backList:MutableList<Array<IntArray>> = mutableListOf()

    /**
     * 测试数据
     */
    private fun testData() {
        attr = arrayOf(
                intArrayOf(0, 2, 0, 8, 16),
                intArrayOf(0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0),
                intArrayOf(2, 0, 0, 0, 2))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec).toFloat()
        itemHeight = viewHeight / verticalNum
        itemWidht = viewWidth / horizontalNum
    }

    /**初始化*/
    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.GameView2048, defStyleAttr, 0)
        if (isTest) {
            testData()
        } else {
            for (i in 0 until verticalNum) {
                attr[i] = IntArray(horizontalNum)
            }
            for (i in 0..1) {
                val h = (Math.random() * horizontalNum).toInt()
                val v = (Math.random() * verticalNum).toInt()
                if (attr[h][v] == 0) {
                    attr[h][v] = 2
                }
            }
        }
        backList.add(attr)
        horizontalNum = typedArray.getInteger(R.styleable.GameView2048_game_horizontal, 5)
        verticalNum = typedArray.getInteger(R.styleable.GameView2048_game_vertical, 5)
        bgColor = typedArray.getColor(R.styleable.GameView2048_game_bg, bgColor)
        dividerColor = typedArray.getColor(R.styleable.GameView2048_game_divider_color, dividerColor)
        dividerSize = typedArray.getDimension(R.styleable.GameView2048_game_divider_size, dividerSize)
        textColor = typedArray.getColor(R.styleable.GameView2048_game_text_color, textColor)
        textSize = typedArray.getDimension(R.styleable.GameView2048_game_text_size, textSize)
        typedArray.recycle()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.strokeWidth = dividerSize
        paint.color = dividerColor
        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = textSize
        textPaint.color = textColor
        colorAttr= intArrayOf( Color.parseColor("#EEE4DA") , Color.parseColor("#EDE0C8") ,
                               Color.parseColor("#F2B179") , Color.parseColor("#F59563") ,
                               Color.parseColor("#F67C5F") , Color.parseColor("#EDCF72") ,
                               Color.parseColor("#F2CF60") , Color.parseColor("#F65E3B") ,
                               Color.parseColor("#EDC850") , Color.parseColor("#EDC53F") )
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(bgColor)
        paint.color = dividerColor
        //绘制横向分割线
        for (i in 0..verticalNum) {
            canvas.drawLine(0f, i * itemWidht, viewWidth, i * itemWidht, paint)
        }
        //绘制纵向分割线
        for (i in 0..horizontalNum) {
            canvas.drawLine(i * itemHeight, 0f, i * itemHeight, viewHeight, paint)
        }
        for (i in attr.indices) {
            for (j in 0 until attr[i].size) {
                if (attr[i][j] != 0) {
                    paint.color= colorAttr[CountUtils.log2(attr[i][j].toDouble()).toInt()]
                    canvas.drawRect(itemWidht * i + dividerSize / 2, itemHeight * j + dividerSize / 2,
                            itemWidht * (i + 1) - dividerSize / 2, itemHeight * (j + 1) - dividerSize / 2, paint)
                    canvas.drawText(attr[i][j].toString() + "", (i + 0.5).toFloat() * itemWidht,
                            (j + 0.5).toFloat() * itemHeight - (textPaint.descent() + textPaint.ascent()) / 2, textPaint)
                }
            }
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                isMove = false
                startX = event.x.toInt()
                startY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> isMove = true
            MotionEvent.ACTION_UP -> {
                val endX = event.x.toInt()
                val endY = event.y.toInt()
                if (isMove) {
                    val offsetX = endX - startX
                    val offsetY = endY - startY
                    isChange = false
                    when{
                        offsetX > 0 && Math.abs(offsetX) > Math.abs(offsetY) -> slideAfter(3)
                        offsetX < 0 && Math.abs(offsetX) > Math.abs(offsetY) -> slideAfter(2)
                        offsetY > 0 && Math.abs(offsetX) < Math.abs(offsetY) ->  slideAfter(1)
                        offsetY < 0 && Math.abs(offsetX) < Math.abs(offsetY) -> slideAfter(0)
                    }
                }
            }
        }
        return true
    }

    /**
     * 滑动之后操作
     * @param direction 滑动的方向 0：表示上划，1：表示下划，2：表示左划，3：表示右划
     */
    private fun slideAfter(direction: Int) {
        when (direction) {
            0 -> for (i in attr.indices) {
                var index = 0
                for (j in 0 until attr[i].size) {
                    if (attr[i][j] != 0) {
                        if (attr[i][index] == 0 && j != index) {
                            isChange = true
                            attr[i][index] = attr[i][j]
                            attr[i][j] = 0
                        } else if (attr[i][index] == attr[i][j] && j != 0 && j != index) {
                            isChange = true
                            attr[i][index] = attr[i][j] * 2
                            attr[i][j] = 0
                            totalScore += attr[i][index] * scale
                            index += 1
                        } else if (attr[i][index] != attr[i][j] && j != index) {
                            attr[i][index + 1] = attr[i][j]
                            if (j - index > 1) {
                                isChange = true
                                attr[i][j] = 0
                            }
                            index += 1
                        }
                    }
                }
            }
            1 -> for (i in attr.indices) {
                var index = attr[i].size - 1
                for (j in attr[i].size - 1 downTo 0) {
                    if (attr[i][j] != 0) {
                        if (attr[i][index] == 0 && j != index) {
                            isChange = true
                            attr[i][index] = attr[i][j]
                            attr[i][j] = 0
                        } else if (attr[i][index] == attr[i][j] && j != attr[i].size - 1 && j != index) {
                            isChange = true
                            attr[i][index] = attr[i][j] * 2
                            attr[i][j] = 0
                            totalScore += attr[i][index] * scale
                            index -= 1
                        } else if (attr[i][index] != attr[i][j] && j != index) {
                            attr[i][index - 1] = attr[i][j]
                            if (index - j > 1) {
                                isChange = true
                                attr[i][j] = 0
                            }
                            index -= 1
                        }
                    }
                }
            }
            2 ->
                //左划
                for (i in attr.indices) {
                    var index = 0
                    for (j in attr.indices) {
                        if (attr[j][i] != 0) {
                            if (attr[index][i] == 0 && j != index) {
                                isChange = true
                                attr[index][i] = attr[j][i]
                                attr[j][i] = 0
                            } else if (attr[index][i] == attr[j][i] && j != 0 && j != index) {
                                isChange = true
                                attr[index][i] = attr[j][i] * 2
                                attr[j][i] = 0
                                totalScore += attr[index][i] * scale
                                index += 1
                            } else if (attr[index][i] != attr[j][i] && j != index) {
                                attr[index + 1][i] = attr[j][i]
                                if (j - index > 1) {
                                    isChange = true
                                    attr[j][i] = 0
                                }
                                index += 1
                            }
                        }
                    }
                }
            3 ->
                //右划
                for (i in attr.indices) {
                    var index = attr.size - 1
                    for (j in attr.indices.reversed()) {
                        if (attr[j][i] != 0) {
                            if (attr[index][i] == 0 && j != index) {
                                isChange = true
                                attr[index][i] = attr[j][i]
                                attr[j][i] = 0
                            } else if (attr[index][i] == attr[j][i] && j != 0 && j != index) {
                                isChange = true
                                attr[index][i] = attr[j][i] * 2
                                attr[j][i] = 0
                                totalScore += attr[index][i] * scale
                                index -= 1
                            } else if (attr[index][i] != attr[j][i] && j != index) {
                                attr[index - 1][i] = attr[j][i]
                                if (index - j > 1) {
                                    isChange = true
                                    attr[j][i] = 0
                                }
                                index -= 1
                            }
                        }
                    }
                }
        }
        if (listener != null) {
            listener!!.onChange(totalScore)
        }
        makeRandom()
        if (isChange){
            if (backList.size>4){
                backList.removeAt(0)
            }
            Log.i("TAG","attr == ${JsonUtil.toJson(attr)}")
            val copy: Array<IntArray> = Array(verticalNum){IntArray(horizontalNum)}
            for (index in attr.indices){
                val temp = IntArray(horizontalNum)
                System.arraycopy(attr[index], 0, temp, 0, attr[index].size)
                copy[index]=temp
            }
            backList.add(copy)
            Log.i("TAG","backList == ${JsonUtil.toJson(backList)}")
        }
    }

    /**重新生成随机数 */
    private fun makeRandom() {
        if (isChange) {
            while (true) {
                val h = (Math.random() * horizontalNum).toInt()
                val v = (Math.random() * horizontalNum).toInt()
                val s = (Math.random() * 10).toInt()
                if (attr[h][v] == 0) {
                    if (s>7){
                        attr[h][v] = 4
                    }else{
                        attr[h][v] = 2
                    }
                    invalidate()
                    return
                }
            }
        }
        if (isFinish()){
            if (listener!=null){
                listener!!.onFinish()
            }
        }
    }

    /**
     * 重新开始
     */
    fun reStart() {
        backList.clear()
        totalScore=0
        attr =  Array(verticalNum){IntArray(horizontalNum)}
        for (i in 0 until verticalNum) {
            attr[i] = IntArray(horizontalNum)
        }
        for (i in 0..1) {
            val h = (Math.random() * horizontalNum).toInt()
            val v = (Math.random() * verticalNum).toInt()
            if (attr[h][v] == 0) {
                attr[h][v] = 2
            }
        }
        requestLayout()
    }

    /**
     * 判断是否结束
     */
    private fun isFinish() :Boolean{
        for (i in attr.indices){
            for (j in 0 until attr.size){
                if (attr[i][j]==0){
                    return isFinish@false
                }
                if (j<attr.size-1){
                    if (attr[i][j]==attr[i][j+1]){
                        return isFinish@false
                    }
                }
                if (i<attr.size-1){
                    if (attr[j][i] == attr[j][i+1]){
                        return isFinish@false
                    }
                }
            }
        }
        return true
    }

    /**后退一步*/
    fun stepBack(){
        if (backList.size>1){
            attr=backList[backList.size-2]
            backList.removeAt(backList.size-1)
            invalidate()
        }
    }

    /**
     * 回调分数
     */
    interface OnScoreChangeListener {
        fun onChange(total: Int)
        fun onFinish()
    }

    fun setOnScoreChangeListener(listener: OnScoreChangeListener) {
        this.listener = listener
    }

    /*****************************************get/set方法***************************************/

    /**设置分割线颜色 */
    fun setDividerColor(@ColorRes color: Int) {
        dividerColor = color
    }

    /**设置分割线粗细 */
    fun setDividerSize(size: Float) {
        dividerSize = size
    }

    /**设置背景颜色*/
    override fun setBackgroundColor(@ColorRes color: Int) {
        bgColor = color
    }

    /**设置文字颜色 */
    fun setTextColor(@ColorRes color: Int) {
        textColor = color
    }

    /**设置文字大小 */
    fun setTextSize(size: Float) {
        textSize = size
    }

    /**设置坐标大小,如 :5X5等  */
    fun setCoordinateSize(horizontal: Int, vertical: Int) {
        horizontalNum = horizontal
        verticalNum = vertical
        reStart()
    }

    /**设置坐标大小 */
    fun setCoordinateSize(size:Int){
        setCoordinateSize(size,size)
    }

    /**设置分数比例 每次消除合并后的值*scale 加在总分数上 */
    fun setScoreScale(scale: Int) {
        this.scale = scale
    }

    /**设置数据*/
    fun setData(data:Array<IntArray>?){
        if (data!=null && data.isNotEmpty()){
            attr=data
            invalidate()
        }
    }
    /**获取数据*/
    fun getData():Array<IntArray>{
        return attr
    }

    /**设置分数*/
    fun setScore(score:Int){
        totalScore=score
    }

    /**获取分数*/
    fun getScore():Int{
        return totalScore
    }

    /**设置颜色集合*/
    fun setColors(colors:IntArray){
        colorAttr=colors
        invalidate()
    }
}
