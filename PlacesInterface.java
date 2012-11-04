import java.rmi.*;
/*
 * Interface definition for the Places server.
 */
public interface PlacesInterface extends Remote {
    /**
     * Gets data for a given place
     * @throws RemoteException if problems communicating with remote server.
     * @param  city the city to search for
     * @param  state the state to search for
     * @return Place information for the queried place, or null if no data was found.
     */
    public Place find_place(String city, String state) throws RemoteException;
}
