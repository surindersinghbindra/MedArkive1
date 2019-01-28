package com.medarkive.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.medarkive.Beans.BookmarkBean;
import com.medarkive.Beans.CPDBean;

import android.content.Context;
import android.util.Log;

public class JsonManipulator {
	private float credit = 0;
	private float totalCreditLimit = 0;
	private ArrayList<CPDBean> cpdArray = null;
	private ArrayList<CPDBean> cpdUKArraylist = null;
	private HashMap<String, ArrayList<CPDBean>> map;
	private ArrayList<CPDBean> temparr = null;
	private ArrayList<CPDBean> summaryData = null;
	private String typename;
	private String typeid;
	private String name = null;
	private String cpd_id = null;
	DecimalFormat df = new DecimalFormat("#.##"); 

	public ArrayList<CPDBean> convertJsonToArrayList(JSONObject jobj,
			Context ctx) {
		try {
			SessionManager sm = new SessionManager(ctx);
			String cpdChoice = sm.getCPDChoice();
			JSONArray arr = jobj.getJSONObject("cpd_data").getJSONArray(
					"cpd_data");
			if (cpdChoice.contains("1")) {
				// map = new HashMap<String, ArrayList<CPDBean>>();
				cpdArray = new ArrayList<CPDBean>();

				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
					}
					if (arr.getJSONObject(i).has("credit")) {
						Float d = Float.parseFloat(arr.getJSONObject(i)
								.getString("credit"));
						String.format("%.2f", d);
						totalCreditLimit = d;
						// totalCreditLimit = Float.parseFloat(arr
						// .getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}
					temparr = new ArrayList<CPDBean>();
					CPDBean cpdBean = new CPDBean();
					CPDBean allDataBean = null;
					cpdBean.setName(name);
					cpdBean.setCpd_id(cpd_id);
					cpdBean.setCredit_limit(totalCreditLimit + "");
					JSONArray objArr = arr.getJSONObject(i).getJSONArray(
							"cpd_types");
					for (int j = 0; j < objArr.length(); j++) {
						JSONArray arrcpd = objArr.getJSONObject(j)
								.getJSONArray("cpd_events");
						typename = objArr.getJSONObject(j).getString(
								"type_name");
						typeid = objArr.getJSONObject(j).getString(
								"cpd_type_id");
						if (arrcpd.length() > 0) {
							for (int k = 0; k < arrcpd.length(); k++) {
								JSONObject obj1 = arrcpd.getJSONObject(k);
								allDataBean = new CPDBean();
								if (obj1.has("credit")) {
									credit = credit + Float.parseFloat(obj1
											.getString("credit"));
									
//									String.format("%.2f", credit);
//									Log.v("check ", ","+credit);
									
//									String twoDigitNum = df.format(myNum);
//									Log.v("check ", ","+twoDigitNum);
								}
								if (obj1.has("clinical")) {
									allDataBean.setClinical(obj1
											.getString("clinical"));
								}
								if (obj1.has("type_date")) {
									allDataBean.setType_date(obj1
											.getString("type_date"));
								}
								if (obj1.has("cpd_event_id")) {
									allDataBean.setCpd_event_id(obj1
											.getString("cpd_event_id"));
								}
								if (obj1.has("credit")) {
//									Log.v("t2t-->",
//											"." + obj1.getString("credit"));
									Float d = Float.parseFloat(obj1
											.getString("credit"));
//									String.format("%.2f", d);
//									Log.v("tt-->", "." + d);
									allDataBean.setCredit(d + "");
								}
								if (obj1.has("cpd_user_type")) {
									allDataBean.setCpd_user_type(obj1
											.getString("cpd_user_type"));
								}
								if (obj1.has("event_description")) {
									allDataBean.setEvent_description(obj1
											.getString("event_description"));
								}
								if (obj1.has("cpd_id")) {
									allDataBean.setCpd_id(obj1
											.getString("cpd_id"));
								}
								if (obj1.has("created")) {
									allDataBean.setCreated(obj1
											.getString("created"));
								}
								allDataBean.setName(name);
								allDataBean.setType_name(typename);
								allDataBean.setCpd_type_id(typeid);
								allDataBean.setCpd_id(cpd_id);
								temparr.add(allDataBean);
							}
						} else {
							allDataBean = new CPDBean();
							allDataBean.setName(name);
							allDataBean.setType_name(typename);
							allDataBean.setCpd_type_id(typeid);
							allDataBean.setCpd_id(cpd_id);
							temparr.add(allDataBean);
						}
					}
					credit = Float.parseFloat(df.format(credit));
					cpdBean.setCredit_detail(credit + "/" + totalCreditLimit);
					cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
					cpdBean.setCredit(credit + "");
					cpdArray.add(cpdBean);
					credit = 0;
					totalCreditLimit = 0;
					// map.put(name, temparr);

				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = Float.parseFloat(df.format(credit));
					credit = credit + Float.parseFloat(bean.getCredit());
					String.format("%.2f",credit);
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
				}

				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				cpdobj.setCredit_percentage((credit * 100) / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				// map.put("Summary", summaryData);
				cpdUKArraylist.addAll(cpdArray);
			} else if (cpdChoice.contains("2")) {
				CPDBean allDataBean = null;
				// map = new HashMap<String, ArrayList<CPDBean>>();
				cpdArray = new ArrayList<CPDBean>();
				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
						// System.out.println("name   " + name);
					}
					if (arr.getJSONObject(i).has("credit")) {
						totalCreditLimit = Float.parseFloat(arr
								.getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}

					CPDBean cpdBean = new CPDBean();
					cpdBean.setName(name);
					cpdBean.setCpd_id(cpd_id);
					cpdBean.setCredit_limit(totalCreditLimit + "");
					temparr = new ArrayList<CPDBean>();
					JSONArray obj = arr.getJSONObject(i).getJSONArray(
							"cpd_types");
					if (obj.length() > 0) {
						for (int j = 0; j < obj.length(); j++) {
							JSONObject o = obj.getJSONObject(j);
							allDataBean = new CPDBean();
							if (o.has("credit")) {
								credit = credit
										+ Float.parseFloat(o
												.getString("credit"));
//								String.format("%.2f", credit);
							}
							if (o.has("clinical")) {
								allDataBean
										.setClinical(o.getString("clinical"));
							}
							if (o.has("type_name")) {
								allDataBean.setType_name(o
										.getString("type_name"));
							}
							if (o.has("cpd_type_id")) {
								allDataBean.setCpd_type_id(o
										.getString("cpd_type_id"));
							}
							if (o.has("type_date")) {
								allDataBean.setType_date(o
										.getString("type_date"));
							}
							if (o.has("cpd_event_id")) {
								allDataBean.setCpd_event_id(o
										.getString("cpd_event_id"));
							}
							if (o.has("credit")) {
								allDataBean.setCredit(o.getString("credit"));
							}
							if (o.has("cpd_user_type")) {
								allDataBean.setCpd_user_type(o
										.getString("cpd_user_type"));
							}
							if (o.has("event_description")) {
								allDataBean.setEvent_description(o
										.getString("event_description"));
							}
							if (o.has("cpd_id")) {
								allDataBean.setCpd_id(o.getString("cpd_id"));
							}
							if (o.has("created")) {
								allDataBean.setCreated(o.getString("created"));
							}
							allDataBean.setName(name);
							allDataBean.setCpd_id(cpd_id);
							temparr.add(allDataBean);
						}
					} else {
						allDataBean = new CPDBean();
						allDataBean.setName(name);
						allDataBean.setCpd_id(cpd_id);
						temparr.add(allDataBean);
					}
					credit = Float.parseFloat(df.format(credit));
					cpdBean.setCredit_detail(credit + "/" + totalCreditLimit);
					cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
					cpdBean.setCredit(credit + "");
					cpdArray.add(cpdBean);
					credit = 0;
					totalCreditLimit = 0;
					// map.put(name, temparr);
				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = credit + Float.parseFloat(bean.getCredit());
//					String.format("%.2f", credit);
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
//					String.format("%.2f", totalCreditLimit);
				}

				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				credit = Float.parseFloat(df.format(credit));
				cpdobj.setCredit_percentage( credit * 100 / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				cpdUKArraylist.addAll(cpdArray);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cpdUKArraylist;
	}

	public HashMap<String, ArrayList<CPDBean>> jsonToMap(JSONObject jObj,
			Context ctx) {

		try {
			SessionManager sm = new SessionManager(ctx);
			String cpdChoice = sm.getCPDChoice();
			JSONArray arr = jObj.getJSONObject("cpd_data").getJSONArray(
					"cpd_data");
			if (cpdChoice.contains("1")) {
				map = new HashMap<String, ArrayList<CPDBean>>();
				summaryData = new ArrayList<CPDBean>();
				cpdArray = new ArrayList<CPDBean>();

				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
					}
					if (arr.getJSONObject(i).has("credit")) {
						totalCreditLimit = Float.parseFloat(arr
								.getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}
					temparr = new ArrayList<CPDBean>();
					CPDBean cpdBean = new CPDBean();
					CPDBean allDataBean = null;
					cpdBean.setName(name);
					cpdBean.setCpd_id(cpd_id);
					cpdBean.setCredit_limit(totalCreditLimit + "");
					JSONArray objArr = arr.getJSONObject(i).getJSONArray(
							"cpd_types");
					for (int j = 0; j < objArr.length(); j++) {
						JSONArray arrcpd = objArr.getJSONObject(j)
								.getJSONArray("cpd_events");
						// System.out.println("json 0-0--0"
						// + objArr.getJSONObject(j));
						typename = objArr.getJSONObject(j).getString(
								"type_name");
						typeid = objArr.getJSONObject(j).getString(
								"cpd_type_id");
						if (arrcpd.length() > 0) {
							for (int k = 0; k < arrcpd.length(); k++) {
								JSONObject obj1 = arrcpd.getJSONObject(k);
								allDataBean = new CPDBean();
								CPDBean summarybean = new CPDBean();
								if (obj1.has("credit")) {
									credit = credit
											+ Float.parseFloat(obj1
													.getString("credit"));
								}
								if (obj1.has("clinical")) {
									allDataBean.setClinical(obj1
											.getString("clinical"));
									summarybean.setClinical(obj1
											.getString("clinical"));

								}
								if (obj1.has("type_date")) {
									allDataBean.setType_date(obj1
											.getString("type_date"));
									summarybean.setType_date(obj1
											.getString("type_date"));
								}
								if (obj1.has("cpd_event_id")) {
									allDataBean.setCpd_event_id(obj1
											.getString("cpd_event_id"));
									summarybean.setCpd_event_id(obj1
											.getString("cpd_event_id"));
								}
								if (obj1.has("credit")) {
									allDataBean.setCredit(obj1
											.getString("credit"));
									summarybean.setCredit(obj1
											.getString("credit"));
								}
								if (obj1.has("cpd_user_type")) {
									allDataBean.setCpd_user_type(obj1
											.getString("cpd_user_type"));
									summarybean.setCpd_user_type(obj1
											.getString("cpd_user_type"));
								}
								if (obj1.has("event_description")) {
									allDataBean.setEvent_description(obj1
											.getString("event_description"));
									summarybean.setEvent_description(obj1
											.getString("event_description"));
								}
								if (obj1.has("created")) {
									allDataBean.setCreated(obj1
											.getString("created"));
									summarybean.setCreated(obj1
											.getString("created"));
								}

								allDataBean.setType_name(typename);
								allDataBean.setCpd_type_id(typeid);
								allDataBean.setCpd_id(cpd_id);
								allDataBean.setName(name);

								summarybean.setType_name(typename);
								summarybean.setCpd_type_id(typeid);
								summarybean.setCpd_id(cpd_id);
								summarybean.setName(name);

								temparr.add(allDataBean);
								summaryData.add(summarybean);
							}
						} else {
							allDataBean = new CPDBean();
							allDataBean.setName(name);
							allDataBean.setType_name(typename);
							allDataBean.setCpd_type_id(typeid);
							allDataBean.setCpd_id(cpd_id);
							temparr.add(allDataBean);
						}
					}
					cpdBean.setCredit_detail(credit + "/" + totalCreditLimit);
					cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
					cpdBean.setCredit(credit + "");
					cpdArray.add(cpdBean);
					credit = 0;
					totalCreditLimit = 0;
					map.put(name, temparr);

				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = credit + Float.parseFloat(bean.getCredit());
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
				}

				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				cpdobj.setCredit_percentage((credit * 100) / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				for (CPDBean b : summaryData) {
					b.setName("Summary");
				}
				map.put("Summary", summaryData);
				cpdUKArraylist.addAll(cpdArray);
			} else if (cpdChoice.contains("2")) {
				CPDBean allDataBean = null;
				map = new HashMap<String, ArrayList<CPDBean>>();
				cpdArray = new ArrayList<CPDBean>();
				summaryData = new ArrayList<CPDBean>();
				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
						// System.out.println("name   " + name);
					}
					if (arr.getJSONObject(i).has("credit")) {
						totalCreditLimit = Float.parseFloat(arr
								.getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}

					CPDBean cpdBean = new CPDBean();
					cpdBean.setName(name);
					cpdBean.setCpd_id(cpd_id);
					cpdBean.setCredit_limit(totalCreditLimit + "");
					temparr = new ArrayList<CPDBean>();
					JSONArray obj = arr.getJSONObject(i).getJSONArray(
							"cpd_types");
					if (obj.length() > 0) {
						for (int j = 0; j < obj.length(); j++) {
							JSONObject o = obj.getJSONObject(j);
							allDataBean = new CPDBean();
							CPDBean summaryBean = new CPDBean();
							if (o.has("credit")) {
								credit = credit
										+ Float.parseFloat(o
												.getString("credit"));
							}
							if (o.has("clinical")) {
								allDataBean
										.setClinical(o.getString("clinical"));
								summaryBean
										.setClinical(o.getString("clinical"));
							}
							if (o.has("type_name")) {
								allDataBean.setType_name(o
										.getString("type_name"));
								summaryBean.setType_name(o
										.getString("type_name"));
							}
							if (o.has("cpd_type_id")) {
								allDataBean.setCpd_type_id(o
										.getString("cpd_type_id"));
								summaryBean.setCpd_type_id(o
										.getString("cpd_type_id"));
							}
							if (o.has("type_date")) {
								allDataBean.setType_date(o
										.getString("type_date"));
								summaryBean.setType_date(o
										.getString("type_date"));
							}
							if (o.has("cpd_event_id")) {
								allDataBean.setCpd_event_id(o
										.getString("cpd_event_id"));
								summaryBean.setCpd_event_id(o
										.getString("cpd_event_id"));
							}
							if (o.has("credit")) {
								allDataBean.setCredit(o.getString("credit"));
								summaryBean.setCredit(o.getString("credit"));
							}
							if (o.has("cpd_user_type")) {
								allDataBean.setCpd_user_type(o
										.getString("cpd_user_type"));
								summaryBean.setCpd_user_type(o
										.getString("cpd_user_type"));
							}
							if (o.has("event_description")) {
								allDataBean.setEvent_description(o
										.getString("event_description"));
								summaryBean.setEvent_description(o
										.getString("event_description"));
							}
							if (o.has("created")) {
								allDataBean.setCreated(o.getString("created"));
								summaryBean.setCreated(o.getString("created"));
							}
							allDataBean.setName(name);
							allDataBean.setCpd_id(cpd_id);
							summaryBean.setName(name);
							summaryBean.setCpd_id(cpd_id);
							temparr.add(allDataBean);
							summaryData.add(summaryBean);
						}
					} else {
						allDataBean = new CPDBean();
						allDataBean.setName(name);
						allDataBean.setCpd_id(cpd_id);
						temparr.add(allDataBean);
					}

					cpdBean.setCredit_detail(credit + "/" + totalCreditLimit);
					cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
					cpdBean.setCredit(credit + "");
					cpdArray.add(cpdBean);
					credit = 0;
					totalCreditLimit = 0;
					map.put(name, temparr);
				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = credit + Float.parseFloat(bean.getCredit());
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
				}

				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				cpdobj.setCredit_percentage((credit * 100) / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				for (CPDBean b : summaryData) {
					b.setName("Summary");
				}
				map.put("Summary", summaryData);
				cpdUKArraylist.addAll(cpdArray);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public HashMap<String, ArrayList<CPDBean>> jsonToMapDeleEven(
			JSONObject jObj, Context ctx) {

		try {
			SessionManager sm = new SessionManager(ctx);
			String cpdChoice = sm.getCPDChoice();
			JSONArray arr = jObj.getJSONObject("delete_cpd_event")
					.getJSONArray("cpd_data");
			if (cpdChoice.contains("1")) {
				map = new HashMap<String, ArrayList<CPDBean>>();
				summaryData = new ArrayList<CPDBean>();
				cpdArray = new ArrayList<CPDBean>();

				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
					}
					if (arr.getJSONObject(i).has("credit")) {
						totalCreditLimit = Float.parseFloat(arr
								.getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}
					temparr = new ArrayList<CPDBean>();
					CPDBean cpdBean = new CPDBean();
					CPDBean allDataBean = null;
					cpdBean.setName(name);
					cpdBean.setCpd_id(cpd_id);
					cpdBean.setCredit_limit(totalCreditLimit + "");
					JSONArray objArr = arr.getJSONObject(i).getJSONArray(
							"cpd_types");
					for (int j = 0; j < objArr.length(); j++) {
						JSONArray arrcpd = objArr.getJSONObject(j)
								.getJSONArray("cpd_events");
						// System.out.println("json 0-0--0"
						// + objArr.getJSONObject(j));
						typename = objArr.getJSONObject(j).getString(
								"type_name");
						typeid = objArr.getJSONObject(j).getString(
								"cpd_type_id");
						if (arrcpd.length() > 0) {
							for (int k = 0; k < arrcpd.length(); k++) {
								JSONObject obj1 = arrcpd.getJSONObject(k);
								allDataBean = new CPDBean();
								CPDBean summarybean = new CPDBean();
								if (obj1.has("credit")) {
									credit = credit
											+ Float.parseFloat(obj1
													.getString("credit"));
								}
								if (obj1.has("clinical")) {
									allDataBean.setClinical(obj1
											.getString("clinical"));
									summarybean.setClinical(obj1
											.getString("clinical"));

								}
								if (obj1.has("type_date")) {
									allDataBean.setType_date(obj1
											.getString("type_date"));
									summarybean.setType_date(obj1
											.getString("type_date"));
								}
								if (obj1.has("cpd_event_id")) {
									allDataBean.setCpd_event_id(obj1
											.getString("cpd_event_id"));
									summarybean.setCpd_event_id(obj1
											.getString("cpd_event_id"));
								}
								if (obj1.has("credit")) {
									allDataBean.setCredit(obj1
											.getString("credit"));
									summarybean.setCredit(obj1
											.getString("credit"));
								}
								if (obj1.has("cpd_user_type")) {
									allDataBean.setCpd_user_type(obj1
											.getString("cpd_user_type"));
									summarybean.setCpd_user_type(obj1
											.getString("cpd_user_type"));
								}
								if (obj1.has("event_description")) {
									allDataBean.setEvent_description(obj1
											.getString("event_description"));
									summarybean.setEvent_description(obj1
											.getString("event_description"));
								}
								if (obj1.has("created")) {
									allDataBean.setCreated(obj1
											.getString("created"));
									summarybean.setCreated(obj1
											.getString("created"));
								}

								allDataBean.setType_name(typename);
								allDataBean.setCpd_type_id(typeid);
								allDataBean.setCpd_id(cpd_id);
								allDataBean.setName(name);

								summarybean.setType_name(typename);
								summarybean.setCpd_type_id(typeid);
								summarybean.setCpd_id(cpd_id);
								summarybean.setName(name);

								temparr.add(allDataBean);
								summaryData.add(summarybean);
							}
						} else {
							allDataBean = new CPDBean();
							allDataBean.setName(name);
							allDataBean.setType_name(typename);
							allDataBean.setCpd_type_id(typeid);
							allDataBean.setCpd_id(cpd_id);
							temparr.add(allDataBean);
						}
					}
					cpdBean.setCredit_detail(credit + "/" + totalCreditLimit);
					cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
					cpdBean.setCredit(credit + "");
					cpdArray.add(cpdBean);
					credit = 0;
					totalCreditLimit = 0;
					map.put(name, temparr);

				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = credit + Float.parseFloat(bean.getCredit());
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
				}

				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				cpdobj.setCredit_percentage((credit * 100) / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				for (CPDBean b : summaryData) {
					b.setName("Summary");
				}
				map.put("Summary", summaryData);
				cpdUKArraylist.addAll(cpdArray);
			} else if (cpdChoice.contains("2")) {
				CPDBean allDataBean = null;
				map = new HashMap<String, ArrayList<CPDBean>>();
				cpdArray = new ArrayList<CPDBean>();
				summaryData = new ArrayList<CPDBean>();
				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
						// System.out.println("name   " + name);
					}
					if (arr.getJSONObject(i).has("credit")) {
						totalCreditLimit = Float.parseFloat(arr
								.getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}

					CPDBean cpdBean = new CPDBean();
					cpdBean.setName(name);
					cpdBean.setCpd_id(cpd_id);
					cpdBean.setCredit_limit(totalCreditLimit + "");
					temparr = new ArrayList<CPDBean>();
					JSONArray obj = arr.getJSONObject(i).getJSONArray(
							"cpd_types");
					if (obj.length() > 0) {
						for (int j = 0; j < obj.length(); j++) {
							JSONObject o = obj.getJSONObject(j);
							allDataBean = new CPDBean();
							CPDBean summaryBean = new CPDBean();
							if (o.has("credit")) {
								credit = credit
										+ Float.parseFloat(o
												.getString("credit"));
							}
							if (o.has("clinical")) {
								allDataBean
										.setClinical(o.getString("clinical"));
								summaryBean
										.setClinical(o.getString("clinical"));
							}
							if (o.has("type_name")) {
								allDataBean.setType_name(o
										.getString("type_name"));
								summaryBean.setType_name(o
										.getString("type_name"));
							}
							if (o.has("cpd_type_id")) {
								allDataBean.setCpd_type_id(o
										.getString("cpd_type_id"));
								summaryBean.setCpd_type_id(o
										.getString("cpd_type_id"));
							}
							if (o.has("type_date")) {
								allDataBean.setType_date(o
										.getString("type_date"));
								summaryBean.setType_date(o
										.getString("type_date"));
							}
							if (o.has("cpd_event_id")) {
								allDataBean.setCpd_event_id(o
										.getString("cpd_event_id"));
								summaryBean.setCpd_event_id(o
										.getString("cpd_event_id"));
							}
							if (o.has("credit")) {
								allDataBean.setCredit(o.getString("credit"));
								summaryBean.setCredit(o.getString("credit"));
							}
							if (o.has("cpd_user_type")) {
								allDataBean.setCpd_user_type(o
										.getString("cpd_user_type"));
								summaryBean.setCpd_user_type(o
										.getString("cpd_user_type"));
							}
							if (o.has("event_description")) {
								allDataBean.setEvent_description(o
										.getString("event_description"));
								summaryBean.setEvent_description(o
										.getString("event_description"));
							}
							if (o.has("created")) {
								allDataBean.setCreated(o.getString("created"));
								summaryBean.setCreated(o.getString("created"));
							}
							allDataBean.setName(name);
							allDataBean.setCpd_id(cpd_id);
							summaryBean.setName(name);
							summaryBean.setCpd_id(cpd_id);
							temparr.add(allDataBean);
							summaryData.add(summaryBean);
						}
					} else {
						allDataBean = new CPDBean();
						allDataBean.setName(name);
						allDataBean.setCpd_id(cpd_id);
						temparr.add(allDataBean);
					}

					cpdBean.setCredit_detail(credit + "/" + totalCreditLimit);
					cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
					cpdBean.setCredit(credit + "");
					cpdArray.add(cpdBean);
					credit = 0;
					totalCreditLimit = 0;
					map.put(name, temparr);
				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = credit + Float.parseFloat(bean.getCredit());
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
				}

				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				cpdobj.setCredit_percentage((credit * 100) / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				for (CPDBean b : summaryData) {
					b.setName("Summary");
				}
				map.put("Summary", summaryData);
				cpdUKArraylist.addAll(cpdArray);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public ArrayList<CPDBean> convertJsonToArrayListforSavedEventDetail(
			JSONObject jObj, Context ctx, int cpdId) {

		try {
			SessionManager sm = new SessionManager(ctx);
			String cpdChoice = sm.getCPDChoice();
			JSONArray arr = jObj.getJSONObject("add_cpd_event").getJSONArray(
					"cpd_data");
			if (cpdChoice.contains("1")) {
				summaryData = new ArrayList<CPDBean>();
				cpdArray = new ArrayList<CPDBean>();
				temparr = new ArrayList<CPDBean>();
				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
					}
					if (arr.getJSONObject(i).has("credit")) {
						totalCreditLimit = Float.parseFloat(arr
								.getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}
					if (cpdId == Integer.parseInt(cpd_id)) {
						CPDBean cpdBean = new CPDBean();
						CPDBean allDataBean = null;
						cpdBean.setName(name);
						cpdBean.setCpd_id(cpd_id);
						cpdBean.setCredit_limit(totalCreditLimit + "");
						JSONArray objArr = arr.getJSONObject(i).getJSONArray(
								"cpd_types");
						for (int j = 0; j < objArr.length(); j++) {
							JSONArray arrcpd = objArr.getJSONObject(j)
									.getJSONArray("cpd_events");
							typename = objArr.getJSONObject(j).getString(
									"type_name");
							typeid = objArr.getJSONObject(j).getString(
									"cpd_type_id");
							if (arrcpd.length() > 0) {
								for (int k = 0; k < arrcpd.length(); k++) {
									JSONObject obj1 = arrcpd.getJSONObject(k);
									allDataBean = new CPDBean();
									if (obj1.has("credit")) {
										credit = credit
												+ Float.parseFloat(obj1
														.getString("credit"));
									}
									if (obj1.has("clinical")) {
										allDataBean.setClinical(obj1
												.getString("clinical"));
									}
									if (obj1.has("type_date")) {
										allDataBean.setType_date(obj1
												.getString("type_date"));
									}
									if (obj1.has("cpd_event_id")) {
										allDataBean.setCpd_event_id(obj1
												.getString("cpd_event_id"));
									}
									if (obj1.has("credit")) {
										allDataBean.setCredit(obj1
												.getString("credit"));
									}
									if (obj1.has("cpd_user_type")) {
										allDataBean.setCpd_user_type(obj1
												.getString("cpd_user_type"));
									}
									if (obj1.has("event_description")) {
										allDataBean
												.setEvent_description(obj1
														.getString("event_description"));
									}
									if (obj1.has("cpd_id")) {
										allDataBean.setCpd_id(obj1
												.getString("cpd_id"));
									}
									if (obj1.has("created")) {
										allDataBean.setCreated(obj1
												.getString("created"));
									}
									allDataBean.setName(name);
									allDataBean.setType_name(typename);
									allDataBean.setCpd_type_id(typeid);
									allDataBean.setCpd_id(cpd_id);
									if (cpdId == Integer.parseInt(cpd_id)) {
										temparr.add(allDataBean);
									}
									summaryData.add(allDataBean);
								}
							} else {
								allDataBean = new CPDBean();
								allDataBean.setName(name);
								allDataBean.setType_name(typename);
								allDataBean.setCpd_type_id(typeid);
								allDataBean.setCpd_id(cpd_id);
								temparr.add(allDataBean);
							}
						}
						cpdBean.setCredit_detail(credit + "/"
								+ totalCreditLimit);
						cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
						cpdBean.setCredit(credit + "");
						cpdArray.add(cpdBean);
						credit = 0;
						totalCreditLimit = 0;
						// map.put(name, temparr);
					}

				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = credit + Float.parseFloat(bean.getCredit());
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
				}

				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				cpdobj.setCredit_percentage((credit * 100) / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				// map.put("Summary", summaryData);
				cpdUKArraylist.addAll(cpdArray);
			} else if (cpdChoice.contains("2")) {
				CPDBean allDataBean = null;
				// map = new HashMap<String, ArrayList<CPDBean>>();
				cpdArray = new ArrayList<CPDBean>();
				summaryData = new ArrayList<CPDBean>();
				temparr = new ArrayList<CPDBean>();
				for (int i = 0; i < arr.length(); i++) {
					if (arr.getJSONObject(i).has("name")) {
						name = arr.getJSONObject(i).get("name").toString();
						// System.out.println("name   " + name);
					}
					if (arr.getJSONObject(i).has("credit")) {
						totalCreditLimit = Float.parseFloat(arr
								.getJSONObject(i).get("credit").toString());
					}
					if (arr.getJSONObject(i).has("cpd_id")) {
						cpd_id = arr.getJSONObject(i).get("cpd_id").toString();
						// System.out.println("cpd_id   " + cpd_id);
					}

					CPDBean cpdBean = new CPDBean();
					cpdBean.setName(name);
					cpdBean.setCpd_id(cpd_id);
					cpdBean.setCredit_limit(totalCreditLimit + "");

					JSONArray obj = arr.getJSONObject(i).getJSONArray(
							"cpd_types");
					if (obj.length() > 0) {
						for (int j = 0; j < obj.length(); j++) {
							JSONObject o = obj.getJSONObject(j);
							allDataBean = new CPDBean();
							if (o.has("credit")) {
								credit = credit
										+ Float.parseFloat(o
												.getString("credit"));
							}
							if (o.has("clinical")) {
								allDataBean
										.setClinical(o.getString("clinical"));
							}
							if (o.has("type_name")) {
								allDataBean.setType_name(o
										.getString("type_name"));
							}
							if (o.has("cpd_type_id")) {
								allDataBean.setCpd_type_id(o
										.getString("cpd_type_id"));
							}
							if (o.has("type_date")) {
								allDataBean.setType_date(o
										.getString("type_date"));
							}
							if (o.has("cpd_event_id")) {
								allDataBean.setCpd_event_id(o
										.getString("cpd_event_id"));
							}
							if (o.has("credit")) {
								allDataBean.setCredit(o.getString("credit"));
							}
							if (o.has("cpd_user_type")) {
								allDataBean.setCpd_user_type(o
										.getString("cpd_user_type"));
							}
							if (o.has("event_description")) {
								allDataBean.setEvent_description(o
										.getString("event_description"));
							}
							if (o.has("cpd_id")) {
								allDataBean.setCpd_id(o.getString("cpd_id"));
							}
							if (o.has("created")) {
								allDataBean.setCreated(o.getString("created"));
							}
							allDataBean.setName(name);
							allDataBean.setCpd_id(cpd_id);
							if (cpdId == Integer.parseInt(cpd_id)) {
								temparr.add(allDataBean);
							}
							summaryData.add(allDataBean);
						}
					} else {
						allDataBean = new CPDBean();
						allDataBean.setName(name);
						allDataBean.setCpd_id(cpd_id);
						temparr.add(allDataBean);
					}

					cpdBean.setCredit_detail(credit + "/" + totalCreditLimit);
					cpdBean.setCredit_percentage(((credit * 100) / totalCreditLimit));
					cpdBean.setCredit(credit + "");
					cpdArray.add(cpdBean);
					credit = 0;
					totalCreditLimit = 0;
					// map.put(name, temparr);
				}
				cpdUKArraylist = new ArrayList<CPDBean>();
				for (CPDBean bean : cpdArray) {
					credit = credit + Float.parseFloat(bean.getCredit());
					totalCreditLimit = totalCreditLimit
							+ Float.parseFloat(bean.getCredit_limit());
				}
				CPDBean cpdobj = new CPDBean();
				cpdobj.setName("Summary");
				cpdobj.setCredit_percentage((credit * 100) / 50);
				cpdobj.setCredit_detail(credit + "" + "/50");
				credit = 0;
				cpdUKArraylist.add(cpdobj);
				// map.put("Summary", summaryData);
				cpdUKArraylist.addAll(cpdArray);
			}
			for (CPDBean bean : cpdUKArraylist) {
				// System.out.println("Name <>><><<>" + bean.getName());
				// System.out.println("CPD event <><><><>"
				// + bean.getEvent_description());
				// System.out.println("CPd event id <><><><>"
				// + bean.getCpd_event_id());
				// System.out.println("CPD type <><><><>" +
				// bean.getType_name());
				// System.out.println("CPde type idd<><><><><>"
				// + bean.getCpd_type_id());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temparr;
	}

	public ArrayList<BookmarkBean> bookmarkJsonToMap(JSONObject obj) {
		ArrayList<BookmarkBean> bookmarkArrList = new ArrayList<BookmarkBean>();
		try {
			JSONArray arr = obj.getJSONObject("show_bookmark").getJSONArray(
					"bookmark_data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject bookmarkJson = arr.getJSONObject(i);
				BookmarkBean bean = new BookmarkBean();
				if (bookmarkJson.has("bookmark_id")) {
					bean.setBookmark_id(bookmarkJson.get("bookmark_id")
							.toString());
				}
				if (bookmarkJson.has("bookmark_title")) {
					bean.setBookmark_title(bookmarkJson.get("bookmark_title")
							.toString());
				}
				if (bookmarkJson.has("bookmark_link")) {
					bean.setBookmark_link(bookmarkJson.get("bookmark_link")
							.toString());
				}
				if (bookmarkJson.has("timestamp")) {
					bean.setTimestamp(bookmarkJson.getString("timestamp"));
				}
				bookmarkArrList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookmarkArrList;
	}

}
