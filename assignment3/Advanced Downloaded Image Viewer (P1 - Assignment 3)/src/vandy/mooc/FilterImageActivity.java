package vandy.mooc;

import android.net.Uri;

public class FilterImageActivity extends GenericImageActivity {

	public static final String ACTION_FILTER = "com.phmb.action.FILTER";
	
	@Override
	protected Uri doInBackgroundHook(Uri uri) {
		return Utils.grayScaleFilter(getApplicationContext(), uri);
	}

}
