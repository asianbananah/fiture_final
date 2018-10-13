package com.lim.fiture.fiture.util;

import com.lim.fiture.fiture.models.User;

/**
 * Created by User on 13/10/2018.
 */

public class GlobalUser {
    private static User mUser;

    public GlobalUser() {
    }

    public static User getmUser() {
        return mUser;
    }

    public static void setmUser(User mUser) {
        GlobalUser.mUser = mUser;
    }
}
