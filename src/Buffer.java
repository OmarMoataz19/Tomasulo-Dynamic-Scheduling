public class Buffer {

	private BufferSlot[] buffer;

	public Buffer(int size, String type) {
		this.buffer = new BufferSlot[size];

		switch (type) {
		case "load":
			for (int i = 0; i < size; i++) {
				this.buffer[i] = new BufferSlot("L" + i);
			}
			break;
		case "store":
			for (int i = 0; i < size; i++) {
				this.buffer[i] = new BufferSlot("S" + i);
			}
			break;
		}

	}

	public BufferSlot getBufferSlot(int index) {
		return this.buffer[index];
	}

	public void setBufferSlot(int index, BufferSlot bufferSlot) {
		this.buffer[index] = bufferSlot;
	}

	public BufferSlot[] getBuffer() {
		return buffer;
	}

	public int getSize() {
		return this.buffer.length;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < this.buffer.length; i++) {
			result += this.buffer[i].toString() + " \n";
		}
		return result;
	}

	public void clearBufferSlot(int index) {
		this.buffer[index].setBusy(false);
		this.buffer[index].setReady(true);
		this.buffer[index].setVj(0.0f);
		this.buffer[index].setQj("0");
		this.buffer[index].setA(0);
	}

	public int getFreeBufferSlot() {
		for (int i = 0; i < this.buffer.length; i++) {
			if (this.buffer[i].isBusy() == false) {
				return i;
			}
		}
		return -1;
	}
}
