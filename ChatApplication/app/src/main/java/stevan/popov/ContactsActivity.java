package stevan.popov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactsActivity extends AppCompatActivity {
    private ChatApplicationDbHelper mDbHelper;
    private AdapterContactsList adapter;
    private int ActiveUserId;

    private ChatApplicationHttpHelper http;
    private Handler handler;
    private Context context;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    private ModelForContactsList[] allContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        final Button logOutButton = (Button) findViewById(R.id.LogOutButtonContactsActivity);
        final Button refreshButton = (Button) findViewById(R.id.RefreshButtonContactsActivity);

        adapter = new AdapterContactsList(this);
        //mDbHelper = new ChatApplicationDbHelper(this);

        context = this;
        http = new ChatApplicationHttpHelper();
        handler = new Handler();

        ListView list = (ListView) findViewById(R.id.ListViewContactsActivity);
        list.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        //ActiveUserId = prefs.getInt("ActiveUser", 0);

        //Go to RegisterActivity
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        try {

                            final boolean success = http.postLogOutFromServer(ContactsActivity.this, LOGOUT_URL);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (success) {
                                        startActivity(new Intent(ContactsActivity.this, MainActivity.class));
                                    } else {
                                        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        String error = prefs.getString("error", null);
                                        Toast.makeText(ContactsActivity.this, error, Toast.LENGTH_SHORT).show();}
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContacts();
            }
        });


    }

    protected void onResume() {
        super.onResume();
        updateContacts();
        /*ModelForContactsList[] contacts = mDbHelper.readContacts();

        adapter.update(contacts);
        adapter.removeContact(ActiveUserId - 1);*/
    }

    public void updateContacts() {

        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray contacts = http.getContactsFromServer(ContactsActivity.this, CONTACTS_URL);
                    handler.post(new Runnable(){
                        public void run() {
                            if (contacts != null) {

                                JSONObject json_contact;
                                allContacts = new ModelForContactsList[contacts.length()];

                                for (int i = 0; i < contacts.length(); i++) {
                                    try {
                                        json_contact = contacts.getJSONObject(i);
                                        allContacts[i] = new ModelForContactsList(json_contact.getString("username"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                adapter.update(allContacts);
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }
}
