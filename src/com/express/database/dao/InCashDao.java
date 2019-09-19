package com.express.database.dao;

import com.express.model.InCash;

public interface InCashDao {
	
	public Long getInCashNum();

	public void setRecord2finsh();
	
	public InCash getInCash(String inCashId);

	void updateInCash(InCash inCash);
}
