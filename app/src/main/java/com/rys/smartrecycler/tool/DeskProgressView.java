package com.rys.smartrecycler.tool;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.rys.smartrecycler.inter.OnDeskSelectListener;

/**
 * 创建时间：2019/6/13
 * 作者：李伟斌
 * 功能描述:
 */
public class DeskProgressView extends View {
    public Rect bounds = new Rect();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float stepHeight = 20;
    private float stepGap = 6;
    private float padding = 6;
    private float leftPading = 30;
    private float topLabelHeight = 0;
    private float bottomLabelHeight = 0;
    private String bottomLabel = "未知";
    private int defaultTextColor =Color.parseColor("#333333");
    private int defaultBackgroundColor =Color.parseColor("#d3d3d3");
    private int highColor =Color.parseColor("#eb712b");
    private int lowColor =Color.parseColor("#387ec1");
    private RectF rectF = new RectF();
    private RectF itemRectF = new RectF();
    private Xfermode xFerMode= new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private int currentPercent = 0;  // 0-100, 步长10
    private int width,height;
    private int position = 0;
    private OnDeskSelectListener onDeskSelectListener;
    public DeskProgressView(Context context,int position,String bottomLabel,int curProgress,int width,int height) {
        super(context);
        this.position = position;
        this.bottomLabel = bottomLabel;
        this.currentPercent = curProgress;
        this.width = width;
        this.height = height;
        initView();
    }

    public DeskProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DeskProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获得所有自定义的参数的值
         */
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BoxProgressView, defStyleAttr, 0);
//        int indexCount = a.getIndexCount();
//        for (int i = 0; i < indexCount; i++) {
//            int attr = a.getIndex(i);
//            switch (attr) {
//                case R.styleable.BoxProgressView_box_label:
//                    bottomLabel = a.getString(attr);
//                    break;
//                case R.styleable.BoxProgressView_box_percent:
//                    currentPercent = a.getInteger(attr, currentPercent);
//                    break;
//            }
//        }
//        a.recycle();

    }

    public void initView(){
        //这种情况下的字体不需要跟随系统而改变，使用dp就好。
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(defaultTextColor);
        //顶部文字高度
        String  topLabel = "$"+currentPercent+"%";
        paint.getTextBounds(topLabel, 0, topLabel.length(), bounds);
        topLabelHeight = padding * 2 + bounds.height();
        //底部文字高度
        paint.getTextBounds(bottomLabel, 0, bottomLabel.length(), bounds);
        bottomLabelHeight = padding * 2 + bounds.height();
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setOnDeskSelectListener(OnDeskSelectListener listener){
        this.onDeskSelectListener = listener;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(leftPading, topLabelHeight, w - leftPading, h - bottomLabelHeight);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float viewWidth = width;
        //View 高度 = 计算容器刻度的每隔高度 +  顶部文字高度 + 底部文字高度 + 9 * 刻度间隔 + 10 * 刻度高度
        float viewHeight = topLabelHeight + stepHeight * 10 + stepGap * 9 + bottomLabelHeight;
        setMeasuredDimension(resolveSizeAndState((int) viewWidth, widthMeasureSpec, 0),
                resolveSizeAndState((int) viewHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        //顶部label
        paint.setColor(defaultTextColor);
        canvas.drawText("$"+currentPercent+"%", width / 2, topLabelHeight - padding, paint);

        //保存状态
//        int saveLayer = canvas.saveLayer(rectF, paint);
        canvas.save();
        paint.setColor(defaultBackgroundColor);
        //刻度容器圆角矩形
        canvas.drawRoundRect(rectF, 6f, 6f, paint);
        //设置离屏缓冲
//        paint.setXfermode(xFerMode);
        //刻度值
        int count = currentPercent / 10;
        paint.setColor(count > 5? highColor : lowColor);
        for (int i=1;i<=count;i++) {
            setItemRectF(i, itemRectF);
            canvas.drawRoundRect(itemRectF, 3f, 3f, paint);
        }
//        paint.setXfermode(null);
//        canvas.restoreToCount(saveLayer);
        canvas.restore();
        //底部label
        paint.setColor(defaultTextColor);
        canvas.drawText(bottomLabel, width / 2, height - padding, paint);

    }


    /**
     * 获取刻度的高
     */
    private void setItemRectF(int count,RectF itemRectF) {
        itemRectF.left = leftPading;
        itemRectF.right = width - leftPading;
        itemRectF.bottom = (height - bottomLabelHeight) - (stepHeight + stepGap) * (count - 1);
        itemRectF.top = (height - bottomLabelHeight) - stepHeight * (count) - stepGap * (count - 1);
    }

    /**
     * 初始化数据
     */
    private void initData(String bottomLabel,int curProgress) {
        this.bottomLabel = bottomLabel;
        this.currentPercent = curProgress;
        invalidate();
    }

    /**
     * 初始化数据 带动画
     */
    @SuppressLint("ObjectAnimatorBinding")
    private void initDataWithAnim(int curProgress) {
        if (curProgress < 0 || curProgress > 100) {
            return;
        }
        ValueAnimator o = ObjectAnimator.ofInt(this, "currentPercent", 0, curProgress);
        o.setDuration(1500);
        o.setInterpolator(new DecelerateInterpolator());
        o.start();
    }

    /**
     * 紧初始化数据，返回动画对象
     */
    @SuppressLint("ObjectAnimatorBinding")
    private ValueAnimator initDataGetAnim(int curProgress){
        if (curProgress < 0 || curProgress > 100) {
            return null;
        }
        ValueAnimator o = ObjectAnimator.ofInt(this, "currentPercent", 0, curProgress);
        o.setDuration(500 + 1000 * (curProgress/100));
        o.setInterpolator(new DecelerateInterpolator());
        return o;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.onDeskSelectListener.onSelectSuccess(position);
        }
        return super.onTouchEvent(event);
    }
}
