package com.example.jbt.weatherjars;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView KindTv;
    EditText CityET;
    Button GoBtn;
    ImageView WheaterIV;
    String wheaterWeb1="http://api.openweathermap.org/data/2.5/weather?q=";
            String wheaterWeb2="&appid=5a50565e4e371c28faa56d856e4e7e98&units=metric";
    String icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KindTv= (TextView) findViewById(R.id.KindTV);
        CityET= (EditText) findViewById(R.id.CityEt);
        WheaterIV= (ImageView) findViewById(R.id.WeatherIV);
        GoBtn= (Button) findViewById(R.id.GoBtn);
        GoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String city=CityET.getText().toString();
                Downloader downloader=new Downloader();
                downloader.execute(wheaterWeb1+city+wheaterWeb2);
                Log.d("fdzfd","fsfsd");

            }
        });
    }

    public class Downloader extends AsyncTask<String ,Void, String>
    {
        @Override
        protected String doInBackground(String... params) {

            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line="";
                while ((line=input.readLine())!=null){
                    response.append(line+"\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (input!=null){
                    try {
                        //must close the reader
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(connection!=null){
                    //must disconnect the connection
                    connection.disconnect();
                }
            }
            return response.toString();

        }


        @Override
        protected void onPostExecute(String jsonText) {


            Gson gson = new Gson();

            JSonresult jSonresult= gson.fromJson(jsonText, JSonresult.class);
            icon=jSonresult.weather.get(0).icon.toString();
            KindTv.setText(jSonresult.weather.get(0).description);

            Log.d("string", jsonText);
            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(WheaterIV);



        }
    }
}
