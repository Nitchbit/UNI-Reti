import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegRemoteInterface extends Remote {
    public int userRegistration(String nickname, String passwd) throws IOException, NullPointerException;
}
