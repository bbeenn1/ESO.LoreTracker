package com.bfixedit.eso.adapters;

/**
 * This class extends BaseExpandableListAdapter class to handle the ExpandableList
 * Created by BRP on 9/12/15.
 */
    import java.util.List;
    import java.util.Map;

    import com.bfixedit.eso.R;

    import android.app.Activity;
    //import android.app.AlertDialog;
    import android.content.Context;
    //import android.content.DialogInterface;
    import android.graphics.Typeface;
    //import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    //import android.view.View.OnClickListener;
    import android.view.ViewGroup;
    import android.widget.BaseExpandableListAdapter;
    //import android.widget.ImageView;
    import android.widget.TextView;

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Activity context;
        private Map<String, List<String>> lbCollections;
        private List<String> lorebooks;

        public ExpandableListAdapter(Activity context, List<String> lorebooks,
                                     Map<String, List<String>> lbCollections) {
            this.context = context;
            this.lbCollections = lbCollections;
            this.lorebooks = lorebooks;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return lbCollections.get(lorebooks.get(groupPosition)).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String childTitle = (String) getChild(groupPosition, childPosition);
            LayoutInflater inflater = context.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.child_item, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.lb_collection_item);
            
            item.setText(childTitle);
            return convertView;
        }

        public int getChildrenCount(int groupPosition) {
            return lbCollections.get(lorebooks.get(groupPosition)).size();
        }

        public Object getGroup(int groupPosition) {
            return lorebooks.get(groupPosition);
        }

        public int getGroupCount() {
            return lorebooks.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String groupTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.group_item, null);
            }
            TextView item = (TextView) convertView.findViewById(R.id.lb_collection_item);
            item.setTypeface(item.getTypeface(), Typeface.BOLD);
            item.setText(groupTitle);
            return convertView;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
