import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Comparator;

/*
 * Search backend for Places. When given a city and state, finds and returns information on it.
 * Keeps an in-memory lookup table of all our known places and their data.
 */
public class Places extends UnicastRemoteObject implements PlacesInterface{


    /* Comparator for the TreeMap of places in each state.
     * Compares places based on their names, but only using the first k chars,
     * where k is the size of the shorted string. This is so we can search for things like 'new york' to find 'new york city'
     */
    private class PlacesCompare implements Comparator<String> {
        public int compare(String first, String second){
            int min_length = Math.min(first.length(), second.length());
            return first.substring(0,min_length).toLowerCase().compareTo(second.substring(0,min_length).toLowerCase());
        }
    }


    public Places() throws RemoteException {
        try{
            // Build our lookup table from the places data file.
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
        // Look up the TreeMap of all places in this state.
        TreeMap<String,Place> places_in_state = lookup_table.get(state.trim().toLowerCase());
        // If we have places in this state, look for the place that matches the name we got.
        // This will return null if we can't find any place that matches the name.
        if (places_in_state != null) {
            return places_in_state.get(city);
        // otherwise, return null.
        } else {
            return null;
        }
    }

    /**
     * Reads census data file, and builds a lookup table containing Place objects. 
     * @throws IOException if file cannot be accessed.
     * @param  file_path path of the file to read.
     * @return Hashmap of state to TreeMaps of name to place objects. So, each state has a TreeMap of all places in that state, and the TreeMaps
     *  are sorted using the name of the place as keys. They are compared using the PlacesCompare Comparator.
     */
    private HashMap<String,TreeMap<String,Place>> parse_places_file(String file_path) throws IOException,ParseException{
        HashMap<String,TreeMap<String,Place>> places = new HashMap<String, TreeMap<String,Place>>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file_path)))); 
        String line;
        while((line = br.readLine()) != null) {
            // Ignore lines that are only whitespace.
            if (line.trim().length() == 0) {
                continue;
            }
            // Pull out data from each field
            String state = line.substring(0,2);
            String name = line.substring(9,73).trim();
            double latitude = Double.parseDouble(line.substring(143,153).trim());
            double longitude = Double.parseDouble(line.substring(153,163).trim());

            Place place = new Place(name,state,latitude, longitude);
            // Initialize TreeMap for this state if it hasn't been initialized yet
            if (! places.containsKey(state.trim().toLowerCase())) {
                places.put(state.trim().toLowerCase(), new TreeMap<String,Place>(new PlacesCompare()));
            }
            // Insert our place object into the TreeMap for this state.
            places.get(state.trim().toLowerCase()).put(name,place);
        }
        return places;
    }
    // Used for quick lookups of places. This is a HashMap of states, where each 2 char state code points
    // to a TreeMap of places, with the keys being the name of the place (compared using the PlacesCompare Comparator).
    // This lets us do lookups in log m time, where m is the number of places in the given state.
    private HashMap<String,TreeMap<String,Place>> lookup_table;
}
