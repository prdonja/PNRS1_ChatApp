package stevan.popov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        final Button logOutButton = (Button) findViewById(R.id.LogOutButtonContactsActivity);
        AdapterContactsList adapter = new AdapterContactsList(this);
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameStevanPopov)));
        adapter.AddCharacter(new ModelForContactsList(getResources().getDrawable(R.drawable.ic_send_button), getResources().getString(R.string.NameMarkoFrancuski)));

        ListView list = (ListView) findViewById(R.id.ListViewContactsActivity);
        list.setAdapter(adapter);
        //Go to RegisterActivity
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
