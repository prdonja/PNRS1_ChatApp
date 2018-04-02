package stevan.popov;


public class ModelForMessageList {
    public String mMessage;
    public Boolean mColor;

    public ModelForMessageList(String mMessage, Boolean mColor) {
        this.mMessage = mMessage;
        this.mColor = mColor;
    }

    public String getmMessage() {

        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public Boolean getmColor() {
        return mColor;
    }

    public void setmColor(Boolean mColor) {
        this.mColor = mColor;
    }
}
