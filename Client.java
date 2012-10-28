import java.rmi.*;

/*
 * Simple example client for the Places server. Queries the server for data on a given city and state.
 */

public class Client {
  public static void main(String args[]) {
    try {
      // Make sure we have the right # of arguments.
      // TODO: Make port optional.
      if (args.length < 3) {
        System.err.println("usage: java Client port city state\n");
        System.exit(1);
      }
      int port = Integer.parseInt(args[0]);
      String url = "//localhost:" + port + "/PlacesSearch";
      System.out.println("looking up " + url);
      PlacesInterface places = (PlacesInterface)Naming.lookup(url);
      Place place = places.find_place(args[1],args[2]);

      if (place != null) {
        System.out.println(place.getName() + ", " + place.getState() + "\t" + place.getLatitude() + ", " + place.getLongitude()); 
      } else {
        System.out.println("Could not find any data for " + args[1] + ","+ args[2]);
      }
    } catch(Exception e) {
      System.err.println("PlacesClient exception: " + e.getMessage());
    }
  }
}
