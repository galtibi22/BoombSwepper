package tbject.com.bombswepper.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import tbject.com.bombswepper.PlayerComparator;
import tbject.com.bombswepper.R;
import tbject.com.bombswepper.pojo.Level;
import tbject.com.bombswepper.pojo.Player;
import tbject.com.bombswepper.services.LocationService;

public class Menu extends TabActivity implements OnTabChangeListener{

    private static Menu instance;
    private ArrayList<Player> players=new ArrayList<>();
    private Level gameLevel;

    public Point screenSize;
    private final String DATA_FILE="data.txt";
    private LocationService gps;



    private LatLng gameLocation;
    private TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide android upper bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        instance=this;
        setScreenSize();
        readDataFile();
        initLocationService();
        initTabs();

    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDataFile();
    }

    private void initTabs(){
        // Get TabHost Refference
        tabHost = getTabHost();

        // Set TabChangeListener called when tab changed
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec spec;
        Intent intent;

        // Create  Intents to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, WinnerTableTab.class);
        spec = tabHost.newTabSpec("First").setIndicator("Table")
                .setContent(intent);
        //Add intent to tab
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MapsTab.class);
        spec = tabHost.newTabSpec("Second").setIndicator("Map")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.getTabWidget().setCurrentTab(0);
    }

    public void initGameTable(View view){
        Button button =(Button)view;
        gameLevel=Level.valueOf(button.getText().toString());
        gameLocation=getCurrentLocation();
    }

    private void setScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
    }

    private LatLng getCurrentLocation(){
        gps = new LocationService(this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            Intent intent = new Intent(Menu.getInstance(), GameInstance.class);
            startActivity(intent);
            return gps.getLocation();
                    /* double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();*/
        }else{
            gps.showSettingsAlert();
            return null;
        }
    }
    /**
     * readDataFile method - read data file and create list of players;
     */
    private void readDataFile(){
        try {
            players.clear();
            FileInputStream file=openFileInput(DATA_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line=br.readLine())!=null){
                String [] playerString=line.split(",");
                Player player=new Player();
                player.setName(playerString[0]);
                player.setTime(Integer.parseInt(playerString[1]));
                player.setLevel(Level.valueOf(playerString[2]));
                LatLng location=new LatLng(Double.parseDouble(playerString[3]),Double.parseDouble(playerString[4]));
                player.setLocation(location);
                players.add(player);
            }
        }   catch (Exception e){
            Log.w("INFO","data.txt not exist. Use demo data");
            insertDemoData();
        }
        Collections.sort(players,new PlayerComparator());
    }

    private void insertDemoData() {

        Player player1=new Player();
        player1.setName("Player1");
        player1.setTime(1000);
        player1.setLevel(Level.EASY);
        player1.setLocation(new LatLng(32.120045,34.808768));
        Player player2=new Player();
        player2.setName("Player2");
        player2.setTime(2000);
        player2.setLevel(Level.MEDIUM);
        player2.setLocation(new LatLng(32.137877,34.804383));
        Player player3=new Player();
        player3.setName("Player3");
        player3.setTime(3000);
        player3.setLevel(Level.HARD);
        player3.setLocation(new LatLng(32.137088,34.798669));
        players.add(player1);
        players.add(player2);
        players.add(player3);

    }
    /**
     saveDataFile method - save list of player to data file with "," sprate
     */
    private void saveDataFile(){
        try {
            FileOutputStream dataFile = openFileOutput(DATA_FILE, Context.MODE_PRIVATE);
            for (Player player:players){
                dataFile.write((player+"\n").getBytes());
            }
            dataFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(Menu.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION)
                != MockPackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(Menu.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Menu.getInstance());
                alertDialog.setTitle("Location services required permissions");
                alertDialog.setMessage("Application will be closed");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }
                });
                alertDialog.show();
            }
        }
    }

    private void initLocationService(){
        int REQUEST_CODE_PERMISSION = 2;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Menu getInstance() {
        return instance;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Level getGameLevel() {
        return gameLevel;
    }

    public LatLng getGameLocation() {
        return gameLocation;
    }
    public Point getScreenSize() {
        return screenSize;
    }

}
