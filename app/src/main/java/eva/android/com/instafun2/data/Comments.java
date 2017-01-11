package eva.android.com.instafun2.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Comments implements Parcelable {
    public ArrayList<String> name;
    public ArrayList<String> text;
    public Comments(ArrayList<String> name, ArrayList<String> text) {
        this.name = name;
        this.text = text;
    }

    private Comments(Parcel in) {
        name = in.createStringArrayList();
        text = in.createStringArrayList();
    }

    public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(name);
        parcel.writeStringList(text);
    }
}
