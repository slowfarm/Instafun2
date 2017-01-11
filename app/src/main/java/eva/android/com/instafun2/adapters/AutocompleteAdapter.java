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
    private ArrayList<Users> mCountry;
    private String mToken;

    public AutocompleteAdapter(Context context, int resource, String token) {
        super(context, resource);
        mCountry = new ArrayList<>();
        mToken = token;
    }

    @Override
    public int getCount() {
        return mCountry.size();
    }

    @Override
    public Users getItem(int position) {
        return mCountry.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    try{
                        //get data from the web
                        String term = constraint.toString();
                        mCountry = new UserTask(term, mToken).execute().get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    filterResults.values = mCountry;
                    filterResults.count = mCountry.size();
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

        return myFilter;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.auto_complete_layout,parent,false);
        Users country = mCountry.get(position);
        TextView countryName = (TextView) view.findViewById(R.id.countryName);
        countryName.setText(country.getName());
        return view;
    }
}