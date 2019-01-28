package com.medarkive.Beans;

import android.os.Parcel;
import android.os.Parcelable;

public class CPDBean  implements Parcelable{
	
	private String credit_limit;
	
	private String event_description;
	
	private String cpd_id;
	
	private String clinical;
	
	private String credit;
	
	private String cpd_event_id;
	
	private String created;
	
	private String cpd_user_type;
	
	private String name;
	
	private String cpd_types;
	
	private String type_name;
	
	private String cpd_type_id;
	
	private String type_date;
	
	private String credit_detail;
	
	private float credit_percentage;
	
	public CPDBean(){}
	
	public CPDBean(String event_description,
			String cpd_id, String clinical, String credit, String cpd_event_id,
			String created, String cpd_user_type, String name,
			String type_name, String cpd_type_id,
			String type_date) {
		this.event_description = event_description;
		this.cpd_id = cpd_id;
		this.clinical = clinical;
		this.credit = credit;
		this.cpd_event_id = cpd_event_id;
		this.created = created;
		this.cpd_user_type = cpd_user_type;
		this.name = name;
		this.type_name = type_name;
		this.cpd_type_id = cpd_type_id;
		this.type_date = type_date;
	}

	public String getEvent_description() {
		return event_description;
	}

	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}

	public String getCpd_id() {
		return cpd_id;
	}

	public void setCpd_id(String cpd_id) {
		this.cpd_id = cpd_id;
	}

	public String getClinical() {
		return clinical;
	}

	public void setClinical(String clinical) {
		this.clinical = clinical;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getCpd_event_id() {
		return cpd_event_id;
	}

	public void setCpd_event_id(String cpd_event_id) {
		this.cpd_event_id = cpd_event_id;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCpd_user_type() {
		return cpd_user_type;
	}

	public void setCpd_user_type(String cpd_user_type) {
		this.cpd_user_type = cpd_user_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpd_types() {
		return cpd_types;
	}

	public void setCpd_types(String cpd_types) {
		this.cpd_types = cpd_types;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getCpd_type_id() {
		return cpd_type_id;
	}

	public void setCpd_type_id(String cpd_type_id) {
		this.cpd_type_id = cpd_type_id;
	}

	public String getType_date() {
		return type_date;
	}

	public void setType_date(String type_date) {
		this.type_date = type_date;
	}

	public String getCredit_detail() {
		return credit_detail;
	}

	public void setCredit_detail(String credit_detail) {
		this.credit_detail = credit_detail;
	}

	public float getCredit_percentage() {
		return credit_percentage;
	}

	public void setCredit_percentage(float credit_percentage) {
		this.credit_percentage = credit_percentage;
	}

	public String getCredit_limit() {
		return credit_limit;
	}

	public void setCredit_limit(String credit_limit) {
		this.credit_limit = credit_limit;
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
