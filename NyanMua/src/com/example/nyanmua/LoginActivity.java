package com.example.nyanmua;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	public static long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		ImageButton zhuren = (ImageButton) findViewById(R.id.imageButtonl);
		ImageButton nyan = (ImageButton) findViewById(R.id.imageButtonr);
		ImageButton modifytel = (ImageButton) findViewById(R.id.modifytel);

		// 根据选择图片选择登陆身份
		editor = getSharedPreferences("SETTING", 0).edit();
		final SharedPreferences reader = getSharedPreferences("SETTING", 0);
		zhuren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editor.putString("role", "zhuren");
				if ((reader.getString("tel", "") != null)
						&& !(reader.getString("tel", "").equals("")))
					;
				else
					editor.putString("tel", "13813777912");
				editor.commit();

				mainActivity();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
			}
		});
		nyan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editor.putString("role", "nyan");
				if ((reader.getString("tel", "") != null)
						&& !(reader.getString("tel", "").equals("")))
					;
				else
					editor.putString("tel", "13756073240");
				editor.commit();
				mainActivity();
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
		});

		modifytel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				midofyActivity();
			}

		});

	}

	public void midofyActivity() {
		Intent intent = new Intent(this, ModifyTargetTel.class);
		startActivity(intent);
		this.finish();
	}

	public void mainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * 返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出",
						Toast.LENGTH_LONG).show();
				exitTime = System.currentTimeMillis();
			} else {
				this.finish();
			}
			return true;
		} else
			return false;
	}
}
