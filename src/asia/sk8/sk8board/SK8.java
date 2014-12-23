package asia.sk8.sk8board;

import com.parse.Parse;
import android.app.Application;
import android.util.Log;

public class SK8 extends Application
{
	public void onCreate()
	{
		super.onCreate();
		Parse.initialize(this, "9ZdvyrirVLKeN5wZTaJ3DO6pATVVQn7qW0SgYO1i", "JOOR8ALig5exc9OYdz84Iio9jLJN5sNgnFPx3q5N");
	}
}
	

