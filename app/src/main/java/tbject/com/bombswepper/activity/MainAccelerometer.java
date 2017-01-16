package tbject.com.bombswepper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import tbject.com.bombswepper.R;
import tbject.com.bombswepper.AccelerometerListener;
import tbject.com.bombswepper.services.AccelerometerService;

public class MainAccelerometer extends Activity implements AccelerometerListener {
    int numOfBomb=0;
    private final double INTERVAL=1;
    private final double DEVIIATION=1.3;
    float startX;
    float startY;
    float startZ;
    float lastX;
    float lastY;
    float lastZ;
    boolean first=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_accelerometer);
        // Check onResume Method to start accelerometer listener
    }

    public void onAccelerationChanged(float x, float y, float z) {
        if (first){
            startX=x;startY=y;startZ=z;
            first=false;
        }
        if (x >  startX)
            if (x> lastX+INTERVAL) {
                numOfBomb++;
                lastX=x;
                Log.w("Pos","add bomb");
                Log.w("Pos","Num of bomb="+numOfBomb);
            }else if (x<lastX-INTERVAL/DEVIIATION){
                if (numOfBomb>0)
                numOfBomb--;
                lastX=x;
                Log.w("Pos","remove bomb");
                Log.w("Pos","Num of bomb="+numOfBomb);
            }
        if (x < startX)
            if (x< lastX-INTERVAL) {
                numOfBomb++;
                lastX=x;
                Log.w("Pos","add bomb");
                Log.w("Pos","Num of bomb="+numOfBomb);
            }else if (x>lastX+INTERVAL/DEVIIATION){
                if (numOfBomb>0)
                numOfBomb--;
                lastX=x;
                Log.w("Pos","remove bomb");
                Log.w("Pos","Num of bomb="+numOfBomb);
            }
       if (y > INTERVAL + startY)
            if (y> lastY+INTERVAL) {
                numOfBomb++;
                lastY=y;
                Log.w("Pos","add bomb");
                Log.w("Pos","Num of bomb="+numOfBomb);
            }else if (y<lastY-INTERVAL/DEVIIATION){
                numOfBomb--;
                lastY=y;
                Log.w("Pos","remove bomb");
                Log.w("Pos","Num of bomb="+numOfBomb);
            }
        if (y < startY-INTERVAL)
            if (y< lastY-INTERVAL) {
                numOfBomb++;
                lastY=y;
                Log.w("Pos","add bomb");
                Log.w("Pos","Num of bomb="+numOfBomb);
            }else if (y>lastY+INTERVAL/DEVIIATION) {
                numOfBomb--;
                lastY = y;
                Log.w("Pos", "remove bomb");
                Log.w("Pos", "Num of bomb=" + numOfBomb);
            }


    }

    public void onShake(float force) {
        // Do your stuff here
        // Called when Motion Detected
        Toast.makeText(getBaseContext(), "Motion detected",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getBaseContext(), "onResume Accelerometer Started",
                Toast.LENGTH_SHORT).show();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerService.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerService.startListening(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerService.isListening()) {

            //Start Accelerometer Listening
            AccelerometerService.stopListening();

            Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (AccelerometerService.isListening()) {

            //Start Accelerometer Listening
            AccelerometerService.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

}