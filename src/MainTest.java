import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainTest {
    public static void main(String[] args) {
        Database myDB = new Database();

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String userInput1 = null, userInput2 = null;
        String name1 = null, name2 = null;
        JsonArray myarr = null, myrank = null;
        try {
            userInput1 = input.readLine();
            userInput2 = input.readLine();
            name1 = userInput1;
            myDB.userRegistration(userInput1, userInput2);
            myDB.userLogin(userInput1, userInput2);
            //myDB.dataMap.get(name1).setScore(3);
            userInput1 = input.readLine();
            userInput2 = input.readLine();
            name2 = userInput1;
            myDB.userRegistration(userInput1, userInput2);
            myDB.userAddFriend(name1, name2);
            myarr = myDB.userListFriends(name1);
            myrank = myDB.showRanking(name1);
            System.out.println(myDB.showUserScore(name1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //myDB.printHashMap();
        for(JsonElement item : myarr) System.out.println(item);
        for(JsonElement item : myrank) System.out.println(item);
    }
}
