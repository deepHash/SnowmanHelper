package ac.shenkar.kaktusx.snowproject;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Date;
import java.util.Random;

/**
 * Created by KaktusX on 02/01/2017.
 */

public class SnowFlake extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    int flakeDrawables[] = new int[5];
    private int nextFlakeDrawable;
    private ValueAnimator flakeAnimation;
    private SnowFlakeListener aListener;
    private boolean dropped;

    public SnowFlake(Context context) {
        super(context);

    }

    public SnowFlake(Context context, int color, int size) {
        super(context);
        aListener = (SnowFlakeListener) context;
        flakeDrawables[0] = R.drawable.snowflake;
        flakeDrawables[1] = R.drawable.snowflake2;
        flakeDrawables[2] = R.drawable.snowflake3;
        flakeDrawables[3] = R.drawable.snowflake4;
        flakeDrawables[4] = R.drawable.snowflake5;

        Random random = new Random(new Date().getTime());
        nextFlakeDrawable = random.nextInt(4);
        this.setImageResource(flakeDrawables[nextFlakeDrawable]);
        this.setColorFilter(color);

        int dpHeight = PixelHelper.pixelsToDp(size, context);
        int dpWidth = PixelHelper.pixelsToDp(size, context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);

    }

    public void dropFlake(int screenHeight, int duaration){
        flakeAnimation = new ValueAnimator();
        flakeAnimation.setDuration(duaration);
        flakeAnimation.setFloatValues(0f, screenHeight);
        flakeAnimation.setInterpolator(new LinearInterpolator());
        flakeAnimation.setTarget(this);
        flakeAnimation.addListener(this);
        flakeAnimation.addUpdateListener(this);
        flakeAnimation.start();
    }
    //setting the flake to remove him
    protected void setDropped(boolean dropped){

        if (dropped){
            animate().cancel();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!dropped) {
            aListener.removeSnowFlake(this, false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setY((float) animation.getAnimatedValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!dropped && event.getAction() == MotionEvent.ACTION_DOWN) {
            aListener.removeSnowFlake(this, true);
            dropped = true;
            flakeAnimation.cancel();
        }
        return super.onTouchEvent(event);
    }

    public interface SnowFlakeListener {
        void removeSnowFlake(SnowFlake flake, boolean userTouch);
    }
}
