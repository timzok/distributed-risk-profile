package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Risk provides information about a specific risk category of a fund. The risk
 * categories are Low, Medium and High.
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class Risk {
    /**
     * The risk value.
     */
    private double riskValue;
    /**
     * The number of account holders of the fund being in this risk  category.
     */
    private int accountHolderCount;
    /**
     * The percentage of the fund held by the account holders that are in this risk category.
     */
    private int accountHolderPercentOwnership;

    public Risk() {

    }
}
