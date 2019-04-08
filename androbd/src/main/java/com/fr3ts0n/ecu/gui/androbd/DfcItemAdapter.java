/*
 * (C) Copyright 2015 by fr3ts0n <erwin.scheuch-heilig@gmx.at>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */

package com.fr3ts0n.ecu.gui.androbd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fr3ts0n.ecu.EcuCodeItem;
import com.fr3ts0n.pvs.IndexedProcessVar;
import com.fr3ts0n.pvs.PvList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

/**
 * Adapter to display OBD DFCs
 *
 * @author erwin
 */
public class DfcItemAdapter extends ObdItemAdapter
{

	//PvList list;
	List<String>contents;
	String url="http://mcarfix.demoscad.net/api/error-code";
	HashMap<String, String> paramMap;
	AsyncHttpClient myClient ;
	List<CarFailuresModel>itemList;

	Context context1;
	CarFailuresModel item;

	//ProgressDialog progressDialog;



	public DfcItemAdapter(Context context, int resource, PvList pvs)
	{
		super(context, resource, pvs);

	//	list=pvs;

		contents=new ArrayList<>();
		paramMap = new HashMap<>();
		myClient = new AsyncHttpClient();
		context1=context;
		itemList=new ArrayList<>();

		//progressDialog=new ProgressDialog(context1);



	}

	@Override
	public Collection<Object> getPreferredItems(PvList pvs)
	{
		return pvs.values();
	}

	/* (non-Javadoc)
	 * @see com.fr3ts0n.ecu.gui.androbd.ObdItemAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View v, final ViewGroup parent)
	{

		// get data PV
		final IndexedProcessVar currPv = (IndexedProcessVar) getItem(position);

		if (v == null)
		{
			v = mInflater.inflate(R.layout.pending_faults, parent, false);
		}

		//contents.add(String.valueOf(Objects.requireNonNull(currPv).get(EcuCodeItem.FID_CODE)));
       //String joined = TextUtils.join(", ", contents);

		String listString = "";

		for (String s : contents)
		{
			listString += s + ",";
		}

		//System.out.println(listString);

		TextView tvDescr = v.findViewById(R.id.fault_desc);
		final TextView tvValue = v.findViewById(R.id.fault_code);
		final TextView tvpart= v.findViewById(R.id.part_name);

		String beast=String.valueOf(Objects.requireNonNull(currPv).get(EcuCodeItem.FID_CODE));
		paramMap.put("error_code",beast);
		RequestParams params = new RequestParams(paramMap);

		myClient.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				//progressDialog.dismiss();

				Log.e("Mat_Error",throwable.getLocalizedMessage());

			}


			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				// called when response HTTP status is "200 OK"

				//progressDialog.dismiss();
				try {
					JSONObject jsonObject  = new JSONObject(responseString);
					boolean status = jsonObject.optBoolean("success", false);
					JSONArray array = jsonObject.getJSONObject("message").getJSONArray("msg");
					Log.v("Array_size",""+array.length());

					if(status){
						//Toast.makeText(context1,"Faults  Found",Toast.LENGTH_LONG).show();

						Log.v("success","faults found");

						for(int i=0;i<array.length();i++){

							JSONObject o=array.getJSONObject(i);
							item=new CarFailuresModel(
									o.getString("id"),
									o.getString("errorcode"),
									o.getString("errordesc"),
									o.getString("partname")
							);
							itemList.add(item);
						}

						Log.v("Object_No",""+itemList.size());

						try {
							tvpart.setText("Part Name: "+itemList.get(position).getPartname());

						}catch (Exception e){



						}


					}else{

						//Toast.makeText(context1,"Faults Not Found",Toast.LENGTH_LONG).show();

						Log.v("Faul_Error","Faults Not Found");
					}


				} catch (JSONException e) {
					Log.e("APiError",e.getLocalizedMessage());
				}

			}
		});

		//tvValue.setText(String.valueOf(Objects.requireNonNull(currPv).get(EcuCodeItem.FID_CODE))+" list_code = "+contents.get(position)+" fidel");
       // tvValue.setText(String.valueOf(Objects.requireNonNull(currPv).get(EcuCodeItem.FID_CODE))+" part_name = "+itemList.get(position).getErrordesc()+" fidel");

		//tvValue.setText(String.valueOf(Objects.requireNonNull(currPv).get(EcuCodeItem.FID_CODE)));

       // contents.add(String.valueOf(currPv.get(EcuCodeItem.FID_DESCRIPT)));



      /*  try {00m
            tvValue.setText(String.valueOf(currPv.get(EcuCodeItem.FID_CODE))+" size of list = "+contents.size());

        }catch (Exception e){



        }


*/

        tvValue.setText("Fault Code: "+String.valueOf(currPv.get(EcuCodeItem.FID_CODE)));
        tvDescr.setText("Description: "+String.valueOf(currPv.get(EcuCodeItem.FID_DESCRIPT)));

       // currPv.get()

		return v;
	}

	/* (non-Javadoc)
	 * @see com.fr3ts0n.ecu.gui.androbd.ObdItemAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View v, ViewGroup parent)
	{
		return getView(position, v, parent);
	}



}
