package com.express.database.dao;

import java.util.List;

import com.express.model.AreaNorm;

public interface AreaNormDao {
	
	public List<AreaNorm> getValue(String area);
	
	public List<AreaNorm> getAreaAreaNorm();
	
	public List<AreaNorm> getAreaNorms(String name);
	
	public boolean setAreaPrice(AreaNorm area);

}
