package com.lau.front_end;

import androidx.appcompat.app.AppCompatActivity;

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

    // Put your ipv4 address here
    String ipv4 = "192.168.1.4";
    String get_url = "http://" + ipv4 + "/CSC431-Currency-Converter/server/apis/rate.inc.php";
    String post_url = "http://" + ipv4 + "/CSC431-Currency-Converter/server/apis/convert.inc.php";
    ImageView from;
    ImageView to;
    EditText input_view;
    TextView result_view;
    TextView rate_view;
    boolean is_usd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // The below is not my own code, it was retrieved when I searched how to change the status bar color from:
        // https://www.geeksforgeeks.org/how-to-change-the-color-of-status-bar-in-an-android-app/
        // N.b: Button color and action bar were also changed and hidden respectively from the themes and values folders (as well as of course layout)
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.teal_200));
        }
        from = (ImageView) findViewById(R.id.usd);
        to = (ImageView) findViewById(R.id.lbp);
        input_view = (EditText) findViewById(R.id.input);
        result_view = (TextView) findViewById(R.id.result);
        rate_view = (TextView) findViewById(R.id.rate);

        // Calls the get api to get the rate
        DownloadTask task = new DownloadTask();
        task.execute(get_url);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            // To send the result if any
            String result = "";
            try {

                URL url = new URL(urls[0]);
                // Opening the connection
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                InputStream in = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                // Reading the response character by character
                while (data != -1) {
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
            // Reads the rate from the json object and displays it on the screen
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

    public void logoConvert(View view) {
        // Reverses the usd and lbp images when the user clicks on the convert icon
        // is_usd is a boolean to know from what currency to what currency the user
        // wants to convert
        if (is_usd) {
            from.setImageResource(R.drawable.lbp);
            to.setImageResource(R.drawable.dollar);
        } else {
            from.setImageResource(R.drawable.dollar);
            to.setImageResource(R.drawable.lbp);
        }
        is_usd = !is_usd;
    }

    public void convert(View view) {
        // The code below is based on the HttpClient documentation
        // Our code sets up the connection and then sends a post request with the specified parameter which are the amount to convert
        // as well as the currency provided by the user, then it retrieves a response with the status, result and rate and each of these
        // are displayed or manipulated appropriately.

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                HttpClient http_client = new DefaultHttpClient();
                HttpPost http_post = new HttpPost(post_url);

                String currency, input;
                currency = (is_usd) ? "usd" : "lbp";
                input = input_view.getText().toString();

                BasicNameValuePair currency_param = new BasicNameValuePair("currency", currency);
                BasicNameValuePair input_param = new BasicNameValuePair("amount", input);

                List<NameValuePair> name_value_pair_list = new ArrayList<NameValuePair>();
                name_value_pair_list.add(currency_param);
                name_value_pair_list.add(input_param);

                try {
                    // This is used to send the list with the api in an encoded form entity
                    UrlEncodedFormEntity url_encoded_form_entity = new UrlEncodedFormEntity(name_value_pair_list);

                    // This sets the entity (which holds the list of values) in the http_post object
                    http_post.setEntity(url_encoded_form_entity);

                    // This gets the response from the post api and returns a string of the response.
                    HttpResponse http_response = http_client.execute(http_post);
                    InputStream input_stream = http_response.getEntity().getContent();
                    InputStreamReader input_stream_reader = new InputStreamReader(input_stream);
                    BufferedReader buffered_reader = new BufferedReader(input_stream_reader);
                    StringBuilder string_builder = new StringBuilder();
                    String buffered_str_chunk = null;
                    while ((buffered_str_chunk = buffered_reader.readLine()) != null) {
                        string_builder.append(buffered_str_chunk);
                    }
                    return string_builder.toString();
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
                    // Getting the status that was returned in the json response from the post api
                    String status = json.getString("status");

                    // 200: OK status means the operation was successful
                    if (status.equalsIgnoreCase("200")) {

                        // Getting the resulted amount of the conversion
                        String result = json.getString("result");
                        DecimalFormat decimal_format = new DecimalFormat(".###");
                        result = decimal_format.format(Double.parseDouble(result));
                        result += (is_usd) ? " LBP" : " USD";

                        // Displaying the result to the screen
                        result_view.setText(result);

                        // Displaying the new rate to the screen
                        String new_rate = "1 USD = " + json.getString("rate") + " LBP";
                        rate_view.setText(new_rate);
                    } else {
                        // If the status was 404, the user is notified that something wrong happened
                        String err_msg = "Something wrong happened! Try again!";
                        result_view.setText(err_msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        SendPostReqAsyncTask send_post_req_async_task = new SendPostReqAsyncTask();
        send_post_req_async_task.execute();
    }
}

