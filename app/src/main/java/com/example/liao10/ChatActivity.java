package com.example.liao10;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    private static final String IP = "192.168.0.8";
    private static final int PORT = 9900;
    private static final int DEFAULT_FONTSIZE = 16;

    private User user;
    private ClientThread thread;

    private AlertDialog.Builder dialogBuilder;
    private InputMethodManager inputMethodManager;
    private View.OnClickListener onClickListener;
    private ScrollView chat_srollView;
    private LinearLayout chat_pannel;
    private EditText chat_editText;
    private Button chat_button;

    public User getUser() {

        return user;

    }

    private void loadUserInfo()
    {

        user = getIntent().getParcelableExtra("user");

        System.out.println(user.getUsername() + " information has been load into chat activity.");

    }

    private void loadUIComponents()
    {

        dialogBuilder = new AlertDialog.Builder(this);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        chat_srollView = (ScrollView) findViewById(R.id.chat_scrollView);

        chat_pannel = (LinearLayout)findViewById(R.id.chat_pannel);

        chat_editText = (EditText)findViewById(R.id.chat_editText);

        chat_button = (Button)findViewById(R.id.chat_button);

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(v == chat_button)
                {
                    thread.sendMessageToServer(acquireStringFromEditText());

                    closeKeyboard();

                    chat_srollView.fullScroll(View.FOCUS_DOWN);

                }
                else if(v == chat_pannel)
                {

                    closeKeyboard();

                }

            }

        };

        chat_button.setOnClickListener(onClickListener);
        chat_pannel.setOnClickListener(onClickListener);

        System.out.println("The UI component has been load.");

    }

    private int runClientThread()
    {

        thread = new ClientThread(IP, PORT, this);

        thread.start();

        System.out.println("The client threat is running.");

        return 0;
    }

    public void addStringToChatPannel(String str)
    {

        TextView textView = new TextView(this);
        textView.setTextSize(DEFAULT_FONTSIZE);
        textView.setText(str);

        chat_pannel.addView(textView);

    }

    private void closeKeyboard()
    {

        View view = getCurrentFocus();

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private String acquireStringFromEditText()
    {
        String str = chat_editText.getText().toString();
        chat_editText.setText("");
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loadUserInfo();

        loadUIComponents();

        runClientThread();


    }

    public void stopClientThread()
    {

        thread.interrupt();

        dialogBuilder.setTitle("Info:");
        dialogBuilder.setMessage("Connection lost!");

        onDestroy();

    }

    @Override
    protected void onDestroy() {

        thread.interrupt();

        super.onDestroy();
    }
}
