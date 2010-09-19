package vnd.world;

import java.io.Serializable;

import org.jbox2d.common.Vec2;

public class Position implements Serializable {

	public float x;
	public float y;
	public Integer _map;

	public Position(Integer map_num, float x, float y) {
		setPos(map_num, x, y);
	}

	public Position clone() {
		return new Position(getPos().get_map(), getPos().getX(), getPos()
				.getY());
	}

	@Override
	public String toString() {
		return "[" + get_map() + "," + getX() + "," + getY() + "]";
	}

	public Position getPos() {
		return this;
	}

	public void setPos(Integer map_num, float x, float y) {
		setX(x);
		setY(y);
		set_map(map_num);
	}

	public Vec2 getVec2() {
		return new Vec2(getX(), getY());
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void set_map(Integer _map) {
		this._map = _map;
	}
	//	git test for comments
	public Integer get_map() {
		return _map;
	}
}
