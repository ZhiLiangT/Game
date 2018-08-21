package com.tzl.game.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.tzl.game.R


/**
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
    private var paint: Paint? = null
    /**文字画笔 */
    private var textPaint: Paint? = null

    private val color_2 = Color.parseColor("#EEE4DA")
    private val color_4 = Color.parseColor("#EDE0C8")
    private val color_8 = Color.parseColor("#F2B179")
    private val color_16 = Color.parseColor("#F59563")
    private val color_32 = Color.parseColor("#F67C5F")
    private val color_64 = Color.parseColor("#EDCF72")
    private val color_128 = Color.parseColor("#F2CF60")
    private val color_256 = Color.parseColor("#F65E3B")
    private val color_512 = Color.parseColor("#EDC850")
    private val color_1024 = Color.parseColor("#EDC53F")
    private var startX: Int = 0
    private var startY: Int = 0
    private var isMove = false

    /**判断是否发生了变化 */
    private var isChange = false
    private var listener: OnScoreChangeListener? = null

    /**
     * 测试数据
     */
    private fun testData() {
        attr = arrayOf(intArrayOf(0, 2, 0, 8, 16), intArrayOf(0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0), intArrayOf(2, 0, 0, 0, 2))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec).toFloat()
        itemHeight = viewHeight / verticalNum
        itemWidht = viewWidth / horizontalNum
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.GameView2048, defStyleAttr, 0)
        if (isTest) {
            testData()
        } else {
            for (i in 0 until verticalNum) {
                attr[i] = IntArray(horizontalNum)
            }
            for (i in 0..1) {
                val h = (Math.random() * 5).toInt()
                val v = (Math.random() * 5).toInt()
                if (attr[h][v] == 0) {
                    attr[h][v] = 2
                }
            }
        }
        horizontalNum = typedArray.getInteger(R.styleable.GameView2048_game_horizontal, 5)
        verticalNum = typedArray.getInteger(R.styleable.GameView2048_game_vertical, 5)
        bgColor = typedArray.getColor(R.styleable.GameView2048_game_bg, bgColor)
        dividerColor = typedArray.getColor(R.styleable.GameView2048_game_divider_color, dividerColor)
        dividerSize = typedArray.getDimension(R.styleable.GameView2048_game_divider_size, dividerSize)
        textColor = typedArray.getColor(R.styleable.GameView2048_game_text_color, textColor)
        textSize = typedArray.getDimension(R.styleable.GameView2048_game_text_size, textSize)
        typedArray.recycle()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.strokeWidth = dividerSize
        paint!!.color = dividerColor
        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textPaint!!.textAlign = Paint.Align.CENTER
        textPaint!!.textSize = textSize
        textPaint!!.color = textColor

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(bgColor)
        paint!!.color = dividerColor
        //绘制横向分割线
        for (i in 0..verticalNum) {
            canvas.drawLine(0f, i * itemWidht, viewWidth, i * itemWidht, paint!!)
        }
        //绘制纵向分割线
        for (i in 0..horizontalNum) {
            canvas.drawLine(i * itemHeight, 0f, i * itemHeight, viewHeight, paint!!)
        }
        for (i in attr.indices) {
            for (j in 0 until attr[i].size) {
                if (attr[i][j] != 0) {
                    when (attr[i][j]) {
                        2 -> paint!!.color = color_2
                        4 -> paint!!.color = color_4
                        8 -> paint!!.color = color_8
                        16 -> paint!!.color = color_16
                        32 -> paint!!.color = color_32
                        64 -> paint!!.color = color_64
                        128 -> paint!!.color = color_128
                        256 -> paint!!.color = color_256
                        512 -> paint!!.color = color_512
                        1024 -> paint!!.color = color_1024
                    }
                    canvas.drawRect(itemWidht * i + dividerSize / 2, itemHeight * j + dividerSize / 2,
                            itemWidht * (i + 1) - dividerSize / 2, itemHeight * (j + 1) - dividerSize / 2, paint!!)
                    canvas.drawText(attr[i][j].toString() + "", (i + 0.5).toFloat() * itemWidht,
                            (j + 0.5).toFloat() * itemHeight - (textPaint!!.descent() + textPaint!!.ascent()) / 2, textPaint!!)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
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
    }

    /**重新生成随机数 */
    private fun makeRandom() {
        if (isChange) {
            while (true) {
                val h = (Math.random() * 5).toInt()
                val v = (Math.random() * 5).toInt()
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
    }

    /**
     * 重新开始
     */
    fun reStart() {
        attr =  Array(verticalNum){IntArray(horizontalNum)}
        for (i in 0 until verticalNum) {
            attr[i] = IntArray(horizontalNum)
        }
        for (i in 0..1) {
            val h = (Math.random() * 5).toInt()
            val v = (Math.random() * 5).toInt()
            if (attr[h][v] == 0) {
                if (totalScore>5000 && (h+v)>5){
                    attr[h][v] = 4
                }else{
                    attr[h][v] = 2
                }
            }
        }
    }

    /**
     * 判断是否结束
     */
    fun isFinish() {

    }

    /**
     * 回调分数
     */
    interface OnScoreChangeListener {
        fun onChange(total: Int)
    }

    fun setOnScoreChangeListener(listener: OnScoreChangeListener) {
        this.listener = listener
    }

    /********************************************set方法*****************************************/

    /**设置分割线颜色 */
    fun setDividerColor(@ColorRes color: Int) {
        dividerColor = color
    }

    /**设置分割线粗细 */
    fun setDividerSize(size: Float) {
        dividerSize = size
    }

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

    fun getData():Array<IntArray>{
        return attr
    }
}
