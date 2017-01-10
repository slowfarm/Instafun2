package eva.android.com.instafun2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    AutoCompleteTextView mAutoCompleteTextView;
    ArrayList<String> name = new ArrayList<>();
    String token = "";
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        Intent intent = this.getIntent();
        String url = intent.getStringExtra("url");
        token = url.split("=")[1];

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, name);
        mAutoCompleteTextView.setAdapter(adapter);

        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name.clear();
                try {
                    name.addAll(new UserGetTask(charSequence, token).execute().get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
