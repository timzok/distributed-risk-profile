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
     * The number of investors in this risk category.
     */
	private int investorCount;
    /**
     * The asset value in EUR.
     */
    private double assetValue;
    /**
     * The global asset value in EUR.
     */
    private double globalAssetValue;
    
    public Risk() {

    }
}
