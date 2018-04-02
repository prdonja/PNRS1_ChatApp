package stevan.popov;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {

    AdapterMessageList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final Button logOutButton = (Button) findViewById(R.id.LogOutButtonMessageActivity);
        final Button sendButton = (Button) findViewById(R.id.SendButtonMessageActivity);
        final EditText messageEditText = (EditText) findViewById(R.id.MessageEditTextMessageActivity);
        final TextView textView = (TextView) findViewById(R.id.NameTextViewMessageActivity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("NameFromContactsList");
        textView.setText(name);

        adapter = new AdapterMessageList(this);
        adapter.AddCharacter(new ModelForMessageList("poruka", false));
        adapter.AddCharacter(new ModelForMessageList("poruka", true));
        adapter.AddCharacter(new ModelForMessageList("poruka", false));
        adapter.AddCharacter(new ModelForMessageList("poruka", true));
        adapter.AddCharacter(new ModelForMessageList("poruka", false));
        adapter.AddCharacter(new ModelForMessageList("poruka", true));
        adapter.AddCharacter(new ModelForMessageList("poruka", false));
        adapter.AddCharacter(new ModelForMessageList("poruka", true));
        adapter.AddCharacter(new ModelForMessageList("poruka", false));
        adapter.AddCharacter(new ModelForMessageList("poruka", true));
        adapter.AddCharacter(new ModelForMessageList("poruka", false));
        adapter.AddCharacter(new ModelForMessageList("poruka", true));

        ListView list = (ListView) findViewById(R.id.ListViewMessageActivity);
        list.setDivider(null);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                ModelForMessageList item = (ModelForMessageList) adapter.getItem(position);
                adapter.RemoveMessage(item);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        //Is message writen?
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = messageEditText.getText().toString();
                if (text.length() != 0) {
                    sendButton.setEnabled(true);
                } else {

                    sendButton.setEnabled(false);
                }
            }
        });

        //Send
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                CharSequence text = "Message is sent!";
                int duration = Toast.LENGTH_SHORT;
                adapter.AddCharacter(new ModelForMessageList(messageEditText.getText().toString(), true));

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                messageEditText.setText("");
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }
}
