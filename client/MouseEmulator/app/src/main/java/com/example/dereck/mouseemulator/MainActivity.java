package com.example.dereck.mouseemulator;

import java.net.*;
import java.io.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener, SensorEventListener{
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    Button btn_left;
    Button btn_right;
    Sensor snr;
    SensorManager snrMgr;
    long prevTime = 0, c = 0;
    double dx = new Double(0), dy = new Double(0), checked = new Double(1);
    MouseSend mouseSend;
//    String serverName = "100.64.165.195";
//    OutputStream outToServer;
//    BufferedReader br;
//    InputStream inFromServer;
//    DataInputStream in;
//    String ina = "";
//    DataOutputStream out;
//    int port = 6066;
//    Socket client;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_left = findViewById(R.id.button_left);
        btn_right = findViewById(R.id.button_right);
        snrMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        snr = snrMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        try {
//            Client.init();
//        }
//        catch(IOException e){e.printStackTrace();}
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        snrMgr.registerListener(this, snr, SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

//        try {
//            System.out.println("Connecting to " + serverName + " on port " + port);
//            client = new Socket(serverName, port);
//
//            System.out.println("Just connected to " + client.getRemoteSocketAddress());
//            outToServer = client.getOutputStream();
//            out = new DataOutputStream(outToServer);
//            br = new BufferedReader(new InputStreamReader(System.in));
//            inFromServer = client.getInputStream();
//            in = new DataInputStream(inFromServer);
//
//            ina = "";
//        } catch(Exception e){e.printStackTrace();}
        MouseSend.ddb = new Double[]{dx, dy, checked};
        new MouseSend().execute(new Object[]{});
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.button_left)
        {
            System.out.println("Left button clicked");

            ((TextView)findViewById(R.id.text_bye)).setText("Left button clicked");
        }
        else if (v.getId() == R.id.button_right)
        {
            System.out.println("Right button clicked");
            ((TextView)findViewById(R.id.text_bye)).setText("Right button clicked");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //System.out.printf("Cur: %f %f %f\n",event.values[0] , event.values[1],event.values[2]);
        //System.out.println(event.timestamp);
        double aaa = 0.3;
        //System.out.printf("%f\n", event.values[0]);
        if (event.timestamp-prevTime < 300000000) {
            if (event.values[0] > aaa || event.values[1] > aaa) {
                prevTime = event.timestamp;
            }
            if (dx < 0 && event.values[0] < dx)
                dx = (double)event.values[0];
            else if (dx > 0 && event.values[0] > dx)
                dx = (double)event.values[0];
            if (dy < 0 && event.values[1] < dy)
                dy = (double)event.values[1];
            else if (dy > 0 && event.values[1] > dy)
                dy = (double)event.values[1];
            return;
        }
        if (event.values[0] <aaa && event.values[1] < aaa)
            return;
        int xxx = (int)(dx/Math.abs(dx));
        int yyy = (int)(dy/Math.abs(dy));
        String out = "";
        if (xxx < 0)
            out += "Left ";
        else if (xxx > 0)
            out += "Right ";
        else
            out += "Straight ";
        if (yyy < 0)
            out += "Up";
        else if (yyy > 0)
            out += "Down";
        else;
        System.out.println(out);

//        try {
//            this.out.writeUTF(out + "\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //        try {
//            Client.write(dx / Math.abs(dx), dy / Math.abs(dy));
//        }
//        catch(IOException e){System.out.println(e.getStackTrace());}
        MouseSend.ddb = new Double[]{dx, dy, checked};
        prevTime=event.timestamp;
        dx = (double)event.values[0];
        dy = (double)event.values[1];

        //        int x = Math.round(event.values[0]);
//        int y = Math.round(event.values[1]);

//        if (x == 0)
//            dx = 0;
//        else
//            dx = x/Math.abs(x);

//        if (y == 0)
//            dy = 0;
//        else
//            dy = y/Math.abs(y);

 //       if (dx != 0 || dy != 0) {
 //           System.out.printf("%d %d\n", x, y);
            //MouseClient.write(new byte[]{(byte)dx, (byte)dy, (byte)0});
   //     }
//        System.out.printf("Elapsed: %d %d %d\n", Math.round(x), Math.round(y), Math.round(z));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}



class MouseSend extends AsyncTask {
    String serverName = "100.64.165.195";
    OutputStream outToServer;
    BufferedReader br;
    InputStream inFromServer;
    DataInputStream in;
    String ina = "";
    DataOutputStream out;
    int port = 6066;
    Socket client;
    static Double[] ddb;

    MouseSend() {
    }

    @Override
    protected Object doInBackground(Object[] db) {
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);
            br = new BufferedReader(new InputStreamReader(System.in));
            inFromServer = client.getInputStream();
            in = new DataInputStream(inFromServer);

            ina = "";
        } catch(Exception e){e.printStackTrace();}

        try {
            Thread.sleep(1000);
            while (true) {
                String a = (MouseSend.ddb[0]).toString() + " " + (MouseSend.ddb[1]).toString();
                this.out.writeUTF(a);
                MouseSend.ddb[2] = Double.valueOf(0);
                while (MouseSend.ddb[2] == 0) Thread.sleep(10);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}