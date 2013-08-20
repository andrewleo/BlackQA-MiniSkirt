package com.netease.miniskirt.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.miniskirt.R;
import com.netease.miniskirt.utils.HttpUtil;

public class RegisterActivity extends Activity {
	/**
	 * 注册按钮
	 */
	private Button mRegister;
	private EditText mRegisterName;
	private EditText mRegisterPassword;
	private EditText mRegisterConfPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		findViewById();
		setListener();
	}

	private void findViewById() {
		mRegister = (Button) findViewById(R.id.register_activity_register);
		mRegisterName = (EditText) findViewById(R.id.register_activity_name);
		mRegisterPassword = (EditText) findViewById(R.id.register_activity_password);
		mRegisterConfPassword = (EditText) findViewById(R.id.register_activity_conf_password);
	}

	private void setListener() {
		// 注册按钮监听
		mRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断当前用户是否可以完成注册，成功就跳转到登陆界面
				String registerName = mRegisterName.getText().toString();
				String registerPassword = mRegisterPassword.getText()
						.toString();
				String registerConfPassword = mRegisterConfPassword.getText()
						.toString();
				if ("".equals(registerName)) {
					// 用户输入用户名为空
					Toast.makeText(RegisterActivity.this, "注册名不能为空!",
							Toast.LENGTH_SHORT).show();
				} else if ("".equals(registerPassword)) {
					// 密码不能为空
					Toast.makeText(RegisterActivity.this, "密码不能为空!",
							Toast.LENGTH_SHORT).show();
				} else if (!registerConfPassword.equals(registerPassword)) {
					Toast.makeText(RegisterActivity.this, "两次输入密码不一致!",
							Toast.LENGTH_SHORT).show();
				} else {
					//每次需换个用户名注册，或者清空数据库
					//需要开通网络测试
					String url = "http://www.qiu-chu.com/peach/users/signup";
//					String email = "bingo727-1@163.com";
//					String password = "123456";
//					String password_c = "123456";
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("email", registerName));
					params.add(new BasicNameValuePair("password", registerPassword));
					params.add(new BasicNameValuePair("password_c", registerConfPassword));
					String result = HttpUtil.httpPost(url, params, 201);
					
					Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG).show();
					
					// startActivity(new
					// Intent(RegisterActivity.this,LoginActivity.class));
					if(!result.contains("Error Response")) {
						Toast.makeText(RegisterActivity.this, "注册成功，跳转到登陆界面", Toast.LENGTH_LONG).show();
						startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
						finish();
					} else {
						Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_LONG).show();
					}
//					startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//					finish();
				}
				
				
			}
		});
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
		finish();
	}

}
