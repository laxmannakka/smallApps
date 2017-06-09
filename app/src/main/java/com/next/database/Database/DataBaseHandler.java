package com.next.database.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.DecorContentParent;

import com.next.database.modal.Dept;
import com.next.database.modal.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by next on 24/3/17.
 */
public class DataBaseHandler extends SQLiteOpenHelper
{
    public static String DATABASE_NAME = "OfficeDetails";
    // Table Name
    public static String TABLE_NAME = "department";
    public  static String EMPLOYEE_TABLE_NAME ="employee";
    // for DepartMent  names and columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    public static int DATABASE_NUMBER = 3;

    // for Employee constants
    public  static final String EMP_ID ="empId";
    public static final String EMP_NAME="employeeName";
    public  static final String DATE="joiningDate";
    public static final String MOBILE_NUMBER="mobileNumber";
    public static  final String DEPART_MENT_ID="DepartmentId";


    public DataBaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_NUMBER);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String DepartMent_table = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER," + KEY_NAME + " TEXT not null)";
        db.execSQL(DepartMent_table);

        String Employee_table = "CREATE TABLE " + EMPLOYEE_TABLE_NAME + "("
                + DEPART_MENT_ID + " INTEGER," + EMP_ID +" INTEGER," + EMP_NAME + " TEXT not null," + DATE + " TEXT not null," +MOBILE_NUMBER + " TEXT not null" + ")";
        db.execSQL(Employee_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public void addDepartMentstoDb(Dept dept)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, dept.getGetDepartmentName()); // Contact Name
        values.put(KEY_ID, dept.getDepartmentId()); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_NAME,null, values);
        db.close(); // Closing database connection
    }

    public  void addEmployeeDetails(Employee employee)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DEPART_MENT_ID, employee.getDepartMentId());
        values.put(EMP_ID, employee.getEmpId());
        values.put(EMP_NAME,employee.getEmployeeName());
        values.put(DATE,employee.getJoiningDate());
        values.put(MOBILE_NUMBER,employee.getMobileNumber());

        // Inserting Row
        db.insert(EMPLOYEE_TABLE_NAME,null, values);
        db.close();
    }

    public List<Dept > getAllDepartMents() {

        List<Dept> deptArrayList = new ArrayList<Dept>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Dept dept = new Dept();
                dept.setDepartmentId(Integer.parseInt(cursor.getString(0)));
                dept.setGetDepartmentName(cursor.getString(1));
                // Adding contact to list
                deptArrayList.add(dept);
            } while (cursor.moveToNext());
        }

        // return contact list
        return deptArrayList;
    }
public  List<Employee> getAllEmployees()
{
    List<Employee> deptArrayList = new ArrayList<Employee>();
    String selectQuery = "SELECT  * FROM " + EMPLOYEE_TABLE_NAME;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);


    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
        do {
            Employee employee = new Employee();
            employee.setDepartMentId(Integer.parseInt(cursor.getString(0)));
            employee.setEmpId(Integer.valueOf(cursor.getString(1)));
            employee.setEmployeeName(cursor.getString(2));
            employee.setJoiningDate(cursor.getString(3));
            employee.setMobileNumber(cursor.getString(4));

            deptArrayList.add(employee);
        } while (cursor.moveToNext());
    }

    // return contact list
    return deptArrayList;
}
public void dropTable()
{
    SQLiteDatabase db = this.getWritableDatabase();
    String clearDBQuery = "DELETE FROM "+TABLE_NAME;
    String clearDBQuery2 = "DELETE FROM "+EMPLOYEE_TABLE_NAME;

    db.execSQL(clearDBQuery);
    db.execSQL(clearDBQuery2);
}
}
