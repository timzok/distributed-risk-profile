package com.rbc.rbcone.hackaduck.model;

/**
 * Risk provides information about a specific risk category of a fund. The risk
 * categories are Low, Medium and High.
 * @author user
 *
 */
public class Risk {
	/** The risk value. */
	private double riskValue;
	/** The number of account holders of the fund being in this risk  category.*/
	private int accountHolderCount;
	/** The percentage of the fund held by the account holders that are in this risk category. */
	private int accountHolderPercentOwnership;
	
	public Risk() {
		
	}
	
	public Risk(double aRiskValue, int anAccountHolderCount, int anAccountHolderPercentOwnership) {
		riskValue = aRiskValue;
		accountHolderCount = anAccountHolderCount;
		accountHolderPercentOwnership = anAccountHolderPercentOwnership;
	}
	
	//**************************************************************************
	//**** Getters / Setters ***************************************************
	//**************************************************************************
	
	public double getRiskValue() {
		return riskValue;
	}
	public void setRiskValue(double riskValue) {
		this.riskValue = riskValue;
	}
	public int getAccountHolderCount() {
		return accountHolderCount;
	}
	public void setAccountHolderCount(int accountHolderCount) {
		this.accountHolderCount = accountHolderCount;
	}
	public int getAccountHolderPercentOwnership() {
		return accountHolderPercentOwnership;
	}
	public void setAccountHolderPercentOwnership(int accountHolderPercentOwnership) {
		this.accountHolderPercentOwnership = accountHolderPercentOwnership;
	}
	
	
}
