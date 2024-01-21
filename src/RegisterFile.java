public class RegisterFile {

	private Register[] registers;

	public RegisterFile() {
		this.registers = new Register[32];
		for (int i = 0; i < 32; i++) {
			this.registers[i] = new Register("F" + i);
		}
	}

	public Register getRegister(int index) {
		return this.registers[index];
	}

	public void setRegister(int index, float content, String tag) {
		this.registers[index].setContent(content);
		this.registers[index].setTag(tag);
	}

	public Register[] getRegisters() {
		return registers;
	}

	public int getSize() {
		return this.registers.length;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < this.registers.length; i++) {
			result += this.registers[i].getName() + ": " + this.registers[i].getContent() + " , Tag: "
					+ this.registers[i].getTag() + " \n";
		}
		return result;
	}
}
