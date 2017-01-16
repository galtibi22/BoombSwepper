package tbject.com.bombswepper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import tbject.com.bombswepper.R;

public class OpenPage extends CommonActivity {
    private final int TIME_OUT = 2000;

    private static OpenPage instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_open_page);
        final View myLayout = findViewById(R.id.activity_open_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(OpenPage.this, MainAccelerometer.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);

    }

    public static OpenPage getInstance() {
        return instance;
    }



}
