 package asia.sk8.sk8board;

import java.util.Calendar;
import java.util.Locale;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class TabActivity extends Activity implements TabListener {
	public static int counter;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    NameCardFragment nameCardFragment;
    TaskFragment taskFragment;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("SK8Time");
		query.whereEqualTo("date", "22/12/2014 00:00");
		query.whereEqualTo("user", "Jack Song");
		query.countInBackground(new CountCallback() {
			
			@Override
			public void done(int count, ParseException e) {
				if (e != null) {
					Toast.makeText(TabActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
					return;
				}
				
				if (count == 0) {
					ParseObject sk8Time = ParseObject.create("SK8Time");
					Calendar c = Calendar.getInstance();  
					int yearInt = c.get(Calendar.YEAR);  
					int monthInt = c.get(Calendar.MONTH);  
					int dayInt = c.get(Calendar.DAY_OF_MONTH); 
					String currentDate=dayInt+""+"/"+monthInt+""+"/"+yearInt+"" ;
					sk8Time.put("date", currentDate);
				} else if (count == 1) {
					ParseObject sk8Time;
					try {
						sk8Time = query.getFirst();
						counter = sk8Time.getInt("numberOfDone");
						sk8Time.put("numberOfDone", counter);
						sk8Time.saveInBackground();
					} catch (ParseException e1) {
						Toast.makeText(TabActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	System.exit(0);
    }
    
    @Override
    public void onBackPressed() {
		AlertDialog.Builder backDialogBuilder = new AlertDialog.Builder(this);
		// set title
		backDialogBuilder.setTitle("Do you want to close?");
		// set dialog message
		backDialogBuilder
			.setMessage("Task details")
			.setCancelable(false)
			.setPositiveButton("YES",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					System.exit(0);
				}
        
			})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		
		AlertDialog alertDialog = backDialogBuilder.create();
		 
		// show it
		alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tab, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        	 switch (position) {
        	 case 0:
        		 if (nameCardFragment == null) {
        			 nameCardFragment = NameCardFragment.newInstance(position);
        		 } 
        		 return nameCardFragment;
			case 1:
	       		 if (taskFragment == null) {
	       			 taskFragment = TaskFragment.newInstance(position);
	    		 } 
	    		 return TaskFragment.newInstance(position);
			default:
        		 return null;
        	 }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Card";
                case 1:
                    return "Clock";
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public boolean onContextItemSelected(MenuItem item){
    	setTitle("lalala");
    	return super.onContextItemSelected(item);
    }
   

}
