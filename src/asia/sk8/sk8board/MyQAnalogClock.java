package asia.sk8.sk8board;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Vibrator; 
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Toast;

import asia.sk8.sk8board.R;

public class MyQAnalogClock extends View implements OnClickListener{
	
	//时钟盘，分针、秒针、时针对象  
  
	int workTimeInMinute = 25;
	int restTimeInMinute = 5;
	int viborationStartInSecond = 2 ;
	int viborationEndInSecond = 1;
	boolean workingCondition=false;
	boolean showShadow=false;
	
    Bitmap mBmpDial;  
    Bitmap mBmpHour;  
    Bitmap mBmpMinute;  
    Bitmap mBmpSecond;  
    Bitmap mBmpSchedule;
  
    BitmapDrawable bmdHour;  
    BitmapDrawable bmdMinute;  
    BitmapDrawable bmdSecond;  
    BitmapDrawable bmdDial;  
    BitmapDrawable bmdSchedule;
  
    Paint mPaint;  
  
    Handler tickHandler;  
  
    int mWidth;  
    int mHeigh;  
    int mTempWidth;
    int mTempHeigh;  
    int centerX;  
    int centerY;  
  
    int availableWidth;  
    int availableHeight;
    int i=0;
    int j=0;
    int once=0;
    float shadowRotate;
    
    private Vibrator vibrator;
    
    private Timer timer;
    private TimerTask task = new TimerTask() { 
        @Override 
        public void run() { 
            // TODO Auto-generated method stub 
            Message message = new Message(); 
            message.what = 1; 
            handler.sendMessage(message); 
        } 
    };;
    
    Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
            // TODO Auto-generated method stub 
            // 要做的事情 
        	if (i==0 && j<2){
        		Toast.makeText(getContext(), "You can have a rest~", Toast.LENGTH_SHORT).show();
        		vibrator.vibrate(viborationEndInSecond * 1000);  
        		i++;
        		j++;
        	}
        	else if(i==1){
        		i=0;
        		vibrator.vibrate(viborationStartInSecond * 1000);  
        		Toast.makeText(getContext(), "Start working! :)", Toast.LENGTH_SHORT).show();
        		timer.cancel();
        		task = new TimerTask() { 
        	        @Override 
        	        public void run() { 
        	            // TODO Auto-generated method stub 
        	            Message message = new Message(); 
        	            message.what = 1; 
        	            handler.sendMessage(message); 
        	        } 
        	    };
    			timer = new Timer();
    			timer.schedule(task, workTimeInMinute * 1000 ,restTimeInMinute * 1000); 
    		
        	}
        	else{
        		j=0;
        		TabActivity.counter++;
        		clearClock();
        		Toast.makeText(getContext(), "You can have a long rest~ counter is " + TabActivity.counter, Toast.LENGTH_SHORT).show();
        		workingCondition=false;
        	}
        	
