import java.rmi.*;
import java.rmi.server.*;

// 

public class Places
  extends UnicastRemoteObject
  implements PlacesInterface{

	public Places() throws RemoteException {
	}

	public String find_place(String city, String state) throws RemoteException {
        return "";
	}
}
