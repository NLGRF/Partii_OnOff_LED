package th.or.nectec.partii.ledonoff.partii_onoff_led_android;

import android.app.Application;

import com.karumi.dexter.Dexter;

/**
 * Created by mootorn on 7/15/2017 AD.
 */

public class Permission extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(getApplicationContext());
    }

}