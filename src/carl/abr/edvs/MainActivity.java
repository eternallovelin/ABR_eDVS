package carl.abr.edvs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

/**
*
* @author Nicolas Oros and Julien Martel, 2014
*/
public class MainActivity extends Activity 
{
	static final String TAG = "main activity";

	/** text displayed on GUI*/
	TextView text;
	
	/** image displayed on GUI*/
	ImageView iv;

	/** thread handling serial connection to eDVS*/
	Thread_eDVS the_thread;
	
	/** handler taking care of updating GUI*/
	Handler handler;
	
	/** runnable used to update GUI*/
	Runnable runnable;

	String text_gui;


	//****************************************************** Activity functions *********************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.text_gui);
		iv = (ImageView) findViewById(R.id.an_imageView);
		text_gui = new String("Text GUI");
	}


	@Override
	protected void onResume() 
	{
		super.onResume();
		
		//start thread that will read events
		the_thread = new Thread_eDVS(this);
		the_thread.start();

		//create and start handler used to update GUI
		handler = new Handler();
		runnable = new Runnable() {
			public void run() {
				update_gui();
			}
		};	
		handler.post(runnable);
	}

	@Override
	protected void onPause()
	{
		handler.removeCallbacks(runnable);		
		the_thread.stop_thread();		
		super.onPause();
	}
	
	/**
	 * get data from thread and updates GUI
	 */
	private void update_gui()
	{		
		//get image from thread and scale it
		Bitmap bm2 = Bitmap.createScaledBitmap(the_thread.get_image(), 500, 500, false);
		iv.setImageBitmap(bm2);		
		
		//get nb bytes read from thread and display it
		text_gui = "Bytes read: " + the_thread.get_bytesRead();
		text.setText(text_gui);
		
		handler.postDelayed(runnable, 50);
	}
}
