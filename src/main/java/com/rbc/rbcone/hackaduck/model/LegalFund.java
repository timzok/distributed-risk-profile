package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * LegalFund represents a legal fund.
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class LegalFund {
    /**
     * The fund identifier.
     */
    private String id;
    /**
     * The fund name.
     */
    private String name;

}
