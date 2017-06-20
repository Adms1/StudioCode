package com.waterworks.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.waterworks.R;
import com.waterworks.model.ExpandableListChildModel;

import java.util.HashMap;
import java.util.List;

public class StudentSkillExpListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<ExpandableListChildModel>> _listDataChild;

	public StudentSkillExpListAdapter(Context context, List<String> listDataHeader,
									  HashMap<String, List<ExpandableListChildModel>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public ExpandableListChildModel getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childDate = (String) getChild(groupPosition, childPosition).getDate();
		final String childText = (String) getChild(groupPosition, childPosition).getItem();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item_child, null);
		}

		TextView txtChildDate = (TextView) convertView.findViewById(R.id.txtChildDate);
		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

		txtChildDate.setText(childDate);
		txtListChild.setText(Html.fromHtml(childText));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group_header, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.txtListHeader);
		TextView txtListHeaderArrowSign = (TextView) convertView.findViewById(R.id.txtListHeaderArrowSign);

		if(isExpanded){
			txtListHeaderArrowSign.setText("v");
		}else{
			txtListHeaderArrowSign.setText(">");
		}

		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
