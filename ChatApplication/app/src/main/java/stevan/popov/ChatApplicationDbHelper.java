package stevan.popov;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatApplicationDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "chatAppDatabase1.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_CONTACTS = "Contacts";
    public static final String CONTACTS_COLUMN_CONTACT_ID = "ContactId";
    public static final String CONTACTS_COLUMN_USERNAME = "Username";
    public static final String CONTACTS_COLUMN_FIRST_NAME = "FirstName";
    public static final String CONTACTS_COLUMN_LAST_NAME = "LastName";

    public static final String TABLE_NAME_MESSAGE = "Messages";
    public static final String MESSAGE_COLUMN_MESSAGE_ID = "MessageId";
    public static final String MESSAGE_COLUMN_SENDER_ID = "SenderId";
    public static final String MESSAGE_COLUMN_RECEIVER_ID = "ReceiverId";
    public static final String MESSAGE_COLUMN_MESSAGE = "Message";

    private SQLiteDatabase mDb = null;

    public ChatApplicationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
                CONTACTS_COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                CONTACTS_COLUMN_USERNAME + " TEXT, " +
                CONTACTS_COLUMN_FIRST_NAME + " TEXT, " +
                CONTACTS_COLUMN_LAST_NAME + " TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_NAME_MESSAGE + " (" +
                MESSAGE_COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                MESSAGE_COLUMN_SENDER_ID + " INTEGER, " +
                MESSAGE_COLUMN_RECEIVER_ID + " INTEGER, " +
                MESSAGE_COLUMN_MESSAGE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertContact(ModelForContactsList contacts) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(CONTACTS_COLUMN_CONTACT_ID, contacts.getmContactId());
        values.put(CONTACTS_COLUMN_USERNAME, contacts.getmUsername());
        values.put(CONTACTS_COLUMN_FIRST_NAME, contacts.getmFirstName());
        values.put(CONTACTS_COLUMN_LAST_NAME, contacts.getmLastName());

        db.insert(TABLE_NAME_CONTACTS, null, values);
        close();
    }

    public void insertMessage(ModelForMessageList model) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(MESSAGE_COLUMN_MESSAGE_ID, model.getmMessage_id());
        values.put(MESSAGE_COLUMN_SENDER_ID, model.getmSender_id());
        values.put(MESSAGE_COLUMN_RECEIVER_ID, model.getmReceiver_id());
        values.put(MESSAGE_COLUMN_MESSAGE, model.getmMessage());

        db.insert(TABLE_NAME_MESSAGE, null, values);
        close();
    }

    public ModelForContactsList[] readContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        ModelForContactsList[] contacts = new ModelForContactsList[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContact(cursor);
        }

        close();
        return contacts;
    }

    public ModelForMessageList[] readMessages(String sender, String receiver) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGE, null, "(SenderId =? AND ReceiverId =?) OR (SenderId =? AND ReceiverId =?)", new String[]{sender, receiver, receiver, sender}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        ModelForMessageList[] messages = new ModelForMessageList[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        close();
        return messages;
    }

    public ModelForContactsList readContact(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null,
                CONTACTS_COLUMN_CONTACT_ID + "=?", new String[]{Integer.toString(id)},
                null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        ModelForContactsList contact = createContact(cursor);

        close();

        return contact;
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_CONTACTS, CONTACTS_COLUMN_CONTACT_ID + "=?", new String[]{Integer.toString(id)});
        close();
    }

    public void deleteMessage(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_MESSAGE, MESSAGE_COLUMN_MESSAGE_ID + "=?", new String[]{Integer.toString(id)});
        close();
    }


    private ModelForContactsList createContact(Cursor cursor) {

        int mContactId = cursor.getInt(cursor.getColumnIndex(CONTACTS_COLUMN_CONTACT_ID));
        String mUsername = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_USERNAME));
        String mFirstName = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_FIRST_NAME));
        String mLastName = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_LAST_NAME));

        return new ModelForContactsList(mContactId, mUsername, mFirstName, mLastName);
    }

    private ModelForMessageList createMessage(Cursor cursor) {

        int mMessageId = cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_MESSAGE_ID));
        int mSenderId = cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_SENDER_ID));
        int mReceiver = cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_RECEIVER_ID));
        String mMessage = cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_MESSAGE));

        return new ModelForMessageList(mMessageId, mSenderId, mReceiver, mMessage);
    }
}
