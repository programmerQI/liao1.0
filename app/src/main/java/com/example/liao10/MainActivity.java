package com.example.liao10;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    private String string;

    /*class MyThread extends Thread{

        private MainActivity activity;

        MyThread(MainActivity activity){

            this.activity = activity;

        }

        @Override
        public void run() {

            super.run();


            while(true)
            {

                String string = "This is it.";

                System.out.println(string);

                activity.setString(string);

                try {

                    sleep(3000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

            }
        }
    }*/

    public void setString(String str)
    {

        System.out.println("Set string.");

        TextView textView = new TextView(this);

        textView.setText("");
        textView.setText(str);
        textView.setTextSize(15);

        linearLayout.addView(textView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linearLayout);

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Error!").setTitle("Warning");
        AlertDialog dialog = builder.create();
        dialog.show();*/


        //runOnUiThread(new MyThread(this));

        new Thread(){

            int n = 0;

            @Override
            public void run() {

                super.run();

                while(true)
                {

                    System.out.println("111");

                    n = (n + 1) % 1000;

                    if(n % 2 == 1)
                    {
                        string = "This is it.";
                    }
                    else
                    {
                        string = "No.";
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            setString(new String(string + n));

                        }
                    });

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }.start();

    }
}
