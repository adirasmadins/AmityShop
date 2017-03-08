package com.example.gauravjayasawal.midnightbuyer.Fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gauravjayasawal.midnightbuyer.Adapters.MainpageAdapter;
import com.example.gauravjayasawal.midnightbuyer.ExtraClasses.JSONParser;
import com.example.gauravjayasawal.midnightbuyer.Information.InformationMainPage;
import com.example.gauravjayasawal.midnightbuyer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainFragment extends Fragment  implements SearchView.OnQueryTextListener{

    private static final String SUGGEST_NEW = "http://frame.ueuo.com/midnightshop/suggestNew.php";
    public static String strSuggestion;
    public static String itemTitle[];
    public static String itemPrice[];
    public static String itemAvailability[];
    TextView suggest;
    EditText suggestion;
    JSONParser jsonParser = new JSONParser();
    MainpageAdapter adapter;
    SearchView searchView;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_recycler_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mainRecyclerView);
         adapter = new MainpageAdapter(getActivity(), getdata());
        Log.d("asdfasdfasdf", "" + adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        searchView =(SearchView)view.findViewById(R.id.searchView);
        setUpSearchView();

        suggest = (TextView) view.findViewById(R.id.suggestItems);
        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreator();
            }
        });
        return view;
    }

    private void dialogCreator() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.suggest_dialog_layout);
        dialog.setCancelable(true);
        dialog.show();
        suggestion = (EditText) dialog.findViewById(R.id.suggestionEditText);
        Button confirmSuggest = (Button) dialog.findViewById(R.id.confirmSuggestion);
        confirmSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MakeSuggestion().execute();
                strSuggestion = suggestion.getText().toString();
                dialog.dismiss();
            }
        });
        Button cancelSuggestDialog = (Button) dialog.findViewById(R.id.cancelSuggestionDialog);
        cancelSuggestDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public List<InformationMainPage> getdata() {
        List<InformationMainPage> data = new ArrayList<>();
        try {
            for (int j = 0; j < MainFragment.itemTitle.length; j++) {
                InformationMainPage current = new InformationMainPage();
                current.infoNameOfItem = MainFragment.itemTitle[j];
                current.infoPriceOfItem = MainFragment.itemPrice[j];
                current.infoAvailability = MainFragment.itemAvailability[j];
                data.add(current);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public class MakeSuggestion extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("work bitch", "yea1");

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> param1 = new ArrayList<>();
                param1.add(new BasicNameValuePair("itemName", strSuggestion));

                jsonParser.makeHttpRequest(SUGGEST_NEW, "POST", param1);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("work bitch", "yea");
            Toast.makeText(getActivity(), "Thank you for your suggestion.", Toast.LENGTH_SHORT).show();
        }
    }
    public void setUpSearchView(){
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

        searchView.setQueryHint("Search Here");

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.filter(newText);
        return true;
    }
}
