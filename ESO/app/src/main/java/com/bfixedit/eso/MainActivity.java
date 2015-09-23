package com.bfixedit.eso;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/*
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.bfixedit.eso.TinyDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
*/

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
/**
 * MainActivity starts here
 */
public class MainActivity extends AppCompatActivity {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> lbCollection;
    static ExpandableListView expListView;

    // TODO: sharedPreference/storage keys and values, what do I need here?
    public static final String STORED_LIST = "selectedItemList";
    ArrayList<String> inputArray = new ArrayList<>();

    /**
     * onCreate generates initial VIEWS, DATA, and LISTENERS
     * @param savedInstanceState for activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // VIEWS
        setContentView(R.layout.activity_main);

        // DATA
        createGroupList();      // Creates 'ArrayList<> groupList'
        createCollection();     // Creates 'HashMap<S,L<>>() lbCollection'

        // SETUP
        expListView = (ExpandableListView) findViewById(R.id.explv_collection_list);
        final MyAdapter adapter = new MyAdapter(groupList, lbCollection);
        expListView.setAdapter(adapter);

        // ONCLICK LISTENER
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO: Should I save values as part of toggleSelection?
                adapter.toggleSelection(groupPosition, childPosition);
                // refresh adapter
                adapter.notifyDataSetInvalidated();

                // TODO: This generates ArrayList of selectedItems from Map, inputArray used for storage
                // inputArray = selectedAsList(adapter);
                return false;
            }
        });
    }// END ONCREATE

    // ADAPTERS
    private class MyAdapter<G, C> extends BaseExpandableListAdapter {
        private List<G> groups;
        private Map<G, List<C>> childMap;
        private Map<G, List<C>> selectedItems;

        public MyAdapter(List<G> groups, Map<G, List<C>> childMap){

            this.groups = groups;
            this.childMap = childMap;

            //init selected Items Array
            this.selectedItems = new HashMap<>();
        }

        public Map<G, List<C>> getSelectedItems() {
            return selectedItems;
        }

        public boolean isSelected(int groupPosition, int childPosition){
            G group = groups.get(groupPosition);
            C child = getChild(groupPosition,childPosition);
            List<C> sel = selectedItems.get(group);
            return sel != null && sel.contains(child);
        }

        public void toggleSelection(int groupPosition, int childPosition){
            G group = groups.get(groupPosition);
            C child = getChild(groupPosition,childPosition);

            List<C> sel = selectedItems.get(group);
            if (sel == null){
                sel = new ArrayList<>();
                selectedItems.put(group, sel);
            }

            if (sel.contains(child))
                sel.remove(child);
            else
                sel.add(child);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            G group = getGroup(groupPosition);
            List<C> childList = childMap.get(group);
            int childCount = childList == null ? 0:childList.size();
            return childCount;
        }

        @Override
        public G getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public C getChild(int groupPosition, int childPosition) {
            G group = getGroup(groupPosition);
            return childMap.get(group).get(childPosition);
        }


        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String groupTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.group_item, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.lb_collection_item);
            item.setText(groupTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String childTitle = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_item, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.lb_collection_item);
            item.setText(childTitle);
            if (isSelected(groupPosition,childPosition)){
                convertView.setBackgroundColor(Color.parseColor("#008000"));
            } else{
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            //return node;
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    /**
     * prettyPrintSelected() reformats 'Map selectedItems' to string with punctuation, newline, etc.
     * @param adapter MyAdapter adapter
     * @return String result
     */
    private String prettyPrintSelected(MyAdapter adapter) {
        Map<String, List<String>> selectedItems =  adapter.getSelectedItems();
        String result = "  \nSelected Items in list: \n";
        for(String group: selectedItems.keySet()){
            List<String> collection = selectedItems.get(group);
            if (collection.size() > 0){
                result+="\n" + group + ": ";
                for (String book : collection) {
                    result += book +", ";
                }
            }
        }

        Log.i("prettyPrintSelected", "Completed");
        return result;
    }

