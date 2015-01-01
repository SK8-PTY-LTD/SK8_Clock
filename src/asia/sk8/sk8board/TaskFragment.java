package asia.sk8.sk8board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TaskFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
	
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    public MyQAnalogClock clock;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TaskFragment newInstance(int sectionNumber) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.clock = (MyQAnalogClock) rootView.findViewById(R.id.taskClock);
        
        final ListView mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setVisibility(View.INVISIBLE);
        String[] mForm = new String[]{"img","title1","title2","time"};
        int[] mTo = new int[]{R.id.img,R.id.title1,R.id.title2,R.id.time};
        final List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
        Map<String,Object> mMap = null;
        for(int i=0;i<10;i++){
        	//Setting data into map
        	//Put map into map list
        	mMap=new HashMap<String,Object>();
        	mMap.put("img", R.drawable.ic_launcher);
        	mMap.put("title1", "android"+i+"");
        	mMap.put("title2", "rocky");
        	mMap.put("time", "2014-12-16");
        	mMap.put("checked", false);
        	mMap.put("radio_btn",false);
        	mList.add(mMap);
        }
        final SimpleAdapter mAdapter= new SimpleAdapter(getActivity(), mList, R.layout.cell_list_view, mForm, mTo){
        	
        	public View getView(final int position, View convertView, ViewGroup parent){
        		View view = super.getView(position, convertView, parent);
        		 final HashMap<String,Object> map = (HashMap<String, Object>) this.getItem(position);
        		 //Get checkbox information in each cell
        		 CheckBox checkBox = (CheckBox)view.findViewById(R.id.checked);
        		 checkBox.setChecked((Boolean) map.get("checked"));
        		 checkBox.setOnClickListener(new View.OnClickListener() {  
        	            @Override  
        	            public void onClick(View view) {  
        	                map.put("checked", ((CheckBox)view).isChecked());
        	                Log.d("hehehe",""+((CheckBox)view).isChecked());
        	            }  
        	        });  
        		 return view;
        		 }
        		 
        };
        	
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		//get data from the clicked item
        		
        		view.setSelected(true);
        		
//        		HashMap<String,Object> map=(HashMap<String,Object>) parent.getItemAtPosition(position);
//        		Toast t = Toast.makeText(getActivity(), ""+mList.get(position).get("title1"), Toast.LENGTH_LONG);
//        		t.show();
        	}
        });

        mListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		        final int removePosition = info.position;		
				//Build a dialog
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
				// set title
				alertDialogBuilder.setTitle("Assignee:" + "Rocky");
				// set dialog message
				alertDialogBuilder
					.setMessage("Task details")
					.setCancelable(false)
					.setPositiveButton("Done",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							
							mList.remove(removePosition);
							mAdapter.notifyDataSetChanged();
						
						}
					  })
					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
				
			}  
        });

        return rootView;
    }
    
    public void stopCountingTasks() {
    	this.clock.clearClock();
    }
}