/**
 * 
 */
package ant.properties;

public class Property
{
	public float min;
	public float max;
	public float current;
	public String type;

	public Property(String _type, float _max) 
	{
		setMin(0);
		setMax(_max);
		setCurrent(_max);
		setType(_type);
	}	

	@Override
	public String toString()	{
		return "[" + type + ":" + current + "/" + max + "]";
	}
	
	/**
	 * @return the min
	 */
	public float getMin() {
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(Integer min) {
		this.min = min;
	}
	/**
	 * @return the max
	 */
	public float getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(float max) {
		this.max = max;
	}
	/**
	 * @return the current
	 */
	public float getCurrent() {
		return current;
	}
	/**
	 * @param current the current to set
	 */
	public void setCurrent(float current) {
		this.current = current;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	public float getRatio()	{
		return getCurrent() / getMax();
	}
}
