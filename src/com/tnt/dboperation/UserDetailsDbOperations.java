package com.tnt.dboperation;

import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.entity.UserDetails;

public class UserDetailsDbOperations extends OrmLiteBaseActivity<DatabaseHelper>{

	// get UserDetails dao
	private RuntimeExceptionDao<UserDetails, Integer> userDetailsDao;
	
	/**
	 * this method just initializes the userDetailsDao
	 * @author Rohan
	 */
	private void initializeUserDetailsDoa(){
		try{
		userDetailsDao = getHelper().getUserDetailsDao();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * this method returns a list containing all the user details
	 * @author Rohan
	 * @return
	 */
	
	public List<UserDetails> getAllUserDetails(){
		List<UserDetails> userDetailsList = null;
		try{
			// get the UserDetailsDoa
			initializeUserDetailsDoa();
			// query for all of the data objects in the UserDetails table
			userDetailsList = userDetailsDao.queryForAll();
			// our string builder for building the content-view
			} catch (Exception e){
				e.printStackTrace();
			}
		return userDetailsList; 
	}

}
