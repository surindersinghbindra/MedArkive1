package com.medarkive.Beans;

import android.os.Parcel;
import android.os.Parcelable;

public class PDFTrackerBean implements Parcelable {
	
	private int track_id;

	private String pdf_id;

	private String ip_address;

	private String starttime;

	private String country;

	private String city;

	private String address;

	private String page;

	private String endtime;

	private String interval_sec;

	private String file_id;

	private int offline;

	public PDFTrackerBean(int track_id ,String pdf_id, String ip_address, 
			String starttime, String country, String city, String address,
			String page, String endtime, String interval_sec, String file_id,
			int offline) {
		super();
		this.track_id=track_id;
		this.pdf_id = pdf_id;
		this.ip_address = ip_address;
		this.starttime = starttime;
		this.country = country;
		this.city = city;
		this.address = address;
		this.page = page;
		this.endtime = endtime;
		this.interval_sec = interval_sec;
		this.file_id = file_id;
		this.offline = offline;
	}

	public PDFTrackerBean() {
	}

	public int getTrack_id() {
		return track_id;
	}

	public void setTrack_id(int track_id) {
		this.track_id = track_id;
	}

	public String getPdf_id() {
		return pdf_id;
	}

	public void setPdf_id(String pdf_id) {
		this.pdf_id = pdf_id;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getInterval_sec() {
		return interval_sec;
	}

	public void setInterval_sec(String interval_sec) {
		this.interval_sec = interval_sec;
	}

	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public int getOffline() {
		return offline;
	}

	public void setOffline(int offline) {
		this.offline = offline;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}
}
