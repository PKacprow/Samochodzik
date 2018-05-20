package com.example.piotrek.myapplicationautko2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.ProgressDialog;

public class MainActivity extends AppCompatActivity {

    String addressOfBltDevice = null;
    private ProgressDialog progressOfDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConnectBlt().execute();
    }


    public class ConnectBlt extends AsyncTask<Void, Void, Void>
    {
        /* Connection of Bluetooth in asynch task */

        @Override
        protected void onPreExecute()
        {
            /* Progress dialog */
            System.out.println("Asynch Task1");

            progressOfDialog = ProgressDialog.show(MainActivity.this, "Trwa łączenie...", "Proszę czekać :)");
        }


        @Override
        protected Void doInBackground(Void... arg0)
        {
            /* Proces of connection is done in background */
            System.out.println("Asynch Task2");
            return null;
        }


        @Override
        protected void onPostExecute(Void arg1)
        {
            /* Check did connection is correct */
            System.out.println("Asynch Task3");
        }
    }
}
