package kot.amits.com.kotsystem.DBhelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
    public static String ORDER_TYPE="";
    Cursor global_Cursor;
    public static ArrayList<cart_items> cart_list ;


    public static SharedPreferences sharedpreferences;
    public static String sharedpreference_name="mypreference";
    public static String sharedpreference_password="password";
    public static String sharedpreference_branch_id="branch_id";
    public static String sharedpreference_branch_name="branch_name";

    public static String sharedpreference_column="column";




    public DBmanager(Context c) {
        context = c;
    }

    public DBmanager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        String path = database.getPath();
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
        contentValues.put(dbHelper.cart_status,"1");
        contentValues.put(dbHelper.total,"0");
        contentValues.put(dbHelper.cart_type,ORDER_TYPE);
        return database.insert(dbHelper.cart_details, null, contentValues);

    }
    public String get_order_status(String cart_id){
        global_Cursor=database.rawQuery("select * from "+DBHelper.cart_details+" where "+DBHelper.cart_id+" = ?",new String[]{cart_id});
        global_Cursor.moveToNext();
        String status=global_Cursor.getString(global_Cursor.getColumnIndex(DBHelper.cart_status));
        Log.d("order_status",status);

        return status;
    }



    //get category

    public Cursor getData(String type) {
        Cursor res = database.rawQuery("select * from category  where "+dbHelper.cat_type+"=?", new String[]{type});


        return res;
    }

    //get items

    public Cursor getitems(String category_id) {
        Cursor itemdetails;
        if (category_id.equals("999")){
             itemdetails=database.rawQuery("SELECT i.item_id, i.item_name,i.item_price,i.image\n" + "FROM `cart_items_table` AS c\n" + "    INNER JOIN `item_table` AS i\n" + "    ON c.`c_item_id` = i.`item_id`\n" + "GROUP BY c.`c_item_id`\n" + "ORDER BY SUM(c.`c_qty`) DESC, i.`item_name` ASC\n" + "LIMIT 10",new String[]{});
        }
        else {
            itemdetails=database.rawQuery("select * from item_table where "+dbHelper.cat_cat_id+" =? order by "+dbHelper.item_name +" asc ",new String[]{category_id});
        }
        return itemdetails;
    }
    public Cursor get_ongoing_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.cart_status+" =?",new String[]{"0"});
        return cursor;
    }
    public Cursor get_sent_to_kitchen_order(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.cart_status+" =?",new String[]{"1"});
        return cursor;
    }
    public Cursor get_finished_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.cart_status+" =?",new String[]{"1"});
        return cursor;
    }
    public Cursor get_cancelled_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.cart_status+" =?",new String[]{"1"});
        return cursor;
    }
    public void place_order(List<cart_items> cart){
        ContentValues contentValues = new ContentValues();
        for (int i=0;i<cart.size();i++){

            global_Cursor=database.rawQuery("select * from cart_items_table where cart_details_id = ? and c_item_id = ?",new String[]{CART_ID, String.valueOf(cart.get(i).getItem_id())});
            if (global_Cursor.getCount()<=0){
                Toast.makeText(context, "Reaching else", Toast.LENGTH_SHORT).show();
                contentValues.put(dbHelper.cart_details_id, CART_ID);
                contentValues.put(dbHelper.c_item_id,cart.get(i).getItem_id());
                contentValues.put(dbHelper.c_qty,cart.get(i).get_qty());
                contentValues.put(dbHelper.c_total,cart.get(i).get_total());
                contentValues.put(dbHelper.c_item_order_status,"sent");

                long a= database.insert(dbHelper.cart_items_table, null, contentValues);
                Toast.makeText(context, String.valueOf(a), Toast.LENGTH_SHORT).show();


                ContentValues contentValues1=new ContentValues();
                contentValues1.put(dbHelper.cart_status,"2");

                database.update(dbHelper.cart_details,contentValues1,dbHelper.cart_id +" =?",new String[]{CART_ID});
            }
            else{
                Toast.makeText(context, "reaching if", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public Cursor getpurchase_details() {

        Cursor purchase_details = database.rawQuery("select * from supplier_table s,purchase_table p where p.p_supplier_id=s.supplier_id", new String[]{});
        return purchase_details;

    }


    public Cursor get_active_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.cart_status+" = ? or "+dbHelper.cart_status+" ="+dbHelper.ORDER_SENT+" order by cart_id desc",new String[]{DBHelper.ORDER_CART});
        return cursor;
    }

    public Cursor get_active_order_by_bill(String cart_id){
        Cursor cursor=database.rawQuery("select * from item_table,cart_items_table,cart_details where cart_details.cart_id=cart_items_table.cart_details_id and cart_items_table.c_item_id=item_table.item_id and cart_details.cart_id= ?",new String[]{cart_id});
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
    public String get_header_title_for_bill(){
        String sl_no="no";
        String item="item";
        String qty="qty";
        String price="price";
        String total="total";
        String output;

        sl_no=add_space(5,sl_no);
        item=add_space(27,item);
        price=add_space(6,price);
        qty=add_space(4,qty);
        total=add_space(5,total);
        output=sl_no+item+qty+price+total;

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

    public String get_line(){
        return "------------------------------------------------";
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
    public long addPurchase(String supplier_id, String p_date,String p_description,String p_amount,String p_paid,String p_bal) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(dbHelper.p_supplier_id, supplier_id);
        contentValues.put(dbHelper.p_date, p_date);
        contentValues.put(dbHelper.p_description, p_description);
        contentValues.put(dbHelper.p_amount, p_amount);
        contentValues.put(dbHelper.p_paid, p_paid);
        contentValues.put(dbHelper.p_bal, p_bal);
        contentValues.put(dbHelper.p_upload_status,"0");
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

    public String total_format(int length,String str){
        if (str.length()<length){
            int count=length-str.length();
            for (int c=0;c<count;c++){
                str=" "+str ;
            }
        }
        return str;

    }
    public void finish_order(String cart_id,String total){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBHelper.cart_status,dbHelper.ORDER_FINISHED);
        contentValues.put(DBHelper.total,total);
        database.update(DBHelper.cart_details,contentValues,DBHelper.cart_id +" = ? ",new String []{cart_id});
        }

        public void cancel_order(String order_id){
        ContentValues contentValues=new ContentValues();
        contentValues.put(dbHelper.cart_status,dbHelper.ORDER_CANCEL);
        database.update(dbHelper.cart_details,contentValues,dbHelper.cart_id+" =? ",new String[]{order_id});
        }

        public void feedback(String customer_name,String contact,String cart_id, ArrayList<cart_items> cart_lis,int ambience,int staff,String custom_feedback){

        int customer_id;

            global_Cursor=database.rawQuery("select * from "+dbHelper.customer_table+" where "+dbHelper.customer_contact+" = ?",new String[]{contact});
            if (global_Cursor.getCount()<=0){
                //new customer adding
                ContentValues contentValues=new ContentValues();
                contentValues.put(dbHelper.customer_name,customer_name);
                contentValues.put(dbHelper.customer_contact,contact);
                contentValues.put(dbHelper.customer_upload_status,"0");
                customer_id= (int) database.insert(dbHelper.customer_table,null,contentValues);
                add_feedback(customer_id,cart_id,cart_lis,ambience,staff,custom_feedback);
            }
            else{
                global_Cursor.moveToFirst();
                customer_id=global_Cursor.getInt(global_Cursor.getColumnIndex(dbHelper.customer_id));
                add_feedback(customer_id,cart_id,cart_lis,ambience,staff,custom_feedback);
            }


        }
        public void add_feedback(int customer_id,String cart_id,ArrayList<cart_items> cart_lis,int ambience,int staff,String custom_feedback ){
            long feedback_id;

            Log.d("customer_id",String.valueOf(customer_id));
            //feedback adding
            global_Cursor.moveToNext();
            ContentValues contentValues=new ContentValues();
            contentValues.put(dbHelper.feedback_order_id,CART_ID);
            contentValues.put(dbHelper.ambience_rating,ambience);
            contentValues.put(dbHelper.staff_rating,staff);
            contentValues.put(dbHelper.feedback_review,custom_feedback);
            contentValues.put(dbHelper.feedback_upload_status,0);
            feedback_id=database.insert(dbHelper.feedback_table,null,contentValues);


            //feedback for each item in bill
            ContentValues item_rater=new ContentValues();
            for (cart_items a:cart_lis){
                int item_id=a.getItem_id();
                int rate=a.getrate();

                item_rater.put(dbHelper.feedback_id_id,feedback_id);
                item_rater.put(dbHelper.feedback_items_id,item_id);
                item_rater.put(dbHelper.rating,rate);
                item_rater.put(dbHelper.rating_upload_status,0);
                database.insert(dbHelper.feedback_items_table,null,item_rater);
            }

            ContentValues contentValues1=new ContentValues();
            contentValues1.put(dbHelper.cart_customer_id,customer_id);
            database.update(dbHelper.cart_details,contentValues1,dbHelper.cart_id +" = ?",new String[]{cart_id});
        }

    public Cursor get_supplier_details() {

        Cursor supplier_details = database.rawQuery("select * from supplier_table", new String[]{});
        return supplier_details;

    }

    public Cursor get_expense_details() {

        Cursor expense_details = database.rawQuery("select * from expense_table", new String[]{});
        return expense_details;

    }


    //add expense to db


    public long add_expense(String exp_type, String exp_date, String exp_desc,String exp_amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.e_type, exp_type);
        contentValues.put(dbHelper.e_amount, exp_amount);
        contentValues.put(dbHelper.e_desc, exp_desc);
        contentValues.put(dbHelper.e_date,exp_date);
        contentValues.put(dbHelper.e_upload_status,"0");
        return database.insert(dbHelper.expense_table, null, contentValues);

    }


    //get employee details
    public Cursor get_employee_details() {

        Cursor employee_details = database.rawQuery("select * from employee_table", new String[]{});
        return employee_details;

    }

    //add employee

    public long add_employee(String emp_name, String emp_address, String emp_desig,String emp_salary,String emp_doj,String emp_contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.emp_name, emp_name);
        contentValues.put(dbHelper.emp_address, emp_address);
        contentValues.put(dbHelper.emp_designation, emp_desig);
        contentValues.put(dbHelper.emp_salary,emp_salary);
        contentValues.put(dbHelper.emp_doj,emp_doj);
        contentValues.put(dbHelper.emp_status,"1");
        contentValues.put(dbHelper.emp_contact,emp_contact);
        contentValues.put(dbHelper.emp_upload_status,"0");
        return database.insert(dbHelper.employee_table, null, contentValues);

    }

    public Cursor get_all_sales(){
       return database.rawQuery("select * from "+dbHelper.cart_details,new String[]{});
    }
    public Cursor get_all_sales_report(){
        return database.rawQuery("select * from "+dbHelper.cart_details,new String[]{});
    }
    public Cursor get_all_sales_items(){
       return database.rawQuery("select * from "+dbHelper.cart_items_table,new String[]{});
    }
    public Cursor get_purchase(){
       return database.rawQuery("select * from "+dbHelper.purchase_table,new String[]{});
    }
    public Cursor get_supplier(){
       return database.rawQuery("select * from "+dbHelper.supplier_table,new String[]{});
    }
    public Cursor get_feedback(){
       return database.rawQuery("select * from "+dbHelper.feedback_table,new String[]{});
    }
    public Cursor get_expense(){
       return database.rawQuery("select * from "+dbHelper.expense_table,new String[]{});
    }
    public Cursor get_employee(){
       return database.rawQuery("select * from "+dbHelper.employee_table,new String[]{});
    }


}

