package com.jonasrosendo.httprequisition;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btn_retrieve;
    private TextView txv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_retrieve = findViewById(R.id.btn_retrieve);
        txv_result   = findViewById(R.id.txv_result);

        btn_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cep = "01310100";
                String url_cep = "https://viacep.com.br/ws/"+ cep +"/json/ ";
                MyTask myTask = new MyTask();
                myTask.execute(url_cep);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String string_url = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {

              URL url = new URL(string_url);
              HttpURLConnection connection = (HttpURLConnection) url.openConnection();

              inputStream = connection.getInputStream();
              inputStreamReader = new InputStreamReader(inputStream);

              BufferedReader reader = new BufferedReader(inputStreamReader);
              buffer = new StringBuffer();
              String line = "";

              while ( (line = reader.readLine()) != null){
                  buffer.append(line);
              }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String logradouro = null;
            String cep = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;

            try {
                JSONObject jsonObject = new JSONObject(s);
                logradouro = jsonObject.getString("logradouro");
                cep = jsonObject.getString("cep");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            txv_result.setText(logradouro+ ", " + " " + complemento + " " + bairro + " - " + localidade + ", " + uf + " " + cep );
        }
    }
}
