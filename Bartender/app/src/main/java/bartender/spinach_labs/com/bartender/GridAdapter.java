package bartender.spinach_labs.com.bartender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import bartender.spinach_labs.com.bartender.models.CockTail;

/**
 * Created by vivek on 11/1/2015.
 */
public class GridAdapter extends BaseAdapter {

    ArrayList<CockTail> cockTails;
    Context context;


    public GridAdapter(Context con,ArrayList<CockTail> list){
        this.context = con;
        this.cockTails = list;
    }

    @Override
    public int getCount() {
        return this.cockTails.size();
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CockTail cockTail = this.cockTails.get(position);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_grid, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.grid_img);
        TextView txtName = (TextView)view.findViewById(R.id.txtName);

        Ion.with(imageView)
                .placeholder(R.drawable.preview)
                .error(R.drawable.preview)
                .load("http://assets.absolutdrinks.com/drinks/transparent-background-black/floor-reflection/300x400/" + cockTail.id + ".png");
        txtName.setText(cockTail.name);

        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return this.cockTails.get(position);
    }
}
