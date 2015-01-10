package bartender.spinach_labs.com.bartender.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import bartender.spinach_labs.com.bartender.BartenderApi;
import bartender.spinach_labs.com.bartender.GridAdapter;
import bartender.spinach_labs.com.bartender.MainActivity;
import bartender.spinach_labs.com.bartender.R;
import bartender.spinach_labs.com.bartender.helpers.BartenderHelper;
import bartender.spinach_labs.com.bartender.helpers.BartenderStatic;
import bartender.spinach_labs.com.bartender.models.CockTail;

public class HomeFragment extends android.support.v4.app.Fragment {

    ArrayList<CockTail> cockTails;
    GridView gridCocktail;

    public MainActivity parentActivity;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container,
                false);

        gridCocktail = (GridView)view.findViewById(R.id.gridCocktails);

        gridCocktail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CockTail tail = cockTails.get(position);
                //BartenderHelper.showToast(getActivity(),tail.name);

                Bundle bundle = new Bundle();
                bundle.putString("id", tail.id);

                DrinkFragment fragment2 = new DrinkFragment();
                fragment2.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.addToBackStack("detail");
                fragmentTransaction.commit();
            }
        });

        cockTails = new ArrayList<CockTail>();

        init();

//        CockTail tail;
//
//        tail = new CockTail();
//        tail.name = "name";
//        tail.id = "soviet";
//
//        cockTails.add(tail);
//        cockTails.add(tail);
//        cockTails.add(tail);
//        cockTails.add(tail);
//        cockTails.add(tail);
//        cockTails.add(tail);
//        cockTails.add(tail);
//        cockTails.add(tail);
//        cockTails.add(tail);
//
//        GridAdapter gridAdapter = new GridAdapter(getActivity(),cockTails);
//        gridCocktail.setAdapter(gridAdapter);


        return view;
    }


    private void init(){
        final ProgressDialog progressDialog = BartenderHelper.createProgressDialog(getActivity());
        progressDialog.show();

        String url = BartenderStatic.URL_LIST+BartenderStatic.STR_API_KEY+"="+BartenderStatic.API_KEY;
        Ion.with(getActivity())
                .load(url)
                .setTimeout(60*1000)
                .setHeader(BartenderStatic.STR_MASHAPE_KEY,BartenderStatic.MASHAPE_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        progressDialog.dismiss();

                        if(e == null){
                            JsonArray jsonCocktails = jsonObject.getAsJsonArray("result");
                            for(int i=0;i<jsonCocktails.size();i++){
                                JsonObject objectCocktail = jsonCocktails.get(i).getAsJsonObject();

                                CockTail cockTail = new CockTail();
                                cockTail.name = objectCocktail.get("name").getAsString();
                                cockTail.id = objectCocktail.get("id").getAsString();
                                cockTail.descriptionPlain = objectCocktail.get("descriptionPlain").getAsString();
                                cockTail.isAlcoholic = objectCocktail.get("isAlcoholic").getAsBoolean();
                                cockTail.isCarbonated = objectCocktail.get("isCarbonated").getAsBoolean();
                                cockTail.isHot = objectCocktail.get("isHot").getAsBoolean();

                                cockTails.add(cockTail);
                            }

                            try{
                                GridAdapter gridAdapter = new GridAdapter(getActivity(),cockTails);
                                gridCocktail.setAdapter(gridAdapter);



                            }catch (Exception ex){}
                        }else{
                            BartenderHelper.showToast(getActivity(),"Network Error");
                        }

                    }
                });
    }

}

