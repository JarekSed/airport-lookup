import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.io.*;
import java.text.ParseException;
import java.util.TreeMap;
import java.util.Comparator;

/*
 * Search backend for Places. When given a city and state, finds and returns information on it.
 * Keeps an in-memory lookup table of all our known places and their data.
 */
public class PlacesSearch extends UnicastRemoteObject implements PlacesInterface{

    private class PlacesCompare implements Comparator<String> {
        public int compare(String first, String second){
            // Last 3 chars of each are , and the state code
            String first_name = first.substring(0,first.length() -3);
            String first_state = first.substring(first.length() -2);
            String second_name = second.substring(0,second.length() -3);
            String second_state = second.substring(second.length() -2);

            int min_length = Math.min(first_name.length(), second_name.length());
            int res = first_name.substring(0,min_length-1).toLowerCase().compareTo(second_name.substring(0,min_length-1).toLowerCase());
            if (res != 0) {
                return res;
            } else {
                return first_state.toLowerCase().compareTo(second_state.toLowerCase()); 
            }
        }
    }


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
        Place place = lookup_table.get(city.trim() + "," + state.trim());
        return place;
    }

    /**
     * Reads census data file, and builds a lookup table containing Place objects. 
     * @throws IOException if file cannot be accessed.
     * @param  file_path path of the file to read.
     * @return Hashmap of "city,state" strigns to Place objects.
     */
    private TreeMap<String,Place> parse_places_file(String file_path) throws IOException,ParseException{
        TreeMap<String,Place> places = new TreeMap<String,Place>(new PlacesCompare()); 
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
            places.put(name.trim() + "," + state.trim(), place);
            line_number++;
        }
        return places;
    }
    private TreeMap<String,Place> lookup_table;
}
