package com.coin.coinchallenge;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by joe-work on 3/27/15.
 */
class DataDownloadAsyncTask extends AsyncTask<String, String, Void> {

    private static final String TAG = DataDownloadAsyncTask.class.getSimpleName();

    private ProgressDialog progressDialog = new ProgressDialog(MainActivity.getInstance());
    String result = "";
    DataDownloadCallback callback;

    public interface DataDownloadCallback{
        public void onDataCompiled(final List<CreditCard> data);
    }

    public DataDownloadAsyncTask(final DataDownloadCallback callback){
        this.callback = callback;
    }

    protected void onPreExecute() {
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                DataDownloadAsyncTask.this.cancel(true);
            }
        });
    }

    @Override
    protected Void doInBackground(String... params) {

        //the url goes to a downloadable file
        String url_select = "https://s3.amazonaws.com/mobile.coin.vc/ios/assignment/data.json";

        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        try {
            // Create a URL for the desired page
            URL url = new URL(url_select);

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sBuilder = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                // str is one line of text; readLine() strips the newline character(s)
//                Log.d(TAG, str+" ");
                sBuilder.append(str+ "\n");
            }
            result = sBuilder.toString();
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return null;
    }

    protected void onPostExecute(Void v) {
        //parse JSON data
        try {
            List<CreditCard> data = new ArrayList<CreditCard>();
            JSONObject obj = new JSONObject(result);
            JSONArray jArray = obj.getJSONArray("results");
            for(int i=0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                CreditCard singleCard = new CreditCard();

                singleCard.setTimeCreated(jObject.getString("created"));
                singleCard.setTimeUpdated(jObject.getString("updated"));
                singleCard.first_name = jObject.getString("first_name");
                singleCard.last_name = jObject.getString("last_name");
                singleCard.card_number = jObject.getString("card_number");
                singleCard.expiration_date = jObject.getString("expiration_date");
                singleCard.guid = jObject.getString("guid");
                singleCard.background_image_url = new URL(jObject.getString("background_image_url"));
                singleCard.enabled = jObject.getBoolean("enabled");

                data.add(singleCard);

            } // End Loop

            //sort the data by Newest first
            Collections.sort(data);

            //return all that and then re-populate the list
            callback.onDataCompiled(data);

            this.progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            this.progressDialog.dismiss();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.progressDialog.dismiss();
        }
    }
}
