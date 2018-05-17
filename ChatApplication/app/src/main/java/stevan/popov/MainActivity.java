package stevan.popov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ChatApplicationDbHelper mDbHelper;
    private Context context;
    private ChatApplicationHttpHelper http;
    private Handler handler;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String LOGIN_URL = BASE_URL + "/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText usernameTextEdit = (EditText) findViewById(R.id.UsernameTextEditMainActivity);
        final EditText passwordTextEdit = (EditText) findViewById(R.id.PasswordTextEditMainActivity);
        final Button loginButton = (Button) findViewById(R.id.LoginButtonMainActivity);
        final Button registerButton = (Button) findViewById(R.id.RegisterButtonMainActivity);
        final boolean[] passwordEntered = {false};
        final boolean[] usernameEntered = {false};
        //mDbHelper = new ChatApplicationDbHelper(this);
        context = this;
        http = new ChatApplicationHttpHelper();
        handler = new Handler();

        //Check if password is entered
        usernameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = usernameTextEdit.getText().toString();
                if (text.length() != 0) {
                    usernameEntered[0] = true;
                    if (passwordEntered[0] == true) {
                        loginButton.setEnabled(true);
                    }
                } else {
                    usernameEntered[0] = false;
                    loginButton.setEnabled(false);
                }
            }
        });
        passwordTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = passwordTextEdit.getText().toString();
                if (text.length() >= 6) {
                    passwordEntered[0] = true;
                    if (usernameEntered[0] == true) {
                        loginButton.setEnabled(true);
                    }
                } else {
                    passwordEntered[0] = false;
                    loginButton.setEnabled(false);
                }
            }
        });

        //Go to RegisterActivity
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", usernameTextEdit.getText().toString());
                            jsonObject.put("password", passwordTextEdit.getText().toString());

                            final boolean response = http.postLogedOnServer(MainActivity.this, LOGIN_URL, jsonObject);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response){
                                        Intent intent = new Intent(MainActivity.this, ContactsActivity.class);

                                        SharedPreferences.Editor editor = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                                        editor.putString("ActiveUser", usernameTextEdit.getText().toString());
                                        editor.apply();
                                        startActivity(intent);
                                    }
                                    else {
                                        SharedPreferences mPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        String error = mPreferences.getString("error", null);
                                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                /*ModelForContactsList[] contacts = mDbHelper.readContacts();
                int contact_exists = 1;
                SharedPreferences.Editor editor = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
                if (contacts != null) {
                    for (int i = 0; i < contacts.length; i++) {
                        contact_exists = contacts[i].getmUsername().compareTo(usernameTextEdit.getText().toString());
                        if (contact_exists == 0) {
                            editor.putInt("ActiveUser", contacts[i].getmContactId());
                            editor.apply();
                            break;
                        }
                    }
                }
                if (contact_exists == 0) {

                    Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.UsernameDoesntExists, Toast.LENGTH_LONG).show();
                }
                /*Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                view.getContext().startActivity(intent);*/
            }
        });


    }
}
