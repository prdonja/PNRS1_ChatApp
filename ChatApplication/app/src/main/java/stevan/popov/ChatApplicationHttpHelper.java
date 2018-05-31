package stevan.popov;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class ChatApplicationHttpHelper {
    private static final int SUCCESS = 200;

    public boolean postRegisterOnServer(Context context, String urlString, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        
        if(responseCode!=SUCCESS) {
            SharedPreferences.Editor editor = context.getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
            String responseMsg = urlConnection.getResponseMessage();
            String registerError = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("error", registerError);
            editor.apply();


        }

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }
    public boolean postLogedOnServer(Context context, String urlString, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        String sessionId = urlConnection.getHeaderField("sessionId");

        SharedPreferences.Editor editor = context.getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();

        if(responseCode==SUCCESS) {
            editor.putString("sessionId", sessionId);
            editor.apply();
        } else {
            String responseMsg = urlConnection.getResponseMessage();
            String loginError = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("error", loginError);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getContactsFromServer(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        //urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        String responseMsg = urlConnection.getResponseMessage();

        SharedPreferences.Editor editor = context.getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();

        urlConnection.disconnect();

        if (responseCode == SUCCESS){
            return new JSONArray(jsonString);
        } else {
            String error = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("error", error);
            editor.apply();
            return null;
        }

    }
    public boolean postLogOutFromServer(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        int responseCode =  urlConnection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();

        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String error = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("error", error);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public boolean postMessageToServer(Context context, String urlString, JSONObject jsonObject) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();

        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String error = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("error", error);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getMessagesFromServer(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        String responseMsg = urlConnection.getResponseMessage();

        SharedPreferences.Editor editor = context.getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();

        urlConnection.disconnect();

        if (responseCode == SUCCESS){
            return new JSONArray(jsonString);
        } else {
            String getMessagesErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("getMessagesErr", getMessagesErr);
            editor.apply();
            return null;
        }
    }

    public boolean getNotification(Context context) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL("http://18.205.194.168:80/getfromservice");
        urlConnection = (HttpURLConnection) url.openConnection();

        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);


        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();

        Boolean response = Boolean.valueOf(sb.toString());

        urlConnection.disconnect();
        return (response);
    }

    public boolean checkServer() throws IOException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL("http://18.205.194.168:80");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Connection", "close");
        urlConnection.setConnectTimeout(2000 /* milliseconds */ );

        try {
            urlConnection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
