package vandy.mooc;

import java.io.File;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends LifecycleLoggingActivity {

	private static final int DOWNLOAD_IMAGE_REQUEST = 99;
	private static final int FILTER_IMAGE_REQUEST = 100;

	interface ResultHandler {

		void execute(Intent data);

		void printError(String message);

	}

	private EditText mUrlEditText;

	private Uri mDefaultUrl = Uri
			.parse("http://www.dre.vanderbilt.edu/~schmidt/robot.png");

	private SparseArray<ResultHandler> mSparseHandlerArray;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mSparseHandlerArray = new SparseArray<ResultHandler>();
        
        mSparseHandlerArray.put(DOWNLOAD_IMAGE_REQUEST, new ResultHandler() {
			
			@Override
			public void printError(String message) {
				Utils.showToast(getApplicationContext(), message);
			}
			
			@Override
			public void execute(Intent data) {
				Intent intent = makeFilterImageIntent(data.getData());
				startActivityForResult(intent, FILTER_IMAGE_REQUEST);
			}
		});
        
        mSparseHandlerArray.put(FILTER_IMAGE_REQUEST, new ResultHandler() {
			
			@Override
			public void printError(String message) {
				Utils.showToast(getApplicationContext(), message);
			}
			
			@Override
			public void execute(Intent data) {
				Intent intent = makeGalleryIntent(data.getDataString());
				startActivity(intent);
				
			}
		});
        
        mUrlEditText = (EditText) findViewById(R.id.url);
        
        Button downloadButton = (Button) findViewById(R.id.button1);
    	downloadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				downloadImage(v);
			}
		});
    }
	
	@Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
        	
        	if (requestCode == DOWNLOAD_IMAGE_REQUEST) {
            	Uri path = data.getData();
            	
            	if(null == path) {
            		mSparseHandlerArray.get(requestCode).printError("Invalid URL");
            		return;
            	}
        	}
        	
        	mSparseHandlerArray.get(requestCode).execute(data);
        	
        } else if (resultCode == RESULT_CANCELED) {
        	
        	mSparseHandlerArray.get(requestCode).printError("A problem occurred when trying to download contents at the given URL");
        
        }
    }    

	public void downloadImage(View view) {
		try {
			hideKeyboard(this, mUrlEditText.getWindowToken());

			Uri url = getUrl();
			Intent downloadImageIntent = null;

			if (null != url) {
				downloadImageIntent = makeDownloadImageIntent(url);
			}

			startActivityForResult(downloadImageIntent, DOWNLOAD_IMAGE_REQUEST);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Intent makeGalleryIntent(String pathToImageFile) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(pathToImageFile)),
				"image/*");

		return intent;
	}

	private Intent makeDownloadImageIntent(Uri url) {
		Intent intent =  new Intent(DownloadImageActivity.ACTION_DOWNLOAD, url);
		intent.putExtra(SearchManager.QUERY, url.toString());
		
		return intent;
	}
	
	private Intent makeFilterImageIntent(Uri uri) {
		Intent intent = new Intent(FilterImageActivity.ACTION_FILTER);
		intent.setDataAndType(uri, "image/*");
		
		return intent;
	}

	protected Uri getUrl() {
		Uri url = null;

		url = Uri.parse(mUrlEditText.getText().toString());

		String uri = url.toString();
		if (uri == null || uri.equals(""))
			url = mDefaultUrl;

		if (URLUtil.isValidUrl(url.toString()))
			return url;
		else {
			Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	public void hideKeyboard(Activity activity, IBinder windowToken) {
		InputMethodManager mgr = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(windowToken, 0);
	}
}
