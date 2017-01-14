package tbject.com.bombswepper.pojo;

public class Player{

    private String name;
    private Level level;
    private Integer time;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.getName()+","+this.getTime()+","+this.getLevel();
    }
}

