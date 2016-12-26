package pl.projektorion.krzysztof.blesensortag.fragments.database;

import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * Created by krzysztof on 26.12.16.
 */

public class DBSelectOnChartFlingIDListener implements OnChartGestureListener {

    public interface ChartTask {
        void run(DBSelectOnChartFlingIDListener listener);
    }

    private int chartId = 0;
    private long startAt = 0;
    private long recordsInTotal = 0;
    private long maxElementsPerLoad = 0;
    private float velocityThreshold = 1500.0f;

    private ChartTask task = null;

    public DBSelectOnChartFlingIDListener() {
        maxElementsPerLoad = Long.MAX_VALUE;
    }

    public DBSelectOnChartFlingIDListener(long recordsInTotal, long maxElementsPerLoad, int chartId) {
        this.recordsInTotal = recordsInTotal;
        this.maxElementsPerLoad = maxElementsPerLoad;
        this.chartId = chartId;
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
            startAt += maxElementsPerLoad;
            startAt = startAt > recordsInTotal ? 0 : startAt;
        } else
        if( velocityX > velocityThreshold )
        {
            /*
            Load previous elements
             */
            startAt -= maxElementsPerLoad;
            startAt = startAt < 0 ? 0 : startAt;
        }

        if( initialStartAt != startAt  && task != null) {
            task.run(this);
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
        return maxElementsPerLoad;
    }

    public void setMaxElementsPerLoad(long maxElementsPerLoad) {
        this.maxElementsPerLoad = maxElementsPerLoad;
    }

    public float getVelocityThreshold() {
        return velocityThreshold;
    }

    public void setVelocityThreshold(float velocityThreshold) {
        this.velocityThreshold = velocityThreshold;
    }

    public void setTask(ChartTask task) {
        this.task = task;
    }

    public int getChartId() {
        return chartId;
    }
}
