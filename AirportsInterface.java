import java.rmi.*;
/*
 * Interface definition for the Airports server.
 */
public interface AirportsInterface extends Remote {
    /**
     * Gets airports nearest to a location
     * @throws RemoteException if problems communicating with remote server.
     * @param  lat latitude
     * @param  lon longitude
     * @return 5 Airports nearest to location
     */
	public Airport[] find_airports(double lat, double lon) throws RemoteException;
}
