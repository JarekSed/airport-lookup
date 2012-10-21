import java.rmi.*;

public class PlacesClient{
    public static void main(String args[]) {
        try {
            if (args.length < 3) {
                System.err.println("usage: java PlacesSearchClient port city state\n");
                System.exit(1);
            }
            int port = Integer.parseInt(args[0]);
            String url = "//localhost:" + port + "/PlacesSearch";
            System.out.println("looking up " + url);
            PlacesInterface places = (PlacesInterface)Naming.lookup(url);
            // call the remote method and print the return
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
