import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.text.ParseException;


public class Air
{
	public static void main(String[] args)
	{
		try
		{
			find_airports(38.437613, -122.866375);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}

		System.out.println("\n");

		while (result_list != null)
		{
			System.out.println(result_list.getCode() + ' ' + result_list.getName() + ' ' + result_list.getDistance());
			result_list = result_list.next;
		}
	}

	static Airport result_list = null;

    public static void find_airports(double lat, double lon) throws Exception {
		// To keep this efficient, only a sorted list of 5 airports is kept.
		// This creates a list of 5 "blank" airports to start off.
		for (int i = 0; i < 5; i++)
		{
			result_list = new Airport("", "", 999999999, result_list);
		}

		// Load the file containing airport data
		String file_path = "airport-locations.txt";
		DataInputStream in = new DataInputStream(new FileInputStream(file_path));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		// Read file line by line, skip the first
		String line = br.readLine();
		String temp_s;
		int temp_i;
		int line_number = 1;
		double air_lat, air_lon, distance;

		while ((line = br.readLine()) != null) {
			line_number++;

			// Ignore blank lines
			if (line.trim().equals(""))
				continue;

			// Parse the line and retrieve latitude (air_lat) and longitude (air_lon)
			temp_s = line.substring(6);
			temp_i = temp_s.indexOf(' ');
			air_lat = Double.parseDouble(temp_s.substring(0, temp_i));
			temp_s = temp_s.substring(temp_i).trim();
			temp_i = temp_s.indexOf(' ');
			air_lon = Double.parseDouble(temp_s.substring(0, temp_i));

			// Figure out the distance from the passed city
			// 1.1507794 * 60 * cos-1( sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon2-lon1))
			distance = 1.1507794 * 60 * Math.toDegrees(Math.acos(Math.sin(Math.toRadians(lat)) * Math.sin(Math.toRadians(air_lat)) + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(air_lat)) * Math.cos(Math.toRadians(air_lon - lon))));

			// Check if this airport is closer than the current furthest in our list
			if (distance < result_list.getDistance()) {
				// Cycle through the list until the correct place to add the new airport
				Airport pointer = result_list;
				while (pointer.next != null && pointer.next.getDistance() > distance){
					pointer = pointer.next;}

				// Add the new airport
				pointer.next = new Airport(line.substring(1, 4), line.substring(temp_i).trim(), distance, pointer.next);

				// Nuke the furthest entry from the list
				result_list = result_list.next;
			}

			// @TODO Debug code
			System.out.println(line_number + " '" + air_lat + "' '" + air_lon + "' Distance:" + distance);

			// @TODO Debug code
			/*if (line_number > 8)
			{
				break;
			}*/
			

		}

		// Close the input stream from above
		in.close();
  }
}