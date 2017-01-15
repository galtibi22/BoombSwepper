package tbject.com.bombswepper.pojo;

import tbject.com.bombswepper.activity.Menu;

public class Board {

    private Box [][] boxes;
    private int columns;
    private int rows;
    private int sizeOfBox;
    private int bombs;

    public Board(Level level){
        columns=level.getNumOfBox();
        rows=level.getNumOfBox();
        bombs=level.getNumOfBomb();
        sizeOfBox= Menu.getInstance().getScreenSize().x/level.getNumOfBox();
        boxes=new Box[rows][columns];
    }
    /**
     * addBox method - response to add box to board by the Position of the box
     * @param box
     */
    public void addBox(Box box){
        boxes[box.getPosition().getRow()][box.getPosition().getColumn()]=box;
    }

    public Box getBox(Position position){
        return boxes[position.getRow()][position.getColumn()];
    }

    public Box getBox(int row,int col){
        return boxes[row][col];
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSizeOfBox() {
        return sizeOfBox;
    }

    public void setSizeOfBox(int sizeOfBox) {
        this.sizeOfBox = sizeOfBox;
    }

    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

}
