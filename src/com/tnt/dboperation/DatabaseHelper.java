package com.tnt.dboperation;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tnt.R;
import com.tnt.entity.Account;
import com.tnt.entity.Contact;
import com.tnt.entity.GroupTransaction;
import com.tnt.entity.PersonalTransaction;
import com.tnt.entity.Transaction;
import com.tnt.entity.TransactionType;
import com.tnt.entity.UserDetails;
import com.tnt.utility.ExpenseUtility;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application
	private static final String DATABASE_NAME = "TabsAndTrack.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the UserDetails table
	private RuntimeExceptionDao<UserDetails, Integer> userDetailsRuntimeDao = null;
	private RuntimeExceptionDao<Account, Integer> accountRuntimeDao = null;
	private RuntimeExceptionDao<TransactionType, Integer> transactionTypeRuntimeDao = null;
    private RuntimeExceptionDao<Contact, Integer> contactRuntimeExceptionDao = null;
    private RuntimeExceptionDao<Transaction, Integer> transactionRuntimeExceptionDao = null;
    private RuntimeExceptionDao<GroupTransaction, Integer> groupTransactionRuntimeExceptionDao = null;
    private RuntimeExceptionDao<PersonalTransaction, Integer> personalTransactionRuntimeExceptionDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, UserDetails.class);
			TableUtils.createTable(connectionSource, Account.class);
			TableUtils.createTable(connectionSource, TransactionType.class);
			TableUtils.createTable(connectionSource, Contact.class);
			TableUtils.createTable(connectionSource, Transaction.class);
			TableUtils.createTable(connectionSource, GroupTransaction.class);
			TableUtils.createTable(connectionSource, PersonalTransaction.class);

			// add default values in Transaction
			addDefaultTransactionTypesToDb();
					
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds the default values for Transaction table
	 */
	private void addDefaultTransactionTypesToDb() {
		try {
			transactionTypeRuntimeDao = getTransactionTypeDao();

			List<TransactionType> transactionTypes = ExpenseUtility
					.getTransactionTypeSpinnerDefault();

			for (TransactionType transactionType : transactionTypes) {
				transactionTypeRuntimeDao.create(transactionType);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, UserDetails.class, true);
			TableUtils.dropTable(connectionSource, Account.class, true);
			TableUtils.dropTable(connectionSource, TransactionType.class, true);
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, Transaction.class, true);
            TableUtils.dropTable(connectionSource, GroupTransaction.class, true);
            TableUtils.dropTable(connectionSource, PersonalTransaction.class, true);
            // after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our UserDetails class. It
	 * will create it or just give the cached value.
	 */
	public RuntimeExceptionDao<UserDetails, Integer> getUserDetailsDao()
			throws SQLException {
		if (userDetailsRuntimeDao == null) {
			userDetailsRuntimeDao = getRuntimeExceptionDao(UserDetails.class);
		}
		return userDetailsRuntimeDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our Account class. It will
	 * create it or just give the cached value.
	 */
	public RuntimeExceptionDao<Account, Integer> getAccountDao()
			throws SQLException {
		if (accountRuntimeDao == null) {
			accountRuntimeDao = getRuntimeExceptionDao(Account.class);
		}
		return accountRuntimeDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our TransactionType class.
	 * It will create it or just give the cached value.
	 */
	public RuntimeExceptionDao<TransactionType, Integer> getTransactionTypeDao()
			throws SQLException {
		if (transactionTypeRuntimeDao == null) {
			transactionTypeRuntimeDao = getRuntimeExceptionDao(TransactionType.class);
		}
		return transactionTypeRuntimeDao;
	}

    /**
     * Returns the Database Access Object (DAO) for our Contact class.
     * It will create it or just give the cached value.
     */
    public RuntimeExceptionDao<Contact, Integer> getContactDao()
            throws SQLException {
        if (contactRuntimeExceptionDao == null) {
            contactRuntimeExceptionDao = getRuntimeExceptionDao(Contact.class);
        }
        return contactRuntimeExceptionDao;
    }
    
    /**
     * Returns the Database Access Object (DAO) for Transaction class.
     * It will create it or just give the cached value.
     */
    public RuntimeExceptionDao<Transaction, Integer> getTransactionDao()
            throws SQLException {
        if (transactionRuntimeExceptionDao == null) {
            transactionRuntimeExceptionDao = getRuntimeExceptionDao(Transaction.class);
        }
        return transactionRuntimeExceptionDao;
    }
    
    /**
     * Returns the Database Access Object (DAO) for ContactTransaction class.
     * It will create it or just give the cached value.
     */
    public RuntimeExceptionDao<GroupTransaction, Integer> getGroupTransactionDao()
            throws SQLException {
        if (groupTransactionRuntimeExceptionDao == null) {
            groupTransactionRuntimeExceptionDao = getRuntimeExceptionDao(GroupTransaction.class);
        }
        return groupTransactionRuntimeExceptionDao;
    }
    
    /**
     * Returns the Database Access Object (DAO) for PersonalTransaction class.
     * It will create it or just give the cached value.
     */
    public RuntimeExceptionDao<PersonalTransaction, Integer> getPersonalTransactionDao()
            throws SQLException {
        if (personalTransactionRuntimeExceptionDao == null) {
        	personalTransactionRuntimeExceptionDao = getRuntimeExceptionDao(PersonalTransaction.class);
        }
        return personalTransactionRuntimeExceptionDao;
    }

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		userDetailsRuntimeDao = null;
		accountRuntimeDao = null;
		transactionTypeRuntimeDao = null;
        contactRuntimeExceptionDao  = null;
        transactionRuntimeExceptionDao = null;
        groupTransactionRuntimeExceptionDao = null;
        personalTransactionRuntimeExceptionDao = null;
	}
}
