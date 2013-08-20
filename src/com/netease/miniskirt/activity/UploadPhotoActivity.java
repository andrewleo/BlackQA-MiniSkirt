package com.netease.miniskirt.activity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.netease.miniskirt.R;

public class UploadPhotoActivity extends Activity {

	/**
	 * 拍照按钮
	 */
	private Button mAvatarChangeBtn;

	/**
	 * 提交按钮
	 */
	private Button mUploadBtn;
	/**
	 * 头像图片
	 */
	private ImageView mAvatarUploadImg;
	/**
	 * 下载按钮
	 */
	private Button mAvatarDownload;
	/**
	 * 下载图片
	 */
	private ImageView mAvatarDownloadImg;

	/**
	 * 图片展示按钮
	 */
	private Button mReviewPhotoBtn;

	// 用户照相路径
	private String photoPath;
	// 修剪后的头像路径
	private String avatarPath;

	// 上传头像后保存下来的URL
	private EditText avatarURLEditText;

	public static final String SDCARD_URL = "/sdcard/MiniSkirt/Camera/";

	/**
	 * 退出时间
	 */
	private long mExitTime;
	/**
	 * 退出间隔
	 */
	private static final int INTERVAL = 2000;

	// /////////////////////////////////////////////////////////////////////

	String SD_CARD_TEMP_DIR = Environment.getExternalStorageDirectory()
			+ File.separator + "tmpPhoto1.jpg";
	Bitmap bitmap = null;
	final int TAKE_PICTURE = 1;
	private String picName;
	private String parentFilePath;
	private String key, user_id, email;
	private String newestPicId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_photo_activity);
		key = getIntent().getStringExtra("key");
		user_id = getIntent().getStringExtra("user_id");
		email = getIntent().getStringExtra("email");
		Log.d("miniskirt", key);
		Log.d("miniskirt", user_id);
		Log.d("miniskirt", email);
		parentFilePath = getBaseContext().getFilesDir().toString();
		;
		findViewById();
		// mAvatarUploadImg.setOnClickListener(portraitListener);
		mAvatarChangeBtn.setOnClickListener(portraitListener);
		mUploadBtn.setOnClickListener(uploadListener);
		mAvatarDownload.setOnClickListener(downloadListener);
		mReviewPhotoBtn.setOnClickListener(shareListener);
	}

	private void findViewById() {
		mUploadBtn = (Button) findViewById(R.id.avatar_upload);
		mAvatarChangeBtn = (Button) findViewById(R.id.avatar_upload_change);
		mAvatarUploadImg = (ImageView) findViewById(R.id.avatar_upload_img);
		avatarURLEditText = (EditText) findViewById(R.id.avatar_url);
		mAvatarDownload = (Button) findViewById(R.id.avatar_download);
		mAvatarDownloadImg = (ImageView) findViewById(R.id.avatar_download_img);
		mReviewPhotoBtn = (Button) findViewById(R.id.review_photo_button);

	}

	private OnClickListener portraitListener = new OnClickListener() {

		public void onClick(View v) {
			final String[] menuItem = { "拍照", "从本地选择" };
			new AlertDialog.Builder(UploadPhotoActivity.this)
					.setTitle("选择图片")
					.setItems(
							menuItem,
							new android.content.DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										Intent intent = new Intent(
												"android.media.action.IMAGE_CAPTURE");
										intent.putExtra("crop", "true");
										intent.putExtra("aspectX", 1);
										intent.putExtra("aspectY", 1);
										intent.putExtra("outputX", 320);
										intent.putExtra("outputY", 320);
										intent.putExtra("return-data", true);
										startActivityForResult(intent,
												TAKE_PICTURE);
										break;
									case 1:
										doPickPhotoFromGallery();
										break;
									}
								}
							}).show();
		}
	};

	// 保存相机拍照
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		File dir = new File(parentFilePath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (requestCode == TAKE_PICTURE) {
			if (resultCode == RESULT_OK) {
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				mAvatarUploadImg.setImageBitmap(bm);

				File myCaptureFile = new File(parentFilePath + File.separator
						+ picName + ".jpg");
				try {
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(myCaptureFile));
					bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
					bos.flush();
					bos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inSampleSize = 8;
				File file = new File(SD_CARD_TEMP_DIR);

				if (file.exists()) {
					Bitmap cameraBitmap = BitmapFactory.decodeFile(
							SD_CARD_TEMP_DIR, bitmapOptions);
					bitmap = cameraBitmap;
					mAvatarUploadImg.setImageBitmap(bitmap);
				}

			}
		} else if (requestCode == 2) {
			if (resultCode == Activity.RESULT_OK) {
				Bitmap photo = data.getParcelableExtra("data");
				bitmap = photo;
				File myCaptureFile = new File(parentFilePath + File.separator
						+ picName + ".jpg");
				try {
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(myCaptureFile));
					bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
					bos.flush();
					bos.close();
				} catch (Exception e) {
				}
				if (photo != null) {
					mAvatarUploadImg.setImageBitmap(bitmap);
				}
			}
		}
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, 2);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "未能找到照片", Toast.LENGTH_LONG).show();
		}
	}

	// 封装请求Gallery的intent
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		return intent;
	}

	private OnClickListener downloadListener = new OnClickListener() {

		public void onClick(View v) {
			try {
				// 获取自己分享的图片信息
				String request = "http://www.qiu-chu.com/peach/pictures?key="
						+ key;
				URL url = new URL(request);
				HttpURLConnection urlConn1 = (HttpURLConnection) url
						.openConnection();
				InputStreamReader in = new InputStreamReader(
						urlConn1.getInputStream());
				BufferedReader bufferReader = new BufferedReader(in);
				String result = "";
				String readLine = null;
				while ((readLine = bufferReader.readLine()) != null) {
					result += readLine;
				}
				in.close();
				urlConn1.disconnect();
				Log.d("miniskirt", result);
				if ("[]".equals(result.trim())) {
					Toast.makeText(UploadPhotoActivity.this, "无可下载的图片",
							Toast.LENGTH_SHORT).show();

				}

				JSONArray array = new JSONArray(result);
				int len = array.length();

				JSONObject object = array.getJSONObject(len - 1);
				newestPicId = object.getString("id");
				// 下载图片
				Log.d("miniskirt", newestPicId);
				request = "http://www.qiu-chu.com/peach/pictures/down/"
						+ newestPicId + "?key=" + key;
				url = new URL(request);
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				InputStream is = urlConn.getInputStream();
				Bitmap image = BitmapFactory.decodeStream(is);
				mAvatarDownloadImg.setImageBitmap(image);
				Toast.makeText(UploadPhotoActivity.this, "下载成功",
						Toast.LENGTH_SHORT).show();
				is.close();
				urlConn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 上传
	 */
	private OnClickListener uploadListener = new OnClickListener() {

		public void onClick(View v) {
			try {
				// 添加图片
				HttpClient httpClient = new DefaultHttpClient();
				MultipartEntity mpEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				avatarPath = getBaseContext().getFilesDir().toString()
						+ File.separator + picName + ".jpg";
				mpEntity.addPart("file", new FileBody(new File(getBaseContext()
						.getFilesDir().toString()
						+ File.separator
						+ picName
						+ ".jpg"), picName + ".jpg"));
				// mpEntity.addPart("file", new ContentBody(imge));
				// // 邮箱
				mpEntity.addPart("user_id", new StringBody(user_id));
				String request = "http://www.qiu-chu.com/peach/pictures?key="
						+ key;
				HttpPost post = new HttpPost(request);
				post.setEntity(mpEntity);
				HttpClient dhc = new DefaultHttpClient();

				HttpResponse response = dhc.execute(post);
				HttpEntity entity = response.getEntity();
				String out = EntityUtils.toString(entity);
				Log.d("test", out);
				Log.d("test", String.valueOf(response.getStatusLine()
						.getStatusCode()));
				JSONObject json = new JSONObject(out);
				avatarURLEditText.setText(avatarPath);
				Toast.makeText(UploadPhotoActivity.this, "上传成功",
						Toast.LENGTH_SHORT).show();
				// newestPicId = json.getString("id");
			} catch (Exception e) {
				Log.d("test", "error");
				e.printStackTrace();
			}
		}
	};
	// 分享
	private OnClickListener shareListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.fromFile(new File(avatarPath)));
			intent.putExtra(Intent.EXTRA_TEXT, "可以分享了");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent, getTitle()));
		}
	};

	/**
	 * 返回键监听
	 */
	public void onBackPressed() {
		if (System.currentTimeMillis() - mExitTime > INTERVAL) {
			Toast.makeText(this, "再按一次返回键,可直接退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}
}
