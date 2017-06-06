package com.example.mandy.savingsmanager.manager;

import android.util.Log;

import com.example.mandy.savingsmanager.data.SavingsBean;
import com.example.mandy.savingsmanager.utils.Constants;
import com.example.mandy.savingsmanager.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zzhen on 5/16/2017.
 */

public class DataManager {

    /**
     * Get the recent due savings item
     *
     * @param list all the savings
     * @return date or null
     */
    public static Date getNextDueSavingsItemDate(ArrayList<SavingsBean> list) {

        long due = Long.MAX_VALUE;
        long endTime;
        // small algorithm to get the due savings item in the future
        for (SavingsBean savings : list) {
            endTime = savings.getEndDate();
            if (endTime < due
                    && endTime > new Date().getTime()) {
                // when end date is before today, don't treat it as a due savings since it's a past one
                due = savings.getEndDate();
            }
        }

        if (due != Long.MAX_VALUE) {
            Log.d(Constants.LOG_TAG, "Next due savings item is: " + Utils.formatDate(due));
            return new Date(due);
        } else {
            Log.d(Constants.LOG_TAG, "No due savings item in the future");
            return null;
        }

    }

}
