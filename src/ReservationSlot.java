public class ReservationSlot {

	private String name;
	private String op;
	private float vj;
	private float vk;
	private String qj;
	private String qk;
	private int a;
	private int timer;
	private boolean busy;
	private boolean ready;

	public ReservationSlot(String name) {
		this.name = name;
		this.op = "";
		this.vj = 0.0f;
		this.vk = 0.0f;
		this.qj = "0";
		this.qk = "0";
		this.a = 0;
		this.busy = false;
		this.ready = true;
	}

	public String getName() {
		return name;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public float getVj() {
		return vj;
	}

	public void setVj(float vj) {
		this.vj = vj;
	}

	public float getVk() {
		return vk;
	}

	public void setVk(float vk) {
		this.vk = vk;
	}

	public String getQj() {
		return qj;
	}

	public void setQj(String qj) {
		this.qj = qj;
	}

	public String getQk() {
		return qk;
	}

	public void setQk(String qk) {
		this.qk = qk;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
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

	public String toString() {
		return "Name: " + this.name + " Op: " + this.op + " Vj: " + this.vj + " Vk: " + this.vk + " Qj: " + this.qj
				+ " Qk: " + this.qk + " A: " + this.a + " Busy: " + this.busy + " Ready: " + this.ready;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
}
