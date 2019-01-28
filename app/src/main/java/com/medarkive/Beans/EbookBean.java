package com.medarkive.Beans;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class EbookBean implements Parcelable{
	
	private  String id ;

	private  String pdf_id ;
	
	private  String file_name ;

	private  String pdf_name ;

	private  String chapter ;

	private  String chapter_title ;
	
	private ArrayList<PDFBean> linkedBean;
	
	public EbookBean(){}

	public EbookBean(String id, String pdf_id, String file_name,
			String pdf_name, String chapter, String chapter_title) {
		super();
		this.id = id;
		this.pdf_id = pdf_id;
		this.file_name = file_name;
		this.pdf_name = pdf_name;
		this.chapter = chapter;
		this.chapter_title = chapter_title;
	}
	
	public ArrayList<PDFBean> getLinkedBean() {
		return linkedBean;
	}

	public void setLinkedBean(ArrayList<PDFBean> linkedBean) {
		this.linkedBean = linkedBean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPdf_id() {
		return pdf_id;
	}

	public void setPdf_id(String pdf_id) {
		this.pdf_id = pdf_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getPdf_name() {
		return pdf_name;
	}

	public void setPdf_name(String pdf_name) {
		this.pdf_name = pdf_name;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getChapter_title() {
		return chapter_title;
	}

	public void setChapter_title(String chapter_title) {
		this.chapter_title = chapter_title;
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
