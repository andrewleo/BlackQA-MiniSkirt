package com.netease.miniskirt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.miniskirt.R;
import com.netease.miniskirt.utils.HttpUtil;

/**
 * 登录界面
 * 
 * @author Easer
 * 
 */
public class LoginActivity extends Activity {
	/**
	 * 登录按钮
	 */
	private Button mLogin;
	/**
	 * 微博登陆按钮
	 */
	private TextView mWeiboLogin;

	/**
	 * 注册按钮
	 */
	private TextView mRegister;

	/**
	 * 用户名输入框
	 */
	private EditText mLoginname;

	/**
	 * 密码输入框
	 */
	private EditText mPassword;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		findViewById();
		setListener();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mLogin = (Button) findViewById(R.id.login_activity_login);
		mWeiboLogin = (TextView) findViewById(R.id.login_activity_weiboLogin);
		mRegister = (TextView) findViewById(R.id.login_activity_register);
		mLoginname = (EditText) findViewById(R.id.login_loginname);
		mPassword = (EditText) findViewById(R.id.login_password);
	}

	/**
	 * UI事件监听
	 */
	private void setListener() {
		// 登录按钮监听
		mLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = mLoginname.getText().toString(); // "bingo727@163.com";
				String password = mPassword.getText().toString(); // 123456
				// 这里可以再封装一下
				// 登录成功，再跳转到拍照页面
				if ("".equals(username)) {
					// 用户输入用户名为空
					Toast.makeText(LoginActivity.this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
				} else if ("".equals(password)) {
					// 密码不能为空
					Toast.makeText(LoginActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
				} else {
					// 需要开通网络测试
					String url = "http://www.qiu-chu.com/peach/users/login";
					// String username = "bingo727@163.com";
					// String password = "123456";
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("email", username));
					params.add(new BasicNameValuePair("password", password));
					String result = HttpUtil.httpPost(url, params, 202);
					Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(LoginActivity.this, UploadPhotoActivity.class);
					try {
						JSONObject object = new JSONObject(result);
						intent.putExtra("user_id", object.getString("id"));
						intent.putExtra("email", object.getString("email"));
						intent.putExtra("key", object.getString("token"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Log.d("miniskirt", result);
					if(!result.contains("Error Response")) {
						startActivity(intent);
						finish();
					}
					
//					startActivity(new Intent(LoginActivity.this, UploadPhotoActivity.class));
//					finish();
				}
			}
		});
		

		mWeiboLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				finish();
			}
		});
	}
	

	public void onBackPressed() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
