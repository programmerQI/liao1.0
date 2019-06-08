package com.example.liao10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PollingThread extends Thread{

    private ChatActivity activity;

    private String ip;

    private int port;

    private Socket socket;

    private String name;

    private String pollingResult;



    public PollingThread(ChatActivity activity, String ip, int port, String name)
    {

        this.activity = activity;

        this.ip = ip;

        this.port = port;

        this.name = name;

    }



    @Override
    public void run() {

        super.run();

        while(true)
        {

            pollingResult = "false";

            try {


                socket = new Socket(ip, port);

                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

                printWriter.println(Toolkit.rot13_encrypt(name));

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                pollingResult = bufferedReader.readLine();

                printWriter.close();

                bufferedReader.close();

                socket.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

            if(pollingResult.compareTo("false") == 0){

                activity.stopClientThread();

            }

            try {

                sleep(6000);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }


        }

    }
}
