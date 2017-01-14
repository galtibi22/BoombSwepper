package tbject.com.bombswepper.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collections;

import tbject.com.bombswepper.BoardManager;
import tbject.com.bombswepper.PlayerComparator;
import tbject.com.bombswepper.R;
import tbject.com.bombswepper.pojo.Box;
import tbject.com.bombswepper.pojo.Player;


public class TableGame extends CommonActivity {

    private static TableGame instance;
    private BoardManager boardManager;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private TextView timerValue;
    private TextView totalFlagValue;
    private boolean lost;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_game_table);
        initGameTable();
        initBarGame();
    }


    private void initBarGame() {
        TextView level = (TextView) findViewById(R.id.level_value);
        level.setText(Menu.gameLevel.toString());
        timerValue = (TextView) findViewById(R.id.total_time_value);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        totalFlagValue = (TextView) findViewById(R.id.total_flag_value);
        totalFlagValue.setText(boardManager.getNumOfFlag() + "");
    }

    private void initGameTable() {
        boardManager = new BoardManager();
        boardManager.initBoard(Menu.gameLevel);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.game_table);
        for (int y = 0; y < boardManager.getBoard().getRows(); y++) {
            TableRow tableRow = new TableRow(this);
            for (int x = 0; x < boardManager.getBoard().getColumns(); x++) {
                Box box=boardManager.getBoard().getBox(y,x);
                int boxSize=boardManager.getBoard().getSizeOfBox();
                tableRow.addView(box.getBoxButton(),boxSize,boxSize);
            }
            tableLayout.addView(tableRow);
        }
        tableLayout.requestLayout();
    }


    EditText name;
    int timeOfGameSec;

    public void win() {
        stopTimer();
        timeOfGameSec = (int) timeInMilliseconds / 1000;

        LinearLayout linearLayout = initGameFinishLayout();
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(this);
        title.setText("Good Job");
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(22);
        linearLayout.addView(title);
        if (timeOfGameSec < Menu.players.get(Menu.players.size() - 1).getTime()) {
            TextView newReachedMessage = new TextView(this);
            newReachedMessage.setText("  You have reached a new record." +
                    "\n  You finish level: " + Menu.gameLevel.toString() + " in " + timeOfGameSec + " seconds");
            newReachedMessage.setGravity(Gravity.LEFT);
            newReachedMessage.setTextSize(16);
            linearLayout.addView(newReachedMessage);
            TextView nameView=new TextView(this);
            nameView.setText("  What is your name?");
            nameView.setGravity(Gravity.LEFT);
            nameView.setTextSize(16);
            name = new EditText(this);
            name.setText("name");
            name.setTextSize(12);
            name.setTextColor(Color.GRAY);
            name.setGravity(Gravity.LEFT);
            name.setBackground(getResources().getDrawable(R.drawable.inputname));
            name.setMinWidth(130);
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.LEFT);
            row.setWeightSum(200);
            row.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            row.addView(nameView);
            row.addView(name);
            linearLayout.addView(row);
        }
        linearLayout.addView(initNewGameRequestLayout());
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(linearLayout);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void lost() {
        lost = true;
        for (int x = 0; x < boardManager.getBoard().getRows(); x++)
            for (int y = 0; y < boardManager.getBoard().getColumns(); y++) {
                boardManager.openBox(boardManager.getBoard().getBox(y,x));
            }
        stopTimer();
        LinearLayout linearLayout = initGameFinishLayout();
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(this);
        title.setText("You lost");
        title.setGravity(Gravity.CENTER);
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        linearLayout.addView(title);
        linearLayout.addView(initNewGameRequestLayout());
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(linearLayout);
        alertDialog.show();
    }

    private LinearLayout initNewGameRequestLayout(){
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView newGameMessage = new TextView(this);
        newGameMessage.setGravity(Gravity.LEFT);
        newGameMessage.setTextSize(18);
        newGameMessage.setText("\n  Do you want to play again?");
        linearLayout.addView(newGameMessage);
        Button yes = new Button(this);
        yes.setTextSize(12);
        yes.setText("Yes");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name != null && !name.getText().toString().equals("name") && !name.getText().toString().isEmpty() )
                    addNewPlayer();
                Intent intent = new Intent(TableGame.this, TableGame.class);
                finish();
                startActivity(intent);
            }
        });
        Button no = new Button(this);
        no.setText("No");
        no.setTextSize(12);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name != null && !name.getText().toString().equals("name"))
                    addNewPlayer();
                finish();
            }
        });

        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(yes);
        row.addView(no);
        linearLayout.addView(row);
        return linearLayout;
    }
    private LinearLayout initGameFinishLayout(){
        LinearLayout linearLayout= new LinearLayout(this);
        linearLayout.setBackground(getResources().getDrawable(R.drawable.finishgamemessagebackground));
        linearLayout.setPaddingRelative(4,1,1,4);
        return linearLayout;
    }

    private void addNewPlayer() {
        Player player = new Player();
        player.setLevel(Menu.gameLevel);
        player.setName(name.getText().toString());
        player.setTime(timeOfGameSec);
        Menu.players.remove( Menu.players.size()-1);
        Menu.players.add(player);
        Collections.sort(Menu.players,new PlayerComparator());
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };

    private void stopTimer() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }



    public static TableGame getInstance(){
        return instance;
    }

    public boolean isLost() {
        return lost;
    }

    public void setTotalFlagValue(TextView totalFlagValue) {
        this.totalFlagValue = totalFlagValue;
    }

    public TextView getTotalFlagValue() {
        return totalFlagValue;
    }

}