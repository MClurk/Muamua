package com.example.nyanmua;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyTargetTel extends Activity {
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_target_tel);

		final TextView tv = (TextView) findViewById(R.id.origintarget);
		tv.setText(getSharedPreferences("SETTING", 0).getString("tel", ""));

		final EditText smsedit = (EditText) findViewById(R.id.targettel);
		ImageButton modifyTargetTel = (ImageButton) findViewById(R.id.btnTargetTel);

		modifyTargetTel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String tel = smsedit.getText().toString();
				if (checkTel(tel)) {
					editor = getSharedPreferences("SETTING", 0).edit();
					editor.remove("tel");
					editor.putString("tel", tel);
					editor.commit();
					tv.setText(tel);
					Toast.makeText(getApplicationContext(), "修改为:" + tel,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(getApplicationContext(), "电话不对了啦！！！",
							Toast.LENGTH_LONG).show();
			}

		});

	}

	public Boolean checkTel(String tel) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(tel);
		return m.matches();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		return false;
	}
}
