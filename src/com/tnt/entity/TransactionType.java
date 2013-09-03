package com.tnt.entity;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

public class TransactionType implements Serializable{

	// id is generated by the database and set on the object automatically
	@DatabaseField(generatedId = true)
	int transactionTypeId;
	@DatabaseField
	String transactionTypeName;
	
	public TransactionType() {
		// for ORMLite
	}

	/**
	 * @return the transactionTypeId
	 */
	public int getTransactionTypeId() {
		return transactionTypeId;
	}

	/**
	 * @param transactionTypeId the transactionTypeId to set
	 */
	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionTypeName;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionTypeName = transactionType;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("transactionTypeId: ").append(transactionTypeId).append("  Transaction Type: ")
				.append(transactionTypeName);
		return sb.toString();
	}

}