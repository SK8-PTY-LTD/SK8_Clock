package asia.sk8.sk8board;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class NameCardFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NameCardFragment newInstance(int sectionNumber) {
        NameCardFragment fragment = new NameCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

	public String userName;
	private String userTitle;
	private long userNumber;
	private String userEmail;
	private Bitmap userProfileImage;
	private Bitmap qrImage;
	protected boolean isQRShown;
			


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        final View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        final ParseImageView imgBtView = (ParseImageView) rootView.findViewById(R.id.profileProfileImageButton);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		query.getInBackground("cTEH986Bjz", new GetCallback<ParseObject>() {
        TextView titleTxView = (TextView) rootView.findViewById(R.id.profileTitleTextView);
        TextView numberTxView = (TextView) rootView.findViewById(R.id.profilePhoneNumberTextView);
        TextView mailTxView = (TextView) rootView.findViewById(R.id.profileMailboxTextView);
        TextView nameTxView = (TextView) rootView.findViewById(R.id.profileNameTextView);

			@Override
			public void done(ParseObject user, ParseException e) {
			    if (e == null) {
			    	//Get and set user info
			    	userName = user.getString("name");
			    	userEmail = user.getString("email");
			    	userTitle = user.getString("title");
			    	userNumber = user.getLong("number");
			    	titleTxView.setText(userTitle);
			    	numberTxView.setText(userNumber + "");
			    	Log.d("lalala", userNumber + "");
			    	mailTxView.setText(userEmail);
			    	nameTxView.setText(userName);
			    	Log.d("haha123456", userName);
			    	//Download and set user image
			    	ParseFile imgFile = user.getParseFile("profileImage");
			    	imgBtView.setParseFile(imgFile);
			    	imgBtView.loadInBackground(new GetDataCallback(){
						@Override
						public void done(byte[] imgData, ParseException e) {
//							public static Bitmap Bytes2Bitmap(byte[] b){
//								if (b.length!=0)
//								{
//									return BitmapFactory.decodeByteArray(b,0,b.length);
//								}
//								return null;
//							}
//							imgData.Bytes2Bitmap()
								
							if (e == null) {
								//Decode bytes data to image
								Bitmap img = BitmapFactory.decodeByteArray(imgData,0,imgData.length);
								//Cache image locally
								//So that every time image is needed, we don't need to donwload again
								userProfileImage = img;
							} else {
								Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					});
			    } else {
			    	// something went wrong
			    	Log.d("score", "Error: " + e.getMessage());	    	
			    }
			}
		});

			View.OnClickListener imgBtOnClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Get current image
				//BitmapDrawable img1 = (BitmapDrawable)imgBtView.getDrawable();
				//Bitmap bm1 = img1.getBitmap();
				
				//Download qr on another thread
				new Thread() {
					@Override
					public void run() {
						try {
							String urlString = "https://chart.googleapis.com/chart?chs=320x320&cht=qr&chl=MECARD:N:"+userName+";TITLE:"+userTitle+";TEL:"+userNumber+""+";EMAIL:"+userEmail+";NOTE:sk8.asia;;";
							urlString=urlString.replaceAll(" ","%20");
							Log.d("lalala", urlString);
							URL url;
							url = new URL(urlString);
							Bitmap bm2;
							try {
								bm2 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
								qrImage=bm2;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}
				}.start();
					
				
				if (isQRShown) {
					if (userProfileImage == null){
						imgBtView.setImageResource(R.drawable.profile);
						isQRShown=false;
					}
					else{
						imgBtView.setImageBitmap(userProfileImage);
						isQRShown=false;
					}

				} else {
					if (qrImage == null){
						imgBtView.setImageResource(R.drawable.loading);
						isQRShown=true;
					}
					imgBtView.setImageBitmap(qrImage);
					isQRShown=true;
				}
				
				//imgBtView.setImageBitmap(bmp);
				
			/*	if (bm1 == bm2) {
					// Change to new image
					if (userProfileImage == null) {
						
						// User Profile Image hadn't been downloaded yet, 
						// Or is still downloading
						// Set the image view to default image.
						imgBtView.setImageResource(R.drawable.profile);
					} else {
						// Image had already been downloaded
						// Display image
						
						imgBtView.setImageBitmap(userProfileImage);
					}
				} else {
					imgBtView.setImageBitmap(qrImage);
				}*/
			}
		};
		imgBtView.setOnClickListener(imgBtOnClickListener);
        
        
       
		return rootView;
    }
    

}
