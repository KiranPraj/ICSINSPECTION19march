package com.srj.icsinspection.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


/**
 * Created by abc on 4/5/2018.
 */

public class UpdateHelper {
    private static final String TAG = "UpdateHelper";
    public static final String KEY_UPDATE_ENABLE = "is_update";
    public static final String KEY_UPDATE_VERSION = "version";
    public static final String KEY_UPDATE_UPDATE_URL = "update_url";


    private OnUpdateCheckListener mOnUpdateCheckListener;
    private Context mContext;

    public interface OnUpdateCheckListener {
        void CheckOnUpdateCheckListener(String uriApp);
    }

    public static Builder with(Context mContext) {
        return new Builder(mContext);
    }

    public void check() {

        FirebaseRemoteConfig mConfig = FirebaseRemoteConfig.getInstance();
        if (mConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            String currentVersion = mConfig.getString(KEY_UPDATE_VERSION);
            String appVersion = getAppVersion(mContext);
            String updateURL = mConfig.getString(KEY_UPDATE_UPDATE_URL);

            Log.i(TAG, "check: Internet is available");

            if (!TextUtils.equals(currentVersion, appVersion) && mOnUpdateCheckListener != null)
                mOnUpdateCheckListener.CheckOnUpdateCheckListener(updateURL);
        }
    }

    private String getAppVersion(Context mContext) {
        String result = "";
        try {
            result = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-z]|-", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    UpdateHelper(Context mContext, OnUpdateCheckListener mOnUpdateCheckListener) {
        this.mOnUpdateCheckListener = mOnUpdateCheckListener;
        this.mContext = mContext;
    }

    public static class Builder {
        private Context mContext;
        private OnUpdateCheckListener mOnUpdateCheckListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder onUpdateCheck(OnUpdateCheckListener mListener) {
            this.mOnUpdateCheckListener = mListener;
            return this;
        }

        public UpdateHelper Build() {
            return new UpdateHelper(mContext, mOnUpdateCheckListener);
        }

        public UpdateHelper check() {
            UpdateHelper mHelper = Build();
            mHelper.check();
            return mHelper;
        }
    }
}
