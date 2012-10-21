import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.io.*;
import java.text.ParseException;
/*
 * Search backend for Places. When given a city and state, finds and returns information on it.
 * Keeps an in-memory lookup table of all our known places and their data.
 */
public class PlacesSearch extends UnicastRemoteObject implements PlacesInterface{

    public PlacesSearch() throws RemoteException {
        try{
            lookup_table = parse_places_file("places2k.txt");
        } catch (IOException e){
            System.err.println(e.getMessage());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    /* Implementation of PlacesInterface.find_place
     */
    public Place find_place(String city, String state) throws RemoteException {
        Place place = lookup_table.get(city.toLowerCase() + "," + state.toLowerCase());
        return place;
    }

    /**
     * Reads census data file, and builds a lookup table containing Place objects. 
     * @throws IOException if file cannot be accessed.
     * @param  file_path path of the file to read.
     * @return Hashmap of "city,state" strigns to Place objects.
     */
    private HashMap<String,Place> parse_places_file(String file_path) throws IOException,ParseException{
        HashMap<String,Place> places = new HashMap<String,Place>(); 
        BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file_path)))); 
        String line;
        // We keep track of what line we are on so we can report errors better.
        int line_number =1;
        while((line = br.readLine()) != null) {
            String state = line.substring(0,2);
            String name = line.substring(9,73).trim();
            double latitude = Double.parseDouble(line.substring(143,153).trim());
            double longitude = Double.parseDouble(line.substring(153,163).trim());
            Place place = new Place(name,state,latitude, longitude);
            places.put(name.toLowerCase() + "," + state.toLowerCase(), place);
            line_number++;
        }
        return places;
    }
    private HashMap<String,Place> lookup_table;
}
