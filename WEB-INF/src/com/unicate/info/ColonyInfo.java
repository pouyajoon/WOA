package com.unicate.info;

import vnd.woaobject.properties.Property;

public class ColonyInfo
{
	public Integer antNumInteger = 0;
	public float food_current = 0;
	public float food_max = 0;
	
	public ColonyInfo(Integer _antNumber, Property food_storage)
	{
		antNumInteger = _antNumber;
		food_current = food_storage.getCurrent();
		food_max = food_storage.getMax();
	}
	
	@Override
	public String toString()
	{
		return antNumInteger + ", [" + food_current + "/" + food_max + "]";
	}
}