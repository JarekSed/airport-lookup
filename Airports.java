import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.text.ParseException;

/*
 * Search backend for Airports.
 */

public class Airports extends UnicastRemoteObject implements AirportsInterface {
    public Airports() throws RemoteException {
        try{
            parse_airport_file("airport-locations.txt");
        } catch (IOException e){
            System.err.println(e.getMessage());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    // Stores a full list of airports, populated from a specified txt file 
    private Airport airport_list = null;

    // A sorted list containing the 5 closest airports to a specified set of coordinates
    private Airport result_list = null;

    /*
    * Populates result_list with the 5 airports closest to the specified coordinates
    */
    public Airport find_airports(double lat, double lon) throws RemoteException {
        // A sorted list of 5 closest airports is kept. This deletes any existing
        // list and creates a list of 5 "blank" airports, to start off with.
        result_list = null;
        for (int i = 0; i < 5; i++)
            result_list = new Airport("", "", "", 0, 0, 999999999, result_list);

        // Run through airport_list and compare entries to the given coordinates
        Airport airport_pointer = airport_list;
        double distance;

        while (airport_pointer != null) {
            // Figure out the distance between the passed city and a particular airport
            // 1.1507794 * 60 * cos-1( sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon2-lon1))
            distance = 1.1507794 * 60 * Math.toDegrees(Math.acos(Math.sin(Math.toRadians(lat)) * Math.sin(Math.toRadians(airport_pointer.getLatitude())) + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(airport_pointer.getLatitude())) * Math.cos(Math.toRadians(airport_pointer.getLongitude() - lon))));

            // Check if this airport is closer than the current furthest in our list
            if (distance < result_list.getDistance()) {
                // Cycle through the list until the correct place to add the new airport
                Airport result_pointer = result_list;
                while (result_pointer.next != null && result_pointer.next.getDistance() > distance){
                    result_pointer = result_pointer.next;}

                // Add the new airport
                result_pointer.next = new Airport(airport_pointer.getCode(), airport_pointer.getName(), airport_pointer.getState(), airport_pointer.getLatitude(), airport_pointer.getLongitude(), distance, result_pointer.next);

                // Nuke the furthest entry from the results list (to maintain only 5)
                result_list = result_list.next;
            }

            // On to the next airport in the list
            airport_pointer = airport_pointer.next;
        }

        // Reverse result_list so the closest airport is first.
        Airport old_head = result_list;
        Airport temp_head = null;
        result_list = null;

        while (old_head != null)
        {
                temp_head = old_head;
                old_head = old_head.next;
                temp_head.next = result_list;
                result_list = temp_head;
        }

        return result_list;
    }

    /*
     * Read the specifed file line by line and populate airport_list will all airports.
     */
    private void parse_airport_file(String file_path) throws IOException,ParseException
    {
        // Load the file containing airport data
        DataInputStream in = new DataInputStream(new FileInputStream(file_path));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // Skip the first line, and keep track of the line numbers to make debugging easier
        String line = br.readLine();
        String temp_string, name, state;
        int temp_index;
        int line_number = 1;
        double air_lat, air_lon;

        while ((line = br.readLine()) != null) {
            line_number++;

            // Ignore blank lines
            if (line.trim().equals(""))
                continue;

            // Parse the line and retrieve the airport's coordinates
            temp_string = line.substring(6);
            temp_index = temp_string.indexOf(' ');
            air_lat = Double.parseDouble(temp_string.substring(0, temp_index));
            temp_string = temp_string.substring(temp_index).trim();
            temp_index = temp_string.indexOf(' ');
            air_lon = Double.parseDouble(temp_string.substring(0, temp_index));

            // Parse the locality's name
            name = temp_string.substring(temp_index).trim();
            temp_index = name.length();
            state = name.substring(temp_index - 2);
            name = name.substring(0, temp_index - 3);

            // Add the airport to beginning of airport_list
            airport_list = new Airport(line.substring(1, 4), name, state, air_lat, air_lon, 0, airport_list);

            // @TODO Debug code
            //System.out.println(line_number + " '" + name + "' '" + state + "' '" + air_lat + "' '" + air_lon);

            // @TODO Debug code
            /*if (line_number > 50)
            {
                break;
            }*/
        }

        // Close the input stream from above
        in.close();
    }
}
