package vandy.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

public class RetainedFragment extends Fragment {
	
	private GenericImageActivity mGenericImageActivityCallback;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		new Task().execute(mGenericImageActivityCallback.getIntent().getData());
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mGenericImageActivityCallback = (GenericImageActivity) activity;
	}
	
	private class Task extends AsyncTask<Uri, Void, Uri> {

		@Override
		protected Uri doInBackground(Uri... params) {
			return mGenericImageActivityCallback.doInBackgroundHook(params[0]);
		}
		
		@Override
		protected void onPostExecute(Uri result) {
			super.onPostExecute(result);
			
			if(result != null) {
				Intent intent = new Intent();
				intent.setData(result);
				mGenericImageActivityCallback.setResult(Activity.RESULT_OK, intent);
			} else {
				mGenericImageActivityCallback.setResult(Activity.RESULT_CANCELED);
			}
			
			mGenericImageActivityCallback.finish();
		}
		
	}

}
