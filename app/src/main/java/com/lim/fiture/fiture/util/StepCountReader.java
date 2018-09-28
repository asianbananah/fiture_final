package com.lim.fiture.fiture.util;

import android.content.Context;
import android.util.Log;

import com.lim.fiture.fiture.activities.MainActivity;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthDataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by User on 27/05/2018.
 */

public class StepCountReader {

    public static final String STEP_SUMMARY_DATA_TYPE_NAME = "com.samsung.shealth.step_daily_trend";

    public static final long TODAY_START_UTC_TIME;
    public static final long MONTH_START_UTC_TIME;
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    private static final String ALIAS_TOTAL_COUNT = "count";
    private static final String ALIAS_DEVICE_UUID = "deviceuuid";

    private final HealthDataResolver mResolver;
    private final Context mContext;

    private static final String TAG = "stepCountReaderTag";

    public StepCountReader(HealthDataStore store, Context context) {
        mResolver = new HealthDataResolver(store, null);
        mContext = context;
    }

    static {
        TODAY_START_UTC_TIME = getTodayStartUtcTime();
        MONTH_START_UTC_TIME = getMonthStartUtcTime();
    }

    private static long getTodayStartUtcTime() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Log.d(TAG, "Today : " + today.getTimeInMillis());

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    private static long getMonthStartUtcTime(){
        Calendar firstDayofMonth = Calendar.getInstance(TimeZone.getTimeZone("GMT+0800"));
        Log.d("stepCheck", "First day of month : " + firstDayofMonth.getTimeInMillis());

        firstDayofMonth.set(Calendar.DAY_OF_MONTH,1);
        firstDayofMonth.set(Calendar.HOUR_OF_DAY,0);
        firstDayofMonth.set(Calendar.MINUTE,0);
        firstDayofMonth.set(Calendar.SECOND,0);
        firstDayofMonth.set(Calendar.MILLISECOND,0);
        Log.d("stepCheck", "First day in mill: " + firstDayofMonth.getTimeInMillis());
        return firstDayofMonth.getTimeInMillis();
    }

    public void requestMonthlyStepCount(final long startTime) {

        // Get sum of step counts by device
        HealthDataResolver.AggregateRequest request = new HealthDataResolver.AggregateRequest.Builder()
                .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                .addFunction(HealthDataResolver.AggregateRequest.AggregateFunction.SUM, HealthConstants.StepCount.COUNT, ALIAS_TOTAL_COUNT)
                .addGroup(HealthConstants.StepCount.DEVICE_UUID, ALIAS_DEVICE_UUID)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        startTime, TODAY_START_UTC_TIME + ONE_DAY)
                .setSort(ALIAS_TOTAL_COUNT, HealthDataResolver.SortOrder.DESC)
                .build();

        try {
            mResolver.aggregate(request).setResultListener(result -> {
                int totalCount = 0;
                String deviceUuid = null;
                int counter = 0;

                try {
                    Iterator<HealthData> iterator = result.iterator();
                    if (iterator.hasNext()) {
                        HealthData data = iterator.next();
                        totalCount = data.getInt(ALIAS_TOTAL_COUNT);
                        Log.d("stepCheck", "stepCheck" + counter++ + ": " +totalCount );
                        deviceUuid = data.getString(ALIAS_DEVICE_UUID);
                    }
                } finally {
                    ((MainActivity)mContext).updateTotalMonthlyStepsView(totalCount);
                    result.close();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Getting step count fails.", e);
        }
    }
}
