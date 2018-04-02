package stevan.popov;

import android.graphics.drawable.Drawable;

/**
 * Created by steva on 4/1/2018.
 */

public class ModelForContactsList {
    public Drawable mImage;
    public String mName;
    public String mFirstCharacterOfName;

    public ModelForContactsList(Drawable mImige, String mName) {
        this.mImage = mImage;
        this.mName = mName;
        this.mFirstCharacterOfName = mName.substring(0, 1);
    }

    public Drawable getmImige() {
        return mImage;
    }

    public String getmName() {
        return mName;
    }

    public String getmFirstCharacterOfName() {
        return mFirstCharacterOfName;
    }

    public void setmImige(Drawable mImige) {
        this.mImage = mImige;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmFirstCharacterOfName(String mFirstCharacterOfName) {
        this.mFirstCharacterOfName = mFirstCharacterOfName;
    }
}
