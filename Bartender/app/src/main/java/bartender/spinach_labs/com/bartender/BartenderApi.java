package bartender.spinach_labs.com.bartender;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.callback.ListenCallback;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import bartender.spinach_labs.com.bartender.helpers.BartenderHelper;
import bartender.spinach_labs.com.bartender.helpers.BartenderStatic;

/**
 * Created by vivek on 10/1/2015.
 */
public class BartenderApi {

    Context context;

    public BartenderApi(Context con){
        this.context = con;
    }

    public void getList(){
        final ProgressDialog progressDialog =  BartenderHelper.createProgressDialog(this.context);
        progressDialog.show();

        String url = BartenderStatic.URL_LIST+BartenderStatic.STR_API_KEY+"="+BartenderStatic.API_KEY;
        Ion.with(this.context)
                .load(url)
                .setHeader(BartenderStatic.STR_MASHAPE_KEY,BartenderStatic.MASHAPE_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        BartenderHelper.showToast(context,"koi");
                        progressDialog.dismiss();
                    }
                });
    }

    public void test(){
        final ProgressDialog progressDialog =  BartenderHelper.createProgressDialog(this.context);
        progressDialog.show();

        //String url = BartenderStatic.URL_LIST+BartenderStatic.STR_API_KEY+"="+BartenderStatic.API_KEY;
        String url = "http://google.com/";
        Ion.with(this.context)
                .load(url)
                .setHeader(BartenderStatic.STR_MASHAPE_KEY,BartenderStatic.MASHAPE_KEY)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String s) {
                        progressDialog.dismiss();
                        if(e == null){
                            BartenderHelper.showToast(context,"koi");
                        }else{
                            BartenderHelper.showToast(context,e.getMessage());
                        }
                    }
                });
    }



}
