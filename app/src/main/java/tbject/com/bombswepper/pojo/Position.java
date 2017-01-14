package tbject.com.bombswepper.pojo;

public class Position {
    private int row;
    private int column;

    public Position(){

    }

    public Position(int row,int column){
        setColumn(column);
        setRow(row);
    }
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
