package com.lau.front_end;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView usd;
    ImageView lbp;
    EditText input;
    TextView amount;
    TextView rate_view;
    boolean change = true;
    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            String result = ""; //to store result
            URL url;
            HttpURLConnection http;

            try {
                url = new URL(urls[0]);
                http = (HttpURLConnection) url.openConnection(); //connecting api

                InputStream in = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) { //reading character by character
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }
        protected void onPostExecute(String s) {
      //  handling data

            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                String rate = "1 USD = " + json.getString("rate") + " LBP";
                rate_view.setText(rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //The below is not my own code, it was retrieved when I searched how to change the status bar color from:
        //https://www.geeksforgeeks.org/how-to-change-the-color-of-status-bar-in-an-android-app/
        //N.b: Button color and action bar were also changed and hidden respectively from the themes and values folders (as well as of course layout)
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.teal_200));
        }
        usd = (ImageView) findViewById(R.id.dollar);
        lbp = (ImageView) findViewById(R.id.lbp);
        input = (EditText) findViewById(R.id.input);
        amount=(TextView) findViewById(R.id.amount);
        rate_view=(TextView) findViewById(R.id.rate);

        //get api

         String url = "http://192.168.2.201/CSC431-Currency-Converter/server/apis/rate.inc.php";

         DownloadTask task = new DownloadTask();
         task.execute(url);





    }
    public void logoConvert(View view) {
        //Converts the logo images when the user clicks on the convert logo
        if (change) { //boolean to allow it to switch both ways
            usd.setImageResource(R.drawable.lbp);
            lbp.setImageResource(R.drawable.dollar);
            change = false;
        } else {
            usd.setImageResource(R.drawable.dollar);
            lbp.setImageResource(R.drawable.lbp);
            change = true;
        }
    }
    public void convert( View view){
        //this code was retrieved online after a lot of research and appropriate changes were made to fit into our app
        String post_url = "http://192.168.2.201/CSC431-Currency-Converter/server/apis/convert.inc.php";

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(post_url);
                String currency, amount;
                currency = (change) ? "usd" : "lbp";
                amount = input.getText().toString();
                BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair("currency", currency);
                BasicNameValuePair passwordBasicNameValuePAir = new BasicNameValuePair("amount", amount);
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(usernameBasicNameValuePair);
                nameValuePairList.add(passwordBasicNameValuePAir);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    InputStream inputStream = httpResponse.getEntity().getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String bufferedStrChunk = null;
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }
                    return stringBuilder.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject json = new JSONObject(s);
                    String status = json.getString("status"); //the status was returned as a response from our api
                    if (status.equalsIgnoreCase("200")) {// 200 Ok status means the operation was sucessfull
                        String result = json.getString("result");
                        DecimalFormat decimal_format = new DecimalFormat(".##");
                        result = decimal_format.format(Double.parseDouble(result));
                        result += (change) ? " LBP": " USD";
                        amount.setText(result);// setting the converted
                        String new_rate = "1 USD = " + json.getString("rate") + " LBP";
                        rate_view.setText(new_rate); // setting the new rate
                    } else {// if not then notify the user that something wrong happened
                        amount.setText("Something wrong happened! Try again.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
}

