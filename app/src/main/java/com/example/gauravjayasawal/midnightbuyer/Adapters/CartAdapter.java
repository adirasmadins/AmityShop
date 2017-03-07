package com.example.gauravjayasawal.midnightbuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gauravjayasawal.midnightbuyer.CartMain;
import com.example.gauravjayasawal.midnightbuyer.Information.InformationCart;
import com.example.gauravjayasawal.midnightbuyer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<InformationCart> data = Collections.emptyList();

    public CartAdapter(Context context, List<InformationCart> data) {
        Log.d("LOG", "" + context);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cart_final, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final InformationCart current = data.get(position);
        Log.d("asdfasdfasdf", "" + data.size());
        holder.cartFinalName.setText(current.infoCartNameOfItem);
        holder.cartFinalQuantity.setText(current.infoCartQuantityOfItem);
        holder.cartFinalPrice.setText(current.infoCartTotalPriceOfItem);
        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpref;
                sharedpref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor edit = sharedpref.edit();

                final String existingCart = sharedpref.getString("cart", "");

                String a[] = existingCart.split(">");

                List<String> list = new ArrayList<String>();

                for (int i = 1; i < a.length; i++) {
                    list.add(a[i]);
                }
                list.remove(position);
                notifyDataSetChanged();
                a = list.toArray(new String[list.size()]);

                String newCart = "";

                for (int i = 0; i < a.length; i++) {
                    Log.d("herum", "2 " + a[i]);
                }

                for (int i = 0; i < a.length; i++) {
                    newCart = newCart + ">" + a[i];
                }

                edit.putString("cart", newCart);
                edit.putString("cartCount", String.valueOf(getItemCount()-1));
                edit.apply();
                Toast.makeText(context, "Successfully removed item!!", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(context, CartMain.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(in);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartFinalName;
        TextView cartFinalQuantity;
        TextView cartFinalPrice;
        TextView removeText;
        CardView removeFromCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            removeText = (TextView) itemView.findViewById(R.id.removeText);
            removeFromCart = (CardView) itemView.findViewById(R.id.cardRemoveFromCart);
            cartFinalName = (TextView) itemView.findViewById(R.id.cartFinalName);
            cartFinalQuantity = (TextView) itemView.findViewById(R.id.cartFinalQuantity);
            cartFinalPrice = (TextView) itemView.findViewById(R.id.cartFinalPrice);
        }
    }
}
