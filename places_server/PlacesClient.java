import java.rmi.*;

public class PlacesClient{
    public static void main(String args[]) {
        try {
            if (args.length < 2) {
                System.err.println("usage: java PlacesCLient port city state\n");
                System.exit(1);
            }
            int port = Integer.parseInt(args[0]);
            String url = "//localhost:" + port + "/Places";
            System.out.println("looking up " + url);
            PlacesInterface places = (PlacesInterface)Naming.lookup(url);
            for (int i=1; i < args.length; ++i){
                // call the remote method and print the return
            }
        } catch(Exception e) {
            System.out.println("PlacesClient exception: " + e);
        }
    }
}
