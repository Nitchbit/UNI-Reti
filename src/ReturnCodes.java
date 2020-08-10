import javax.crypto.Cipher;

public class ReturnCodes {
    public enum Codex {
        EMPTY_NICK_OR_PASS,
        ALREADY_REGISTERED,
        USER_NOT_FOUND,
        WRONG_PASSWORD,
        ALREADY_LOGGED_IN,
        ALREADY_LOGGED_OUT,
        ALREADY_FRIENDS,
        NOT_A_FRIEND,
        USER_NOT_ONLINE,
        SUCCESS,
        COMMAND_NOT_FOUND,
        UNKNOWN_CODE
    }
    public static String toMessage(ReturnCodes.Codex message) {
        switch (message) {
            case EMPTY_NICK_OR_PASS:
                return "Empty username or empty password";
            case ALREADY_REGISTERED:
                return "Username not available";
            case USER_NOT_FOUND:
                return "User not found";
            case WRONG_PASSWORD:
                return "Incorrect password";
            case ALREADY_LOGGED_IN:
                return "This user is already logged in";
            case ALREADY_LOGGED_OUT:
                return "This user is already logged out";
            case ALREADY_FRIENDS:
                return "This user is already your friends";
            case NOT_A_FRIEND:
                return "This user is not your friend";
            case USER_NOT_ONLINE:
                return "This user is not online";
            case SUCCESS:
                return "Request succeeded";
            case COMMAND_NOT_FOUND:
                return "Request failed";
            default:
                return "Unknown code";
        }
    }
    public static ReturnCodes.Codex toCodex(String message) {
        switch (message) {
            case "Empty username or empty password":
                return Codex.EMPTY_NICK_OR_PASS;
            case "Username not available":
                return Codex.ALREADY_REGISTERED;
            case "User not found":
                return Codex.USER_NOT_FOUND;
            case "Incorrect password":
                return Codex.WRONG_PASSWORD;
            case "This user is already logged in":
                return Codex.ALREADY_LOGGED_IN;
            case "This user is already logged out":
                return Codex.ALREADY_LOGGED_OUT;
            case "This user is already your friends":
                return Codex.ALREADY_FRIENDS;
            case "This user is not your friend":
                return Codex.NOT_A_FRIEND;
            case "This user is not online":
                return Codex.USER_NOT_ONLINE;
            case "Request succeeded":
                return Codex.SUCCESS;
            case "Request failed":
                return Codex.COMMAND_NOT_FOUND;
            default:
                return Codex.UNKNOWN_CODE;

        }
    }
}
