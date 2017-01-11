package eva.android.com.instafun2.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class UserData implements Parcelable {
    public ArrayList<String> photoLowResolution;
    public ArrayList<String> photoStandardResolution;
    public String maxId;
    public String username;

    public ArrayList<Comments> comments = new ArrayList<>();

    public UserData(String username, ArrayList<String> photoLowResolution, ArrayList<String> photoStandardResolution,String maxId, ArrayList<Comments> comments) {
        this.username = username;
        this.photoLowResolution = photoLowResolution;
        this.photoStandardResolution = photoStandardResolution;
        this.maxId = maxId;
        this.comments = comments;
    }

    protected UserData(Parcel in) {
        photoLowResolution = in.createStringArrayList();
        photoStandardResolution = in.createStringArrayList();
        maxId = in.readString();
        username = in.readString();
        comments = in.createTypedArrayList(Comments.CREATOR);
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public void add(UserData userdata) {
        this.username = userdata.username;
        this.photoLowResolution.addAll(userdata.photoLowResolution);
        this.photoStandardResolution.addAll(userdata.photoStandardResolution);
        this.comments.addAll(userdata.comments);
        this.maxId = userdata.maxId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(photoLowResolution);
        parcel.writeStringList(photoStandardResolution);
        parcel.writeString(maxId);
        parcel.writeString(username);
        parcel.writeTypedList(comments);
    }
}
