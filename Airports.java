import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.io.*;
import java.text.ParseException;
import java.util.TreeMap;
import java.util.Comparator;

/*
 * Search backend for Airports.
 */

public class Airports extends UnicastRemoteObject implements AirportsInterface {
  public Airports() throws RemoteException {}

	public Airport[] find_airports(double lat, double lon) throws RemoteException {
    return null;
  }
}
