package com.netease.miniskirt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.netease.miniskirt.R;

public class ReviewPhotoActivity extends Activity {
	/**
	 * 返回按钮
	 */
	private Button mBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_photo_activity);
		mBack = (Button)findViewById(R.id.review_photo_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ReviewPhotoActivity.this, UploadPhotoActivity.class));
				finish();
			}
		});
	}
}
