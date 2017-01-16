package tbject.com.bombswepper;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import tbject.com.bombswepper.activity.GameInstance;
import tbject.com.bombswepper.pojo.Board;
import tbject.com.bombswepper.pojo.Box;
import tbject.com.bombswepper.pojo.Level;
import tbject.com.bombswepper.pojo.Position;


public class BoardManager {
   
    private GameInstance gameInstance = GameInstance.getInstance();
    private Board board;
    private ArrayList<Position> boxQueue;
    private int numOfOpenBox;
    private int numOfFlag;
    private ArrayList<Box> boxAddBombs;
    private ArrayList<Box> boxClosed;
    public BoardManager(){
    }

    public void initBoard(Level level) {
        boxQueue=new ArrayList<>();
        boxClosed=new ArrayList<>();
        board=new Board(level);
        numOfFlag=board.getBombs();
        boxAddBombs=new ArrayList<>();
        initBoxes();
        placeBombs();
        calculateHints();
        initButtons();
    }

    private void initBoxes(){
        for (int y = 0; y < board.getRows(); y++)
            for (int x = 0; x < board.getColumns(); x++) {
                Box box=new Box(new Position(y,x));
                board.addBox(box);
            }
    }

    private void placeBombs() {
        int bombPlaced = 0;
        Random random = new Random(); // this generates random numbers for us
        while(bombPlaced < board.getBombs()) {
            int x = random.nextInt(board.getColumns()); // a number between 0 and columns - 1
            int y = random.nextInt(board.getRows());
            // make sure we don't place a boomb on top of another
            Box box=board.getBox(y,x);
            if(box.getNum() != -1) {
                box.setNum(-1);
                bombPlaced ++;
            }
        }
    }

    private void calculateHints() {
        for(int y = 0; y < board.getRows(); y ++) {
            for(int x = 0; x < board.getColumns(); x ++) {
                Box box=board.getBox(y,x);
                if(box.getNum() != -1) {
                    box.setNum(minesNear(y, x));
                }
            }
        }
    }

    private int minesNear(int y, int x) {
        int mines = 0;
        // check mines in all directions
        mines += mineAt(y - 1, x - 1);  // NW
        mines += mineAt(y - 1, x);      // N
        mines += mineAt(y - 1, x + 1);  // NE
        mines += mineAt(y, x - 1);      // W
        mines += mineAt(y, x + 1);      // E
        mines += mineAt(y + 1, x - 1);  // SW
        mines += mineAt(y + 1, x);      // S
        mines += mineAt(y + 1, x + 1);  // SE
        if(mines > 0) {
            return mines;
        } else {
            return 0;
        }
    }

    // returns 1 if there's a mine a y,x or 0 if there isn't
    private int mineAt(int y, int x) {
        if(y >= 0 && y < board.getRows() && x >= 0 && x < board.getColumns() && board.getBox(y,x).getNum() == -1) {
            return 1;
        } else {
            return 0;
        }
    }

    private void initButtons(){
        for(int y=0;y<board.getRows();y++)
            for (int x=0;x<board.getColumns();x++)
                initButton(board.getBox(y,x));
    }

