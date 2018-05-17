package stevan.popov;


public class ModelForMessageList {
    private int mMessage_id;
    private String mSender_id;
    private int mReceiver_id;
    private String mMessage;

    public ModelForMessageList(int mMessage_id, String mSender_id, int mReceiver_id, String mMessage) {
        this.mMessage_id = mMessage_id;
        this.mSender_id = mSender_id;
        this.mReceiver_id = mReceiver_id;
        this.mMessage = mMessage;
    }

    public ModelForMessageList(String mSender_id, String mMessage) {
        this.mSender_id = mSender_id;
        this.mMessage = mMessage;
    }

    public int getmMessage_id() {
        return mMessage_id;
    }

    public void setmMessage_id(int mMessage_id) {
        this.mMessage_id = mMessage_id;
    }

    public String getmSender_id() {
        return mSender_id;
    }

    public void setmSender_id(String mSender_id) {
        this.mSender_id = mSender_id;
    }

    public int getmReceiver_id() {
        return mReceiver_id;
    }

    public void setmReceiver_id(int mReceiver_id) {
        this.mReceiver_id = mReceiver_id;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
