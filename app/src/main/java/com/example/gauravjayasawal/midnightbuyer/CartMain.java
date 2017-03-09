package com.example.gauravjayasawal.midnightbuyer;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
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

    String blockValidity = "0", floorValidity = "0", roomValidity = "0", numberValidity = "0", suiteValidity = "0";

    TextView checkOut;
    EditText name;
    EditText phoneNumber;
    TextView phoneNumberValidationText;
    ImageView phoneNumberValidationImage;
    EditText hostelBlock;
    TextView hostelBlockValidationText;
    ImageView hostelBlockValidationImage;
    EditText hostelFloor;
    TextView hostelFloorValidationText;
    ImageView hostelFloorValidationImage;
    EditText hostelSuite;
    TextView hostelSuiteValidationText;
    ImageView hostelSuiteValidationImage;
    EditText hostelRoom;
    TextView hostelRoomValidationText;
    ImageView hostelRoomValidationImage;
    CheckBox rememberDetails;
    TextView allFieldsRequired;

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

        allFieldsRequired = (TextView) dialog.findViewById(R.id.allFieldsRequired);
        phoneNumberValidationText = (TextView) dialog.findViewById(R.id.phoneNumberValidation);
        phoneNumberValidationImage = (ImageView) dialog.findViewById(R.id.phoneNumberValidationImage);
        hostelBlockValidationText = (TextView) dialog.findViewById(R.id.blockValidation);
        hostelBlockValidationImage = (ImageView) dialog.findViewById(R.id.hostelBlockValidationImage);
        hostelFloorValidationText = (TextView) dialog.findViewById(R.id.floorValidation);
        hostelFloorValidationImage = (ImageView) dialog.findViewById(R.id.hostelFloorValidationImage);
        hostelSuiteValidationText = (TextView) dialog.findViewById(R.id.suiteValidation);
        hostelSuiteValidationImage = (ImageView) dialog.findViewById(R.id.hostelSuiteValidationImage);
        hostelRoomValidationText = (TextView) dialog.findViewById(R.id.roomValidation);
        hostelRoomValidationImage = (ImageView) dialog.findViewById(R.id.hostelRoomValidationImage);

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

            validateFields();
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
                if (blockValidity.equals("1") && roomValidity.equals("1") && numberValidity.equals("1") && floorValidity.equals("1") && name.getText().toString().length() > 0 && suiteValidity.equals("1")) {
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
                } else {
                    allFieldsRequired.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void validateFields() {
        hostelBlockValidationText.setText("Valid Hostel Block");
        hostelBlockValidationText.setTextColor(Color.parseColor("#67C100"));
        hostelBlockValidationImage.setVisibility(View.VISIBLE);
        blockValidity = "1";
        hostelBlockValidationImage.setImageResource(R.drawable.tick_green);
        hostelBlockValidationText.setVisibility(View.VISIBLE);

        hostelFloorValidationText.setTextColor(Color.parseColor("#67C100"));
        hostelFloorValidationText.setVisibility(View.VISIBLE);
        hostelFloorValidationText.setText("Valid Floor");
        floorValidity = "1";
        hostelFloorValidationImage.setImageResource(R.drawable.tick_green);
        hostelFloorValidationImage.setVisibility(View.VISIBLE);

        hostelRoomValidationText.setTextColor(Color.parseColor("#67C100"));
        hostelRoomValidationText.setVisibility(View.VISIBLE);
        hostelRoomValidationText.setText("Valid Room");
        roomValidity = "1";
        hostelRoomValidationImage.setImageResource(R.drawable.tick_green);
        hostelRoomValidationImage.setVisibility(View.VISIBLE);

        phoneNumberValidationText.setText("Valid Format");
        numberValidity = "1";
        phoneNumberValidationText.setVisibility(View.VISIBLE);
        phoneNumberValidationText.setTextColor(Color.parseColor("#67C100"));
        phoneNumberValidationImage.setImageResource(R.drawable.tick_green);
        phoneNumberValidationImage.setVisibility(View.VISIBLE);

        hostelSuiteValidationText.setText("Valid Suite");
        suiteValidity = "1";
        hostelSuiteValidationText.setVisibility(View.VISIBLE);
        hostelSuiteValidationText.setTextColor(Color.parseColor("#67C100"));
        hostelSuiteValidationImage.setImageResource(R.drawable.tick_green);
        hostelSuiteValidationImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        rememberDetails.setVisibility(View.VISIBLE);
        allFieldsRequired.setVisibility(View.INVISIBLE);
        if (hostelBlock.getText().toString().length() > 0) {
            if (hostelBlock.getText().toString().equals("B") | hostelBlock.getText().toString().equals("b") | hostelBlock.getText().toString().equals("b1") | hostelBlock.getText().toString().equals("a") | hostelBlock.getText().toString().equals("B1") | hostelBlock.getText().toString().equals("A")) {
                hostelBlockValidationText.setText("Valid Hostel Block");
                hostelBlockValidationText.setTextColor(Color.parseColor("#67C100"));
                blockValidity = "1";
                hostelBlockValidationImage.setVisibility(View.VISIBLE);
                hostelBlockValidationImage.setImageResource(R.drawable.tick_green);
                hostelBlockValidationText.setVisibility(View.VISIBLE);
            } else {
                if (hostelBlock.getText().toString().equals("D") | hostelBlock.getText().toString().equals("d") | hostelBlock.getText().toString().equals("d1") | hostelBlock.getText().toString().equals("D1") | hostelBlock.getText().toString().equals("C") | hostelBlock.getText().toString().equals("c") | hostelBlock.getText().toString().equals("E") | hostelBlock.getText().toString().equals("e") | hostelBlock.getText().toString().equals("E1") | hostelBlock.getText().toString().equals("F") | hostelBlock.getText().toString().equals("e1") | hostelBlock.getText().toString().equals("f")) {
                    hostelBlockValidationText.setTextColor(Color.parseColor("#D72828"));
                    hostelBlockValidationText.setText("Sorry! Hostel Block Not Supported Yet");
                    hostelBlockValidationText.setVisibility(View.VISIBLE);
                    blockValidity = "0";
                    hostelBlockValidationImage.setImageResource(R.drawable.errorpass);
                    hostelBlockValidationText.setVisibility(View.VISIBLE);
                } else {
                    hostelBlockValidationText.setText("Invalid Hostel Block");
                    hostelBlockValidationText.setTextColor(Color.parseColor("#D72828"));
                    hostelBlockValidationText.setVisibility(View.VISIBLE);
                    blockValidity = "0";
                    hostelBlockValidationImage.setVisibility(View.VISIBLE);
                    hostelBlockValidationImage.setImageResource(R.drawable.errorpass);
                }
            }
        } else {
            hostelBlockValidationImage.setVisibility(View.INVISIBLE);
            blockValidity = "0";
            hostelBlockValidationText.setVisibility(View.INVISIBLE);
        }
        if (hostelFloor.getText().toString().length() > 0) {
            if (hostelFloor.getText().toString().equals("1") | hostelFloor.getText().toString().equals("2") | hostelFloor.getText().toString().equals("3") | hostelFloor.getText().toString().equals("4") | hostelFloor.getText().toString().equals("5")) {
                hostelFloorValidationText.setTextColor(Color.parseColor("#67C100"));
                hostelFloorValidationText.setVisibility(View.VISIBLE);
                hostelFloorValidationText.setText("Valid Floor");
                floorValidity = "1";
                hostelFloorValidationImage.setImageResource(R.drawable.tick_green);
                hostelFloorValidationImage.setVisibility(View.VISIBLE);
            } else {
                hostelFloorValidationText.setTextColor(Color.parseColor("#D72828"));
                hostelFloorValidationText.setVisibility(View.VISIBLE);
                hostelFloorValidationText.setText("Invalid Floor");
                floorValidity = "0";
                hostelFloorValidationImage.setImageResource(R.drawable.errorpass);
                hostelFloorValidationImage.setVisibility(View.VISIBLE);
            }
        } else {
            floorValidity = "0";
            hostelFloorValidationText.setVisibility(View.INVISIBLE);
            hostelFloorValidationImage.setVisibility(View.INVISIBLE);
        }
        if (hostelRoom.getText().toString().length() > 0) {
            if (hostelRoom.getText().toString().equals("1") | hostelRoom.getText().toString().equals("2") | hostelRoom.getText().toString().equals("3")) {
                hostelRoomValidationText.setTextColor(Color.parseColor("#67C100"));
                hostelRoomValidationText.setVisibility(View.VISIBLE);
                hostelRoomValidationText.setText("Valid Room");
                roomValidity = "1";
                hostelRoomValidationImage.setImageResource(R.drawable.tick_green);
                hostelRoomValidationImage.setVisibility(View.VISIBLE);
            } else {
                hostelRoomValidationText.setTextColor(Color.parseColor("#D72828"));
                hostelRoomValidationText.setVisibility(View.VISIBLE);
                hostelRoomValidationText.setText("Invalid Room");
                roomValidity = "0";
                hostelRoomValidationImage.setImageResource(R.drawable.errorpass);
                hostelRoomValidationImage.setVisibility(View.VISIBLE);
            }
        } else {
            hostelRoomValidationText.setVisibility(View.INVISIBLE);
            hostelRoomValidationImage.setVisibility(View.INVISIBLE);
            roomValidity = "0";
        }

        if (phoneNumber.getText().toString().length() > 0) {
            if (phoneNumber.getText().toString().length() > 10) {
                phoneNumberValidationText.setText("10 digit number only");
                phoneNumberValidationText.setVisibility(View.VISIBLE);
                phoneNumberValidationText.setTextColor(Color.parseColor("#D72828"));
                numberValidity = "0";
                phoneNumberValidationImage.setImageResource(R.drawable.errorpass);
                phoneNumberValidationImage.setVisibility(View.VISIBLE);
            } else {
                if (phoneNumber.getText().toString().matches("^[0-9]*$")) {
                    if (phoneNumber.getText().toString().length() < 10) {
                        phoneNumberValidationText.setText("Too short");
                        phoneNumberValidationText.setVisibility(View.VISIBLE);
                        numberValidity = "0";
                        phoneNumberValidationText.setTextColor(Color.parseColor("#D72828"));
                        phoneNumberValidationImage.setImageResource(R.drawable.errorpass);
                        phoneNumberValidationImage.setVisibility(View.VISIBLE);
                    } else {
                        phoneNumberValidationText.setText("Valid Format");
                        phoneNumberValidationText.setVisibility(View.VISIBLE);
                        phoneNumberValidationText.setTextColor(Color.parseColor("#67C100"));
                        numberValidity = "1";
                        phoneNumberValidationImage.setImageResource(R.drawable.tick_green);
                        phoneNumberValidationImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    phoneNumberValidationText.setText("10 digit number only");
                    phoneNumberValidationText.setVisibility(View.VISIBLE);
                    phoneNumberValidationText.setTextColor(Color.parseColor("#D72828"));
                    numberValidity = "0";
                    phoneNumberValidationImage.setImageResource(R.drawable.errorpass);
                    phoneNumberValidationImage.setVisibility(View.VISIBLE);
                }
            }
        } else {
            numberValidity = "0";
            phoneNumberValidationText.setVisibility(View.INVISIBLE);
            phoneNumberValidationImage.setVisibility(View.INVISIBLE);
        }
        if (hostelSuite.getText().toString().length() > 0) {
            if (hostelSuite.getText().toString().matches("^[0-9]*$")) {
                if (Integer.parseInt(hostelSuite.getText().toString()) < 14) {
                    hostelSuiteValidationText.setText("Valid Suite");
                    hostelSuiteValidationText.setVisibility(View.VISIBLE);
                    hostelSuiteValidationText.setTextColor(Color.parseColor("#67C100"));
                    suiteValidity = "1";
                    hostelSuiteValidationImage.setImageResource(R.drawable.tick_green);
                    hostelSuiteValidationImage.setVisibility(View.VISIBLE);
                } else {
                    hostelSuiteValidationText.setText("Invalid Suite");
                    hostelSuiteValidationText.setVisibility(View.VISIBLE);
                    hostelSuiteValidationText.setTextColor(Color.parseColor("#D72828"));
                    suiteValidity = "0";
                    hostelSuiteValidationImage.setImageResource(R.drawable.errorpass);
                    hostelSuiteValidationImage.setVisibility(View.VISIBLE);
                }
            } else {
                hostelSuiteValidationText.setText("Invalid Format");
                hostelSuiteValidationText.setVisibility(View.VISIBLE);
                hostelSuiteValidationText.setTextColor(Color.parseColor("#D72828"));
                suiteValidity = "0";
                hostelSuiteValidationImage.setImageResource(R.drawable.errorpass);
                hostelSuiteValidationImage.setVisibility(View.VISIBLE);
            }
        } else {
            suiteValidity = "0";
            hostelSuiteValidationText.setVisibility(View.INVISIBLE);
            hostelSuiteValidationImage.setVisibility(View.INVISIBLE);
        }

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
                Log.d("Cart TOTALGG", "c " + item.length);
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

            Log.d("Cart TOTALGG", "c 2" + countTotal);

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