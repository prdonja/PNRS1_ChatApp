package stevan.popov;

import android.graphics.drawable.Drawable;


public class ModelForContactsList {
    public String mName;

    private int mContactId;
    private String mUsername;
    private String mFirstName;
    private String mLastName;

    public ModelForContactsList(String mUsername) {
        this.mUsername = mUsername;
    }

    public ModelForContactsList(int mContactId, String mUsername, String mFirstName, String mLastName) {
        this.mContactId = mContactId;
        this.mUsername = mUsername;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }

    public int getmContactId() {
        return mContactId;
    }

    public void setmContactId(int mContactId) {
        this.mContactId = mContactId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }


    public String getmName() {
        return mName;
    }



    public void setmName(String mName) {
        this.mName = mName;
    }

}
