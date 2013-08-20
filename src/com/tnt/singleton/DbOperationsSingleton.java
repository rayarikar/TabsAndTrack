package com.tnt.singleton;

import com.tnt.dboperation.UserDetailsDbOperations;

public class DbOperationsSingleton {

	private static UserDetailsDbOperations userDetailsDbOpsObj;
	
	/**
	 * returns a UserDetailDbOperations instance. 
	 * @author Rohan
	 * @return
	 */
	public static UserDetailsDbOperations getUserDetailsDbOperationsInstance(){
		
		if(userDetailsDbOpsObj != null){
			return userDetailsDbOpsObj;
		}
		return (new UserDetailsDbOperations());
	}
	
}
