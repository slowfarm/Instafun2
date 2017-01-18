package eva.android.com.instafun2.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.dataSources.UserTask;
import eva.android.com.instafun2.data.Users;

public class AutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<Users> mUsers;
    private String mToken;

    public AutocompleteAdapter(Context context, int resource, String token) {
        super(context, resource);
        mUsers = new ArrayList<>();
        mToken = token;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Users getItem(int position) {
        return mUsers.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    try{
                        //get data from the web
                        String term = constraint.toString();
                        mUsers = new UserTask(term, mToken).execute().get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    filterResults.values = mUsers;
                    filterResults.count = mUsers.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.auto_complete_layout,parent,false);
        Users users = mUsers.get(position);
        TextView username = (TextView) view.findViewById(R.id.username);
        username.setText(users.getName());
        return view;
    }
}