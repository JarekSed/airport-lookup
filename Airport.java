import java.io.Serializable;

/*
 * Simple class to store data about individual airports.
 */
public class Airport implements Serializable {
  public Airport (String code, String name, double latitude, double longitude, double distance, Airport next) {
    this.code = code;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.distance = distance;
    this.next = next;
  }

  public String getCode() { return code; }
  public String getName() { return name; }
  public double getLatitude() { return latitude; }
  public double getLongitude() { return longitude; }
  public double getDistance() { return distance; }

  private String code;
  private String name;
  private double latitude;
  private double longitude;
  private double distance;

  public Airport next;
}
