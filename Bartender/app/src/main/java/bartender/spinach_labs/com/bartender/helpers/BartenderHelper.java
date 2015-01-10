package bartender.spinach_labs.com.bartender.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import bartender.spinach_labs.com.bartender.R;

/**
 * Created by vivek on 10/1/2015.
 */
public class BartenderHelper {

    public static void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 10);
        toast.show();
    }

    public static boolean setPrefrence(SharedPreferences pref, String key, String value) {
        try{
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key,value);
            editor.commit();
            return true;
        }catch (Exception ex){
            return false;
        }
    }


    public static String getPrefrence(SharedPreferences pref, String key) {
        try{
            return pref.getString(key,"");
        }catch (Exception ex){
            return "";
        }
    }

    public static String getGmailAccId(Context context){
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccounts();
        for (Account ac : accounts) {
            if(ac.type.equals("com.google")){
                return ac.name;
            }
        }
        return "";
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }

}
