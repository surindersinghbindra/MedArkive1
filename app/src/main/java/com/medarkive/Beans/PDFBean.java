package com.medarkive.Beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PDFBean implements Serializable {

	private String PK;

	private String PDF_TITLE;

	private String PDF_SUB_TITLE;

	private String PDF_ID;
	
	private String FILE_ID;

	private String FILE_TYPE;

	private String PDF_THUMB;

	private String PDF_LOGO;

	private String PDF_FILE;

	private String FIRST_PAGE;

	private String ACTIVATED_DATE;

	private String FILE_CODE;

	public PDFBean() {
	}

	public PDFBean(String pDF_TITLE, String pDF_SUB_TITLE, String pDF_ID, String fILE_TYPE, String pDF_THUMB, String pDF_LOGO, String pDF_FILE, String fIRST_PAGE, String aCTIVATED_DATE) {
		super();
		PDF_TITLE = pDF_TITLE;
		PDF_SUB_TITLE = pDF_SUB_TITLE;
		PDF_ID = pDF_ID;
		FILE_TYPE = fILE_TYPE;
		PDF_THUMB = pDF_THUMB;
		PDF_LOGO = pDF_LOGO;
		PDF_FILE = pDF_FILE;
		FIRST_PAGE = fIRST_PAGE;
		ACTIVATED_DATE = aCTIVATED_DATE;
	}

	/*
	 * values.put(file_path, pdfDetail.getPDF_FILE()); values.put(file_id,
	 * pdfDetail.getPDF_ID()); values.put(file_title, pdfDetail.getPDF_TITLE());
	 * values.put(file_sub_title, pdfDetail.getPDF_SUB_TITLE());
	 * values.put(file_code, pdfDetail.getFILE_CODE()); values.put(file_name,
	 * pdfDetail.getFIRST_PAGE()); // values.put(file_thumb,
	 * pdfDetail.getPDF_THUMB()); values.put(file_type,
	 * pdfDetail.getFILE_TYPE());
	 */
	public PDFBean(String pDF_FILE, String pDF_ID, String pDF_TITLE, String pDF_SUB_TITLE, String FILE_CODE, String FIRST_PAGE, String pDF_THUMB, String FILE_TYPE) {
		super();
		this.PDF_TITLE = pDF_TITLE;
		this.PDF_SUB_TITLE = pDF_SUB_TITLE;
		this.PDF_ID = pDF_ID;
		this.FILE_TYPE = FILE_TYPE;
		this.PDF_THUMB = pDF_THUMB;
		this.PDF_FILE = pDF_FILE;
		this.FILE_CODE = FILE_CODE;
		this.FIRST_PAGE = FIRST_PAGE;
	}

	public String getFILE_CODE() {
		return FILE_CODE;
	}

	public void setFILE_CODE(String fILE_CODE) {
		FILE_CODE = fILE_CODE;
	}

	public String getPk() {
		return PK;
	}

	public void setPk(String pk) {
		this.PK = pk;
	}

	public String getPDF_TITLE() {
		return PDF_TITLE;
	}

	public void setPDF_TITLE(String pDF_TITLE) {
		PDF_TITLE = pDF_TITLE;
	}

	public String getPDF_SUB_TITLE() {
		return PDF_SUB_TITLE;
	}

	public void setPDF_SUB_TITLE(String pDF_SUB_TITLE) {
		PDF_SUB_TITLE = pDF_SUB_TITLE;
	}

	public String getPDF_ID() {
		return PDF_ID;
	}

	public void setPDF_ID(String pDF_ID) {
		PDF_ID = pDF_ID;
	}

	public String getFILE_TYPE() {
		return FILE_TYPE;
	}

	public void setFILE_TYPE(String fILE_TYPE) {
		FILE_TYPE = fILE_TYPE;
	}

	public String getPDF_THUMB() {
		return PDF_THUMB;
	}

	public void setPDF_THUMB(String pDF_THUMB) {
		PDF_THUMB = pDF_THUMB;
	}

	public String getPDF_LOGO() {
		return PDF_LOGO;
	}

	public void setPDF_LOGO(String pDF_LOGO) {
		PDF_LOGO = pDF_LOGO;
	}

	public String getPDF_FILE() {
		return PDF_FILE;
	}

	public void setPDF_FILE(String pDF_FILE) {
		PDF_FILE = pDF_FILE;
	}

	public String getFIRST_PAGE() {
		return FIRST_PAGE;
	}

	public void setFIRST_PAGE(String fIRST_PAGE) {
		FIRST_PAGE = fIRST_PAGE;
	}

	public String getACTIVATED_DATE() {
		return ACTIVATED_DATE;
	}

	public void setACTIVATED_DATE(String aCTIVATED_DATE) {
		ACTIVATED_DATE = aCTIVATED_DATE;
	}

	public String getFILE_ID() {
		return FILE_ID;
	}

	public void setFILE_ID(String fILE_ID) {
		FILE_ID = fILE_ID;
	}

}
