package com.example.gauravjayasawal.midnightbuyer.Fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gauravjayasawal.midnightbuyer.Adapters.CartAdapter;
import com.example.gauravjayasawal.midnightbuyer.Information.InformationCart;
import com.example.gauravjayasawal.midnightbuyer.R;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CartFragment extends Fragment {
    public static String item[];
    public String quantity[];
    public String price[];

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_recycler_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cartRecyclerView);

        SharedPreferences sharedpref;
        sharedpref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final String cartConcatString = sharedpref.getString("cart", "");
        String a[] = cartConcatString.split(">");
        if (a.length == 0) {
            Toast.makeText(getActivity(), "The cart is empty", Toast.LENGTH_SHORT).show();
        } else {
            //initializing string
            item = new String[a.length-1];
            quantity = new String[a.length-1];
            price = new String[a.length-1];


            for (int i = 1; i < a.length; i++) {
                Log.d("AAAA","I1 "+a[i]);
                String b[] = a[i].split("\\.");
                Log.d("bvaluem"," "+b[0]);
                Log.d("bvaluem"," "+b[1]);
                Log.d("bvaluem"," "+b[2]);
                quantity[i - 1] = b[0];
                item[i - 1] = b[1];
                price[i - 1] = b[2];
            }
        }
        CartAdapter adapter = new CartAdapter(getActivity(), getdata());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public List<InformationCart> getdata() {
        List<InformationCart> data = new ArrayList<>();
        for (int j = 0; j < item.length; j++) {
            InformationCart current = new InformationCart();
            current.infoCartNameOfItem = item[j];
            current.infoCartQuantityOfItem = quantity[j];
            current.infoCartTotalPriceOfItem = String.valueOf(Integer.parseInt(price[j])*Integer.parseInt(quantity[j]));
            data.add(current);
        }
        return data;
    }
}