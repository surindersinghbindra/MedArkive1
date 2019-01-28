package com.medarkive.Utilities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.medarkive.Beans.EbookBean;
import com.medarkive.Beans.PDFBean;
import com.medarkive.Beans.PDFTrackerBean;
import com.medarkive.Beans.TrackerDataBean;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "RepArkiveCoreData";

	private static final String TABLE_EBOOK = "ZEBOOKDATA";

	private static final String TABLE_PDFS = "ZPDFDATA";

	private static final String TABLE_VIDEO_URL = "ZPDFVIDEOURL";

	private static final String TABLE_TRACKER = "ZTRACKDATA";

	private static final String TABLE_PDF_TRACKER = "ZPDFTRACKDATA";

	private final String TRACK_ID = "track_id";

	private final String IP_ADDRESS = "ip_address";

	private final String START_TIME = "starttime";

	private final String COUNTRY = "country";

	private final String CITY = "city";

	private final String ADDRESS = "address";

	private final String PAGE = "page";

	private final String END_TIME = "endtime";

	private final String INTERVAL_SEC = "interval_sec";

	private final String FILE_ID = "file_id";

	private final String OFFLINE = "offline";

	private final String EBOOK_ID = "id";

	private final String FILE_NAME = "file_name";

	private final String PDF_NAME = "pdf_name";

	private final String CHAPTER = "chapter";

	private final String CHAPTER_TITLE = "chapter_title";

	private final String PDF_TITLE = "pdf_title";

	private final String PDF_SUB_TITLE = "pdf_sub_title";

	private final String PDF_ID = "pdf_id";

	private final String FILE_TYPE = "file_type";

	private final String TITLE = "title";

	private final String LINK_URL = "link_url";

	private final String TIME_TRACK = "time_track";

	private final String TRACK_TYPE = "type";

	private final String PDF_THUMB = "pdf_thumb";

	private final String PDF_LOGO = "pdf_logo";

	private final String PDF_FILE = "pdf_file";

	private final String FIRST_PAGE = "first_page_bg";

	private final String ACTIVATED_DATE = "activated_date";

	private final String file_path = "file_path";

	private final String file_id = "file_id";

	private final String file_title = "file_title";

	private final String file_sub_title = "file_sub_title";

	private final String file_code = "file_code";

	private final String file_name = "file_name";

	private final String file_thumb = "file_thumb";

	private final String file_type = "file_type";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PDF_TABLE = "CREATE TABLE " + TABLE_PDFS + "(" + PDF_TITLE + " TEXT," + PDF_ID + " INTEGER UNIQUE," + PDF_SUB_TITLE + " TEXT," + PDF_LOGO + " TEXT," + PDF_THUMB + " TEXT,"
				+ PDF_FILE + " TEXT," + FIRST_PAGE + " TEXT," + FILE_TYPE + " TEXT," + ACTIVATED_DATE + " TEXT" + ")";
		System.out.println("Create table --->>>>" + CREATE_PDF_TABLE);

		String CREATE_TRACKER_DATA_TABLE = "CREATE TABLE " + TABLE_TRACKER + " (" + PDF_ID + " INTEGER UNIQUE," + TITLE + " TEXT, " + LINK_URL + " TEXT, " + TIME_TRACK + " TEXT, " + TRACK_TYPE
				+ " TEXT)";
		System.out.println("Create table --->>>>" + CREATE_TRACKER_DATA_TABLE);

		String CREATE_EBOOK_DATA_TABLE = "CREATE TABLE " + TABLE_EBOOK + " (" + EBOOK_ID + " INTEGER UNIQUE," + PDF_ID + " TEXT, " + FILE_NAME + " TEXT, " + PDF_NAME + " TEXT, " + CHAPTER + " TEXT, "
				+ CHAPTER_TITLE + " TEXT)";

		System.out.println("Create table --->>>>" + CREATE_EBOOK_DATA_TABLE);

		String CREATE_PDF_TRACKING_TABLE = "CREATE TABLE " + TABLE_PDF_TRACKER + " (" + TRACK_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, " + PDF_ID + " INTEGER ," + IP_ADDRESS + " TEXT, "
				+ START_TIME + " TEXT, " + COUNTRY + " TEXT, " + CITY + " TEXT, " + ADDRESS + " TEXT, " + PAGE + " TEXT, " + END_TIME + " TEXT, " + INTERVAL_SEC + " TEXT, " + FILE_ID + " TEXT, "
				+ OFFLINE + " INTEGER)";

		System.out.println("Create table --->>>>" + CREATE_PDF_TRACKING_TABLE);

		String CREATE_PDF_VIDEO_URL_TABLE = "CREATE TABLE " + TABLE_VIDEO_URL + "(" + file_path + " TEXT," + file_id + " INTEGER," + file_title + " TEXT," + file_sub_title + " TEXT," + file_code
				+ " TEXT," + file_name + " TEXT," + file_thumb + " TEXT," + file_type + " TEXT, " + PDF_ID + " INTEGER" + ")";
		System.out.println("Create table --->>>>" + CREATE_PDF_VIDEO_URL_TABLE);

		/*
		 * file_path "file_id" "file_title" "file_sub_title" "file_code"
		 * "file_name" "file_thumb" "file_type"
		 */

		db.execSQL(CREATE_PDF_TABLE);

		db.execSQL(CREATE_TRACKER_DATA_TABLE);

		db.execSQL(CREATE_EBOOK_DATA_TABLE);

		db.execSQL(CREATE_PDF_TRACKING_TABLE);

		db.execSQL(CREATE_PDF_VIDEO_URL_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDFS);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKER);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EBOOK);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDF_TRACKER);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_URL);

		// Create tables again
		onCreate(db);
	}

	// All CRUDE functions defined below

	public void deleteAllPDFTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PDFS + ";");
		db.execSQL("VACUUM; ");

	}

	public void deleteAllTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PDFS + ";");
		db.execSQL("VACUUM; ");

		db.execSQL("DELETE FROM " + TABLE_TRACKER + ";");
		db.execSQL("VACUUM; ");

		db.execSQL("DELETE FROM " + TABLE_EBOOK + ";");
		db.execSQL("VACUUM; ");

		db.execSQL("DELETE FROM " + TABLE_PDF_TRACKER + ";");
		db.execSQL("VACUUM; ");

		db.execSQL("DELETE FROM " + TABLE_VIDEO_URL + ";");
		db.execSQL("VACUUM; ");
	}

	public void addPDF(PDFBean pdfDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PDF_TITLE, pdfDetail.getPDF_TITLE());
		values.put(PDF_ID, pdfDetail.getPDF_ID());
		values.put(PDF_SUB_TITLE, pdfDetail.getPDF_SUB_TITLE());
		values.put(PDF_LOGO, pdfDetail.getPDF_LOGO());
		values.put(PDF_THUMB, pdfDetail.getPDF_THUMB());
		values.put(PDF_FILE, pdfDetail.getPDF_FILE()); //
		values.put(FIRST_PAGE, pdfDetail.getFIRST_PAGE());
		values.put(FILE_TYPE, pdfDetail.getFILE_TYPE());
		values.put(ACTIVATED_DATE, pdfDetail.getACTIVATED_DATE());

		// Inserting Row
		try {
			db.insert(TABLE_PDFS, null, values);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addVideo(PDFBean pdfDetail) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(file_path, pdfDetail.getPDF_FILE());
		values.put(file_id, pdfDetail.getFILE_ID());
		values.put(file_title, pdfDetail.getPDF_TITLE());
		values.put(file_sub_title, pdfDetail.getPDF_SUB_TITLE());
		values.put(file_code, pdfDetail.getFILE_CODE());
		values.put(file_name, pdfDetail.getFIRST_PAGE()); //
		values.put(file_thumb, pdfDetail.getPDF_THUMB());
		values.put(file_type, pdfDetail.getFILE_TYPE());
		values.put(PDF_ID, pdfDetail.getPDF_ID());
		// Inserting Row
		try {
			db.insert(TABLE_VIDEO_URL, null, values);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addTrackedData(TrackerDataBean trackerDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PDF_ID, trackerDetail.getId());
		values.put(TITLE, trackerDetail.getTitle());
		values.put(LINK_URL, trackerDetail.getLink());
		values.put(TIME_TRACK, trackerDetail.getSpentTime());
		values.put(TRACK_TYPE, trackerDetail.getTrackType());

		// Inserting Row
		try {
			db.insert(TABLE_TRACKER, null, values);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addEbook(EbookBean ebookDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(EBOOK_ID, ebookDetail.getId());
		values.put(PDF_ID, ebookDetail.getPdf_id());
		values.put(FILE_NAME, ebookDetail.getFile_name());
		values.put(PDF_NAME, ebookDetail.getPdf_name());
		values.put(CHAPTER, ebookDetail.getChapter());
		values.put(CHAPTER_TITLE, ebookDetail.getChapter_title()); //

		// Inserting Row
		try {
			db.insert(TABLE_EBOOK, null, values);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPDFTrackedData(PDFTrackerBean trackerDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PDF_ID, trackerDetail.getPdf_id());
		values.put(IP_ADDRESS, trackerDetail.getIp_address());
		values.put(START_TIME, trackerDetail.getStarttime());
		values.put(COUNTRY, trackerDetail.getCountry());
		values.put(CITY, trackerDetail.getCity());
		values.put(ADDRESS, trackerDetail.getAddress());
		values.put(PAGE, trackerDetail.getPage());
		values.put(END_TIME, trackerDetail.getEndtime());
		values.put(INTERVAL_SEC, trackerDetail.getInterval_sec());
		values.put(FILE_ID, trackerDetail.getFile_id());
		values.put(OFFLINE, trackerDetail.getOffline());
		// Inserting Row
		try {
			db.insert(TABLE_PDF_TRACKER, null, values);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<TrackerDataBean> getAllTrackedData() {
		List<TrackerDataBean> trackedDataList = new ArrayList<TrackerDataBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TRACKER;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		System.out.println("Cursore ------>>>>" + cursor.getCount());
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				System.out.println("0" + cursor.getString(0));
				System.out.println("1" + cursor.getString(1));
				System.out.println("2" + cursor.getString(2));
				System.out.println("3" + cursor.getString(3));

				TrackerDataBean trackedData = new TrackerDataBean(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				// Adding contact to list
				trackedDataList.add(trackedData);
			} while (cursor.moveToNext());
		}
		// return contact list
		return trackedDataList;
	}

	// Getting All PDFs
	public List<PDFBean> getAllPDF() {
		List<PDFBean> pdfList = new ArrayList<PDFBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PDFS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		System.out.println("Cursore ------>>>>" + cursor.getCount());
		// looping through all rows and adding to list
		// for(int i =0 ; i < cursor.getCount() ;i++){
		// System.out.println(cursor.getColumnName(i)+"--------> "+cursor.getString(i));
		// }
		if (cursor.moveToFirst()) {
			do {
				PDFBean pdf = new PDFBean(cursor.getString(0), cursor.getString(2), cursor.getString(1), cursor.getString(7), cursor.getString(4), cursor.getString(6), cursor.getString(5),
						cursor.getString(3), cursor.getString(8));
				// Adding contact to list
				pdfList.add(pdf);
			} while (cursor.moveToNext());
		}

		// return contact list
		return pdfList;
	}

	// get All video links
	public ArrayList<PDFBean> getAllVideoURL() {
		ArrayList<PDFBean> pdfList = new ArrayList<PDFBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_URL;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		System.out.println("Cursore ------>>>>" + cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				// (String pDF_FILE, String pDF_ID, String pDF_TITLE, String
				// pDF_SUB_TITLE, String FILE_CODE, String FIRST_PAGE, String
				// pDF_THUMB, String FILE_TYPE)
				PDFBean pdf = new PDFBean();
				pdf.setPDF_FILE(cursor.getString(0));
				pdf.setFILE_ID(cursor.getString(1));
				pdf.setPDF_TITLE(cursor.getString(2));
				pdf.setPDF_SUB_TITLE(cursor.getString(3));
				pdf.setFILE_CODE(cursor.getString(4));
				pdf.setFIRST_PAGE(cursor.getString(5));
				pdf.setPDF_THUMB(cursor.getString(6));
				pdf.setFILE_TYPE(cursor.getString(7));
				pdf.setPDF_ID(cursor.getString(8));

				pdfList.add(pdf);
			} while (cursor.moveToNext());
		}
		return pdfList;
	}

	public ArrayList<PDFBean> getEbookVideoURL(String pdfId) {
		ArrayList<PDFBean> pdfList = new ArrayList<PDFBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_URL + " WHERE pdf_id ='" + pdfId + "';";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		System.out.println("Cursore ------>>>>" + cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				PDFBean pdf = new PDFBean();
				pdf.setPDF_FILE(cursor.getString(0));
				pdf.setFILE_ID(cursor.getString(1));
				pdf.setPDF_TITLE(cursor.getString(2));
				pdf.setPDF_SUB_TITLE(cursor.getString(3));
				pdf.setFILE_CODE(cursor.getString(4));
				pdf.setFIRST_PAGE(cursor.getString(5));
				pdf.setPDF_THUMB(cursor.getString(6));
				pdf.setFILE_TYPE(cursor.getString(7));
				pdf.setPDF_ID(cursor.getString(8));
				pdfList.add(pdf);
			} while (cursor.moveToNext());
		}
		return pdfList;
	}

	public ArrayList<EbookBean> getEbookData(String pdf_id) {
		ArrayList<EbookBean> ebookDataList = new ArrayList<EbookBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EBOOK + " WHERE pdf_id=" + pdf_id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		System.out.println("Cursore ------>>>>" + cursor.getCount());
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				System.out.println("0" + cursor.getString(0));
				System.out.println("1" + cursor.getString(1));
				System.out.println("2" + cursor.getString(2));
				System.out.println("3" + cursor.getString(3));
				System.out.println("4" + cursor.getString(4));
				System.out.println("5" + cursor.getString(5));

				EbookBean trackedData = new EbookBean(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
				// Adding contact to list
				ebookDataList.add(trackedData);
			} while (cursor.moveToNext());
		}
		// return contact list
		return ebookDataList;
	}

	public void updatePDFTrackerData(int tracker_id) {

		String updateQuery = "UPDATE " + TABLE_PDF_TRACKER + " SET " + OFFLINE + " =  0 WHERE " + TRACK_ID + " = " + tracker_id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(updateQuery, null);
		try {
			System.out.println("cusros --->>" + cursor.getString(0));
			System.out.println("cusros --->>" + cursor.getString(1));
			System.out.println("cusros --->>" + cursor.getString(2));
			System.out.println("cusros --->>" + cursor.getString(3));
			System.out.println("cusros --->>" + cursor.getString(4));
			System.out.println("cusros --->>" + cursor.getString(5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<PDFTrackerBean> getAllPDFTrackedData() {
		List<PDFTrackerBean> trackedDataList = new ArrayList<PDFTrackerBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PDF_TRACKER;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		System.out.println("Cursore ------>>>>" + cursor.getCount());
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				System.out.println("0" + cursor.getString(0));
				System.out.println("1" + cursor.getString(1));
				System.out.println("2" + cursor.getString(2));
				System.out.println("3" + cursor.getString(3));
				System.out.println("4" + cursor.getString(4));
				System.out.println("5" + cursor.getString(5));
				System.out.println("6" + cursor.getString(6));
				System.out.println("7" + cursor.getString(7));
				System.out.println("8" + cursor.getString(8));
				System.out.println("9" + cursor.getString(9));
				System.out.println("10" + cursor.getString(10));

				PDFTrackerBean trackedData = new PDFTrackerBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11));
				// Adding contact to list
				trackedDataList.add(trackedData);
			} while (cursor.moveToNext());
		}
		// return contact list
		return trackedDataList;
	}

	public List<PDFTrackerBean> getAllOFFLINEPDFTrackedData() {
		List<PDFTrackerBean> trackedDataList = new ArrayList<PDFTrackerBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PDF_TRACKER + " WHERE " + OFFLINE + " = 1 ;";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PDFTrackerBean trackedData = new PDFTrackerBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11));
				// Adding contact to list
				trackedDataList.add(trackedData);
			} while (cursor.moveToNext());
		}
		// return contact list
		return trackedDataList;
	}

	public List<PDFTrackerBean> getAllonlinePDFTrackedData() {
		List<PDFTrackerBean> trackedDataList = new ArrayList<PDFTrackerBean>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PDF_TRACKER + " WHERE " + OFFLINE + " = 0 ;";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PDFTrackerBean trackedData = new PDFTrackerBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11));
				// Adding contact to list
				trackedDataList.add(trackedData);
			} while (cursor.moveToNext());
		}
		// return contact list
		return trackedDataList;
	}

	public PDFBean getPDF(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = " SELECT * FROM " + TABLE_PDFS + " WHERE " + PDF_ID + "=" + id;

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		PDFBean pdf = new PDFBean(cursor.getString(0), cursor.getString(2), cursor.getString(1), cursor.getString(7), cursor.getString(4), cursor.getString(6), cursor.getString(5),
				cursor.getString(3), cursor.getString(8));
		// return contact
		return pdf;
	}

	public int getPDFCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PDFS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public int updatePDFData(PDFBean pdf) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PDF_TITLE, pdf.getPDF_TITLE());
		values.put(PDF_SUB_TITLE, pdf.getPDF_SUB_TITLE());

		// updating row
		return db.update(TABLE_PDFS, values, PDF_ID + " = ?", new String[] { String.valueOf(pdf.getPDF_ID()) });
	}

	// Deleting single contact
	public void deletePDFData(PDFBean pdf) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PDFS, PDF_ID + " = ?", new String[] { String.valueOf(pdf.getPDF_ID()) });
		db.close();

	}
}
