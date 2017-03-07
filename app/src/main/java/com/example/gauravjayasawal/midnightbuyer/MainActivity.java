package com.example.gauravjayasawal.midnightbuyer;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gauravjayasawal.midnightbuyer.ExtraClasses.JSONParser;
import com.example.gauravjayasawal.midnightbuyer.Fragment.MainFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PULL_ALL_ITEMS = "http://frame.ueuo.com/midnightshop/pullFoodItems.php";
    private static final String SHOP_STATUS = "http://frame.ueuo.com/midnightshop/shopOpenOrNot.php";
    static String shopOpen;
    JSONParser jsonParser = new JSONParser();
    static Toolbar toolbar;
    Context context;
    Menu mMenu;
    ArrayList<String> alNameOfItem = new ArrayList<>();
    ArrayList<String> alPriceOfItem = new ArrayList<>();
    ArrayList<String> alAvailabilityOfItem = new ArrayList<>();
    static TextView counter;
    TextView orderRecievedText;
    CardView cartImage;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_paster);

        TextView noInternet = (TextView) findViewById(R.id.noInternet);
        TextView refresh = (TextView) findViewById(R.id.refresh);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setLogo(R.drawable.logowhite);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbarCounterText);
        cartImage = (CardView) toolbar.findViewById(R.id.cardCart);

        SharedPreferences sharedpref;
        sharedpref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final String cartCount = sharedpref.getString("cartCount", "0");
        title.setText(cartCount);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
            noInternet.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.VISIBLE);
            cartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                }
            });
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MainActivity.this, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(in);
                }
            });
        } else {
            cartImage.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    SharedPreferences sharedpref;
                    sharedpref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    final String existingCart = sharedpref.getString("cart", "");
                    Log.d("Cart", "is " + existingCart);

                    Intent in = new Intent(MainActivity.this, CartMain.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(in);
                }
            });
            noInternet.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            cartImage.setClickable(false);
            new checkIfShopIsOpen().execute();
        }
    }

    public void changeToolbar(int a) {
        TextView title = (TextView) toolbar.findViewById(R.id.toolbarCounterText);
        title.setText(String.valueOf(a));
    }

    public class checkIfShopIsOpen extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("pullitems", "yes"));
            try {
                JSONObject json = jsonParser.makeHttpRequest(SHOP_STATUS, "POST", param);
                MainActivity.shopOpen = json.getString("a0");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (MainActivity.shopOpen.equals("0")) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_shop_closed);
                dialog.setCancelable(true);
                dialog.show();
                dialog.setCancelable(false);
                CardView exit = (CardView) dialog.findViewById(R.id.exitApplication);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            } else {
                new pullAllFoodItemsFromList().execute();
            }
        }
    }

    public class pullAllFoodItemsFromList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> param1 = new ArrayList<>();
                param1.add(new BasicNameValuePair("pullitems", "yes"));

                alNameOfItem.clear();
                alPriceOfItem.clear();
                alAvailabilityOfItem.clear();

                JSONObject json = jsonParser.makeHttpRequest(PULL_ALL_ITEMS, "POST", param1);

                for (int i = 0; i < json.length(); i++) {
                    alNameOfItem.add(json.getString("a" + i));
                    alPriceOfItem.add(json.getString("b" + i));
                    alAvailabilityOfItem.add(json.getString("c" + i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainFragment.itemTitle = new String[alNameOfItem.size()];
            MainFragment.itemPrice = new String[alPriceOfItem.size()];
            MainFragment.itemAvailability = new String[alAvailabilityOfItem.size()];

            Log.d("work bitch", "yea2" + alNameOfItem.get(0));
            Log.d("work bitch", "yea2" + alNameOfItem.get(0));
            Log.d("work bitch", "yea2" + alNameOfItem.get(0));

            MainFragment.itemTitle = alNameOfItem.toArray(new String[alNameOfItem.size()]);
            MainFragment.itemPrice = alPriceOfItem.toArray(new String[alPriceOfItem.size()]);
            MainFragment.itemAvailability = alAvailabilityOfItem.toArray(new String[alAvailabilityOfItem.size()]);
//            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("work bitch", "yea");
            MainFragment mainFragment = new MainFragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.mainRelativePaster, mainFragment, "asdf");
            try {
                transaction.commit();
            }catch (Exception e){

            }
                cartImage.setClickable(true);

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.cartActivity) {
            SharedPreferences sharedpref;
            sharedpref = PreferenceManager.getDefaultSharedPreferences(this);
            final String existingCart = sharedpref.getString("cart", "");
            Log.d("Cart", "is " + existingCart);

            Intent in = new Intent(MainActivity.this, CartMain.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }
}
