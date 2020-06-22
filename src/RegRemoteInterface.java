import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegRemoteInterface extends Remote {
    public ReturnCodes.Codex userRegistration(String nickname, String passwd) throws RemoteException, NullPointerException;
}
