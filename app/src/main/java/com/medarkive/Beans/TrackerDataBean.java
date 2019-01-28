package com.medarkive.Beans;

public class TrackerDataBean {
	
	String id;
	String link;
	String title;
	String spentTime;
	String trackType;
	
	public TrackerDataBean(String id ,String title , String link , String spentTime){
		this.id=id;
		this.link=link;
		this.title=title;
		this.spentTime=spentTime;
	}
	
	public TrackerDataBean(){}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSpentTime() {
		return spentTime;
	}

	public void setSpentTime(String spentTime) {
		this.spentTime = spentTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTrackType() {
		return trackType;
	}

	public void setTrackType(String trackType) {
		this.trackType = trackType;
	}

}
