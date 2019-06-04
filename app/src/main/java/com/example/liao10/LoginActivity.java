package com.example.liao10;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.liao10.User.KEY_USERNAME;

public class LoginActivity extends AppCompatActivity {

    // Private members:


    // Constant value of file name and Directory.
    private static final String STORAGE_PATH = "Liao";
    private static final String CONFIG_FILE = "user.conf";


    // Configuration files
    private File root_path;
    private File conf_file;


    // The user class
    private User user;


    // The UI components
    private AlertDialog.Builder dialogBuilder;
    private EditText username_etest;
    private Button login_button;





    // Create dialog builder
    private void initDialogBuilder()
    {
        dialogBuilder = new AlertDialog.Builder(this);
    }


    // Create the default storage path if the path was not exist.
    private int creatStoragePath()
    {

        root_path = new File(Environment.getExternalStorageDirectory() , STORAGE_PATH);

        if(!root_path.exists())
        {

            root_path.mkdir();

            System.out.println("The storage path has been created!");
            System.out.println(root_path.getAbsolutePath());

            return 1;

        }

        System.out.println("The storage path has already exist!");

        return 0;
    }


    //
    private int loadConfValues()
    {

        conf_file = new File(root_path, CONFIG_FILE);

        if(!conf_file.exists())
        {

            try {

                conf_file.createNewFile();

            } catch (IOException e) {

                System.out.println("Creating configuration file failed.");
                e.printStackTrace();
                return -1;

            }

            System.out.println("The configuration file has bee created.");
            // If the conf file hasn't been created, return 0;
            return 0;
        }

        System.out.println("The configuration file has already exist!");

        //

        System.out.println("Loading the configuration value");

        try {

            // Read lines from the file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(conf_file));

            String tmp = "";
            String[] strs = new String[5];

            while((tmp = bufferedReader.readLine()) != null)
            {

                Toolkit.split(tmp, Toolkit.CHARACTER_DIVIDER, strs, 5);

                System.out.println(strs[0] + " " + strs[1]);

                // Create new user
                if(strs[0].compareTo(KEY_USERNAME) == 0)
                {

                    user = new User(strs[1]);

                    bufferedReader.close();

                    System.out.println("Loading success.");

                    return 1;

                }

            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return 0;
    }


    // Pass to the chat activity
    private void openChatActivity()
    {

        System.out.println("Starting the chat activity.");

        Intent intent = new Intent(this, ChatActivity.class);

        // Pass the user information
        intent.putExtra("user", user);

        startActivity(intent);

    }


    // Create new configuration file
    private void creatDefautConfigurationFile()
    {

        // Get the UI components
        username_etest = (EditText)findViewById(R.id.login_textEdit);
        login_button = (Button)findViewById(R.id.login_button);

        // Set the click listener to the login button.
        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get the user input from the EditText
                String username = username_etest.getText().toString();

                // Check if the input is valid
                if(username.isEmpty())
                {
                    dialogBuilder.setMessage("The user name cannot be empty!").setTitle("Error!");
                    dialogBuilder.create().show();

                    System.out.println("Input Error!");

                    return;
                }
                else if(username.length() > 10)
                {
                    dialogBuilder.setMessage("The user name is toot long!").setTitle("Error!");
                    dialogBuilder.create().show();

                    System.out.println("Input Error!");

                    return;
                }

                // Create new user
                user = new User(username);

                System.out.println(user.getUsername() + "has been created");

                // Write the user name to default configuration file
                try {

                    FileWriter writer = new FileWriter(conf_file);
                    writer.append(KEY_USERNAME + "=" + username);
                    writer.flush();
                    writer.close();

                    System.out.println("The user name has been saved.");

                } catch (IOException e) {

                    e.printStackTrace();

                }

                openChatActivity();

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //System.out.println("AAAAAAAAAAAAAAAAAAAA");

        super.onCreate(savedInstanceState);

        initDialogBuilder();

        creatStoragePath();

        if(loadConfValues() == 1)
        {

            System.out.println("The user info has been loaded.");
            openChatActivity();

        }
        else
        {

            setContentView(R.layout.activity_login);

            creatDefautConfigurationFile();

        }





        /*File root = new File(Environment.getExternalStorageDirectory())

        System.out.println("HHHHHHHHHHHHHHHHHHHHH");

        File root = new File(Environment.getExternalStorageDirectory(),"Notes");

        if(!root.exists())
        {

            System.out.println("!!!!!!!!!!!!!!!!!!");
            root.mkdir();
        }

        File file = new File(root, "text.txt");
        try {

            FileWriter writer = new FileWriter(file);
            writer.append("This is it!");
            writer.flush();

            System.out.println("#################");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
