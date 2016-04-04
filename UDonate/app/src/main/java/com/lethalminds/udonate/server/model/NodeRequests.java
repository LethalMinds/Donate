package com.lethalminds.udonate.server.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.server.model.callbacks.GetCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nishok on 4/2/2016.
 */
public class NodeRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://10.0.2.2:9090/";
    String uname, pass;

    public NodeRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public NodeRequests(String uname, String pass, Context context){
        this(context);
        this.uname = uname;
        this.pass = pass;
    }

    public void fetchNewsCollectionAsyncTask(GetCallback deptCallback) {
        progressDialog.show();
        new fetchNewsCollectionAsyncTask(deptCallback).execute();
    }

    public void authenticateUserCollectionAsyncTask(GetCallback deptCallback) {
        progressDialog.show();
        new authenticateUserCollectionAsyncTask(this.uname, this.pass, deptCallback).execute();
    }

    public class fetchNewsCollectionAsyncTask extends AsyncTask<Void, Void, ArrayList<JSONObject>> {
        GetCallback callback;
        ArrayList<JSONObject> deptCourseList = new ArrayList<JSONObject>();

        public fetchNewsCollectionAsyncTask(GetCallback callback) {
            this.callback = callback;
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            BufferedReader reader = null;
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "news/getNews");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //Post Method
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    deptCourseList.add(jsonarray.getJSONObject(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return deptCourseList;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            callback.done(result);
        }
    }

    public class authenticateUserCollectionAsyncTask extends AsyncTask<Void, Void, User> {
        GetCallback callback;
        User returnedUser = null;
        String uname, pass;

        public authenticateUserCollectionAsyncTask(String username, String password, GetCallback callback) {
            this.callback = callback;
            this.uname = username;
            this.pass = password;
        }

        @Override
        protected User doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", this.uname);
            dataToSend.put("password", this.pass);
            String encodedStr = getEncodedData(dataToSend);
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "users/authenticate");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //Post Method
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(encodedStr);
                writer.flush();
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0) {
                    String username = jObject.getString("username");
                    String email = jObject.getString("email");
                    String address = jObject.getString("address");
                    String dob = jObject.getString("dob");
                    returnedUser = new User(username, email, pass, dob, address, jObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            callback.done(result);
        }
    }

    public String getEncodedData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (sb.length() > 0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }
}
