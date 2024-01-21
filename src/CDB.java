public class CDB {

	private float content;
	private String tag;
	private boolean busy;

	public CDB() {
		this.content = 0;
		this.tag = "0";
		this.busy = false;
	}

	public float getContent() {
		return content;
	}

	public void setContent(float content) {
		this.content = content;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public String toString() {
		return "CDB" + "\n" + "Data: " + this.content + " " + "Tag: " + this.tag;
	}
}
