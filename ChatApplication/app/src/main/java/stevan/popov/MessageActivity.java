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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MessageActivity extends AppCompatActivity {

    AdapterMessageList adapter;
    private int ActiveUserId;
    private String ActiveUserIdStr;
    private int MessageReciverId;
    private String MessageReciverIdStr;
    private ChatApplicationDbHelper mDbHelper;

    private ChatApplicationHttpHelper http;
    private Handler handler;
    private Context context;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_MESSAGE_URL = BASE_URL + "/message";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    private static ModelForMessageList[] all_messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final Button logOutButton = (Button) findViewById(R.id.LogOutButtonMessageActivity);
        final Button sendButton = (Button) findViewById(R.id.SendButtonMessageActivity);
        final Button refreshButton = (Button) findViewById(R.id.RefreshButtonMessageActivity);
        final EditText messageEditText = (EditText) findViewById(R.id.MessageEditTextMessageActivity);
        final TextView textView = (TextView) findViewById(R.id.NameTextViewMessageActivity);

        mDbHelper = new ChatApplicationDbHelper(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("NameFromContactsList");
        textView.setText(name);
        context = this;
        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        /*ActiveUserId = prefs.getInt("ActiveUser", 5);
        MessageReciverId = prefs.getInt("receiverId", 5);*/
        ActiveUserIdStr = prefs.getString("ActiveUser", null);
        MessageReciverIdStr = prefs.getString("receiverId", null);
        http = new ChatApplicationHttpHelper();
        handler = new Handler();


        adapter = new AdapterMessageList(this);

        ListView list = (ListView) findViewById(R.id.ListViewMessageActivity);
        list.setDivider(null);
        list.setAdapter(adapter);

        /*list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDbHelper.deleteMessage(position);
                String tempActiveUserId = String.valueOf(ActiveUserId);
                String tempSender = String.valueOf(MessageReciverId);
                ModelForMessageList[] messages = mDbHelper.readMessages(tempActiveUserId, tempSender);

                adapter.update(messages);
                return true;
            }
        });*/

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
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMessages();
            }
        });
        //Send
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("receiver", MessageReciverIdStr);
                            jsonObject.put("data", messageEditText.getText().toString());

                            final boolean success = http.postMessageToServer(MessageActivity.this, POST_MESSAGE_URL, jsonObject);

                            handler.post(new Runnable() {
                                public void run() {
                                    if (success) {
                                        Toast.makeText(MessageActivity.this, getText(R.string.MessageSent), Toast.LENGTH_SHORT).show();
                                        messageEditText.getText().clear();
                                        updateMessages();
                                    } else {
                                        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        String sendMsgErr = prefs.getString("error", null);
                                        Toast.makeText(MessageActivity.this, sendMsgErr, Toast.LENGTH_SHORT).show();
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

                /*Context context = getApplicationContext();
                CharSequence text = "Message is sent!";
                int duration = Toast.LENGTH_SHORT;
                //adapter.AddCharacter(new ModelForMessageList(messageEditText.getText().toString(), true));
                String messageText = messageEditText.getText().toString();

                ModelForMessageList m_message = new ModelForMessageList(0, MessageReciverId, ActiveUserId, messageText);
                mDbHelper.insertMessage(m_message);
                String tempActiveUserId = String.valueOf(ActiveUserId);
                String tempSender = String.valueOf(MessageReciverId);
                ModelForMessageList[] messages = mDbHelper.readMessages(tempActiveUserId, tempSender);

                adapter.update(messages);

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                messageEditText.setText("");*/
                updateMessages();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        try {

                            final boolean success = http.postLogOutFromServer(MessageActivity.this, LOGOUT_URL);

                            handler.post(new Runnable() {
                                public void run() {
                                    if (success) {
                                        startActivity(new Intent(MessageActivity.this, MainActivity.class));
                                    } else {
                                        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        String error = prefs.getString("error", null);
                                        Toast.makeText(MessageActivity.this, error, Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    protected void onResume() {
        super.onResume();
        updateMessages();
        /*String tempActiveUserId = String.valueOf(ActiveUserId);
        String tempSender = String.valueOf(MessageReciverId);
        ModelForMessageList[] messages = mDbHelper.readMessages(tempActiveUserId, tempSender);

        adapter.update(messages);*/
    }

    public void updateMessages() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray messages = http.getMessagesFromServer(MessageActivity.this, GET_MESSAGE_URL+MessageReciverIdStr);
                    handler.post(new Runnable(){
                        public void run() {
                            if (messages != null) {

                                JSONObject json_message;
                                all_messages = new ModelForMessageList[messages.length()];

                                for (int i = 0; i < messages.length(); i++) {
                                    try {
                                        json_message = messages.getJSONObject(i);
                                        all_messages[i] = new ModelForMessageList(json_message.getString("sender"),json_message.getString("data"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                adapter.update(all_messages);
                            } else {
                                SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                String error = prefs.getString("error", null);
                                Toast.makeText(MessageActivity.this, error, Toast.LENGTH_SHORT).show();
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
