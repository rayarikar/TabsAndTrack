package com.tnt.utility;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.tnt.entity.*;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	private static final Class<?>[] classes = new Class[] {
		UserDetails.class,
		Account.class,
		TransactionType.class,
        Contact.class,
        Transaction.class,
        GroupTransaction.class,
        PersonalTransaction.class
	};
	public static void main(String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt");
	}
}
