package de.podfetcher.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import de.podfetcher.R;
import de.podfetcher.feed.Feed;
import de.podfetcher.storage.DownloadRequester;
import de.podfetcher.util.URLChecker;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/** Activity for adding/editing a Feed */
public class AddFeedActivity extends SherlockActivity {
    private static final String TAG = "AddFeedActivity";
    
	private EditText etxtFeedurl;
	private Button butConfirm;
	private Button butCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfeed);

		etxtFeedurl = (EditText) findViewById(R.id.etxtFeedurl);
		butConfirm = (Button) findViewById(R.id.butConfirm);
		butCancel = (Button) findViewById(R.id.butCancel);

		butConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewFeed();		
			}
		});

		butCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});


		
	}

	private void addNewFeed() {
		String url = etxtFeedurl.getText().toString();	
		url = URLChecker.prepareURL(url);

		if(url != null) {
			Feed feed = new Feed(url);
			DownloadRequester req = DownloadRequester.getInstance();
			req.downloadFeed(this, feed);
			setResult(RESULT_OK);
			finish();
		}
	}
	
	
}
