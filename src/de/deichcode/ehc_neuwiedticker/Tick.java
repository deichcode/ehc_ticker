package de.deichcode.ehc_neuwiedticker;

public class Tick {
	private String headline;
	private String time;
	private String text;
	
	public Tick (String headline, String time, String text) {
		this.headline = headline;
		this.time = time;
		this.text = text;
	}
	
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
