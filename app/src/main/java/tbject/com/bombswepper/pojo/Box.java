package tbject.com.bombswepper.pojo;

import android.widget.Button;

public class Box {
    private int num;
    private boolean flag;
    private Button boxButton;
    private boolean open;
    private Position position;

    public Box(){

    }

    public Box(Position position){
        setPosition(position);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    public Button getBoxButton() {
        return boxButton;
    }

    public void setBoxButton(Button boxButton) {
        this.boxButton = boxButton;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
