package com.qbcps.sifterclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MilestonesActivity extends ListActivity {
	
	public static final String MILESTONE_NAME = "name";
	public static final String MILESTONE = "milestone";
	
	// Members
	private JSONArray mMilestoneArray;
	private JSONObject[] mAllMilestones;
	private SifterHelper mSifterHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		registerForContextMenu(getListView());
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return;
		try {
			JSONArray milestones = new JSONArray(extras.getString(SifterReader.MILESTONES));
			if (milestones != null) {
				mMilestoneArray = milestones; 
				getMilestones();
				fillData();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mSifterHelper.onException(e.toString()); // return not needed
		}
	}
	
	private void getMilestones() {
		int numberMilestones = mMilestoneArray.length();
		JSONObject[] allMilestones = new JSONObject[numberMilestones];
		try {
			for (int i = 0; i < numberMilestones; i++)
				allMilestones[i] = mMilestoneArray.getJSONObject(i);	
		} catch (JSONException e) {
			e.printStackTrace();
			mSifterHelper.onException(e.toString());
			return;
		}
		mAllMilestones = allMilestones;
	}

	private void fillData() {
		int mNum = mAllMilestones.length;
		String[] m = new String[mNum];
		try {
			for (int j = 0; j < mNum; j++)
				m[j] = mAllMilestones[j].getString(MILESTONE_NAME);
		} catch (JSONException e) {
			e.printStackTrace();
			mSifterHelper.onException(e.toString());
			return;
		}
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, m));
	}
	
	/** start MilestoneDetail activity for clicked project in list. */
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, MilestoneDetail.class);
        intent.putExtra(MILESTONE, mAllMilestones[(int)id].toString());
        // TODO use safe long typecast to int
        startActivity(intent);
    }
}
