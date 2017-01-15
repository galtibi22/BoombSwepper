package tbject.com.bombswepper.services;

import android.os.Handler;
import android.os.SystemClock;

import tbject.com.bombswepper.activity.GameInstance;

public class TimerService {
    private Handler customHandler = new Handler();
    private long totlaTimeMili = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private long startTime = 0L;


    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            totlaTimeMili = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + totlaTimeMili;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            GameInstance.getInstance().getTimerValue().setText("" + mins + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };

    /**
     * stopTimer method - return the total time in mili from timerService.start until timerService.stop
     * @return
     */
    public long stopTimer() {
        timeSwapBuff += totlaTimeMili;
        customHandler.removeCallbacks(updateTimerThread);
        long totalTime=totlaTimeMili;
        totlaTimeMili=0;
        return totalTime;
    }

    /**
     *startTimerService method - start the timer service
     */
    public void startTimer(){
        customHandler.postDelayed(updateTimerThread, 0);
        startTime = SystemClock.uptimeMillis();
    }
}
