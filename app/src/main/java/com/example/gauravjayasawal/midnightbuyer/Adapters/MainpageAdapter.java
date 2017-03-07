package com.example.gauravjayasawal.midnightbuyer.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gauravjayasawal.midnightbuyer.Information.InformationMainPage;
import com.example.gauravjayasawal.midnightbuyer.MainActivity;
import com.example.gauravjayasawal.midnightbuyer.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class MainpageAdapter extends RecyclerView.Adapter<MainpageAdapter.MyViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<InformationMainPage> data = Collections.emptyList();

    public static String sItemName;
    public static String sPrice;

    public MainpageAdapter(Context context, List<InformationMainPage> data) {
        Log.d("LOG", "" + context);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_buyer, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final InformationMainPage current = data.get(position);
        Log.d("asdfasdfasdf", "" + data.size());
        holder.tvNameOfItem.setText(current.infoNameOfItem);
        holder.tvPriceOfItem.setText(current.infoPriceOfItem);
        String a = current.infoNameOfItem.replaceAll("\\s", "");

        Picasso.with(context).load("http://aadeshrana.esy.es/" + a + current.infoPriceOfItem + ".jpg").fit().placeholder(R.drawable.noimageplaceholder).into(holder.imageMain);


        if (current.infoAvailability.equals("0")) {
            holder.tvAvailabilityOfItem.setText("OUT OF STOCK");
        } else {
            holder.tvAvailabilityOfItem.setText("AVAILABLE");
        }
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current.infoAvailability.equals("0")) {
                    Toast.makeText(context, "Sorry!! item unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    int tempcnt = Integer.parseInt(holder.itemCount.getText().toString());
                    holder.itemCount.setText(String.valueOf(tempcnt + 1));
                }
            }
        });
        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current.infoAvailability.equals("0")) {
                    Toast.makeText(context, "Sorry!! item unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    int tempcnt = Integer.parseInt(holder.itemCount.getText().toString());
                    if (tempcnt <= 0) {
                        holder.itemCount.setText("0");
                    } else
                        holder.itemCount.setText(String.valueOf(tempcnt - 1));
                }
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempcnt1 = holder.itemCount.getText().toString();
                Log.d("value ", "is" + tempcnt1);
                if (tempcnt1.equals("0")) {
                    Toast.makeText(context, "You cannot add 0 quantity", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences sharedpref;
                    sharedpref = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sharedpref.edit();
                    final String existingCart = sharedpref.getString("cart", "");
                    edit.putString("cart", existingCart + ">" + tempcnt1 + "." + current.infoNameOfItem + "." + current.infoPriceOfItem);
                    final String existingCount = sharedpref.getString("cartCount", "0");
                    int neCount = Integer.parseInt(existingCount) + 1;
                    edit.putString("cartCount", String.valueOf(neCount));
                    edit.apply();

                    Toast.makeText(context, "Successfully added to cart", Toast.LENGTH_SHORT).show();

                    MainActivity ma = new MainActivity();
                    ma.changeToolbar(neCount);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameOfItem;
        TextView tvPriceOfItem;
        TextView tvAvailabilityOfItem;
        CardView cvItem;
        Button add;
        Button subtract;
        ImageView imageMain;
        TextView itemCount;
        CardView addToCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageMain = (ImageView) itemView.findViewById(R.id.imageBuyer);
            addToCart = (CardView) itemView.findViewById(R.id.addToCart);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);
            tvNameOfItem = (TextView) itemView.findViewById(R.id.titleOfItem);
            tvPriceOfItem = (TextView) itemView.findViewById(R.id.priceOfItem);
            tvAvailabilityOfItem = (TextView) itemView.findViewById(R.id.availabilityOfItem);
            add = (Button) itemView.findViewById(R.id.addButton);
            subtract = (Button) itemView.findViewById(R.id.subtractButton);
            itemCount = (TextView) itemView.findViewById(R.id.tvCount);
        }
    }
}