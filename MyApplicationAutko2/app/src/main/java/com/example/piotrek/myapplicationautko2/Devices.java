/*! \file Devices.java
*  \brief Java file storing Devices class
*/
/*****************************************************************************
 * Based on template: File_Template.txt                                      *
 *                                                                           *
 * PROJECT ID:   Autko                                                       *
 *                                                                           *
 * FILE DESCRIPTION:                                                         *
 * This file supports devices functionality.                                 *
 * ***************************************************************************
 * AUTHORS:                                                                  *
 * Piotr Kacprowicz 214401                                                   *
 * Patryk Cieślak 214397                                                     *
 * Location: Łódź                                                            *
 *****************************************************************************/

package com.example.piotrek.myapplicationautko2;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Devices extends AppCompatActivity
{
    //widgets
    Button btnPaired;
    ListView devicelist;
    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "@string/Devices_Adress";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        //Calling widgets
        btnPaired = (Button) findViewById(R.id.button);
        devicelist = (ListView) findViewById(R.id.listView);

        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //Show a message when no bluetooth adapter
            Toast.makeText(getApplicationContext(),
                    "@string/Devices_CommandNonAvailableBluetooth", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        } else if (!myBluetooth.isEnabled()) {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });

    }

    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                //Get the device's name and the address
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "@string/Devices_CommandNoPairedDevices",
                    Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        //Method called when the device from the list is clicked
        devicelist.setOnItemClickListener(myListClickListener);

    }


    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(Devices.this, MainActivity.class);

            //Change the activity.
            //this will be received at ledControl (class) Activity
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };
}
