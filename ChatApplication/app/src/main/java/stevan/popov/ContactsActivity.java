package stevan.popov;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity {
    private ChatApplicationDbHelper mDbHelper;
    private AdapterContactsList adapter;
    private int ActiveUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        final Button logOutButton = (Button) findViewById(R.id.LogOutButtonContactsActivity);
        adapter = new AdapterContactsList(this);
        mDbHelper = new ChatApplicationDbHelper(this);

        ListView list = (ListView) findViewById(R.id.ListViewContactsActivity);
        list.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        ActiveUserId = prefs.getInt("ActiveUser", 0);

        //Go to RegisterActivity
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    protected void onResume() {
        super.onResume();

        ModelForContactsList[] contacts = mDbHelper.readContacts();

        adapter.update(contacts);
        adapter.removeContact(ActiveUserId - 1);
    }
}
