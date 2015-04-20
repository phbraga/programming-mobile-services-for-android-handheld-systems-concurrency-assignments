package vandy.mooc;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;

public abstract class GenericImageActivity extends Activity{
	
	protected static final String TAG = "TAG_RETAINED_FRAGMENT";
	protected RetainedFragment mRetainedFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentManager fragmentManager = getFragmentManager();
		mRetainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag(TAG);
		
		if(mRetainedFragment == null) {
			mRetainedFragment = new RetainedFragment();
			fragmentManager.beginTransaction().add(mRetainedFragment, TAG).commit();
		}
	}
	
	protected abstract Uri doInBackgroundHook (Uri uri);

}
