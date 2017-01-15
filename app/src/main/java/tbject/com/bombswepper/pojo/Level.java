package tbject.com.bombswepper.pojo;


public enum Level {
    EASY(1,10),
    MEDIUM(10,10),
    HARD(10,5);

    int numOfBomb;
    int numOfBox;

    Level(int numOfBomb,int numOfBox){
       setNumOfBomb(numOfBomb);
        setNumOfBox(numOfBox);
    }

    public int getNumOfBomb() {
        return numOfBomb;
    }

    public void setNumOfBomb(int numOfBomb) {
        this.numOfBomb = numOfBomb;
    }

    public int getNumOfBox() {
        return numOfBox;
    }

    public void setNumOfBox(int numOfBox) {
        this.numOfBox = numOfBox;
    }
}
