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

  private Airport airport_list = null;
  private Airport result_list = null;

  public Airport find_airports(double lat, double lon) throws RemoteException {
    // A sorted list of 5 closest airports is kept. This deletes any existing
    // list and creates a list of 5 "blank" airports, to start off with.
    result_list = null;
    for (int i = 0; i < 5; i++)
      result_list = new Airport("", "", 0, 0, 999999999, result_list);

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
        result_pointer.next = new Airport(airport_pointer.getCode(), airport_pointer.getName(), airport_pointer.getLatitude(), airport_pointer.getLongitude(), distance, result_pointer.next);

        // Nuke the furthest entry from the results list (to maintain only 5)
        result_list = result_list.next;
      }

      // On to the next airport in the list
      airport_pointer = airport_pointer.next;
    }

    return result_list;
  }

  private void parse_airport_file(String file_path) throws IOException,ParseException
  {
    // Load the file containing airport data
    DataInputStream in = new DataInputStream(new FileInputStream(file_path));
    BufferedReader br = new BufferedReader(new InputStreamReader(in));

    // Read file line by line, skip the first. Populate airport_list
    String line = br.readLine();
    String temp_s;
    int temp_i;
    int line_number = 1;
    double air_lat, air_lon;

    while ((line = br.readLine()) != null) {
      line_number++;

      // Ignore blank lines
      if (line.trim().equals(""))
        continue;

      // Parse the line and retrieve details about the airport
      temp_s = line.substring(6);
      temp_i = temp_s.indexOf(' ');
      air_lat = Double.parseDouble(temp_s.substring(0, temp_i));
      temp_s = temp_s.substring(temp_i).trim();
      temp_i = temp_s.indexOf(' ');
      air_lon = Double.parseDouble(temp_s.substring(0, temp_i));

      // Add the airport to beginning of airport_list
      airport_list = new Airport(line.substring(1, 4), temp_s.substring(temp_i).trim(), air_lat, air_lon, 0, airport_list);

      // @TODO Debug code
      //System.out.println(line_number + " '" + temp_s.substring(temp_i).trim() + "' '" + air_lat + "' '" + air_lon/* + "' Distance:" + distance*/);

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
