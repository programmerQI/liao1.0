package com.example.liao10;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PollingThread extends Thread{

    private static final String POLLING_CODE = "*poll*";

    private ChatActivity activity;

    private String ip;

    private int port;

    private Socket socket;

    private String name;

    private String pollingResult;

    private Handler handler;



    public PollingThread(ChatActivity activity, String ip, int port, String name, Handler handler)
    {

        this.activity = activity;

        this.ip = ip;

        this.port = port;

        this.name = name;

        this.handler = handler;

    }



    @Override
    public void run() {

        super.run();

        while(true)
        {

            try {

                sleep(6000);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            pollingResult = "false";

            try {

                socket = new Socket(ip, port);

                System.out.println("Polling..");

                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

                printWriter.println(Toolkit.rot13_encrypt(POLLING_CODE));

                printWriter.flush();

                printWriter.println(Toolkit.rot13_encrypt(name));

                printWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                pollingResult = Toolkit.rot13_decrypt(bufferedReader.readLine());

                System.out.println("*****Polling result : " + pollingResult + "*****");

                printWriter.close();

                bufferedReader.close();

                socket.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

            if(pollingResult.compareTo("true") != 0){

                System.out.println("xx");
                //Message closeMessage = Message.obtain(handler, activity.LOST_CONNECTION);
                //closeMessage.sendToTarget();
                activity.stopClientThread();

            }


        }

    }
}
