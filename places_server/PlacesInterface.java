import java.rmi.*;

public interface PlacesInterface extends Remote {
	public String find_place(String city, String state) throws RemoteException;
}
