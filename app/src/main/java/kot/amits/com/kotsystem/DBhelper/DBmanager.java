package kot.amits.com.kotsystem.DBhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kot.amits.com.kotsystem.items_adapter.cart_items;

public class DBmanager {

    private DBHelper dbHelper;

    private Context context;

    public SQLiteDatabase database;

    public static String CART_ID="";
    Cursor global_Cursor;



    public DBmanager(Context c) {
        context = c;
    }

    public DBmanager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        String path = database.getPath();
        Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
        return this;
    }

    public long insertcategory(String name, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.cat_name, name);
        contentValues.put(dbHelper.cat_type, type);
        return database.insert(dbHelper.category, null, contentValues);
    }
    public long insertitems(String name, String cat_id, String price,String itemimage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.item_name, name);
        contentValues.put(dbHelper.cat_cat_id, cat_id);
        contentValues.put(dbHelper.item_price, price);
        contentValues.put(dbHelper.image, itemimage);

        return database.insert(dbHelper.item_table, null, contentValues);

    }

    public long add_to_cart_details() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.date, get_date());
        contentValues.put(dbHelper.time, get_time());
        contentValues.put(dbHelper.status,"1");

        return database.insert(dbHelper.cart_details, null, contentValues);

    }



    //get category

    public Cursor getData(String type) {
        Cursor res = database.rawQuery("select * from category  where "+dbHelper.cat_type+"=?", new String[]{type});


        return res;
    }

    //get items

    public Cursor getitems(String category_id)
    {

        Cursor itemdetails=database.rawQuery("select * from item_table where "+dbHelper.cat_cat_id+" =?",new String[]{category_id});
        return itemdetails;
    }
    public Cursor get_ongoing_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"0"});
        return cursor;
    }
    public Cursor get_sent_to_kitchen_order(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"1"});
        return cursor;
    }
    public Cursor get_finished_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"1"});
        return cursor;
    }
    public Cursor get_cancelled_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"1"});
        return cursor;
    }
    public void place_order(List<cart_items> cart){
        ContentValues contentValues = new ContentValues();
        for (int i=0;i<cart.size();i++){

            global_Cursor=database.rawQuery("select * from cart_items_table where cart_details_id = ? and c_item_id = ?",new String[]{CART_ID, String.valueOf(cart.get(i).getItem_id())});
            if (global_Cursor.getCount()<=0){
                contentValues.put(dbHelper.cart_details_id, CART_ID);
                contentValues.put(dbHelper.c_item_id,cart.get(i).getItem_id());
                contentValues.put(dbHelper.c_qty,cart.get(i).get_qty());
                contentValues.put(dbHelper.c_total,cart.get(i).get_total());
                contentValues.put(dbHelper.c_item_order_status,"sent");

                long a= database.insert(dbHelper.cart_items_table, null, contentValues);
                Toast.makeText(context, String.valueOf(a), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "reaching if", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public Cursor getpurchase_details() {

        Cursor purchase_details = database.rawQuery("select * from purchase_table", new String[]{});
        return purchase_details;

    }


    public Cursor get_active_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where status = ? order by cart_id desc",new String[]{"1"});
        return cursor;
    }

    public Cursor get_active_order_by_bill(String cart_id){
        Cursor cursor=database.rawQuery("select * from item_table,cart_items_table where cart_items_table.cart_details_id=item_table.item_id and cart_items_table.cart_details_id= ?",new String[]{cart_id});
        return cursor;
    }

    public String get_date(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String get_time(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;

    }

    public Cursor getitemlist()
    {
        Cursor getitemlist=database.rawQuery("select * from item_table",new String[]{});
        return getitemlist;
    }

    public String add_space(int length,String str){
        if (str.length()<length){
            int count=length-str.length();
            for (int c=0;c<count;c++){
                str=str+" " ;
            }
        }
        return str;

    }

    public String get_header_title_for_kitchen(){
        String sl_no="sl.no";
        String item="item";
        String qty="qty";
        String output;

        sl_no=add_space(10,sl_no);
        item=add_space(30,item);
        qty=add_space(8,qty);
        output=sl_no+item+qty;

        return output;
    }
    public String get_footer(){
        String power="Powered by Amitech Solutions";
        String line="------------------------------------------------";
        String footer=line+"\n"
                +add_space(power);
        return footer+"\n"+line;
    }
    public String get_website(){
        String power="www.amitechsolutions.com";
        String line="------------------------------------------------";
        String footer=line+"\n"
                +add_space(power);
        return footer+"\n"+line;
    }


    public String add_space(String s){
        int c=s.length();
        int count=48;
        int differene=count-c;
        int rightspace=differene/2;
        int leftspace=differene/2;

        int i;
        for (i=0;i<=leftspace-2;i++){
            s=" "+s;
        }
        for (int j=0;j<=rightspace-2;j++){
            s=s+" ";

        }
        return s;
    }

    //get supplier names to autocomplete view

    public Cursor getSuppliername()
    {
        Cursor suppliername=database.rawQuery("select * from supplier_table",new String[]{});
        return suppliername;
    }

    //insert items to purcase_details
    public long addPurchase(String p_date,String p_description,String p_amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.p_date, p_date);
        contentValues.put(dbHelper.p_description, p_description);
        contentValues.put(dbHelper.p_amount, p_amount);
        contentValues.put(dbHelper.p_upload_status, "added");
        return database.insert(dbHelper.purchase_table, null, contentValues);
    }


    public long add_suppliers(String supplier_name, String supplier_address, String supplier_contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.supplier_name, supplier_name);
        contentValues.put(dbHelper.supplier_address, supplier_address);
        contentValues.put(dbHelper.supplier_contact, supplier_contact);
        contentValues.put(dbHelper.supplier_upload_status,"0");

        return database.insert(dbHelper.supplier_table, null, contentValues);

    }





}

