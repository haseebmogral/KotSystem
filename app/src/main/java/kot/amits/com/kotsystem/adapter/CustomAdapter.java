package kot.amits.com.kotsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.category_selection;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private String [] app_name;
    private int [] app_icon;

    public CustomAdapter(Context context, String [] app_name, int [] app_icon) {

        this.context=context;
        this.app_icon=app_icon;
        this.app_name=app_name;
    }

    public CustomAdapter(ArrayList dataModels, category_selection category_selection) {
    }

    @Override
    public int getCount() {

        return app_name.length;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.custom_grid, null);
        TextView name=(TextView)convertView.findViewById(R.id.textView1);
        ImageView icon=(ImageView)convertView.findViewById(R.id.imageView);
        name.setText(app_name[position]);
        icon.setImageResource(app_icon[position]);
        return convertView;
    }
}
