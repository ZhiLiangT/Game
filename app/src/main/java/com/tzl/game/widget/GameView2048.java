package com.tzl.game.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tzl.game.R;


/**
 * Create by 田志亮 on 2018/8/18
 */
public class GameView2048 extends View {

    /**横向数量*/
    private int horizontalNum=5;
    /**纵向数量*/
    private int verticalNum=5;
    /**View的宽度*/
    private float viewWidth;
    /**View的高度*/
    private float viewHeight;
    /**每个小方块的宽*/
    private float itemWidht;
    /**每个小方块的高*/
    private float itemHeight;
    /**背景颜色*/
    private int bgColor=Color.parseColor("#C9BDB3");
    /**分割线颜色*/
    private int dividerColor=Color.parseColor("#464C62");
    /**分割线的宽度*/
    private float dividerSize=5;
    /**数字的颜色*/
    private int textColor=Color.BLACK;
    /**数字的大小*/
    private float textSize=30;
    /**数据集合*/
    private int [][] attr=null;
    /**总分数*/
    private int totalScore;
    /**分数比例*/
    private int scale=1;
    /**是否开启Test模式*/
    private boolean isTest=false;
    private Context context;
    /**画笔*/
    private Paint paint;
    /**文字画笔*/
    private Paint textPaint;

    private int color_2=Color.parseColor("#EEE4DA");
    private int color_4=Color.parseColor("#EDE0C8");
    private int color_8=Color.parseColor("#F2B179");
    private int color_16=Color.parseColor("#F59563");
    private int color_32=Color.parseColor("#F67C5F");
    private int color_64=Color.parseColor("#EDCF72");
    private int color_128=Color.parseColor("#F2CF60");
    private int color_256=Color.parseColor("#F65E3B");
    private int color_512=Color.parseColor("#EDC850");
    private int color_1024=Color.parseColor("#EDC53F");

    public GameView2048(Context context) {
        this(context,null);
    }

