package tbject.com.bombswepper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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


public class Menu extends CommonActivity {
    public static ArrayList<Player> players=new ArrayList<>();
    public static Level gameLevel;
    public static Point screenSize;
    private final String DATA_FILE="data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        setScreenSize();
        readDataFile();
        insertPlayerToTable();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        insertPlayerToTable();
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveDataFile();
    }
    private void setScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
    }

    public void initGameTable(View view){
        Button button =(Button)view;
        gameLevel=Level.valueOf(button.getText().toString());
        Intent intent = new Intent(this, TableGame.class);
        startActivity(intent);

    }

    public void insertPlayerToTable() {
        TableLayout tableLayout= (TableLayout) findViewById(R.id.score_table_layout);
        tableLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        if (tableLayout.getChildCount() >1) {
            View mainRow = tableLayout.getChildAt(0);
            tableLayout.removeAllViews();
            tableLayout.addView(mainRow);
        }

        for (int i=0;i<players.size();i++){

            TableRow tableRow=new TableRow(this);
            if (i%2==0)
                tableRow.setBackgroundColor(Color.parseColor("#d4e3fc"));
            else
                tableRow.setBackgroundColor(Color.parseColor("#76ABF9"));
            TextView name=initTableTextView();
            name.setText(players.get(i).getName());
            name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            name.setTextSize(18);

            TextView time=initTableTextView();
            time.setText(players.get(i).getTime()+"");
            time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            time.setTextSize(18);

            TextView level=initTableTextView();
            level.setText(players.get(i).getLevel().toString());
            level.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            level.setTextSize(18);
            tableRow.addView(name);
            tableRow.addView(level);
            tableRow.addView(time);
            tableLayout.addView(tableRow);
        }
    }
    private TextView initTableTextView(){
        TextView textView=new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(18);
        return textView;
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
        Player player2=new Player();
        player2.setName("Player2");
        player2.setTime(2000);
        player2.setLevel(Level.MEDIUM);
        Player player3=new Player();
        player3.setName("Player3");
        player3.setTime(3000);
        player3.setLevel(Level.HARD);
        players.add(player1);
        players.add(player2);
        players.add(player3);

    }

    /**
     * saveDataFile method - save list of player to data file with "," sprate
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

}