            super.handleMessage(msg); 
        }
    };
    
    public MyQAnalogClock(Context context,AttributeSet attr)  
    {  
    	super(context);  
    	vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        
        mBmpHour = BitmapFactory.decodeResource(getResources(), R.drawable.clock_hour);  
        bmdHour = new BitmapDrawable(mBmpHour);  
  
        mBmpMinute = BitmapFactory.decodeResource(getResources(),  
                R.drawable.clock_minute);  
        bmdMinute = new BitmapDrawable(mBmpMinute);  
  
        mBmpSecond = BitmapFactory.decodeResource(getResources(),  
                R.drawable.clockgoog_minute);  
        bmdSecond = new BitmapDrawable(mBmpSecond);  
        
        
        
        
        
        
        //Change a photo to show the shadow
        mBmpSchedule=BitmapFactory.decodeResource(getResources(), R.drawable.profile);
        bmdSchedule= new BitmapDrawable(mBmpSchedule);
        
        
        
        
        
        
        mBmpDial = BitmapFactory.decodeResource(getResources(),  
                R.drawable.clock_dial);  
        bmdDial = new BitmapDrawable(mBmpDial);  
//        mWidth = mBmpDial.getWidth();  
//        mHeigh = mBmpDial.getHeight();  
        //Get window width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); 
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        availableWidth = size.x;
        availableHeight = size.y;
        //Do clock settings
        mWidth = (int) (availableWidth * 0.55);
        mHeigh = (int) (availableWidth * 0.55);
        centerX = availableWidth / 2;  
        centerY = (int) (mHeigh/2);

        mTempWidth = bmdSecond.getIntrinsicWidth();  
  
        mPaint = new Paint();  
        mPaint.setColor(Color.BLUE);  
        
        setOnClickListener(this);
        
        run();
    }  
  
    public void run() {  
        tickHandler = new Handler();  
        tickHandler.post(tickRunnable);  
    }  
  
    private Runnable tickRunnable = new Runnable() {  
        public void run() {  
            postInvalidate();  
            tickHandler.postDelayed(tickRunnable, 1000);  
        }  
    };  
  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  

        
        Calendar cal = Calendar.getInstance(); 
        int hour = cal.get(Calendar.HOUR);  
        int minute = cal.get(Calendar.MINUTE);  
        int second = cal.get(Calendar.SECOND);
        float hourRotate = hour * 30.0f + minute / 60.0f * 30.0f;  
        float minuteRotate = minute * 6.0f;  
        float secondRotate = second * 6.0f; 
  
        boolean scaled = false;  
  
        if (availableWidth < mWidth || availableHeight < mHeigh) {  
            scaled = true;  
            float scale = Math.min((float) availableWidth / (float) mWidth, (float) availableHeight / (float) mHeigh);  
            canvas.save();  
            canvas.scale(scale, scale, centerX, centerY);  
        }  
  
        bmdDial.setBounds(centerX - (mWidth / 2), centerY - (mHeigh / 2),  
                centerX + (mWidth / 2), centerY + (mHeigh / 2));  
        bmdDial.draw(canvas);  
  
        if (workingCondition==true) {
			if (once==0){
				once=1;
				Calendar c = Calendar.getInstance();
				int minuteInt = c.get(Calendar.MINUTE);
				shadowRotate = minuteInt * 6.0f;
			} 
			
	        mTempHeigh = (int) (availableWidth * 0.44);
	        mTempWidth = mTempHeigh; 
	        canvas.save();  
	        canvas.rotate(shadowRotate, centerX, centerY);  
	        bmdSchedule.setBounds(centerX - (mTempWidth / 2), centerY  
	                - (mTempHeigh / 2), centerX + (mTempWidth / 2), centerY  
	                + (mTempHeigh / 2));  
	        bmdSchedule.draw(canvas); 
	        canvas.restore(); 
		}
        mTempHeigh = (int) (availableWidth * 0.44);
        mTempWidth = (int) (mTempHeigh/6);  
          
        canvas.save();  
        canvas.rotate(hourRotate, centerX, centerY);  
        bmdHour.setBounds(centerX - (mTempWidth / 2), centerY  
                - (mTempHeigh / 2), centerX + (mTempWidth / 2), centerY  
                + (mTempHeigh / 2));  
        bmdHour.draw(canvas);  
  
        canvas.restore();  
  
        mTempHeigh = (int) (availableWidth * 0.44);
        mTempWidth = (int) (mTempHeigh/6); 
        canvas.save();  
        canvas.rotate(minuteRotate, centerX, centerY);  
        bmdMinute.setBounds(centerX - (mTempWidth / 2), centerY  
                - (mTempHeigh / 2), centerX + (mTempWidth / 2), centerY  
                + (mTempHeigh / 2));  
        bmdMinute.draw(canvas);  
  
		        
        canvas.restore();  
  
        mTempHeigh = (int) (availableWidth * 0.44);
        mTempWidth = (int) (mTempHeigh/6); 
        canvas.rotate(secondRotate, centerX, centerY);  
        bmdSecond.setBounds(centerX - (mTempWidth / 2), centerY  
                - (mTempHeigh / 2), centerX + (mTempWidth / 2), centerY  
                + (mTempHeigh / 2));  
        bmdSecond.draw(canvas);  
 

		
        if (scaled) {  
            canvas.restore();  
        }  
    }
    
    
	@Override
	public void onClick(View v) {
		if (workingCondition==false)
		{
			workingCondition=true;
			once=0;
			Toast.makeText(getContext(), "Start working! :)", Toast.LENGTH_SHORT).show();
			timer = new Timer();
			timer.schedule(task, workTimeInMinute * 1000,restTimeInMinute * 1000);
 
	        

		} else {
			
		}
	}
	
	public void clearClock() {
		vibrator.vibrate(viborationStartInSecond*1000);  
		timer.cancel();
		task = new TimerTask() { 
	        @Override 
	        public void run() { 
	            // TODO Auto-generated method stub 
	            Message message = new Message(); 
	            message.what = 1; 
	            handler.sendMessage(message); 
	            
	        } 
	    };
	}
} 