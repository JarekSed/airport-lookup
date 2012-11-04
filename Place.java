import java.io.Serializable;

/*
 * Simple class to store data about a place so it can be marshalled for RMI calls.
 */

public class Place implements Serializable{
    public Place(String name, String state, double latitude, double longitude){
        this.name = name;
        this.state = state;
        this.latitude = latitude;
        this.longitude=longitude;
    }

    public String getName() { return name; }
    public String getState() { return state; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    private String name;
    private String state;
    private double latitude;
    private double longitude;
}
