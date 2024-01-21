public class BufferSlot {

	private String name;
	private boolean busy;
	private boolean ready;
	private float vj;
	private String qj;
	private int timer;

	private int a;

	public BufferSlot(String name) {
		this.name = name;
		this.busy = false;
		this.ready = true;
		this.vj = 0.0f;
		this.qj = "0";
		this.a = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public float getVj() {
		return vj;
	}

	public void setVj(float vj) {
		this.vj = vj;
	}

	public String getQj() {
		return qj;
	}

	public void setQj(String qj) {
		this.qj = qj;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public String toString() {
		return "BufferSlot [name=" + name + ", busy=" + busy + ", ready=" + ready + ", vj=" + vj + ", qj=" + qj + ", a="
				+ a + "]";
	}

}
