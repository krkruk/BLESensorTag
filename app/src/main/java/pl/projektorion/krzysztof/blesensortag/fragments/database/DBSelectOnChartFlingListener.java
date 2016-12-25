package pl.projektorion.krzysztof.blesensortag.fragments.database;

import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectOnChartFlingListener implements OnChartGestureListener {

    private long startAt = 0;
    private long recordsInTotal = 0;
    private final long MAX_NO_ELEMENTS_PER_LOAD;
    private float velocityThreshold = 1500.0f;

    private Runnable task = null;

    public DBSelectOnChartFlingListener(long recordsInTotal, long MAX_NO_ELEMENTS_PER_LOAD) {
        this.recordsInTotal = recordsInTotal;
        this.MAX_NO_ELEMENTS_PER_LOAD = MAX_NO_ELEMENTS_PER_LOAD;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", String.format("vX: %f, vY: %f", velocityX, velocityY));
        final long initialStartAt = startAt;

        if( velocityX < -velocityThreshold )
        {
            /*
            Load new elements
             */
            startAt += MAX_NO_ELEMENTS_PER_LOAD;
            startAt = startAt > recordsInTotal ? 0 : startAt;
        } else
        if( velocityX > velocityThreshold )
        {
            /*
            Load previous elements
             */
            startAt -= MAX_NO_ELEMENTS_PER_LOAD;
            startAt = startAt < 0 ? 0 : startAt;
        }

        if( initialStartAt != startAt  && task != null) {
            task.run();
            Log.i("StartAt", "Started: " + startAt);
        }
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    public long getStartAt() {
        return startAt;
    }

    public long getMaxElementsPerLoad() {
        return MAX_NO_ELEMENTS_PER_LOAD;
    }

    public float getVelocityThreshold() {
        return velocityThreshold;
    }

    public void setVelocityThreshold(float velocityThreshold) {
        this.velocityThreshold = velocityThreshold;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }
}
