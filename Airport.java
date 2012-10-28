import java.io.Serializable;

/*
 * Simple class to waste my time and make a really simple remote method call
 * super, super complicated by introducing a new aggregate data type.
 */

public class Airport implements Serializable {

  public Airport (String code, String name, double distance) {
    this.code = code;
    this.name = name;
    this.distance = distance;
  }

  public String getName() { return name; }
  public String getCode() { return code; }
  public double getDistance() { return distance; }
  
  private String code;
  private String name;
  private double distance;

}
