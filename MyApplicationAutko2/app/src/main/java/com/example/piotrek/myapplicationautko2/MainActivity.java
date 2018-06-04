package com.example.piotrek.myapplicationautko2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    String addressOfBltDevice = null;
    private ProgressDialog progressOfDialog;
    BluetoothAdapter myBltDevice = null;
    BluetoothSocket bltSocket = null;
    private boolean isBltConnected = false;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //ToDO Move to strings.xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // new ConnectBlt().execute();       //ToDo Uncomment
    }


    public class ConnectBlt extends AsyncTask<Void, Void, Void>
    {
        /* Connection of Bluetooth in asynch task */
        private boolean isSuccessConnection = true;

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

            try
            {
                if (bltSocket == null || !isBltConnected)
                {
                    myBltDevice = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBltDevice.getRemoteDevice(addressOfBltDevice);  // Connect to device's adress
                    bltSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);  //Create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bltSocket.connect();
                }
            } catch (IOException exception)
            {
                isSuccessConnection = false;    // failed if device is not available
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            /* Check did connection is correct */
            System.out.println("Asynch Task3");

            super.onPostExecute(result);

            if (!isSuccessConnection)
            {
                /* Problem with connection */
                finish();
            } else {
                /* Connect */
                isBltConnected = true;
            }
            progressOfDialog.dismiss();
        }
    }
}