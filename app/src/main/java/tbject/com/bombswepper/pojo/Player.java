package tbject.com.bombswepper.pojo;

import com.google.android.gms.maps.model.LatLng;

public class Player{

    private String name;
    private Level level;
    private Integer time;
    private LatLng location;
    private String address;

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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }


    public String toString(){
        return this.getName()+","+this.getTime()+","+this.getLevel()+","+this.address+ ","+ this.getLocation().latitude +" ," +this.getLocation().longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}



