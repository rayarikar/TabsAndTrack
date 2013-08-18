package com.tnt.utility;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.tnt.entity.UserDetails;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	private static final Class<?>[] classes = new Class[] {
		UserDetails.class,
	};
	public static void main(String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt");
	}
}
