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

    private String message;

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

            System.out.println("8761");

            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedWriter = new BufferedWriter(outputStreamWriter);

            printWriter = new PrintWriter(socket.getOutputStream());

            System.out.println("9012");

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

        System.out.println("Prepare to receive message from server.");

        message = "";

        try{

            while((message = bufferedReader.readLine()) != null)
            {

                System.out.println("A message has been received.");

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        activity.addStringToChatPannel(Toolkit.rot13_decrypt(message));

                    }

                });

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

        printWriter.println(Toolkit.rot13_encrypt(message));
        printWriter.flush();

        System.out.println("A message has been send to server.");

    }

    public void quietCloseStreams()
    {

        if(bufferedReader != null)
        {
            try {

                bufferedReader.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        if(inputStreamReader != null)
        {
            try {

                inputStreamReader.close();

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

        quietCloseStreams();

        quietCloseSocket();

        System.out.println("The client thread is interrupting.");

        super.interrupt();

    }
}
