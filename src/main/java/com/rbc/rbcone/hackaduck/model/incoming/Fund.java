package com.rbc.rbcone.hackaduck.model.incoming;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class Fund {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	public String id;
	
	public String domiciliation;
	public String name;

	
	/*@ManyToMany(fetch = FetchType.EAGER, targetEntity=Resources.class)
    @JoinTable(name = "clients_access_resources", joinColumns = { @JoinColumn(name = "client_id") }, inverseJoinColumns = { @JoinColumn(name = "res_id") })
    public Set<Resources> getClientResources() {
        return this.clientResources;
    }*/
	
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = AccountHolder.class)
	@JoinTable(name="FUNDS_ACCOUNTHOLDERS", 
			   joinColumns=@JoinColumn(name="fund_id",referencedColumnName="id"),
			   inverseJoinColumns=@JoinColumn(name="account_holder_id",referencedColumnName="ID")
				)
	public Set<AccountHolder> accountHolders;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="PROMOTER_ID")
	public Promoter promoter;
	
	public String getId() {
		return id;
	}
	@JsonSetter("fundId")
	public void setId(String id) {
		this.id = id;
	}
	public String getDomiciliation() {
		return domiciliation;
	}
	
	@JsonSetter("domicialition")
	public void setDomiciliation(String domiciliation) {
		this.domiciliation = domiciliation;
	}
	public String getName() {
		return name;
	}
	
	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}

	public Set<AccountHolder> getAccountHolders() {
		return accountHolders;
	}
	
	@JsonSetter("AccountHolders")
	public void setAccountHolders(Set<AccountHolder> accountHolders) {
		this.accountHolders = accountHolders;
	}
	public Promoter getPromoter() {
		return promoter;
	}
	public void setPromoter(Promoter promoter) {
		this.promoter = promoter;
	}
	
}
