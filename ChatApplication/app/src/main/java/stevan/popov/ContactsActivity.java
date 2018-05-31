package stevan.popov;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

public class ContactsActivity extends AppCompatActivity implements ServiceConnection {
    private ChatApplicationDbHelper mDbHelper;
    private AdapterContactsList adapter;
    private int ActiveUserId;

    private ChatApplicationHttpHelper http;
    private Handler handler;
    private Context context;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";
    private INotificationBinder mService = null;

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


        bindService(new Intent(ContactsActivity.this, NotificationService.class), this, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(this);
        }
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

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = INotificationBinder.Stub.asInterface(iBinder);
                try {
                        mService.setCallback(new NotificationCallback());
                    } catch (RemoteException e) {
                    }
            }

            @Override
    public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
            }

    private class NotificationCallback extends INotificationCallback.Stub {

        @Override
        public void onCallbackCall() throws RemoteException {

            final ChatApplicationHttpHelper httpHelper = new ChatApplicationHttpHelper();
            final Handler handler = new Handler();

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setSmallIcon(R.drawable.ic_send_button)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.mipmap.ic_launcher))
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getText(R.string.YouHaveNewMessage))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean response = httpHelper.getNotification(ContactsActivity.this);

                        handler.post(new Runnable() {
                            public void run() {
                                if (response) {
                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(2, mBuilder.build());
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
