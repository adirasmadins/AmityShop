package com.example.gauravjayasawal.midnightbuyer;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gauravjayasawal.midnightbuyer.ExtraClasses.JSONParser;
import com.example.gauravjayasawal.midnightbuyer.Fragment.CartFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Jayasawal on 2/23/2017.
 */

public class CartMain extends AppCompatActivity implements TextWatcher {

    String SEND_NOTIFICATION_TO_USER = "http://frame.ueuo.com/images/NotificationFirebaseShop/send_notification_shop.php";

    private static final String PLACE_ORDER = "http://frame.ueuo.com/midnightshop/placeOrder.php";
    Toolbar toolbar;
    JSONParser jsonParser = new JSONParser();
    static String countTotal;
    String item[];
    String quantity[];
    String finalPrice[];
    private ProgressDialog pdialog;

    TextView checkOut;
    EditText name;
    EditText phoneNumber;
    EditText hostelBlock;
    EditText hostelFloor;
    EditText hostelSuite;
    EditText hostelRoom;
    CheckBox rememberDetails;

    String sname;
    String sPhoneNumber;
    String sHostelBlock;
    String sHostelFloor;
    String sHostelSuite;
    String sHostelRoom;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_paster);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
//        toolbar.setLogo(R.drawable.logowhite);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CartFragment cartFragment = new CartFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.cartRelativePaster, cartFragment, "asdf");
        transaction.commit();

        checkOut = (TextView) findViewById(R.id.checkOut);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CartFragment.item.length == 0) {
                    Toast.makeText(CartMain.this, "The cart is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    finalDialogCreator();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(CartMain.this, MainActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(in);
    }

    private void finalDialogCreator() {
        final Dialog dialog = new Dialog(CartMain.this);
        dialog.setContentView(R.layout.checkout_dialog_layout);
        dialog.setCancelable(true);
        dialog.show();

        name = (EditText) dialog.findViewById(R.id.checkOutName);
        phoneNumber = (EditText) dialog.findViewById(R.id.checkOutPhoneNumber);
        hostelBlock = (EditText) dialog.findViewById(R.id.checkoutHostelAddressBlock);
        hostelFloor = (EditText) dialog.findViewById(R.id.checkoutHostelAddressFloor);
        hostelSuite = (EditText) dialog.findViewById(R.id.checkoutHostelAddressSuite);
        hostelRoom = (EditText) dialog.findViewById(R.id.checkoutHostelAddressRoom);
        rememberDetails = (CheckBox) dialog.findViewById(R.id.rememberCheckBox);

        SharedPreferences sharedpref;
        sharedpref = PreferenceManager.getDefaultSharedPreferences(CartMain.this);

        final String sharedTry = sharedpref.getString("Name", "");
        Log.d("Name", "Shared" + sharedTry);

        if (sharedTry.length() > 0) {
            name.setText(sharedpref.getString("Name", ""));
            phoneNumber.setText(sharedpref.getString("PhoneNumber", ""));
            hostelBlock.setText(sharedpref.getString("HostelBlock", ""));
            hostelFloor.setText(sharedpref.getString("HostelFloor", ""));
            hostelSuite.setText(sharedpref.getString("HostelSuite", ""));
            hostelRoom.setText(sharedpref.getString("HostelRoom", ""));
            rememberDetails.setVisibility(View.GONE);
        }

        name.addTextChangedListener(this);
        phoneNumber.addTextChangedListener(this);
        hostelBlock.addTextChangedListener(this);
        hostelFloor.addTextChangedListener(this);
        hostelSuite.addTextChangedListener(this);
        hostelRoom.addTextChangedListener(this);

        CardView confirm = (CardView) dialog.findViewById(R.id.placeOrderFinal);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sname = name.getText().toString();
                sPhoneNumber = phoneNumber.getText().toString();
                sHostelBlock = hostelBlock.getText().toString();
                sHostelFloor = hostelFloor.getText().toString();
                sHostelSuite = hostelSuite.getText().toString();
                sHostelRoom = hostelRoom.getText().toString();

                if (rememberDetails.isChecked()) {
                    ////// SHARED PREFERENCE MA STORE GARNE

                    SharedPreferences sharedpref;
                    sharedpref = PreferenceManager.getDefaultSharedPreferences(CartMain.this);
                    String cart = sharedpref.getString("cart", "");
                    Log.d("Cart", "is1" + cart);
                    SharedPreferences.Editor edit = sharedpref.edit();
                    edit.clear();
                    edit.putString("cart", cart);
                    edit.putString("Name", sname);
                    edit.putString("PhoneNumber", sPhoneNumber);
                    edit.putString("HostelBlock", sHostelBlock);
                    edit.putString("HostelFloor", sHostelFloor);
                    edit.putString("HostelSuite", sHostelSuite);
                    edit.putString("HostelRoom", sHostelRoom);
                    edit.apply();
                }

//                Make asyntask
                new PlaceFinalOrder().execute();

                dialog.dismiss();
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        rememberDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public class PlaceFinalOrder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("work bitch", "yea1");

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                SharedPreferences sharedpref;
                sharedpref = PreferenceManager.getDefaultSharedPreferences(CartMain.this);
                SharedPreferences.Editor edit = sharedpref.edit();
                edit.clear();
                final String existingCart = sharedpref.getString("cart", "");
                String a[] = existingCart.split(">");

                item = new String[a.length - 1];
                quantity = new String[a.length - 1];
                finalPrice = new String[a.length - 1];
                for (int i = 1; i < a.length; i++) {
                    String b[] = a[i].split("\\.");
                    item[i - 1] = b[1];
                    quantity[i - 1] = b[0];
                    finalPrice[i - 1] = String.valueOf(Integer.parseInt(b[2]) * Integer.parseInt(b[0]));
                }
                Log.d("Cart TOTALGG","c " +item.length);
                countTotal = String.valueOf(item.length);

                for (int i = 0; i < item.length; i++) {
                    Log.d("Value of", "a1 " + item[i]);
                }
                for (int i = 0; i < item.length; i++) {
                    Log.d("Value of", "a2 " + quantity[i]);
                }
                for (int i = 0; i < item.length; i++) {
                    Log.d("Value of", "a3 " + finalPrice[i]);
                }

                for (int i = 0; i < item.length; i++) {
                    List<NameValuePair> param1 = new ArrayList<>();
                    param1.add(new BasicNameValuePair("itemName", item[i]));
                    param1.add(new BasicNameValuePair("quantity", quantity[i]));
                    param1.add(new BasicNameValuePair("totalPrice", finalPrice[i]));
                    param1.add(new BasicNameValuePair("nameOfBuyer", sname));
                    param1.add(new BasicNameValuePair("phoneNumber", sPhoneNumber));
                    param1.add(new BasicNameValuePair("hostelAddress", sHostelBlock + "-" + sHostelFloor + "-" + sHostelSuite + "-" + sHostelRoom));

                    jsonParser.makeHttpRequest(PLACE_ORDER, "POST", param1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new SendNotification().execute();
            final Dialog dialog = new Dialog(CartMain.this);
            dialog.setContentView(R.layout.order_placed_dialog);
            dialog.show();
            dialog.setCancelable(false);
            CardView ok = (CardView) dialog.findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedpref;
                    sharedpref = PreferenceManager.getDefaultSharedPreferences(CartMain.this);
                    SharedPreferences.Editor edit = sharedpref.edit();
                    edit.putString("cart", "");
                    edit.putString("cartCount", "0");
                    edit.apply();
                    Intent in = new Intent(CartMain.this, CartMain.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(in);
                }
            });
        }
    }

    class SendNotification extends AsyncTask<String, String, String> {
        List<NameValuePair> params1 = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {

            Log.d("Cart TOTALGG","c 2" +countTotal);

            params1.add(new BasicNameValuePair("user", "shopAdmin"));
            params1.add(new BasicNameValuePair("message", "NEW ORDER OF " + countTotal + " ITEMS"));
            params1.add(new BasicNameValuePair("chatSendersArray", "NEW ORDER OF " + countTotal + " ITEMS"));

            jsonParser.makeHttpRequest(SEND_NOTIFICATION_TO_USER, "POST", params1);
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}