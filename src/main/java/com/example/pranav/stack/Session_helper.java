package com.example.pranav.stack;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by sid on 13/8/15.
 */
public class Session_helper {
    public static String TAG= Session_helper.class.getSimpleName();

    SharedPreferences preferences;
    Editor editor;
    Context _context;

    int PRIVATE_MODE=0;

    private static final String PREF_FILE_NAME="DroidLogin";
    private static final String KEYGEN="loggedkey";
    private static final String USER_NAME="username";
    private static final String IP_ADDR="ipaddr";

    public Session_helper(Context context){
        this._context=context;
        preferences=_context.getSharedPreferences(PREF_FILE_NAME,PRIVATE_MODE);
        editor=preferences.edit();
    }
    public void setLogin(boolean loggedin,String username, String ip_addr)
    {
        editor.putBoolean(KEYGEN,loggedin);
        editor.putString(USER_NAME, username);
        editor.putString(IP_ADDR,ip_addr);
        editor.commit();
        Log.d(TAG, "User login session");
    }

    public boolean isLogged(){
        return preferences.getBoolean(KEYGEN,false);
    }
    public String getUserName() {
        return preferences.getString(USER_NAME, null);
    }
    public String getIpAddr() {
        return preferences.getString(IP_ADDR,null);
    }


}
