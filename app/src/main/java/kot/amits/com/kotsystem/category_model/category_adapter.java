package kot.amits.com.kotsystem.category_model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.category_selection;

public class category_adapter extends ArrayAdapter {

    private ArrayList dataSet;
    Context mContext;
    ArrayList<String> cat_List;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
        TextView catid;

    }

    public category_adapter(ArrayList data, Context context,ArrayList<String> list) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.cat_List = list;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public DataModel getItem(int position) {
        return (DataModel) dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        final DataModel item = getItem(position);


        viewHolder.txtName.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked);
        Log.d("checkstatus", String.valueOf(item.checked));
//        viewHolder.catid.setText(item.cat_id);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    item.setChecked(true);
                    Toast.makeText(mContext,String.valueOf(item.cat_id) , Toast.LENGTH_SHORT).show();
                    cat_List.add(String.valueOf(item.name+"/"+item.cat_id));
                }
                else if (!buttonView.isChecked()){
                    item.setChecked(false);
                    cat_List.remove(String.valueOf(item.name+"/"+item.cat_id));
                }
            }
        });


        return result;
    }


}
