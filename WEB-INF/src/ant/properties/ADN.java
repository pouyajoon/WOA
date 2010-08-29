/**
 * 
 */
package ant.properties;

import java.util.HashMap;


public class ADN 
{
	public HashMap<String, Property> properties;
	
	public ADN() 
	{
		super();
		properties = new HashMap<String, Property>();
	}
	
	public boolean isEmpty(String name)
	{
		return (getCurrent(name) == 0.0f);
	}
	
	public boolean hasMax(String name)
	{
		 return getCurrent(name) == getMax(name);
	}
	
	/**
	 * @param name : the property's name
	 * @param property : the property
	 */
	public void set(String name, Property property) {
		properties.put(name, property);
	}
	
	public void updateCurrent(String name, float this_number)
	{
		properties.get(name).setCurrent(this_number);
	}
	
	public void reduce(String name, float this_number)
	{
		float n = properties.get(name).getCurrent();
		properties.get(name).setCurrent(n - this_number);	
		if (getCurrent(name) < 0)
			updateCurrent(name, 0);
	}

	public void increase(String name, float this_number)
	{
		float n = properties.get(name).getCurrent();
		properties.get(name).setCurrent(n + this_number);	
		if (getCurrent(name) > getMax(name))
			updateCurrent(name, getMax(name));
	}
	
	public String toString(String n)	{
		return getCurrent(n) + "/" + getMax(n);
	}
	
	
	public float getRatio(String n)	{
		if (properties.containsKey(n))
			return properties.get(n).getRatio();
		return 0;
	}
	
	public float getCurrent(String n)	{
		if (properties.containsKey(n))
			return properties.get(n).getCurrent();
		return 0;				
	}

	public float getMax(String n)	{
		return properties.get(n).getMax();		
	}	
}
