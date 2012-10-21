import java.rmi.*;

public interface PlacesInterface extends Remote {
	public Place find_place(String city, String state) throws RemoteException;
}
