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
                return "This user is not your friends";
            case USER_NOT_ONLINE:
                return "The user is not online";
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
            case "EMPTY_NICK_OR_PASS":
                return Codex.EMPTY_NICK_OR_PASS;
            case "ALREADY_REGISTERED":
                return Codex.ALREADY_REGISTERED;
            case "USER_NOT_FOUND":
                return Codex.USER_NOT_FOUND;
            case "WRONG_PASSWORD":
                return Codex.WRONG_PASSWORD;
            case "ALREADY_LOGGED_IN":
                return Codex.ALREADY_LOGGED_IN;
            case "ALREADY_LOGGED_OUT":
                return Codex.ALREADY_LOGGED_OUT;
            case "ALREADY_FRIENDS":
                return Codex.ALREADY_FRIENDS;
            case "NOT_A_FRIEND":
                return Codex.NOT_A_FRIEND;
            case "USER_NOT_ONLINE":
                return Codex.USER_NOT_ONLINE;
            case "SUCCESS":
                return Codex.SUCCESS;
            case "COMMAND_NOT_FOUND":
                return Codex.COMMAND_NOT_FOUND;
            case "Request succeeded":
                return Codex.SUCCESS;
            default:
                return Codex.UNKNOWN_CODE;

        }
    }
}
