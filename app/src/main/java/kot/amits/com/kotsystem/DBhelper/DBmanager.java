package kot.amits.com.kotsystem.DBhelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kot.amits.com.kotsystem.items_adapter.cart_items;

import static kot.amits.com.kotsystem.DBhelper.DBHelper.cart_details;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.cart_items_table;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.item_table;

public class DBmanager {

    private DBHelper dbHelper;

    private Context context;

    public SQLiteDatabase database;

    public static String CART_ID = "";
    public static String ORDER_TYPE = "";
    Cursor global_Cursor;
    public static ArrayList<cart_items> cart_list;


    public static SharedPreferences sharedpreferences;
    public static String sharedpreference_name = "mypreference";

    public static String sharedpreference_password = "password";
    public static String sharedpreference_branch_id = "branch_id";
    public static String sharedpreference_business_id = "business_id";
    public static String sharedpreference_branch_name = "branch_name";
    public static String sharedpreference_branch_address = "branch_address";
    public static String sharedpreference_column = "column";
    public static String sharedpreference_app_setup = "setup";


    public DBmanager(Context c) {
        context = c;
    }

    public DBmanager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        String path = database.getPath();
        return this;
    }

    public long insertcategory(int cat_id,String name, String type,String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.cat_id, cat_id);
        contentValues.put(dbHelper.cat_name, name);
        contentValues.put(dbHelper.cat_type, type);
        contentValues.put(dbHelper.cat_status, status);
        return database.insert(dbHelper.category, null, contentValues);
    }

    public int update_category(String cat,String status){
        ContentValues contentValues=new ContentValues();
        contentValues.put(dbHelper.cat_name,cat);
        contentValues.put(dbHelper.cat_status,status);
       return database.update(dbHelper.category,contentValues,dbHelper.cat_name+" = ?",new String[]{cat});
    }

    public long insertitems(int item_id,String name, String cat_id, String price, String itemimage,String active_status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.item_id, item_id);
        contentValues.put(dbHelper.item_name, name);
        contentValues.put(dbHelper.cat_cat_id, cat_id);
        contentValues.put(dbHelper.item_price, price);
        contentValues.put(dbHelper.image, itemimage);
        contentValues.put(dbHelper.item_active_status, active_status);

        return database.insert(item_table, null, contentValues);

    }

    public void update_items(String item_id,String status){
        ContentValues contentValues=new ContentValues();
        contentValues.put(dbHelper.item_active_status,status);
        database.update(DBHelper.item_table,contentValues,dbHelper.item_id+" = ?",new String[]{item_id});
    }

    public boolean isactive_category(String cat_id){
        Cursor cursor=null;
        cursor= database.rawQuery("select * from "+dbHelper.category +" where "+dbHelper.cat_id+" = ? and "+ dbHelper.cat_status+ " = 1",new String[]{cat_id});
        if (cursor.getCount()<=0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isactive_item(String item_id){
        Cursor cursor=null;
        cursor= database.rawQuery("select * from "+dbHelper.item_table +" where "+dbHelper.item_id+" = ? and "+ dbHelper.item_active_status+ " = ?",new String[]{item_id,"1"});
        if (cursor.getCount()<=0){
            return false;
        }
        else{
            return true;
        }
    }


    public long add_to_cart_details() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.date, get_date());
        contentValues.put(dbHelper.time, get_time());
        contentValues.put(dbHelper.cart_status, dbHelper.ORDER_CART);
        contentValues.put(dbHelper.total, "0");
        contentValues.put(dbHelper.cart_type, ORDER_TYPE);
        return database.insert(cart_details, null, contentValues);

    }

    public String get_order_status(String cart_id) {
        global_Cursor = database.rawQuery("select * from " + cart_details + " where " + DBHelper.cart_id + " = ?", new String[]{cart_id});
        global_Cursor.moveToNext();
        String status = global_Cursor.getString(global_Cursor.getColumnIndex(DBHelper.cart_status));
        Log.d("order_status", status);

        return status;
    }


    //get category

    public Cursor getData(String type) {
        Cursor res = database.rawQuery("select * from category  where " + dbHelper.cat_type + "=? and "+dbHelper.cat_status+" = ?", new String[]{type,"1"});
        return res;
    }
    public Cursor getData() {
        Cursor res = database.rawQuery("select * from "+dbHelper.category, new String[]{});
        return res;
    }

    //get items

    public Cursor getitems(String category_id) {
        Cursor itemdetails;
        if (category_id.equals("999")) {
            itemdetails = database.rawQuery("SELECT i.item_id, i.item_name,i.item_price,i.image\n" + "FROM `cart_items_table` AS c\n" + "    INNER JOIN `item_table` AS i\n" + "    ON c.`c_item_id` = i.`item_id`\n" + "GROUP BY c.`c_item_id`\n" + "ORDER BY SUM(c.`c_qty`) DESC, i.`item_name` ASC\n" + "LIMIT 10", new String[]{});
        } else {
            itemdetails = database.rawQuery("select * from item_table where " + dbHelper.cat_cat_id + " =? and "+dbHelper.item_active_status+" = ? order by " + dbHelper.item_name + " asc ", new String[]{category_id,"1"});
        }
        return itemdetails;
    }

    public Cursor get_items(){
        return database.rawQuery("select * from "+dbHelper.item_table,null);
    }

    public Cursor get_ongoing_orders() {
        Cursor cursor = database.rawQuery("select * from cart_details where " + dbHelper.cart_status + " =?", new String[]{"0"});
        return cursor;
    }

    public Cursor get_sent_to_kitchen_order() {
        Cursor cursor = database.rawQuery("select * from cart_details where " + dbHelper.cart_status + " =?", new String[]{"1"});
        return cursor;
    }

    public Cursor get_finished_orders() {
        Cursor cursor = database.rawQuery("select * from cart_details where " + dbHelper.cart_status + " =?", new String[]{"1"});
        return cursor;
    }

    public Cursor get_cancelled_orders() {
        Cursor cursor = database.rawQuery("select * from cart_details where " + dbHelper.cart_status + " =?", new String[]{"1"});
        return cursor;
    }

    public void place_order(List<cart_items> cart) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < cart.size(); i++) {

            global_Cursor = database.rawQuery("select * from cart_items_table where cart_details_id = ? and c_item_id = ?", new String[]{CART_ID, String.valueOf(cart.get(i).getItem_id())});
            if (global_Cursor.getCount() <= 0) {
                Toast.makeText(context, "Reaching else", Toast.LENGTH_SHORT).show();
                contentValues.put(dbHelper.cart_details_id, CART_ID);
                contentValues.put(dbHelper.c_item_id, cart.get(i).getItem_id());
                contentValues.put(dbHelper.c_qty, cart.get(i).get_qty());
                contentValues.put(dbHelper.c_total, cart.get(i).get_total());
                contentValues.put(dbHelper.c_item_order_status, "sent");

                long a = database.insert(cart_items_table, null, contentValues);
                Toast.makeText(context, String.valueOf(a), Toast.LENGTH_SHORT).show();


                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(dbHelper.cart_status, DBHelper.ORDER_SENT);
                contentValues1.put(dbHelper.upload_status, DBHelper.UPLOAD_STATUS_FAILED);

                database.update(cart_details, contentValues1, dbHelper.cart_id + " =?", new String[]{CART_ID});
            } else {
                Toast.makeText(context, "reaching if", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public Cursor getpurchase_details() {

        Cursor purchase_details = database.rawQuery("select * from supplier_table s,purchase_table p where p.p_supplier_id=s.supplier_id", new String[]{});
        return purchase_details;

    }


    public Cursor get_active_orders() {
        Cursor cursor = database.rawQuery("select * from cart_details where " + dbHelper.cart_status + " = ? or " + dbHelper.cart_status + " =" + dbHelper.ORDER_SENT + " order by cart_id desc", new String[]{DBHelper.ORDER_CART});
        return cursor;
    }

    public Cursor get_active_order_by_bill(String cart_id) {
        Cursor cursor = database.rawQuery("select * from item_table,cart_items_table,cart_details where cart_details.cart_id=cart_items_table.cart_details_id and cart_items_table.c_item_id=item_table.item_id and cart_details.cart_id= ?", new String[]{cart_id});
        return cursor;
    }

    public String get_date() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String get_time() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;

    }

    public Cursor getitemlist() {
        Cursor getitemlist = database.rawQuery("select * from item_table", new String[]{});
        return getitemlist;
    }

    public String add_space(int length, String str) {
        if (str.length() < length) {
            int count = length - str.length();
            for (int c = 0; c < count; c++) {
                str = str + " ";
            }
        }
        return str;

    }

    public String get_header_title_for_kitchen() {
        String sl_no = "sl.no";
        String item = "item";
        String qty = "qty";
        String output;

        sl_no = add_space(10, sl_no);
        item = add_space(30, item);
        qty = add_space(8, qty);
        output = sl_no + item + qty;

        return output;
    }

    public String get_header_title_for_bill() {
        String sl_no = "no";
        String item = "item";
        String qty = "qty";
        String price = "price";
        String total = "total";
        String output;

        sl_no = add_space(5, sl_no);
        item = add_space(27, item);
        price = add_space(6, price);
        qty = add_space(4, qty);
        total = add_space(5, total);
        output = sl_no + item + qty + price + total;

        return output;
    }

    public String get_footer() {
        String power = "Powered by Amitech Solutions";
        String line = "------------------------------------------------";
        String footer = line + "\n" + add_space(power);
        return footer + "\n" + line;
    }

    public String get_website() {
        String power = "www.amitechsolutions.com";
        String line = "------------------------------------------------";
        String footer = line + "\n" + add_space(power);
        return footer + "\n" + line;
    }

    public String get_line() {
        return "------------------------------------------------";
    }


    public String add_space(String s) {
        int c = s.length();
        int count = 48;
        int differene = count - c;
        int rightspace = differene / 2;
        int leftspace = differene / 2;

        int i;
        for (i = 0; i <= leftspace - 2; i++) {
            s = " " + s;
        }
        for (int j = 0; j <= rightspace - 2; j++) {
            s = s + " ";

        }
        return s;
    }

    //get supplier names to autocomplete view

    public Cursor getSuppliername() {
        Cursor suppliername = database.rawQuery("select * from supplier_table", new String[]{});
        return suppliername;
    }

    //insert items to purcase_details
    public long addPurchase(String supplier_id, String p_date, String p_description, String p_amount, String p_paid, String p_bal) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(dbHelper.p_supplier_id, supplier_id);
        contentValues.put(dbHelper.p_date, p_date);
        contentValues.put(dbHelper.p_description, p_description);
        contentValues.put(dbHelper.p_amount, p_amount);
        contentValues.put(dbHelper.p_paid, p_paid);
        contentValues.put(dbHelper.p_bal, p_bal);
        contentValues.put(dbHelper.p_upload_status, "0");
        return database.insert(dbHelper.purchase_table, null, contentValues);
    }


    public long add_suppliers(String supplier_name, String supplier_address, String supplier_contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.supplier_name, supplier_name);
        contentValues.put(dbHelper.supplier_address, supplier_address);
        contentValues.put(dbHelper.supplier_contact, supplier_contact);
        contentValues.put(dbHelper.supplier_upload_status, "0");

        return database.insert(dbHelper.supplier_table, null, contentValues);

    }

    public String total_format(int length, String str) {
        if (str.length() < length) {
            int count = length - str.length();
            for (int c = 0; c < count; c++) {
                str = " " + str;
            }
        }
        return str;

    }

    public void finish_order(String cart_id, String total) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.cart_status, dbHelper.ORDER_FINISHED);
        contentValues.put(DBHelper.total, total);
        database.update(cart_details, contentValues, DBHelper.cart_id + " = ? ", new String[]{cart_id});
    }

    public void cancel_order(String order_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.cart_status, dbHelper.ORDER_CANCEL);
        database.update(cart_details, contentValues, dbHelper.cart_id + " =? ", new String[]{order_id});
    }

    public void feedback(String customer_name, String contact, String cart_id, ArrayList<cart_items> cart_lis, int ambience, int staff, String custom_feedback) {

        int customer_id;

        global_Cursor = database.rawQuery("select * from " + dbHelper.customer_table + " where " + dbHelper.customer_contact + " = ?", new String[]{contact});
        if (global_Cursor.getCount() <= 0) {
            //new customer adding
            ContentValues contentValues = new ContentValues();
            contentValues.put(dbHelper.customer_name, customer_name);
            contentValues.put(dbHelper.customer_contact, contact);
            contentValues.put(dbHelper.customer_upload_status, "0");
            customer_id = (int) database.insert(dbHelper.customer_table, null, contentValues);
            add_feedback(customer_id, cart_id, cart_lis, ambience, staff, custom_feedback);
        } else {
            global_Cursor.moveToFirst();
            customer_id = global_Cursor.getInt(global_Cursor.getColumnIndex(dbHelper.customer_id));
            add_feedback(customer_id, cart_id, cart_lis, ambience, staff, custom_feedback);
        }


    }

    public void add_feedback(int customer_id, String cart_id, ArrayList<cart_items> cart_lis, int ambience, int staff, String custom_feedback) {
        long feedback_id;

        Log.d("customer_id", String.valueOf(customer_id));
        //feedback adding
        global_Cursor.moveToNext();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.feedback_order_id, CART_ID);
        contentValues.put(dbHelper.ambience_rating, ambience);
        contentValues.put(dbHelper.staff_rating, staff);
        contentValues.put(dbHelper.feedback_review, custom_feedback);
        contentValues.put(dbHelper.feedback_upload_status, 0);
        feedback_id = database.insert(dbHelper.feedback_table, null, contentValues);


        //feedback for each item in bill
        ContentValues item_rater = new ContentValues();
        for (cart_items a : cart_lis) {
            int item_id = a.getItem_id();
            int rate = a.getrate();

            item_rater.put(dbHelper.feedback_id_id, feedback_id);
            item_rater.put(dbHelper.feedback_items_id, item_id);
            item_rater.put(dbHelper.rating, rate);
            item_rater.put(dbHelper.rating_upload_status, 0);
            database.insert(dbHelper.feedback_items_table, null, item_rater);
        }

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(dbHelper.cart_customer_id, customer_id);
        database.update(cart_details, contentValues1, dbHelper.cart_id + " = ?", new String[]{cart_id});
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


    public long add_expense(String exp_type, String exp_date, String exp_desc, String exp_amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.e_type, exp_type);
        contentValues.put(dbHelper.e_amount, exp_amount);
        contentValues.put(dbHelper.e_desc, exp_desc);
        contentValues.put(dbHelper.e_date, exp_date);
        contentValues.put(dbHelper.e_upload_status, "0");
        return database.insert(dbHelper.expense_table, null, contentValues);

    }


    //get employee details
    public Cursor get_employee_details() {

        Cursor employee_details = database.rawQuery("select * from employee_table", new String[]{});
        return employee_details;

    }

    //add employee

    public long add_employee(String emp_name, String emp_address, String emp_desig, String emp_salary, String emp_doj, String emp_contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.emp_name, emp_name);
        contentValues.put(dbHelper.emp_address, emp_address);
        contentValues.put(dbHelper.emp_designation, emp_desig);
        contentValues.put(dbHelper.emp_salary, emp_salary);
        contentValues.put(dbHelper.emp_doj, emp_doj);
        contentValues.put(dbHelper.emp_status, "1");
        contentValues.put(dbHelper.emp_contact, emp_contact);
        contentValues.put(dbHelper.emp_upload_status, "0");
        return database.insert(dbHelper.employee_table, null, contentValues);

    }

    public Cursor get_all_sales() {
        return database.rawQuery("select * from " + cart_details + " where " + dbHelper.cart_status + " =? order by " + dbHelper.cart_id + " DESC ", new String[]{dbHelper.ORDER_FINISHED});
    }

    public Cursor get_sales_items(String bill_no) {
        return database.rawQuery("select * from " + cart_items_table + " cit ," + cart_details + " cdt, " + item_table + " i  where cdt.cart_id=cit.cart_details_id and cit.c_item_id=i.item_id and cdt.cart_id=?", new String[]{bill_no});
    }

    public Cursor get_all_sales_report() {
        return database.rawQuery("select * from " + cart_details + " where " + dbHelper.cart_status + " = ? AND " + dbHelper.upload_status + " = ?", new String[]{dbHelper.ORDER_FINISHED, DBHelper.UPLOAD_STATUS_FAILED});
    }

    public Cursor get_all_sales_items() {
        return database.rawQuery("select * from " + cart_items_table, new String[]{});
    }

    public Cursor get_all_sales_items_upload() {
        return database.rawQuery("select * from " + cart_items_table + " cit ," + cart_details + " cdt where cdt." + dbHelper.cart_status + " = " + dbHelper.ORDER_FINISHED + " AND cdt." + dbHelper.cart_id + " = cit." + dbHelper.c_i_id, new String[]{});
    }

    public Cursor get_purchase() {
        return database.rawQuery("select * from " + dbHelper.purchase_table, new String[]{});
    }

    public Cursor get_supplier() {
        return database.rawQuery("select * from " + dbHelper.supplier_table, new String[]{});
    }

    public Cursor get_feedback() {
        return database.rawQuery("select * from " + dbHelper.feedback_table + " ft, " + dbHelper.customer_table + " ct, " + dbHelper.cart_details + " cdt  where" + " ft.feedback_order_id=cdt.cart_id and cdt.cart_customer_id=ct.customer_id", new String[]{});
    }

    //last code from this date
    public Cursor get_feedback_items(String f_id) {
        return database.rawQuery("select * from  feedback_items_table,item_table  where item_table.item_id=feedback_items_table.feedback_items_id and feedback_items_table.feedback_id_id=?", new String[]{f_id});
    }

    public Cursor get_expense() {
        return database.rawQuery("select * from " + dbHelper.expense_table, new String[]{});
    }

    public Cursor get_expense_for_sync() {
        return database.rawQuery("select * from " + dbHelper.expense_table + " where " + dbHelper.e_upload_status + " =?", new String[]{dbHelper.UPLOAD_STATUS_FAILED});
    }

    public Cursor get_employee() {
        return database.rawQuery("select * from " + dbHelper.employee_table, new String[]{});
    }

    public void update_expense_upload_status(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.e_upload_status, dbHelper.UPLOAD_STATUS_SUCCESS);
        database.update(dbHelper.expense_table, contentValues, dbHelper.e_id + " = ? ", new String[]{id});


    }

    public void update_sales_details_upload_status(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.upload_status, dbHelper.UPLOAD_STATUS_SUCCESS);
        database.update(cart_details, contentValues, dbHelper.cart_id + " = ? ", new String[]{id});


    }

    public Cursor get_branch_feedbacks() {
        Cursor cursor=null;
        cursor=database.rawQuery("select avg(" + dbHelper.ambience_rating + ") as ambience ,avg(" + dbHelper.staff_rating + ") as staff from " + dbHelper.feedback_table, new String[]{});
        return cursor;
    }

    public Cursor get_branch_food_feedbacks() {
        Cursor cursor=null;
         cursor=database.rawQuery("select avg(" + dbHelper.rating + ") as food_rate  from " + dbHelper.feedback_items_table, new String[]{});
         return cursor;
    }


    //dashboard reports
    public String get_todays_total_sales() {
        String amount = "0";
        Cursor cursor = database.rawQuery("select sum(" + dbHelper.total + ") as total from " + dbHelper.cart_details + " where " + dbHelper.date + " = ?", new String[]{get_date()});
        cursor.moveToFirst();
        amount = cursor.getString(cursor.getColumnIndex("total"));
        if (amount == null) {
            amount = "0";
            return amount;
        }
        return amount;
    }

    public String get_todays_total_purchase() {
        String amount = "0";
        Cursor cursor = database.rawQuery("select sum(" + dbHelper.p_amount + ") as total from " + dbHelper.purchase_table + " where " + dbHelper.p_date + " = ?", new String[]{get_date()});
        cursor.moveToFirst();
        amount = cursor.getString(cursor.getColumnIndex("total"));
        if (amount == null) {
            amount = "0";
            return amount;
        }
        return amount;
    }
    public String get_todays_total_purchase_paid_amount() {
        String amount = "0";
        Cursor cursor = database.rawQuery("select sum(" + dbHelper.p_paid + ") as total from " + dbHelper.purchase_table + " where " + dbHelper.p_date + " = ?", new String[]{get_date()});
        cursor.moveToFirst();
        amount = cursor.getString(cursor.getColumnIndex("total"));
        if (amount == null) {
            amount = "0";
            return amount;
        }
        return amount;
    }

    public String get_todays_total_expense() {
        String amount = "0";
        Cursor cursor = database.rawQuery("select sum(" + dbHelper.e_amount + ") as total from " + dbHelper.expense_table + " where " + dbHelper.e_date + " = ?", new String[]{get_date()});
        cursor.moveToFirst();
        amount = cursor.getString(cursor.getColumnIndex("total"));
        if (amount == null) {
            amount = "0";
            return amount;
        }

        return amount;
    }

    public String get_todays_cash_in_hand() {
        long sales, expense, purchase, cash_in_hand = 0;
        sales = Long.parseLong(get_todays_total_sales());
        expense = Long.parseLong(get_todays_total_expense());
        purchase = Long.parseLong(get_todays_total_purchase_paid_amount());
        cash_in_hand = sales - (expense + purchase);

        return String.valueOf(cash_in_hand);
    }

    public Cursor get_top_items() {
        Cursor item_cursor = database.rawQuery("SELECT i.item_id, i.item_name,i.item_price,i.image\n" + "FROM `cart_items_table` AS c\n" + "    INNER JOIN `item_table` AS i\n" + "    ON c.`c_item_id` = i.`item_id`\n" + "GROUP BY c.`c_item_id`\n" + "ORDER BY SUM(c.`c_qty`) DESC, i.`item_name` ASC\n" + "LIMIT 10", new String[]{});
        return item_cursor;
    }

    //salary

    public Cursor get_all_employees_name() {
        Cursor employeee_names = database.rawQuery("select * from " + dbHelper.employee_table, new String[]{});
        return employeee_names;
    }

    public String check_salary(String empl_id, String month, String year) {
        String salary;
        Cursor cursor = database.rawQuery("select * from salary_table where empl_id=? and strftime('%m',giving_date) =? and strftime('%Y',giving_date)=?", new String[]{empl_id, month, year});
        Log.d("cursor count", String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            Toast.makeText(context, "already paid some", Toast.LENGTH_SHORT).show();

            cursor.moveToLast();
             salary = cursor.getString(cursor.getColumnIndex(dbHelper.s_balance));
            Log.d("balance of man", salary);


            return salary+"/"+get_salary(empl_id);
        } else {

            return get_salary(empl_id)+"/"+get_salary(empl_id);
        }
    }
    public String get_salary(String e_id){
       Cursor cursor=database.rawQuery("select * from employee_table where emp_id = ?",new String[]{e_id});
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(DBHelper.emp_salary));
            }

    public Cursor get_salary_details()
    {
        Cursor salary_details= database.rawQuery("select * from "+dbHelper.salary_table+" sl, "+dbHelper.employee_table+" et where et.emp_id=sl.empl_id ",new String[]{});
        return salary_details;

    }

    public long add_salary_details(String employee_id, String sa_date,String month,String year, String salary_amount_paid, String sal_balance) {

        Log.d("emp_id",employee_id);
        Cursor cursor=database.rawQuery("select * from "+dbHelper.salary_table+" where " +dbHelper.empl_id+" =? and strftime('%m',"+dbHelper.salary_date+") = "+month+" and   strftime('%Y',"+dbHelper.salary_date+") = "+year+" ",new String[]{employee_id});

        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.empl_id, employee_id);
        contentValues.put(dbHelper.giving_date, get_date());
        contentValues.put(dbHelper.salary_date, sa_date);
        contentValues.put(dbHelper.s_paid, salary_amount_paid);
        contentValues.put(dbHelper.s_balance, sal_balance);
        contentValues.put(dbHelper.status, "paid");
        contentValues.put(dbHelper.s_upload_status, 0);

        return database.insert(dbHelper.salary_table, null, contentValues);

    }

}

