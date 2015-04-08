package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    private Uri url;
    private Uri path;
    private Intent data;
    private Handler mHandler;
    
    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // you fill in here.
    	super.onCreate(savedInstanceState);
    	
    	mHandler = new Handler(Looper.getMainLooper());
    	
        // Get the URL associated with the Intent data.
        // you fill in here.
    	url = getIntent().getData();
    	
        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.  See
        // http://stackoverflow.com/questions/20412871/is-it-safe-to-finish-an-android-activity-from-a-background-thread
        // for more discussion about this topic.
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				path = DownloadUtils.downloadImage(getApplicationContext(), url);
				data = new Intent();
				data.setData(path);
				setResult(RESULT_OK, data);
			
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						DownloadImageActivity.this.finish();
					}
				});
			}
		}).start();

    }
}
