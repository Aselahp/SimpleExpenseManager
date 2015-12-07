package lk.ac.mrt.cse.dbs.simpleexpensemanager.Database;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by H.P. Asela on 12/3/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper database = null;
    public static final String DATABASE_NAME = "130045U.db";
    public static final String ACCOUNT_TABLE_NAME = "Account"
    ;
    public static final String ACCOUNT_COLUMN_accountNo = "accountNo";
    public static final String ACCOUNT_COLUMN_bank = "bank";
    public static final String ACCOUNT_COLUMN_holder = "holder";
    public static final String ACCOUNT_COLUMN_balance = "balance";

    public static final String LOGS_TABLE_NAME = "Logs";
    public static final String LOGS_COLUMN_accountNo = "accountNum";
    public static final String LOGS_COLUMN_date = "date";
    public static final String LOGS_COLUMN_type = "type";
    public static final String LOGS_COLUMN_amount = "amount";

    private HashMap hp;

    private DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    public static DBHelper getInstance(Context context){
        if(database==null){synchronized (DBHelper.class){database = new DBHelper(context);}}
        return database;
    }

    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table Account " +
                        "(accountNo text primary key, bank text not null,holder text not null,balance real not null)"
        );
        db.execSQL(
                "create table Logs " +
                        "(accountNum text not null, date text not null,type text not null,amount real not null,foreign key Account references accountNo)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Account");
        db.execSQL("DROP TABLE IF EXISTS Logs");
        onCreate(db);
    }

    public boolean insertDataAccount  (String accountNo, String bank, String holder,double balance )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", accountNo);
        contentValues.put("bank", bank);
        contentValues.put("holder", holder);
        contentValues.put("balance", balance);
        db.insert("Account", null, contentValues);
        return true;
    }
    public boolean insertAccount  (Account acc )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", acc.getAccountNo());
        contentValues.put("bank", acc.getBankName());
        contentValues.put("holder", acc.getAccountHolderName());
        contentValues.put("balance", acc.getBalance());
        db.insert("Account", null, contentValues);
        return true;
    }
    public boolean insertDataLogs  (String accountNo, String date, String type,double amount )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNum", accountNo);
        contentValues.put("date", date);
        contentValues.put("type", type);
        contentValues.put("amount", amount);
        db.insert("Logs", null, contentValues);
        return true;
    }

    public Account getDataAccount(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from  Account where accountNo=" + id + "", null);
        Account account=new Account(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_accountNo)),res.getString(res.getColumnIndex(ACCOUNT_COLUMN_bank)),res.getString(res.getColumnIndex(ACCOUNT_COLUMN_holder)), Double.parseDouble(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_balance))));
        return account;
    }


    public int numberOfRowsLogs(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOGS_TABLE_NAME);
        return numRows;
    }
    public int numberOfRowsAccount(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACCOUNT_TABLE_NAME);
        return numRows;
    }

    public boolean updateAccount (String id, String bank, String holder,double balance)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", id);
        contentValues.put("bank", bank);
        contentValues.put("holder", holder);
        contentValues.put("balance", balance);
        db.update("Account", contentValues, "accountNo = ? ", new String[] { id } );
        return true;
    }


    public Integer deleteAccount (String account)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Account",
                "accountNo = ? ",
                new String[] { account });
    }

    public List<String> getAllAccountnumber()
    {
        List<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_accountNo)));
            res.moveToNext();
        }
        return array_list;
    }
    public List<Account> getAllAccounts()
    {
        List<Account> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Account account=new Account(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_accountNo)),res.getString(res.getColumnIndex(ACCOUNT_COLUMN_bank)),res.getString(res.getColumnIndex(ACCOUNT_COLUMN_holder)), Double.parseDouble(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_balance))));
           array_list.add(account);
            res.moveToNext();
        }
        return array_list;
    }
    public List<Transaction> getAllLogs() throws ParseException {
        List<Transaction> array_list = new ArrayList<Transaction>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Logs", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Transaction trans=new Transaction((getDate(res.getString(res.getColumnIndex(LOGS_COLUMN_date)))),res.getString(res.getColumnIndex(LOGS_COLUMN_accountNo)), ExpenseType.valueOf(res.getString(res.getColumnIndex(LOGS_COLUMN_type))),Double.parseDouble(res.getString(res.getColumnIndex(LOGS_COLUMN_amount))));
            array_list.add(trans);
            res.moveToNext();
        }
        return array_list;
    }
    public static Date getDate(String datenew){
        Date date = null;
        DateFormat fomatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        try {
            date = fomatter.parse(datenew);
        } catch (ParseException ex) {

        }
        return date;
    }
    public boolean updateBalance(String accountNo, ExpenseType expenseType, double amount){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args =  { accountNo};
        Cursor res =  db.rawQuery("select * from  Account where accountNo=" + accountNo + "", null);
        if(res.getCount()==0) {
            db.close();
            return false;
        }
        res.moveToFirst();
        Double newBalance = res.getDouble(res.getColumnIndex(ACCOUNT_COLUMN_balance));
        switch (expenseType) {
            case EXPENSE:
                newBalance -= amount;
                break;
            case INCOME:
                newBalance += amount;
                break;
        }
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", res.getString(res.getColumnIndex(ACCOUNT_COLUMN_accountNo)));
        contentValues.put("bank", res.getString(res.getColumnIndex(ACCOUNT_COLUMN_bank)));
        contentValues.put("holder", res.getString(res.getColumnIndex(ACCOUNT_COLUMN_holder)));
        contentValues.put("balance", newBalance);
        db.update("Account", contentValues, "accountNo = ? ", new String[] { accountNo } );
        return true;

    }
}
