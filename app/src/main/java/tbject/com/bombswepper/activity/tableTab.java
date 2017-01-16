package tbject.com.bombswepper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import tbject.com.bombswepper.R;
import tbject.com.bombswepper.pojo.Player;

public class tableTab extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_tab);
        insertPlayerToTable();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        insertPlayerToTable();
    }

    public void insertPlayerToTable() {
        TableLayout tableLayout= (TableLayout) findViewById(R.id.score_table_layout);
        tableLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        if (tableLayout.getChildCount() >1) {
            View mainRow = tableLayout.getChildAt(0);
            tableLayout.removeAllViews();
            tableLayout.addView(mainRow);
        }
        ArrayList<Player> players= Menu.getInstance().getPlayers();
        for (int i=0;i<players.size();i++){

            TableRow tableRow=new TableRow(this);
            if (i%2==0)
                tableRow.setBackgroundColor(Color.parseColor("#d4e3fc"));
            else
                tableRow.setBackgroundColor(Color.parseColor("#76ABF9"));
            TextView name=initTableTextView();
            name.setText(players.get(i).getName());
            name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            name.setTextSize(16);

            TextView time=initTableTextView();
            time.setText(players.get(i).getTime()+"");
            time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            time.setTextSize(16);

            TextView level=initTableTextView();
            level.setText(players.get(i).getLevel().toString());
            level.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            level.setTextSize(16);

            TextView address=initTableTextView();
            address.setText(players.get(i).getAddress());
            address.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            address.setTextSize(10);

            tableRow.addView(name);
            tableRow.addView(level);
            tableRow.addView(time);
            tableRow.addView(address);

            tableLayout.addView(tableRow);
        }
    }
    private TextView initTableTextView(){
        TextView textView=new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(18);
        return textView;
    }

}
