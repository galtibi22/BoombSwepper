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
    public static final int NUM_OF_PLAYERS=10;
    private static Menu instance;
    private ArrayList<Player> players=new ArrayList<>();
    private Level gameLevel;
    public Point screenSize;
    private final String DATA_FILE="data.txt";
    private LocationService locationService;
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
        checkLocationPermiton();
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
        intent = new Intent().setClass(this, TableTab.class);
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

    public void initGameInstance(View view){
        if (getCurrentLocation() != null) {
            Button button = (Button) view;
            gameLevel = Level.valueOf(button.getText().toString());
            Intent intent = new Intent(Menu.getInstance(), GameInstance.class);
            startActivity(intent);
        }
        }

    private void setScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
    }

    public LatLng getCurrentLocation(){
        locationService = new LocationService(this);
        LatLng location=null;
        // check if GPS enabled
        if(locationService.canGetLocation()){
            location=locationService.getLocation();
        }else{
            locationService.showSettingsAlert();
        }
        return location;
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
                player.setAddress(playerString[3].replaceAll("@","\n"));
                LatLng location=new LatLng(Double.parseDouble(playerString[4]),Double.parseDouble(playerString[5]));
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
        player1.setAddress("Alumin 25 \nIsrael");
        Player player2=new Player();
        player2.setName("Player2");
        player2.setTime(2000);
        player2.setLevel(Level.MEDIUM);
        player2.setLocation(new LatLng(32.137877,34.804383));
        player2.setAddress("Compound according rolls Ramat Hasharon \nIsrael");
        Player player3=new Player();
        player3.setName("Player3");
        player3.setTime(3000);
        player3.setLevel(Level.HARD);
        player3.setLocation(new LatLng(32.137088,34.798669));
        player3.setAddress("Namir 301 Tel Aviv\nIsrael");
        Player player4=new Player();
        player4.setName("Player4");
        player4.setTime(3021);
        player4.setLevel(Level.EASY);
        player4.setLocation(new LatLng(32.093754, 34.874269));
        player4.setAddress("Prisoners of Zion 11 Petah Tikva\nIsrael");
        Player player5=new Player();
        player5.setName("Player5");
        player5.setTime(211);
        player5.setLevel(Level.MEDIUM);
        player5.setLocation(new LatLng(32.093732, 34.874212));
        player5.setAddress("Prisoners of Zion 13 Petah Tikva\nIsrael");
        Player player6=new Player();
        player6.setName("Player6");
        player6.setTime(11);
        player6.setLevel(Level.MEDIUM);
        player6.setLocation(new LatLng(32.093714, 34.874212));
        player6.setAddress("Prisoners of Zion 56 Petah Tikva\nIsrael");
        Player player7=new Player();
        player7.setName("Player7");
        player7.setTime(20);
        player7.setLevel(Level.HARD);
        player7.setLocation(new LatLng(32.137821,34.804311));
        player7.setAddress("Compound according rolls Ramat Hasharon \nIsrael");
        Player player8=new Player();
        player8.setName("Player8");
        player8.setTime(321);
        player8.setLevel(Level.EASY);
        player8.setLocation(new LatLng(32.093721, 34.872269));
        player8.setAddress("Prisoners of Zion 2 Petah Tikva\nIsrael");
        Player player9=new Player();
        player9.setName("Player9");
        player9.setTime(321);
        player9.setLevel(Level.EASY);
        player9.setLocation(new LatLng(32.493721, 34.872269));
        player9.setAddress("Prisoners of Zion 6432 Petah Tikva\nIsrael");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);
        players.add(player7);
        players.add(player8);
        players.add(player9);
        players.add(player7);
    }
    /**
     saveDataFile method - save list of player to data file with "," sprate
     */
    public void saveDataFile(){
        try {
            FileOutputStream dataFile = openFileOutput(DATA_FILE, Context.MODE_PRIVATE);
            dataFile.flush();
            for (Player player:players){
                dataFile.write((player.toString().replaceAll("\n","@")+"\n").getBytes());
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

    private void checkLocationPermiton(){
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

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }
}
