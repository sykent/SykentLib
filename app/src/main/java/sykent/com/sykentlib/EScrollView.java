package sykent.com.sykentlib;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/16
 */
public class EScrollView extends View {

    private Scroller mScroller = new Scroller(getContext());

    private int mLastX;
    private int mLastY;

    public EScrollView(Context context) {
        super(context);
    }

    public EScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        mScroller.startScroll(scrollX, 0, deltaX, 0, 1000);
        Log.d("TTT", "scrollX: " + scrollX + "  deltaX: " + deltaX);
        invalidate();
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastX;
                int dy = y - mLastY;
                int translationX = (int) (getTranslationX() + dx);
                int translationY = (int) (getTranslationY() + dy);
                smoothScrollTo(translationX, translationY);
                setTranslationX(translationX);
//                setTranslationY(translationY);
                Log.d("TTT", "dx: " + dx + "  dy: " + dy);
                break;
        }

        mLastX = x;
        mLastY = y;

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xffff0000);
    }
}
