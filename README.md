# Amity Shop
This is the Amity Shop application. This readme contains information about the application and its features

**Demo**

![First Page](https://github.com/jgaurav6/AmityShop/blob/master/app/src/main/res/drawable/firstpage.png)
**Sample Code**
```Java
package sajha.com.sajha;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Gaurav Jayasawal on 6/12/2017.
 */

public class Form1 extends Fragment implements Step {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.economic_help, container, false);
        return view;

    }
}
```

***Download/Clone Project*** 

https://github.com/jgaurav6/AmityShop.git

***Download apk file***
https://github.com/jgaurav6/AmityShop/raw/master/app-debug.apk

   First Header | Second Header
------------ | -------------
itemr1c1 | itemr1c2
itemr2c1 | itemr2c2

**Services**

- [x] Buying from Shop 
- [x] Requesting new Items to be kept on shop
- [ ] Selling own items
