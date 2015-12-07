package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Database.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by H.P. Asela on 12/4/2015.
 */
public class PersistentAccountDAO implements AccountDAO {
    DBHelper help = null;

    public PersistentAccountDAO(Context context){
        this.help = DBHelper.getInstance(context);
    }


    @Override
    public List<String> getAccountNumbersList() {
        return help.getAllAccountnumber();
    }

    @Override
    public List<Account> getAccountsList() {
        return help.getAllAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return help.getDataAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        help.insertAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        help.deleteAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
            help.updateBalance(accountNo,expenseType,amount);
    }
}
