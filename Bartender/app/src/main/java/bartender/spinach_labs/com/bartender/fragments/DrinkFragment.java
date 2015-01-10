package bartender.spinach_labs.com.bartender.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.util.ArrayList;

import bartender.spinach_labs.com.bartender.R;
import bartender.spinach_labs.com.bartender.helpers.BartenderHelper;
import bartender.spinach_labs.com.bartender.helpers.BartenderStatic;
import bartender.spinach_labs.com.bartender.models.CockTail;

public class DrinkFragment extends android.support.v4.app.Fragment {

    String id;

    TextView txtDrinkName;
    TextView txtDesc;
    TextView txtSkill;
    TextView txtColor;

    ImageView imgDrink;
    ImageView imgVidPreview;
    ImageView imgAlcoholic;
    ImageView imgPlay;

    RatingBar ratingDrink;

    FrameLayout layoutVidPre;


    public DrinkFragment() {
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

        this.id = getArguments().getString("id");

        View view = inflater.inflate(R.layout.fragment_drink, container,
                false);

        txtDrinkName = (TextView)view.findViewById(R.id.txtDrinkName);
        txtDesc = (TextView)view.findViewById(R.id.txtdesc);
        txtSkill = (TextView)view.findViewById(R.id.txtSkill);
        txtColor = (TextView)view.findViewById(R.id.txtColor);

        imgDrink = (ImageView)view.findViewById(R.id.img_drink);
        imgVidPreview = (ImageView)view.findViewById(R.id.imgVidPreview);
        imgAlcoholic = (ImageView)view.findViewById(R.id.imgAlcoholic);
        imgPlay = (ImageView)view.findViewById(R.id.imgPlay);

        ratingDrink = (RatingBar)view.findViewById(R.id.rating);
        ratingDrink.setMax(10);
        ratingDrink.setNumStars(5);

        layoutVidPre = (FrameLayout)view.findViewById(R.id.layoutVidPreview);

        init();
        return view;
    }


    JsonObject vid;
    private void init(){
        try{
            final ProgressDialog progressDialog = BartenderHelper.createProgressDialog(getActivity());
            progressDialog.show();
            String url = BartenderStatic.URL_DRINK+this.id+"?"+BartenderStatic.STR_API_KEY+"="+BartenderStatic.API_KEY;
            Ion.with(getActivity())
                    .load(url)
                    .setHeader(BartenderStatic.STR_MASHAPE_KEY,BartenderStatic.MASHAPE_KEY)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject jsonObject) {
                            progressDialog.dismiss();
                            if(e == null){

                                JsonArray jsonCocktails = jsonObject.getAsJsonArray("result");
                                JsonObject objectCocktail = jsonCocktails.get(0).getAsJsonObject();

                                CockTail cockTail = new CockTail();
                                try{
                                    cockTail.name = objectCocktail.get("name").getAsString();
                                    cockTail.id = objectCocktail.get("id").getAsString();
                                    cockTail.descriptionPlain = objectCocktail.get("descriptionPlain").getAsString();
                                    cockTail.isAlcoholic = objectCocktail.get("isAlcoholic").getAsBoolean();
                                    cockTail.isCarbonated = objectCocktail.get("isCarbonated").getAsBoolean();
                                    cockTail.isHot = objectCocktail.get("isHot").getAsBoolean();
                                    cockTail.rating = objectCocktail.get("rating").getAsInt();
                                    cockTail.color = objectCocktail.get("color").getAsString();
                                    cockTail.story = objectCocktail.get("story").getAsString();
                                    cockTail.descriptionPlain = objectCocktail.get("descriptionPlain").getAsString();
                                }catch (Exception ex){}

                                try{
                                    JsonObject skillObject = objectCocktail.get("skill").getAsJsonObject();
                                    cockTail.skill = skillObject.get("name").getAsString();
                                    txtDrinkName.setText(cockTail.name);
                                    txtSkill.setText(cockTail.skill);
                                    txtDesc.setText(cockTail.descriptionPlain);
                                    txtColor.setText(cockTail.color);
                                }catch (Exception ex){}

                                try{
                                    JsonArray videos = objectCocktail.get("videos").getAsJsonArray();
                                    if(videos.size()>0){
                                        vid = videos.get(0).getAsJsonObject();
                                        if(vid.get("type").getAsString().equals("youtube")){
                                            layoutVidPre.setVisibility(View.VISIBLE);
                                        }else{
                                            vid = videos.get(1).getAsJsonObject();
                                            if(vid.get("type").getAsString().equals("youtube")){
                                                layoutVidPre.setVisibility(View.VISIBLE);
                                            }else{
                                                layoutVidPre.setVisibility(View.GONE);
                                            }
                                        }
                                        if(vid != null){
                                            imgPlay.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try{
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+vid.get("video").getAsString())));
                                                        //String youtubeUrl = "http://www.youtube.com/watch?v="+vid.get("video").getAsString();
                                                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
                                                    }catch (Exception ex){}
                                                }
                                            });
                                        }
                                    }
                                }catch (Exception ex){
                                    layoutVidPre.setVisibility(View.GONE);
                                }

                                try{
                                    float rating = cockTail.rating/10;
                                    ratingDrink.setRating((float)rating);
                                }catch (Exception ex){}

                                if(cockTail.isAlcoholic){
                                    imgAlcoholic.setImageResource(R.drawable.green);
                                }else{
                                    imgAlcoholic.setImageResource(R.drawable.red);
                                }

                                Ion.with(imgDrink)
                                        .placeholder(R.drawable.preview)
                                        .error(R.drawable.preview)
                                        .load("http://assets.absolutdrinks.com/drinks/transparent-background-black/floor-reflection/300x400/" + cockTail.id + ".png");

                                Ion.with(imgVidPreview)
                                        .placeholder(R.drawable.preview)
                                        .error(R.drawable.preview)
                                        .load("http://assets.absolutdrinks.com/drinks/transparent-background-black/floor-reflection/300x400/" + cockTail.id + ".png");

                            }else{
                                BartenderHelper.showToast(getActivity(),"Network Error");
                            }
                        }
                    });
        }catch (Exception ex){}
    }

}
