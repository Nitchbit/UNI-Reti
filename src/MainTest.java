import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;

public class MainTest {
    public static void main(String[] args) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        try {
            String word = rd.readLine();
            while (!word.equals("EXIT")) {
                URL newUrl = new URL("https://api.mymemory.translated.net/get?q=" + word + "&langpair=it|en");
                HttpsURLConnection connection = (HttpsURLConnection) newUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                if (connection.getResponseCode() != 200) {
                    throw new RuntimeException("Request failed: " + connection.getResponseCode());
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                JSONParser parser = new JSONParser();
                JSONObject o = (JSONObject) parser.parse(line);
                JSONObject obj = (JSONObject) o.get("responseData");
                System.out.println(obj.get("translatedText").toString().toLowerCase());
                connection.disconnect();
                word = rd.readLine();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        /*

        String userInput1 = null, userInput2 = null;
        String name1 = null, name2 = null;
        JsonArray myarr = null, myrank = null;
        try {
            userInput1 = input.readLine();
            userInput2 = input.readLine();
            name1 = userInput1;
            myDB.userRegistration(userInput1, userInput2);
            //myDB.userLogin(userInput1, userInput2);
            //myDB.dataMap.get(name1).setScore(3);
            userInput1 = input.readLine();
            userInput2 = input.readLine();
            name2 = userInput1;
            myDB.userRegistration(userInput1, userInput2);
            myDB.userAddFriend(name1, name2);
            //myarr = myDB.userListFriends(name1);
            //myrank = myDB.showRanking(name1);
            //System.out.println(myDB.showUserScore(name1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //myDB.printHashMap();
        //for(JsonElement item : myarr) System.out.println(item);
        //for(JsonElement item : myrank) System.out.println(item);

         */
    }
}
