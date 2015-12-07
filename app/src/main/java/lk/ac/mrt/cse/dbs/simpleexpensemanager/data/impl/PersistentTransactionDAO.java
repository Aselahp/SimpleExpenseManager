package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Database.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by H.P. Asela on 12/4/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {

    DBHelper help = null;

    public PersistentTransactionDAO(Context context){
        this.help = DBHelper.getInstance(context);
    }


    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        help.insertDataLogs(accountNo,date.toString(),expenseType.toString(),amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        return help.getAllLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> transactions = help.getAllLogs();
        int capacity= transactions.size();
        if (capacity <= limit) {
            return transactions;
        }
        return transactions.subList(capacity-limit,capacity);
    }

}