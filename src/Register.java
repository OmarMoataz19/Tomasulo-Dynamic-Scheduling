public class Register {
	private String name;
	private String tag;
	private float content;

	public Register(String name) {
		this.name = name;
		this.tag = "0";
		this.content = 0;
	}

	public void setContent(float content) {
		this.content = content;

	}

	public float getContent() {
		return this.content;
	}

	public String getName() {
		return this.name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