    public GameView2048(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GameView2048(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(attrs,defStyleAttr);
    }

    /**
     * 测试数据
     */
    public void testData(){
        attr= new int[][]{
                new int[]{0, 2, 0, 8, 16},
                new int[]{0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0},
                new int[]{2, 0, 0, 0, 2}
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth=MeasureSpec.getSize(widthMeasureSpec);
        viewHeight=MeasureSpec.getSize(heightMeasureSpec);
        itemHeight=viewHeight/verticalNum;
        itemWidht=viewWidth/horizontalNum;
    }

    private void init(AttributeSet attrs,int defStyleAttr) {
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameView2048,defStyleAttr,0);
        if (isTest){
            testData();
        }else {
            attr=new int[verticalNum][];
            for (int i=0;i<verticalNum;i++){
                attr[i]=new int[horizontalNum];
            }
            for (int i=0;i<2;i++){
                int h= (int) (Math.random()*5);
                int v= (int) (Math.random()*5);
                if (attr[h][v]==0){
                    attr[h][v]=2;
                }
            }
        }
        horizontalNum=typedArray.getInteger(R.styleable.GameView2048_game_horizontal,5);
        verticalNum=typedArray.getInteger(R.styleable.GameView2048_game_vertical,5);
        bgColor=typedArray.getColor(R.styleable.GameView2048_game_bg, bgColor);
        dividerColor=typedArray.getColor(R.styleable.GameView2048_game_divider_color,dividerColor);
        dividerSize=typedArray.getDimension(R.styleable.GameView2048_game_divider_size,dividerSize);
        textColor=typedArray.getColor(R.styleable.GameView2048_game_text_color,textColor);
        textSize=typedArray.getDimension(R.styleable.GameView2048_game_text_size,textSize);
        typedArray.recycle();
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(dividerSize);
        paint.setColor(dividerColor);
        textPaint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgColor);
        paint.setColor(dividerColor);
        //绘制横向分割线
        for (int i=0;i<=verticalNum;i++){
            canvas.drawLine(0,i*(itemWidht),viewWidth,i*(itemWidht),paint);
        }
        //绘制纵向分割线
        for (int i=0;i<=horizontalNum;i++){
            canvas.drawLine(i*itemHeight,0,i*itemHeight,viewHeight,paint);
        }
        for (int i=0;i<attr.length;i++){
            for (int j=0;j<attr[i].length;j++){
                if (attr[i][j]!=0){
                    switch (attr[i][j]){
                        case 2:
                            paint.setColor(color_2);
                            break;
                        case 4:
                            paint.setColor(color_4);
                            break;
                        case 8:
                            paint.setColor(color_8);
                            break;
                        case 16:
                            paint.setColor(color_16);
                            break;
                        case  32:
                            paint.setColor(color_32);
                            break;
                        case  64:
                            paint.setColor(color_64);
                            break;
                        case 128:
                            paint.setColor(color_128);
                            break;
                        case 256:
                            paint.setColor(color_256);
                            break;
                        case 512:
                            paint.setColor(color_512);
                            break;
                        case 1024:
                            paint.setColor(color_1024);
                            break;
                    }
                    canvas.drawRect(itemWidht*i+dividerSize/2,itemHeight*j+dividerSize/2,
                            itemWidht*(i+1)-dividerSize/2,itemHeight*(j+1)-dividerSize/2,paint);
                    canvas.drawText(attr[i][j]+"",(float)(i+0.5)*itemWidht,
                            (float)(j+0.5)*itemHeight-((textPaint.descent() + textPaint.ascent())/ 2),textPaint);
                }
            }
        }
    }
    private int startX;
    private int startY;
    private boolean isMove=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isMove=false;
                startX= (int) event.getX();
                startY= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                isMove=true;
                break;
            case MotionEvent.ACTION_UP:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                if (isMove){
                    int offsetX= endX -startX;
                    int offsetY= endY -startY;
                    isChange=false;
                    if (offsetX>0&&Math.abs(offsetX)>Math.abs(offsetY)){
                        //右滑
                        slideAfter(3);
                    }else if (offsetX<0&&Math.abs(offsetX)>Math.abs(offsetY)){
                        //左划
                        slideAfter(2);
                    }else if (offsetY>0&&Math.abs(offsetX)<Math.abs(offsetY)){
                        //下划
                        slideAfter(1);
                    }else if (offsetY<0&&Math.abs(offsetX)<Math.abs(offsetY)){
                        //上划
                        slideAfter(0);
                    }
                }
                break;
        }
        return true;
    }

    /**判断是否发生了变化*/
    private boolean isChange=false;

    /**
     * 滑动之后操作
     * @param direction 滑动的方向 0：表示上划，1：表示下划，2：表示左划，3：表示右划
     */
    public void slideAfter(int direction){
        switch (direction){
            case 0:
                for (int i=0;i<attr.length;i++){
                    int index=0;
                    for (int j=0;j<attr[i].length;j++){
                        if (attr[i][j]!=0){
                            if (attr[i][index]==0 && j!=index){
                                isChange=true;
                                attr[i][index]=attr[i][j];
                                attr[i][j]=0;
                            }else if(attr[i][index]==attr[i][j] && j!=0 && j!=index){
                                isChange=true;
                                attr[i][index]=attr[i][j]*2;
                                attr[i][j]=0;
                                totalScore+=attr[i][index]*scale;
                                index=index+1;
                            }else if (attr[i][index]!=attr[i][j] && j!=index){
                                attr[i][index+1]=attr[i][j];
                                if (j-index>1){
                                    isChange=true;
                                    attr[i][j]=0;
                                }
                                index=index+1;
                            }
                        }
                    }
                }
                break;
            case 1:
                for (int i=0;i<attr.length;i++){
                    int index=attr[i].length-1;
                    for (int j=attr[i].length-1;j>=0;j--){
                        if (attr[i][j]!=0){
                            if (attr[i][index]==0 && j!=index){
                                isChange=true;
                                attr[i][index]=attr[i][j];
                                attr[i][j]=0;
                            }else if(attr[i][index]==attr[i][j] && j!=attr[i].length-1 && j!=index){
                                isChange=true;
                                attr[i][index]=attr[i][j]*2;
                                attr[i][j]=0;
                                totalScore+=attr[i][index]*scale;
                                index=index-1;
                            }else if (attr[i][index]!=attr[i][j] && j!=index){
                                attr[i][index-1]=attr[i][j];
                                if (index-j>1){
                                    isChange=true;
                                    attr[i][j]=0;
                                }
                                index=index-1;
                            }
                        }
                    }
                }
                break;
            case 2:
                //左划
                for (int i=0;i<attr.length;i++){
                    int index=0;
                    for (int j=0;j<attr.length;j++){
                        if (attr[j][i]!=0){
                            if (attr[index][i]==0 && j!=index){
                                isChange=true;
                                attr[index][i]=attr[j][i];
                                attr[j][i]=0;
                            }else if (attr[index][i]==attr[j][i] && j!=0 && j!=index){
                                isChange=true;
                                attr[index][i]=attr[j][i]*2;
                                attr[j][i]=0;
                                totalScore+=attr[index][i]*scale;
                                index=index+1;
                            }else if (attr[index][i]!=attr[j][i] && j!=index){
                                attr[index+1][i]=attr[j][i];
                                if (j-index>1){
                                    isChange=true;
                                    attr[j][i]=0;
                                }
                                index=index+1;
                            }
                        }
                    }
                }

                break;
            case 3:
                //右划
                for (int i=0;i<attr.length;i++){
                    int index=attr.length-1;
                    for (int j=attr.length-1;j>=0;j--){
                        if (attr[j][i]!=0){
                            if (attr[index][i]==0 && j!=index){
                                isChange=true;
                                attr[index][i]=attr[j][i];
                                attr[j][i]=0;
                            }else if (attr[index][i]==attr[j][i] && j!=0 && j!=index){
                                isChange=true;
                                attr[index][i]=attr[j][i]*2;
                                attr[j][i]=0;
                                totalScore+=attr[index][i]*scale;
                                index=index-1;
                            }else if (attr[index][i]!=attr[j][i] && j!=index){
                                attr[index-1][i]=attr[j][i];
                                if (index-j>1){
                                    isChange=true;
                                    attr[j][i]=0;
                                }
                                index=index-1;
                            }
                        }
                    }
                }
                break;
        }
        if (listener!=null){
            listener.onChange(totalScore);
        }
        makeRandom();
    }

    /**重新生成随机数*/
    public void makeRandom(){
        if (isChange){
            while (true){
                int h= (int) (Math.random()*5);
                int v= (int) (Math.random()*5);
                if (attr[h][v]==0){
                    attr[h][v]=2;
                    invalidate();
                    return;
                }
            }
        }
    }

    /**
     * 重新开始
     */
    public void reStart(){
        attr=new int[verticalNum][];
        for (int i=0;i<verticalNum;i++){
            attr[i]=new int[horizontalNum];
        }
        for (int i=0;i<2;i++){
            int h= (int) (Math.random()*5);
            int v= (int) (Math.random()*5);
            if (attr[h][v]==0){
                attr[h][v]=2;
            }
        }
    }

    /**
     * 判断是否结束
     */
    public void isFinish(){

    }

    /**
     * 回调分数
     */
    public interface OnScoreChangeListener{
        void onChange(int total);
    }
    private OnScoreChangeListener listener;

    public void setOnScoreChangeListener(OnScoreChangeListener listener){
        this.listener=listener;
    }

    /********************************************set方法*******************************************/

    /**设置分割线颜色*/
    public void setDividerColor(@ColorRes int color){
        dividerColor=color;
    }
    /**设置分割线粗细*/
    public void setDividerSize(float size){
        dividerSize=size;
    }

    public void setBackgroundColor(@ColorRes int color){
        bgColor=color;
    }
    /**设置文字颜色*/
    public void setTextColor(@ColorRes int color){
        textColor=color;
    }
    /**设置文字大小*/
    public void setTextSize(float size){
        textSize=size;
    }
    /**设置坐标大小,如 :5X5等 */
    public void setCoordinateSize(int horizontal,int vertical){
        horizontalNum=horizontal;
        verticalNum=vertical;
    }

    /**设置分数比例 每次消除合并后的值*scale 加在总分数上*/
    public void setScoreScale(int scale){
        this.scale=scale;
    }

}
