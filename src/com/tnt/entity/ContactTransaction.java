package com.tnt.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class ContactTransaction implements Serializable{
	// id is generated by the database and set on the object automatically
	@DatabaseField(generatedId = true)
	int contactTransactionId;
	@DatabaseField
	int transactionId;
	@DatabaseField
	int contactId;
	@DatabaseField
	double contactAmount;
	
	public ContactTransaction() {
		// TODO Auto-generated constructor stub
		// for ORMLite
	}
	
	/**
	 * @return the contactTransactionId
	 */
	public int getContactTransactionId() {
		return contactTransactionId;
	}

	/**
	 * @param contactTransactionId the contactTransactionId to set
	 */
	public void setContactTransactionId(int contactTransactionId) {
		this.contactTransactionId = contactTransactionId;
	}

	/**
	 * @return the transactionId
	 */
	public int getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the contactId
	 */
	public int getContactId() {
		return contactId;
	}

	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	/**
	 * @return the contactAmount
	 */
	public double getContactAmount() {
		return contactAmount;
	}

	/**
	 * @param contactAmount the contactAmount to set
	 */
	public void setContactAmount(double contactAmount) {
		this.contactAmount = contactAmount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Contact Transaction Id: ").append(contactTransactionId).append("  Transaction Id: ")
				.append(transactionId).append("Contact Id: ").append(contactId).append("  Contact Amount: ")
				.append(contactAmount);
		return sb.toString();
	}

}