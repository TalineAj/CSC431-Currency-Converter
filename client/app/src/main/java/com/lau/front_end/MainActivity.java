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

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                String rate = json.getString("rate");
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

        //The code in line 23-->27 is not my own code, it was retrieved when I searched how to change the status bar color from:
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
    public void LogoConvert(View view) {
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
    public void Convert( View view){
    //if change is true this means it is from usd to lbp

    }

}