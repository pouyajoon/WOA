package org.jbox2d.testbed;

interface KeyPressFunction {
	void function();
}

class KeyPressEvent {
	char key = (char) 0;
	int keyCode = 0;
	String keyString; // Ex: SHIFT
	String description; // Ex : Turns the camera to the right
	KeyPressFunction function;

	KeyPressEvent(char key, String keyString, String description,
			KeyPressFunction function) {
		this.key = key;
		this.keyString = keyString;
		this.description = description;
		this.function = function;
	}

	KeyPressEvent(int keyCode, String keyString, String description,
			KeyPressFunction function) {
		this.keyCode = keyCode;
		this.function = function;
	}

	public boolean test(char key, int keyCode) {
		if (this.key != 0 && this.key == key || this.keyCode != 0
				&& this.keyCode == keyCode) {
			this.function.function();
			return true;
		}
		return false;
	}
}
