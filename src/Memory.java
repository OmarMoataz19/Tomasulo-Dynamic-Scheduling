public class Memory {
	private float[] memory;

	public Memory(int size) {
		this.memory = new float[size];
	}

	public float getMemory(int index) {
		return this.memory[index];
	}

	public void setMemory(int index, float content) {
		this.memory[index] = content;
	}

	public float[] getMemory() {
		return memory;
	}

	public int getSize() {
		return this.memory.length;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < this.memory.length; i++) {
			result += "M" + i + ": " + this.memory[i] + " \n";
		}
		return result;
	}
}
