# Amity Shop
This is the Amity Shop application. This readme contains information about the application and its features.

**Demo**

<img src="https://github.com/jgaurav6/AmityShop/blob/master/app/src/main/res/drawable/firstpage.png" width="330" height="550">
<img src="https://github.com/jgaurav6/AmityShop/blob/master/app/src/main/res/drawable/img1.png" width="330" height="550">
<img src="https://github.com/jgaurav6/AmityShop/blob/master/app/src/main/res/drawable/img4.png" width="330" height="550">
**Sample Code**

```Java
package com.example.gauravjayasawal.midnightbuyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Gaurav Jayasawal on 3/9/2017.
 */

public class TutorialClass extends AppCompatActivity {
    CardView next;
    TextView nextText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);

        SharedPreferences sharedpref;
        sharedpref = PreferenceManager.getDefaultSharedPreferences(TutorialClass.this);
        final String firstTime = sharedpref.getString("firstTime", "0");
        if (firstTime.equals("1")) {
            Intent in = new Intent(TutorialClass.this, MainActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(in);
        } else {
            final ImageView imageView = (ImageView) findViewById(R.id.tutorialImage);
            imageView.setImageResource(R.drawable.img2);
            next = (CardView) findViewById(R.id.cardNext);
            nextText = (TextView) findViewById(R.id.nextText);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageResource(R.drawable.firstpage);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageView.setImageResource(R.drawable.img1);
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imageView.setImageResource(R.drawable.img3);
                                    next.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            imageView.setImageResource(R.drawable.img4);
                                            next.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    imageView.setImageResource(R.drawable.img5);
                                                    next.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            imageView.setImageResource(R.drawable.img6);
                                                            next.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    imageView.setImageResource(R.drawable.img7);
                                                                    nextText.setText("FINISH");
                                                                    next.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TutorialClass.this);
                                                                            SharedPreferences.Editor edit = sharedPreferences.edit();
                                                                            edit.putString("firstTime", "1");
                                                                            edit.apply();
                                                                            Intent in = new Intent(TutorialClass.this, MainActivity.class);
                                                                            startActivity(in);
                                                                        }
                                                                    });
                                                                }
                                                            });


                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    });
                                }
                            });

                        }
                    });
                }
            });
        }
    }
}

```

***Download/Clone Project*** 

https://github.com/jgaurav6/AmityShop.git

***Download apk file***

https://github.com/jgaurav6/AmityShop/raw/master/app-debug.apk

**Services**

- [x] Buying from Shop 
- [x] Requesting new Items to be kept on shop
- [ ] Selling own items