    /**
     * selectedAsList() converts 'Map selectedItems' to ArrayList
     * @param adapter MyAdapter adapter
     * @return ArrayList<String> result
     */
    private ArrayList<String> selectedAsList(MyAdapter adapter){
        Map<String, List<String>> selectedItems =  adapter.getSelectedItems();
        ArrayList<String> result =  new ArrayList<>();
        for(List<String> collection: selectedItems.values())
            result.addAll(collection);
        Log.i("selectedAsList", "Completed");
        return result;
    }

    /**
     * Storage operations
     * TODO sharedPreferences, TinyDB, or GSON
     */
    private void saveData() {
        TinyDB tinydb = new TinyDB(MainActivity.this);
        tinydb.putListString(STORED_LIST, inputArray);
    }

    private void loadData() {
        TinyDB tinydb = new TinyDB(MainActivity.this);
        inputArray = tinydb.getListString(STORED_LIST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

/**
 * original auto-generated @Override statements follow
 * onCreateOptionsMenu
 * onOptionsItemSelected
 */
    /**
     * Options Menu (Create)
     * @param menu  Options Menu
     * @return      true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Options Menu (Add Items)
     * @param item  MenuItem
     * @return      Selected Options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates ArrayList groupList from arr_lb_collection using Arrays.asList referencing xml
     * This is where the Parent Items are 'created'
     */
    private void createGroupList() {
        Resources res = this.getResources();
        groupList = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.arr_lb_collection)));
    }

    /**
     * Creates Collection of lorebooks and builds a LinkedHashMap with group
     * This is where the Child Items are 'created'
     * TODO: Any way to automate assigning arrays (or skip redeclaration)?
     */
    private void createCollection() {
        Resources res = this.getResources();

        String[] lorebooks_alikrdesertlore    = res.getStringArray(R.array.lorebooks_alikrdesertlore);
        String[] lorebooks_auridonlore        = res.getStringArray(R.array.lorebooks_auridonlore);
        String[] lorebooks_bangkorailore      = res.getStringArray(R.array.lorebooks_bangkorailore);
        String[] lorebooks_biographies        = res.getStringArray(R.array.lorebooks_biographies);
        String[] lorebooks_coldharbourlore    = res.getStringArray(R.array.lorebooks_coldharbourlore);
        String[] lorebooks_daedricprinces     = res.getStringArray(R.array.lorebooks_daedricprinces);
        String[] lorebooks_deshaanlore        = res.getStringArray(R.array.lorebooks_deshaanlore);
        String[] lorebooks_divinesanddeities  = res.getStringArray(R.array.lorebooks_divinesanddeities);
        String[] lorebooks_dungeonlore        = res.getStringArray(R.array.lorebooks_dungeonlore);
        String[] lorebooks_dwemer             = res.getStringArray(R.array.lorebooks_dwemer);
        String[] lorebooks_eastmarchlore      = res.getStringArray(R.array.lorebooks_eastmarchlore);
        String[] lorebooks_glenumbralore      = res.getStringArray(R.array.lorebooks_glenumbralore);
        String[] lorebooks_grahtwoodlore      = res.getStringArray(R.array.lorebooks_grahtwoodlore);
        String[] lorebooks_greenshadelore     = res.getStringArray(R.array.lorebooks_greenshadelore);
        String[] lorebooks_legendsofnirn      = res.getStringArray(R.array.lorebooks_legendsofnirn);
        String[] lorebooks_literature         = res.getStringArray(R.array.lorebooks_literature);
        String[] lorebooks_magicandmagicka    = res.getStringArray(R.array.lorebooks_magicandmagicka);
        String[] lorebooks_malabaltorlore     = res.getStringArray(R.array.lorebooks_malabaltorlore);
        String[] lorebooks_mythsofthemundus   = res.getStringArray(R.array.lorebooks_mythsofthemundus);
        String[] lorebooks_oblivionlore       = res.getStringArray(R.array.lorebooks_oblivionlore);
        String[] lorebooks_poetryandsong      = res.getStringArray(R.array.lorebooks_poetryandsong);
        String[] lorebooks_reapersmarchlore   = res.getStringArray(R.array.lorebooks_reapersmarchlore);
        String[] lorebooks_rivenspirelore     = res.getStringArray(R.array.lorebooks_rivenspirelore);
        String[] lorebooks_shadowfenlore      = res.getStringArray(R.array.lorebooks_shadowfenlore);
        String[] lorebooks_stonefallslore     = res.getStringArray(R.array.lorebooks_stonefallslore);
        String[] lorebooks_stormhavenlore     = res.getStringArray(R.array.lorebooks_stormhavenlore);
        String[] lorebooks_tamrielhistory     = res.getStringArray(R.array.lorebooks_tamrielhistory);
        String[] lorebooks_theriftlore        = res.getStringArray(R.array.lorebooks_theriftlore);
        String[] lorebooks_thetrialofeyevea   = res.getStringArray(R.array.lorebooks_thetrialofeyevea);
        String[] lorebooks_unknown            = res.getStringArray(R.array.lorebooks_unknown);

        /**
         * create a LinkedHashMap for the children to find their group
         * remember LinkedHashMap was set to: Map<String, List<String>> lbCollection
         */
        lbCollection = new LinkedHashMap<>();

        /**
         * This way works, just lots of chance for error and not flexible,
         * TODO: Any way to automate if / switch statement or just iterate through arrays in order?
         */
        for (String collection : groupList) {
            switch (collection) {
                case "Alik’r Desert Lore":
                    loadChild(lorebooks_alikrdesertlore);
                    break;
                case "Auridon Lore":
                    loadChild(lorebooks_auridonlore);
                    break;
                case "Bangkorai Lore":
                    loadChild(lorebooks_bangkorailore);
                    break;
                case "Biographies":
                    loadChild(lorebooks_biographies);
                    break;
                case "Coldharbour Lore":
                    loadChild(lorebooks_coldharbourlore);
                    break;
                case "Daedric Princes":
                    loadChild(lorebooks_daedricprinces);
                    break;
                case "Deshaan Lore":
                    loadChild(lorebooks_deshaanlore);
                    break;
                case "Divines and Deities":
                    loadChild(lorebooks_divinesanddeities);
                    break;
                case "Dungeon Lore":
                    loadChild(lorebooks_dungeonlore);
                    break;
                case "Dwemer":
                    loadChild(lorebooks_dwemer);
                    break;
                case "Eastmarch Lore":
                    loadChild(lorebooks_eastmarchlore);
                    break;
                case "Glenumbra Lore":
                    loadChild(lorebooks_glenumbralore);
                    break;
                case "Grahtwood Lore":
                    loadChild(lorebooks_grahtwoodlore);
                    break;
                case "Greenshade Lore":
                    loadChild(lorebooks_greenshadelore);
                    break;
                case "Legends of Nirn":
                    loadChild(lorebooks_legendsofnirn);
                    break;
                case "Literature":
                    loadChild(lorebooks_literature);
                    break;
                case "Magic and Magicka":
                    loadChild(lorebooks_magicandmagicka);
                    break;
                case "Malabal Tor Lore":
                    loadChild(lorebooks_malabaltorlore);
                    break;
                case "Myths of the Mundus":
                    loadChild(lorebooks_mythsofthemundus);
                    break;
                case "Oblivion Lore":
                    loadChild(lorebooks_oblivionlore);
                    break;
                case "Poetry and Song":
                    loadChild(lorebooks_poetryandsong);
                    break;
                case "Reaper’s March Lore":
                    loadChild(lorebooks_reapersmarchlore);
                    break;
                case "Rivenspire Lore":
                    loadChild(lorebooks_rivenspirelore);
                    break;
                case "Shadowfen Lore":
                    loadChild(lorebooks_shadowfenlore);
                    break;
                case "Stonefalls Lore":
                    loadChild(lorebooks_stonefallslore);
                    break;
                case "Stormhaven Lore":
                    loadChild(lorebooks_stormhavenlore);
                    break;
                case "Tamriel History":
                    loadChild(lorebooks_tamrielhistory);
                    break;
                case "The Rift Lore":
                    loadChild(lorebooks_theriftlore);
                    break;
                case "The Trial of Eyevea":
                    loadChild(lorebooks_thetrialofeyevea);
                    break;
                default:
                    loadChild(lorebooks_unknown);
                    break;
            }

            lbCollection.put(collection, childList);
        }
    }

    /**
     * adds children to the group-child list
     * @param lb_collection childList
     */
    private void loadChild(String[] lb_collection) {
        childList = new ArrayList<>();
        Collections.addAll(childList, lb_collection);
    }

}
