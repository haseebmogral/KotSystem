package kot.amits.com.kotsystem;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.items_adapter.item_adapter;
import kot.amits.com.kotsystem.items_adapter.item_album;

public class backgroundTask extends AsyncTask<Void,item_album,Void> {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private kot.amits.com.kotsystem.items_adapter.item_adapter.CustomItemClickListener itemClickListener;

    private Context context;
    private item_adapter item_adapter;
    private List<item_album> itemlist;
    private List<cart_items> cart_list;
    private String category;

    Cursor item_cursor;


    public backgroundTask(RecyclerView recyclerView,ProgressBar progressBar, Context mContext, List<item_album> albumList1, List<cart_items> cart_items, kot.amits.com.kotsystem.items_adapter.item_adapter.CustomItemClickListener customItemClickListener,String category){
        this.recyclerView=recyclerView;
        this.progressBar=progressBar;
        this.context=mContext;
        this.itemClickListener=customItemClickListener;
        this.category=category;
        this.cart_list=cart_items;
        this.itemlist=albumList1;
    }


    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        item_adapter=new item_adapter(context, itemlist, cart_list, itemClickListener);
        recyclerView.setAdapter(item_adapter);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onProgressUpdate(item_album... values) {
        itemlist.add(values[0]);
//        Log.d("sizeinonprogress",String.valueOf(itemlist.size()));
        }

    @Override
    protected Void doInBackground(Void... voids) {
        DBmanager mydb;
        mydb = new DBmanager(context);
        mydb.open();

        item_cursor = mydb.getitems(category);
        if (item_cursor.getCount()<=0){
//            Log.d("count", String.valueOf(item_cursor.getCount()));

        }
        else {
            Log.d("count", String.valueOf(item_cursor.getCount()));

            while (item_cursor.moveToNext()) {

                String item_id = String.valueOf(item_cursor.getInt(item_cursor.getColumnIndex(DBHelper.item_id)));
                String itemname = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.item_name));
                String itemprice = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.item_price));
                String image = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.image));

                Log.d("view",item_id+"\n"+itemname+"\n"+itemprice);

                publishProgress(new  item_album(item_id, itemname, itemprice, image));

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            item_cursor.close();

        }

        return null;

    }
}