    private void initButton(Box box){
        Button button = new Button(gameInstance);
        button.setBackground(gameInstance.getResources().getDrawable(R.drawable.box));
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleBoxButtonShortClick(view);
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   return handleBoxButtonLongClick(v);
                }
            });
        button.setTag(box.getPosition().getRow() + "@" + box.getPosition().getColumn());
        box.setBoxButton(button);
        }


    public void handleBoxButtonShortClick(View view) {
        Position pos = getPostionFromTag(view.getTag().toString());
        if (getBoard().getBox(pos).getNum() == -1)
            gameInstance.lost();
        else
            openBoxes(pos);
    }

    public boolean handleBoxButtonLongClick(View view){
        Button button = (Button) view;
        Position pos = getPostionFromTag(view.getTag().toString());
        Box box = getBoard().getBox(pos);
        if (!box.isOpen())
            if (!box.isFlag() && numOfFlag > 0) {
                box.setFlag(true);
                button.setBackgroundResource(R.drawable.smallflag);
                numOfFlag--;
            } else if (box.isFlag()) {
                box.setFlag(false);
                button.setBackgroundResource(R.drawable.box);
                numOfFlag++;
        }
        gameInstance.getTotalFlagValue().setText(numOfFlag + "");
        return true;
    }

    public void openBoxes(Position pos) {
        Box box = getBoard().getBox(pos);
        if (!box.isOpen() && !box.isFlag()) {
            if (box.getNum() == 0) {
                Position position;
                //up
                if (pos.getRow() > 0) {
                    position=new Position(pos.getRow() - 1, pos.getColumn());
                    if (getBoard().getBox(position).getNum()!=-1)
                        boxQueue.add(position);

                }
                //down
                if (pos.getRow() < getBoard().getRows() - 1) {
                    position = new Position(pos.getRow() + 1, pos.getColumn());
                    if (getBoard().getBox(position).getNum() != -1)
                        boxQueue.add(position);
                }
                //left
                if (pos.getColumn() > 0){
                    position = new Position(pos.getRow(), pos.getColumn() - 1);
                    if (getBoard().getBox(position).getNum() != -1)
                        boxQueue.add(position);
                }
                //right
                if (pos.getColumn() < getBoard().getColumns() - 1){
                    position = new Position(pos.getRow(), pos.getColumn() + 1);
                    if (getBoard().getBox(position).getNum() != -1)
                        boxQueue.add(position);
                }
            }
            openBox(box);
        }
        if (!boxQueue.isEmpty()) {
            Position position = boxQueue.get(0);
            boxQueue.remove(0);
            openBoxes(position);
        }
    }

    public void openBox(Box box) {
        if (box.getNum() >= 0 && !box.isFlag()) {
            box.setOpen(true);
            numOfOpenBox++;
            box.getBoxButton().setBackground(gameInstance.getResources().getDrawable(R.drawable.boxclick));
            if (box.getNum() > 0)
                box.getBoxButton().setText(box.getNum() + "");
        }
        if (box.getNum() >= 0 && gameInstance.isLost() && box.isFlag())
            box.getBoxButton().setBackgroundResource(R.drawable.flagnegetive);
        if (box.getNum() == -1) {
            box.setOpen(true);
            if (!box.isFlag())
                box.getBoxButton().setBackgroundResource(R.drawable.bomb);
        }
        int num = getBoard().getColumns() *  getBoard().getRows();
        if (!gameInstance.isLost() && numOfOpenBox == num -  getBoard().getBombs())
            gameInstance.win();
    }
    private void closeBox(Box box){
        box.getBoxButton().setBackground(gameInstance.getResources().getDrawable(R.drawable.box));
        box.setOpen(false);
        box.getBoxButton().setText("");
        numOfOpenBox--;
    }

    public void addBombAndCloseBox(){
        boolean successToAddBomb=false;
        boolean successToCloseBox=false;
        Random random = new Random(); // this generates random numbers for us
        while(!successToAddBomb) {
            int x = random.nextInt(board.getColumns());
            int y = random.nextInt(board.getRows());
            Box box = board.getBox(y, x);
            if (!box.isOpen() && box.getNum() != -1) {
                box.setOldNum(box.getNum());
                box.setNum(-1);
                boxAddBombs.add(box);
                successToAddBomb = true;
                GameInstance.getInstance().getNewBombValue().setText(boxAddBombs.size()+"");
                GameInstance.getInstance().showAToast( "Please return to original position.");
                getBoard().setBombs(getBoard().getBombs()+1);
            }
        }
        if (numOfOpenBox>0)
            while (!successToCloseBox){
                int x = random.nextInt(board.getColumns());
                int y = random.nextInt(board.getRows());
                Box box = board.getBox(y, x);
                if (box.isOpen()){
                    closeBox(box);
                    boxClosed.add(box);
                    successToCloseBox=true;
                }
        }
    }

    public void removeBombAndOpenBox(){
        if (boxAddBombs.size()>0) {
            Box box=boxAddBombs.get(boxAddBombs.size()-1);
            box.setNum(box.getOldNum());
            boxAddBombs.remove(boxAddBombs.size()-1);
            GameInstance.getInstance().getNewBombValue().setText(boxAddBombs.size()+"");
            GameInstance.getInstance().showAToast("Good you start return to original position.");
            getBoard().setBombs(getBoard().getBombs()-1);
        }
        if (boxClosed.size()>0){
            Box box=boxClosed.get(boxClosed.size()-1);
            openBox(box);
            boxClosed.remove(boxClosed.size()-1);
        }
    }

    private Position getPostionFromTag(String tag){
        Position pos = new Position();
        pos.setRow(Integer.parseInt(tag.split("@")[0]));
        pos.setColumn(Integer.parseInt(tag.split("@")[1]));
        return  pos;
    }



   public Board getBoard(){
       return board;
   }

    public int getNumOfFlag() {
        return numOfFlag;
    }

}


