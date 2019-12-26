package com.srj.icsinspection.utils;



import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abc on 4/5/2018.
 */

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();


        // Default Values
        Map<String, Object> map = new HashMap<>();
        map.put(UpdateHelper.KEY_UPDATE_ENABLE, false);
        map.put(UpdateHelper.KEY_UPDATE_VERSION, "1.0");
        map.put(UpdateHelper.KEY_UPDATE_UPDATE_URL, "details?id=com.srj.icsinspection");

        remoteConfig.setDefaults(map);
        remoteConfig.fetch(5).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    remoteConfig.activateFetched();
                }
            }
        });


    }
}
