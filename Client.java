import java.rmi.*;

/*
 * Simple example client for the Places server. Queries the server for data on a given city and state.
 */

public class Client {
    private static int NUM_REQUIRED_ARGUMENTS = 2;

    public static void PrintHelpAndExit() {
        System.err.println("usage: java Client [-h rmiregistryserver] [-p port] city state \n");
        System.exit(1);
    }

    public static void main(String args[]) {
        try {
            // By default, port is 1099.
            int port = 1099;
            // Default rmiregistry host is localhost.
            String host = "localhost";
            if (args.length != 2 && args.length != 4 && args.length != 6) {
                PrintHelpAndExit();
            }

            // Walk through arguments and act on any option flags.
            // Anything that is not an option flag ('-') is put into places_args
            // so we can pull out the city and state after this loop. 
            // We do this so the option flags can appear in any order, before or after the city and state.
            String place_arguments[] = new String[NUM_REQUIRED_ARGUMENTS]; 
            int place_arguments_index =0;

            for (int i =0; i < args.length; i++) {
                // If it starts with '-', it is an option flag.
                if (args[i].charAt(0) == '-') {
                    // If this is not a 2 character option flag, it shouldn't have started with '-'.
                    // Also, if this is the last argument we have, it shouldn't be an option flag 
                    // (since it isn't followed by a value).
                    if (args[i].length() != 2 || i == args.length -1) {
                        PrintHelpAndExit();
                    }

                    switch(args[i].charAt(1)) {
                        case 'p':
                            i++;
                            port = Integer.parseInt(args[i]);
                            break;
                        case 'h':
                            i++;
                            host = args[i];
                            break;
                        default:
                            PrintHelpAndExit();
                    } 

                    // If it isn't an option flag, it must be one of of our place arguments.
                }
                
                else {
                    // If we've already seen all our arguments, we have too many.
                    if (place_arguments_index >= NUM_REQUIRED_ARGUMENTS) {
                        PrintHelpAndExit();
                    }
                    place_arguments[place_arguments_index++] = args[i];
                }
            }
            String city = place_arguments[0];
            String state = place_arguments[1];

            // Connect to Places server
            String url = "//" + host + ":" + port + "/Places";
            System.out.println("looking up " + url);
            PlacesInterface places = (PlacesInterface)Naming.lookup(url);

            // Connect to Airport server
            url = "//" + host + ":" + port + "/AirportSearch";
            System.out.println("looking up " + url);
            AirportsInterface airports = (AirportsInterface) Naming.lookup(url);

            // Find place
            Place place = places.find_place(city, state);

            if (place != null) {
                System.out.println(place.getName() + ", " + place.getState() + "\t" + place.getLatitude() + ", " + place.getLongitude()); 

                // Get nearby airports
                Airport list = airports.find_airports(place.getLatitude(), place.getLongitude());

                if (list != null) {
                    do {
                        System.out.println(
                                "code=" + list.getCode() + ", name=" + list.getName() +
                                ", state=" + list.getState() +
                                ", distance=" + list.getDistance()
                                );

                        list = list.next;
                    } while (list != null);
                } else {
                    System.out.println("Could not find nearby airports");
                }
            } else {
                System.out.println("Could not find any data for " + city + ","+ state);
            }
        } catch(Exception e) {
            System.err.println("Client exception");
            e.printStackTrace();
        }
    }
}
