/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.quicksettings;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.WifiDisplayStatus;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsController;
import com.android.systemui.statusbar.phone.QuickSettingsContainerView;

public class WiFiDisplayTile extends QuickSettingsTile{

    private boolean enabled = false;
    private boolean connected = false;
    public static QuickSettingsTile mInstance;

    public static QuickSettingsTile getInstance(Context context, LayoutInflater inflater,
            QuickSettingsContainerView container, final QuickSettingsController qsc, Handler handler, String id) {
        mInstance = null;
        mInstance = new WiFiDisplayTile(context, inflater, container, qsc);
        return mInstance;
    }

    public WiFiDisplayTile(Context context, LayoutInflater inflater,
            QuickSettingsContainerView container,
            QuickSettingsController qsc) {
        super(context, inflater, container, qsc);

        mOnClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                startSettingsActivity(android.provider.Settings.ACTION_WIFI_DISPLAY_SETTINGS);
            }
        };
        qsc.registerAction(DisplayManager.ACTION_WIFI_DISPLAY_STATUS_CHANGED, this);
        applyWiFiDisplayChanges();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiDisplayStatus status = (WifiDisplayStatus)intent.getParcelableExtra(DisplayManager.EXTRA_WIFI_DISPLAY_STATUS);
        enabled = status.getFeatureState() == WifiDisplayStatus.FEATURE_STATE_ON;
        connected = status.getActiveDisplay() != null;
        applyWiFiDisplayChanges();
    }

    private void applyWiFiDisplayChanges() {
        if(enabled && connected) {
            mLabel = mContext.getString(R.string.quick_settings_wifi_display_label);
            mDrawable = R.drawable.ic_qs_remote_display_connected;
        }else{
            mLabel = mContext.getString(R.string.quick_settings_wifi_display_no_connection_label);
            mDrawable = R.drawable.ic_qs_remote_display;
        }
        if(mTile != null) {
            updateQuickSettings();
        }
    }

    @Override
    void updateQuickSettings() {
        mTile.setVisibility(enabled ? View.VISIBLE : View.GONE);
        super.updateQuickSettings();
    }
}

