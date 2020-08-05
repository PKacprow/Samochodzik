/*! \file Mainactivity.java
*  \brief Java file storing MainActivity and ConnectBlt classes
*/
/*****************************************************************************
 * Based on template: File_Template.txt                                      *
 *                                                                           *
 * PROJECT ID:   Autko                                                       *
 *                                                                           *
 * FILE DESCRIPTION:                                                         *
 * This file supports main functionality.                                    *
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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements Runnable{

    protected Handler handler;
    public void run(){}
    String addressOfBltDevice = null;
    private ProgressDialog progressOfDialog;
    BluetoothAdapter myBltDevice = null;
    BluetoothSocket bltSocket = null;
    private boolean isBltConnected = false;
    TextView textView1, textView2, textView3, textView4, textView5;
    Button frontLamp, rearLamp, startjoy;
    RelativeLayout layout_joystick;
    JoyStickClass js;
    int accelerattion = 0;
    static final UUID myUUID = UUID.fromString("@string/Main_UIDDToSerialBoardConnection");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent newint = getIntent();
        addressOfBltDevice = newint.getStringExtra(Devices.EXTRA_ADDRESS);

        new ConnectBlt().execute();
        frontLamp = (Button) findViewById(R.id.frontLamp);
        rearLamp = (Button) findViewById(R.id.rearLamp);
        startjoy = (Button) findViewById(R.id.startJoy);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);
        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText("@string/Main_CommandX" + String.valueOf(js.getX()));
                    textView2.setText("@string/Main_CommandY" + String.valueOf(js.getY()));
                    accelerattion = js.getY();
                    textView3.setText("@string/Main_CommandAngle" + String.valueOf(js.getAngle()));
                    textView4.setText("@string/Main_CommandDistance" + String.valueOf(
                            js.getDistance()));

                    int direction = js.get8Direction();
                    if (direction == JoyStickClass.STICK_UP) {
                        textView5.setText("@string/Main_CommandDirUp");
                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText("@string/Main_CommandDirUpRight");
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText("@string/Main_CommandDirRight");
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText("@string/Main_CommandDirUpDownRight");
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText("@string/Main_CommandDirDown");
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("@string/Main_CommandDirDownLeft");
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText("@string/Main_CommandDirLeft");
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText("@string/Main_CommandDirUpLeft");
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        textView5.setText("@string/Main_CommandDirCenter");
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("@string/Main_CommandX");
                    textView2.setText("@string/Main_CommandY");
                    textView3.setText("@string/Main_CommandAngle");
                    textView4.setText("@string/Main_CommandDistance");
                    textView5.setText("@string/Main_CommandDirection");
                }
                return true;
            }
        });

        rearLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendnningred("@string/Main_CommandRear");
            }
        });

        frontLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendnningred("@string/Main_CommandFront");
            }
        });

        startjoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new Handler();
                Thread t2 = new Thread(callback2);
                t2.start();

            }
        });
    }

    private Runnable callback2 = new Runnable() {
        @Override
        public void run() {
            int buf = 0;
            int dir = 0;
            while (true) {
                try {
                    int direction = js.get4Direction();
                    String directionAsString = String.valueOf(direction);
                    int  saturation = Integer.valueOf(js.getY());
                    for (int a = 0; a<20 ; a++) {
                        saturation = saturation + Integer.valueOf(js.getY());
                        Thread.sleep(10);
                    }
                    if (saturation < 0) saturation = -saturation;
                    //To ensure proper value in microcontroler atmega, wchich using 8-bit digit
                    saturation = (saturation * 1275) / 20000;
                    if (saturation > 255)
                    {
                        saturation = 255;
                    }

                    if(direction == 1 || direction == 3 || direction == 5  || direction == 7) {
                        if(direction == 1 && dir != 1) {
                            for (int i = 0 ;i<=5;i++) {
                                sendnningred("1");
                                Thread.sleep(40);
                                dir = 1;
                                System.out.println("@string/Main_DebugCommand1");
                            }
                        }
                        if(direction == 5 && dir != 5) {
                            for (int i = 0 ;i<=5;i++) {
                                sendnningred("5");
                                Thread.sleep(40);
                                dir = 5;
                                System.out.println("@string/Main_DebugCommand5");
                            }
                        }
                        if(direction == 3 && dir != 3) {
                            for (int i = 0 ;i<=5;i++) {
                                sendnningred("3");
                                Thread.sleep(40);
                                dir = 3;
                                System.out.println("@string/Main_DebugCommand3");
                            }
                        }
                        if(direction == 7 && dir != 7) {
                            for (int i = 0 ;i<=5;i++) {
                                sendnningred("7");
                                Thread.sleep(40);
                                dir = 7;
                                System.out.println("@string/Main_DebugCommand7");
                            }
                        }

                        sendnningred(""+saturation);
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    //TODO throw exception
                }
            }
        }
    };

    private void Disconnect() {
        if (bltSocket != null)
        {
            try {
                bltSocket.close();
            } catch (IOException e) {
                showmsg("@string/Main_CommandError");
            }
        }
        finish();
    }

    public class ConnectBlt extends AsyncTask<Void, Void, Void>
    {
        private boolean isSuccessConnection = true;

        @Override
        protected void onPreExecute()
        {
            progressOfDialog = ProgressDialog.show(MainActivity.this,
                    "@string/Main_DialodTitle", "@string/Main_DialodMessage");
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            try
            {
                if (bltSocket == null || !isBltConnected)
                {
                    myBltDevice = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBltDevice.getRemoteDevice(addressOfBltDevice);
                    bltSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bltSocket.connect();
                }
            } catch (IOException exception)
            {
                isSuccessConnection = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if (!isSuccessConnection)
            {
                finish();
            } else {
                isBltConnected = true;
            }
            progressOfDialog.dismiss();
        }
    }

    private void showmsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void sendnningred(String sth)
    {
        if (bltSocket != null) {
            try {
                bltSocket.getOutputStream();
                System.out.println("@string/Main_DebugCommandDataSending"+bltSocket);
            } catch (IOException e) {
                //TODO throw exception
            }
        }
    }
}