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
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //ToDO Move to strings.xml

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
                    textView1.setText("X : " + String.valueOf(js.getX()));
                    textView2.setText("Y : " + String.valueOf(js.getY()));
                    accelerattion = js.getY();
                    textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                    textView4.setText("Distance : " + String.valueOf(js.getDistance()));

                    int direction = js.get8Direction();
                    if (direction == JoyStickClass.STICK_UP) {
                        textView5.setText("Direction : Up");
                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        textView5.setText("Direction : Center");
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView3.setText("Angle :");
                    textView4.setText("Distance :");
                    textView5.setText("Direction :");
                }
                return true;
            }
        });

        rearLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendnningred("REAR");
            }
        });

        frontLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendnningred("FRONT");
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
                    saturation = (saturation * 1275) / 20000; //To ensure proper value in microcontroler atmega, wchich using 8-bit digit
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
                                System.out.println("Zmiana kierunku na 1 ");
                            }
                        }
                        if(direction == 5 && dir != 5) {
                            for (int i = 0 ;i<=5;i++) {
                                sendnningred("5");
                                Thread.sleep(40);
                                dir = 5;
                                System.out.println("Zmiana kierunku na 5 ");
                            }
                        }
                        if(direction == 3 && dir != 3) {
                            for (int i = 0 ;i<=5;i++) {
                                sendnningred("3");
                                Thread.sleep(40);
                                dir = 3;
                                System.out.println("Zmiana kierunku na 3 ");
                            }
                        }
                        if(direction == 7 && dir != 7) {
                            for (int i = 0 ;i<=5;i++) {
                                sendnningred("7");
                                Thread.sleep(40);
                                dir = 7;
                                System.out.println("Zmiana kierunku na 7 ");
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
                showmsg("Error");
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
            progressOfDialog = ProgressDialog.show(MainActivity.this, "Trwa łączenie...", "Proszę czekać :)");
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            System.out.println("Asynch Task2");

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
                System.out.println("Wyslano dane"+bltSocket);
            } catch (IOException e) {
                //TODO throw exception
            }
        }
    }
}g