package com.lethalminds.udonate.server.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
    public String uname, pass, email, address, dob, category;

    public NodeRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public NodeRequests(String category, Context context) {
        this(context);
        this.category = category;
    }

    public NodeRequests(String uname, String pass, Context context) {
        this(context);
        this.uname = uname;
        this.pass = pass;
    }

    public NodeRequests(String uname, String pass, String email, String address, String dob, Context context) {
        this(uname, pass, context);
        this.email = email;
        this.address = address;
        this.dob = dob;
    }

    public void fetchNewsCollectionAsyncTask(GetCallback deptCallback) {
        progressDialog.show();
        new fetchNewsCollectionAsyncTask(deptCallback).execute();
    }

    public void fetchTransactionsCollectionAsyncTask(GetCallback deptCallback) {
        progressDialog.show();
        new fetchTransactionsCollectionAsyncTask(this.uname, deptCallback).execute();
    }

    public void fetchDonationCollectionByCategoryAsyncTask(GetCallback deptCallback) {
        progressDialog.show();
        new fetchDonationCollectionByCategoryAsyncTask(this.category, deptCallback).execute();
    }

    public void authenticateUserCollectionAsyncTask(GetCallback deptCallback) {
        progressDialog.show();
        new authenticateUserCollectionAsyncTask(this.uname, this.pass, deptCallback).execute();
    }

    public void updateBasicUserCollectionAsyncTask(GetCallback deptCallback) {
        progressDialog.show();
        new updateBasicUserCollectionAsyncTask(this.uname, this.pass, this.email, this.address, this.dob, deptCallback).execute();
    }

    public void addCardToUserCollectionAsyncTask(String username, String cardnumber, String cardname, String cvv, String expiry, GetCallback deptCallback) {
        progressDialog.show();
        new addCardToUserCollectionAsyncTask(username, cardnumber, cardname, cvv, expiry, deptCallback).execute();
    }

    public void obsoleteCardToUserCollectionAsyncTask(String username, String cardid, GetCallback deptCallback) {
        progressDialog.show();
        new obsoleteCardToUserCollectionAsyncTask(username, cardid, deptCallback).execute();
    }

    public void addTransactionCollectionAsyncTask(JSONObject receiver, String donorName, JSONObject paymentInfo, GetCallback deptCallback) {
        progressDialog.show();
        new addTransactionCollectionAsyncTask(receiver, donorName, paymentInfo, deptCallback).execute();
    }

    public void addUserCollectionAsyncTask(JSONObject user, GetCallback deptCallback) {
        progressDialog.show();
        new addUserCollectionAsyncTask(user, deptCallback).execute();
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

    public class fetchTransactionsCollectionAsyncTask extends AsyncTask<Void, Void, ArrayList<JSONObject>> {
        GetCallback callback;
        String uname;
        ArrayList<JSONObject> transList = new ArrayList<JSONObject>();

        public fetchTransactionsCollectionAsyncTask(String uname, GetCallback callback) {
            this.callback = callback;
            this.uname = uname;
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", this.uname);
            String encodedStr = getEncodedData(dataToSend);
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "users/getAllTransactions");
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
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    transList.add(jsonarray.getJSONObject(i));
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
            return transList;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            callback.done(result);
        }
    }

    public class fetchDonationCollectionByCategoryAsyncTask extends AsyncTask<Void, Void, ArrayList<JSONObject>> {
        GetCallback callback;
        String category;
        ArrayList<JSONObject> donationList = new ArrayList<JSONObject>();

        public fetchDonationCollectionByCategoryAsyncTask(String category, GetCallback callback) {
            this.callback = callback;
            this.category = category;
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("category", this.category);
            dataToSend.put("status", "new");
            String encodedStr = getEncodedData(dataToSend);
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "news/getDonations");
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
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    donationList.add(jsonarray.getJSONObject(i));
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
            return donationList;
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
                    returnedUser = new User(username, email, pass, dob, address, result);
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

    public class updateBasicUserCollectionAsyncTask extends AsyncTask<Void, Void, User> {
        GetCallback callback;
        User returnedUser = null;
        String uname, pass, email, address, dob;

        public updateBasicUserCollectionAsyncTask(String username, String password,
                                                  String email, String address, String dob, GetCallback callback) {
            this.callback = callback;
            this.uname = username;
            this.pass = password;
            this.email = email;
            this.address = address;
            this.dob = dob;
        }

        @Override
        protected User doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", this.uname);
            dataToSend.put("password", this.pass);
            dataToSend.put("email", this.email);
            dataToSend.put("address", this.address);
            dataToSend.put("dob", this.dob);
            String encodedStr = getEncodedData(dataToSend);
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "users/findAndModify");
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
                JSONObject jObject = (JSONObject) (new JSONObject(result)).get("value");
                if (jObject.length() != 0) {
                    String username = jObject.getString("username");
                    String email = jObject.getString("email");
                    String address = jObject.getString("address");
                    String dob = jObject.getString("dob");
                    returnedUser = new User(username, email, pass, dob, address, jObject.toString());
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

    public class addCardToUserCollectionAsyncTask extends AsyncTask<Void, Void, User> {
        GetCallback callback;
        User returnedUser = null;
        String uname, cardnumber, cardname, cvv, expiry;

        public addCardToUserCollectionAsyncTask(String username, String cardnumber, String cardname, String cvv, String expiry, GetCallback callback) {
            this.callback = callback;
            this.uname = username;
            this.cardnumber = cardnumber;
            this.cardname = cardname;
            this.cvv = cvv;
            this.expiry = expiry;
        }

        @Override
        protected User doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", this.uname);
            dataToSend.put("cardnumber", this.cardnumber);
            dataToSend.put("cardname", this.cardname);
            dataToSend.put("cvv", this.cvv);
            dataToSend.put("expiry", this.expiry);
            String encodedStr = getEncodedData(dataToSend);
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "users/findUserAddCard");
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
                JSONObject jObject = (JSONObject) (new JSONObject(result)).get("value");
                if (jObject.length() != 0) {
                    String username = jObject.getString("username");
                    String email = jObject.getString("email");
                    String address = jObject.getString("address");
                    String dob = jObject.getString("dob");
                    returnedUser = new User(username, email, pass, dob, address, jObject.toString());
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

    public class obsoleteCardToUserCollectionAsyncTask extends AsyncTask<Void, Void, User> {
        GetCallback callback;
        User returnedUser = null;
        String uname, cardid;

        public obsoleteCardToUserCollectionAsyncTask(String username, String cardid, GetCallback callback) {
            this.callback = callback;
            this.uname = username;
            this.cardid = cardid;
        }

        @Override
        protected User doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", this.uname);
            dataToSend.put("cardid", this.cardid);
            String encodedStr = getEncodedData(dataToSend);
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "users/findAndObsoleteCard");
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
                JSONObject jObject = (JSONObject) (new JSONObject(result)).get("value");
                if (jObject.length() != 0) {
                    String username = jObject.getString("username");
                    String email = jObject.getString("email");
                    String address = jObject.getString("address");
                    String dob = jObject.getString("dob");
                    returnedUser = new User(username, email, pass, dob, address, jObject.toString());
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

    public class addTransactionCollectionAsyncTask extends AsyncTask<Void, Void, User> {
        GetCallback callback;
        User returnedUser = null;
        JSONObject receiver, paymentInfo;
        String donorName;

        public addTransactionCollectionAsyncTask(JSONObject receiver, String donorName, JSONObject paymentInfo, GetCallback callback) {
            this.callback = callback;
            this.receiver = receiver;
            this.donorName = donorName;
            this.paymentInfo = paymentInfo;
        }

        @Override
        protected User doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            try {
                dataToSend.put("username", this.donorName);
                dataToSend.put("receiver", this.receiver.get("receiver_id").toString());
                dataToSend.put("donationId", this.receiver.get("_id").toString());
                dataToSend.put("payment_info", this.paymentInfo.toString());
                String encodedStr = getEncodedData(dataToSend);
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "transaction/addTransaction");
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
                JSONObject jObject = (JSONObject) (new JSONObject(result)).get("value");
                if (jObject.length() != 0) {
                    String username = jObject.getString("username");
                    String email = jObject.getString("email");
                    String address = jObject.getString("address");
                    String dob = jObject.getString("dob");
                    returnedUser = new User(username, email, pass, dob, address, jObject.toString());
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

    public class addUserCollectionAsyncTask extends AsyncTask<Void, Void, String> {
        GetCallback callback;
        String result = null;
        JSONObject user;

        public addUserCollectionAsyncTask(JSONObject user, GetCallback callback) {
            this.callback = callback;
            this.user = user;
        }

        @Override
        protected String doInBackground(Void... params) {
            BufferedReader reader = null;
            Map<String, String> dataToSend = new HashMap<>();
            try {
                dataToSend.put("user", this.user.toString());
                String encodedStr = getEncodedData(dataToSend);
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "users/addUser");
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
                result = sb.toString();
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
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
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
