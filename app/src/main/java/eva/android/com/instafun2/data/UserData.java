package eva.android.com.instafun2.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class UserData implements Parcelable {

    public ArrayList<String> photoStandardResolution;
    public String maxId;
    public String username;
    public String userPhoto;

    public ArrayList<Comments> comments = new ArrayList<>();

    UserData(String userPhoto, String username, ArrayList<String> photoStandardResolution,
             String maxId, ArrayList<Comments> comments) {
        this.username = username;
        this.photoStandardResolution = photoStandardResolution;
        this.maxId = maxId;
        this.comments = comments;
        this.userPhoto = userPhoto;
    }

    private UserData(Parcel in) {
        photoStandardResolution = in.createStringArrayList();
        maxId = in.readString();
        username = in.readString();
        userPhoto = in.readString();
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
        this.photoStandardResolution.addAll(userdata.photoStandardResolution);
        this.comments.addAll(userdata.comments);
        this.maxId = userdata.maxId;
        this.userPhoto = userdata.userPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(photoStandardResolution);
        parcel.writeString(maxId);
        parcel.writeString(username);
        parcel.writeString(userPhoto);
        parcel.writeTypedList(comments);
    }
}
