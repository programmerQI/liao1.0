package com.example.liao10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{

    private String ip;
    private int port;

    private ChatActivity activity;

    private Socket socket;

    private BufferedReader bufferedReader;
    private InputStreamReader inputStreamReader;
    private PrintWriter printWriter;
    private BufferedWriter bufferedWriter;
    private OutputStreamWriter outputStreamWriter;

    //private String message;

    public ClientThread(String ip, int port, ChatActivity activity)
    {

        this.ip = ip;

        this.port = port;

        this.activity = activity;

    }

    public void connectToServer() {

        try{

            socket = new Socket(ip, port);

            inputStreamReader = new InputStreamReader(socket.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);

            //System.out.println("8761");

            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedWriter = new BufferedWriter(outputStreamWriter);

            printWriter = new PrintWriter(socket.getOutputStream());

            //System.out.println("9012");

            // Send the client identification
            printWriter.println(Toolkit.rot13_encrypt(activity.getUser().getUsername()));
            printWriter.flush();

        } catch (IOException e){

            e.printStackTrace();

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    activity.stopClientThread();

                }

            });

            System.out.println("The socket has been connected to server.");

            return;

        }

    }


    public void receivingMessagesFromServer()
    {

        // This is a UI thread that is used to add message to the chat channel;
        class UIThread extends Thread {

            private String message;

            public UIThread(String message){

                this.message = message;

            }

            @Override
            public void run() {

                super.run();

                activity.addStringToChatPannel(Toolkit.rot13_decrypt(message));

            }
        }



        System.out.println("Prepare to receive message from server.");

        String message = "";

        try{

            while((message = bufferedReader.readLine()) != null)
            {

                System.out.println("A message has been received.");

                //System.out.println(message);

                activity.runOnUiThread(new UIThread(message));

            }

        } catch (IOException e){

            e.printStackTrace();

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    activity.stopClientThread();

                }

            });

            return;

        }

    }


    public void sendMessageToServer(String message)
    {

        if(socket.isClosed())
        {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    activity.stopClientThread();

                }

            });

            return;
        }

        printWriter.println(Toolkit.rot13_encrypt(activity.getUser().getUsername() + ": " + message));
        printWriter.flush();

        System.out.println("A message has been send to server.");

    }

    public void quietCloseStreams()
    {

        System.out.println("89");

        if(bufferedReader != null)
        {
            try {

                bufferedReader.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        System.out.println("14");

        if(inputStreamReader != null)
        {
            try {

                inputStreamReader.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        System.out.println("90");

        if(bufferedWriter != null)
        {
            try {

                bufferedWriter.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        System.out.println("57");

        if(outputStreamWriter != null)
        {
            try {
                outputStreamWriter.close();
            } catch (IOException e) {

                e.printStackTrace();

            }
        }


        if(printWriter != null)
        {

            printWriter.close();

        }

        System.out.println("The client streams have been closed.");

    }

    public void quietCloseSocket()
    {

        if(socket != null)
        {
            if(!socket.isClosed())
            {
                try {

                    socket.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }

        System.out.println("The client socket has been closed.");

    }


    @Override
    public void run() {

        super.run();

        connectToServer();

        receivingMessagesFromServer();

    }


    @Override
    public void interrupt() {
        super.interrupt();

        System.out.println("341");

        //quietCloseStreams();

        System.out.println("669");

        //quietCloseSocket();

        System.out.println("106");

        System.out.println("The client thread is interrupting.");

    }
}
