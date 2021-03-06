/*
 * Copyright (C) 2015 The CyanogenMod Project
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

package com.android.systemui.qs.tiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.internal.logging.MetricsConstants;
import com.android.systemui.R;
import com.android.systemui.qs.QSTile;

public class FastChargeTile extends QSTile<QSTile.BooleanState> {
    private static final String FAST_CHARGE_FILE = "/sys/kernel/fast_charge/force_fast_charge";

    public FastChargeTile(Host host) {
        super(host);
    }

    @Override
    protected BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        toggleState();
    }

    protected void toggleState() {
        String fastChargeState = FileUtils.readOneLine(FAST_CHARGE_FILE);
        if (fastChargeState != null) {
            boolean state = fastChargeState.contentEquals("1");
            FileUtils.writeLine(FAST_CHARGE_FILE, state ? "0" : "1");
        }
        refreshState();
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        String fastChargeState = FileUtils.readOneLine(FAST_CHARGE_FILE);

        state.visible = (fastChargeState != null);
        state.value = state.visible && (fastChargeState.contentEquals("1"));
        state.icon = ResourceIcon.get(state.value ?
                R.drawable.ic_qs_fastcharge_on : R.drawable.ic_qs_fastcharge_off);
        state.label = mContext.getString(state.value
                ? R.string.qs_tile_fastcharge : R.string.qs_tile_fastcharge_off);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsConstants.QS_PANEL;
    }

    @Override
    public void setListening(boolean listening) {
    }
}
