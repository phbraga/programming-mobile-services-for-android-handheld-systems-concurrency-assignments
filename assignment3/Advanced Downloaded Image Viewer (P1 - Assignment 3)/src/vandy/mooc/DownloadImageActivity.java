package vandy.mooc;

import android.net.Uri;

public class DownloadImageActivity extends GenericImageActivity {

	public static final String ACTION_DOWNLOAD = "com.phmb.action.DOWNLOAD";
	
	@Override
	protected Uri doInBackgroundHook(Uri uri) {
		return Utils.downloadImage(getApplicationContext(), uri);
	}
    
}
