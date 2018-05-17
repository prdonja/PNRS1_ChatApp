package stevan.popov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private ChatApplicationDbHelper mDbHelper;

    private ChatApplicationHttpHelper http;
    private Handler handler;
    private Context context;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String REGISTER_URL = BASE_URL + "/register";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText usernameTextEdit = (EditText) findViewById(R.id.UsernameTextEditRegisterActivity);
        final EditText passwordTextEdit = (EditText) findViewById(R.id.PasswordTextEditRegisterActivity);
        final EditText firstNameTextEdit = (EditText) findViewById(R.id.FirstNameEditTextRegisterActivity);
        final EditText lastNameTextEdit = (EditText) findViewById(R.id.LastNameEditTextRegisterActivity);
        final EditText emailTextEdit = (EditText) findViewById(R.id.EmailEditTextRegisterActivity);

        final DatePicker datePicker = (DatePicker) findViewById(R.id.DatePickerRegisterActivity);

        final Button registerButton = (Button) findViewById(R.id.RegisterButtonRegisterActivity);

        final boolean[] passwordEntered = {false};
        final boolean[] usernameEntered = {false};
        final boolean[] emailEntered = {false};

        context = this;
        http = new ChatApplicationHttpHelper();
        handler = new Handler();

        //mDbHelper = new ChatApplicationDbHelper(this);
        //Are Username,pasword,email ok?
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
                    if (passwordEntered[0] == true && emailEntered[0] == true) {
                        registerButton.setEnabled(true);
                    }
                } else {
                    usernameEntered[0] = false;
                    registerButton.setEnabled(false);
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
                    if (usernameEntered[0] == true && emailEntered[0] == true) {
                        registerButton.setEnabled(true);
                    }
                } else {
                    passwordEntered[0] = false;
                    registerButton.setEnabled(false);
                }
            }
        });
        emailTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = emailTextEdit.getText().toString();
                if (text.length() != 0 && Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    emailEntered[0] = true;
                    if (usernameEntered[0] == true && passwordEntered[0] == true) {
                        registerButton.setEnabled(true);
                    }
                } else {
                    emailEntered[0] = false;
                    registerButton.setEnabled(false);
                }
            }
        });
        //Set max date
        Date c = Calendar.getInstance().getTime();
        datePicker.setMaxDate(c.getTime());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", usernameTextEdit.getText().toString());
                            jsonObject.put("password", passwordTextEdit.getText().toString());
                            jsonObject.put("email", emailTextEdit.getText().toString());

                            final boolean response = http.postRegisterOnServer(RegisterActivity.this, REGISTER_URL, jsonObject);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response){
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        SharedPreferences mPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        String error = mPreferences.getString("error", null);
                                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
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
                if (contacts != null) {
                    for (int i = 0; i < contacts.length; i++) {
                        contact_exists = contacts[i].getmUsername().compareTo(usernameTextEdit.getText().toString());
                        if (contact_exists == 0) {
                            break;
                        }
                    }
                }
                if (contact_exists != 0) {
                    ModelForContactsList contact = new ModelForContactsList(0, usernameTextEdit.getText().toString(), firstNameTextEdit.getText().toString(), lastNameTextEdit.getText().toString());
                    mDbHelper.insertContact(contact);
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.UsernameAlreadyExists, Toast.LENGTH_LONG).show();
                }
                */


            }
        });


    }
}
