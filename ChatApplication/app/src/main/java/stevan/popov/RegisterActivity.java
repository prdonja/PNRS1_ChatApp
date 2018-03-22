package stevan.popov;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    @Override
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
                if(text.length() != 0)
                {
                    usernameEntered[0] = true;
                    if (passwordEntered[0] == true && emailEntered[0] == true) {
                        registerButton.setEnabled(true);
                    }
                }
                else
                {
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
                if(text.length() >= 6)
                {
                    passwordEntered[0] = true;
                    if (usernameEntered[0] == true && emailEntered[0] == true) {
                        registerButton.setEnabled(true);
                    }
                }
                else
                {
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
                if(text.length() != 0 && Patterns.EMAIL_ADDRESS.matcher(text).matches())
                {
                    emailEntered[0] = true;
                    if (usernameEntered[0] == true && passwordEntered[0] == true) {
                        registerButton.setEnabled(true);
                    }
                }
                else
                {
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
                Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                view.getContext().startActivity(intent);
            }
        });




    }
}
