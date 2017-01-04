package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RiskEntity represents the distribution of the risk of a specific legal fund
 * relative to an entity. That entity can either represents a geographical
 * region or either a country.ß
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RiskEntity {
    /**
     * The legal fund of which its risk is described by this instance.
     */
    private LegalFund fund;
    /**
     * The unique identifier of the entity (like a geographical region or like an ISO country code).
     */
    private String entityId;
    /**
     * Information about the low risk category.
     */
    private Risk low;
    /**
     * Information about the medium risk category.
     */
    private Risk medium;
    /**
     * Information about the high risk category.
     */
    private Risk high;

}
