package com.example.nyanmua;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.listsms.DetailAdapter;
import com.example.listsms.DetailEntity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// 短信发送
	SmsManager sms = SmsManager.getDefault();
	// 音乐播放器
	MediaPlayer mp;

	private ViewPager viewPager;// 页卡内容
	private List<View> views;// Tab页面列表
	private View viewRemind, viewMain, modSms;// 各个页卡

	// 短信内容页面
	private ListView talkView;
	private List<DetailEntity> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// init pages
		InitViewPager();

		// 初始化各个Button
		initBtns(viewMain);
		initSMS(viewRemind);

		// 音乐播放器
		mp = MediaPlayer.create(this, R.raw.nyan);
		mp.start();
		// 音乐按钮
		ImageButton btnmusic = (ImageButton) viewMain
				.findViewById(R.id.btnMusic);
		btnmusic.setOnClickListener(new OnClickListener() {
			MediaPlayer nyanmp = mp;

			@Override
			public void onClick(View arg0) {
				if (nyanmp.isPlaying()) {
					nyanmp.pause();
				} else
					nyanmp.start();
			}
		});
	}

	/**
	 * 初始化页面卡
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.pager);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		viewRemind = inflater.inflate(R.layout.remind, null);

		listInit(viewRemind);

		viewMain = inflater.inflate(R.layout.activity_main, null);
		modSms = inflater.inflate(R.layout.modify_sms, null);
		views.add(viewRemind);
		views.add(viewMain);
		views.add(modSms);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(1);
	}

	/**
	 * 初始化短信编辑页面
	 * 
	 * @param modSms
	 */
	public void initSMS(final View modSms) {
		// 短信编辑页面
		ImageButton smstextsubmit = (ImageButton) modSms
				.findViewById(R.id.smstextsubmit);
		final EditText smstextedit = (EditText) modSms
				.findViewById(R.id.smstextedit);

		smstextsubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String textSms = "" + smstextedit.getText();
				if (textSms != null) {
					String tel = getSharedPreferences("SETTING", 0).getString(
							"tel", "");
					textSms += "\n 来自nyan客户端";
					try {
						smsSend(getApplicationContext(), textSms,
								R.string.smssend);
						writeToDB(tel, textSms);
						listInit(modSms);
					} catch (Exception e) {
						return;
					}
					smstextedit.setText("");
				}
			}
		});
	}

	/**
	 * 初始化按钮页面
	 * 
	 * @param viewMain
	 */
	public void initBtns(View viewMain) {
		// 主要按钮页面
		ImageButton[] btns = {
				(ImageButton) viewMain.findViewById(R.id.btnPia),
				(ImageButton) viewMain.findViewById(R.id.btnCeng),
				(ImageButton) viewMain.findViewById(R.id.btnMaimeng),
				(ImageButton) viewMain.findViewById(R.id.btnQin),
				(ImageButton) viewMain.findViewById(R.id.btnBao),
				(ImageButton) viewMain.findViewById(R.id.btnChi),
				(ImageButton) viewMain.findViewById(R.id.btnZixi),
				(ImageButton) viewMain.findViewById(R.id.btnJiayou),
				(ImageButton) viewMain.findViewById(R.id.btnAnquan),
				(ImageButton) viewMain.findViewById(R.id.btnShuijiao),
				(ImageButton) viewMain.findViewById(R.id.btnBaozou) };

		final String[] smss = { getString(R.string.smsPia),
				getString(R.string.smsCeng), getString(R.string.smsMaimeng),
				getString(R.string.smsQin), getString(R.string.smsBao),
				getString(R.string.smsChi), getString(R.string.smsZixi),
				getString(R.string.smsJiayou), getString(R.string.smsAnquan),
				getString(R.string.smsShuijiao), getString(R.string.smsBaozou) };

		final String[] nsmss = { getString(R.string.nsmsPia),
				getString(R.string.nsmsCeng), getString(R.string.nsmsMaimeng),
				getString(R.string.nsmsQin), getString(R.string.nsmsBao),
				getString(R.string.nsmsChi), getString(R.string.nsmsZixi),
				getString(R.string.nsmsJiayou), getString(R.string.nsmsAnquan),
				getString(R.string.nsmsShuijiao),
				getString(R.string.nsmsBaozou) };

		final int[] toasts = { R.string.toastPia, R.string.toastCeng,
				R.string.toastMaimeng, R.string.toastQin, R.string.toastBao,
				R.string.toastChi, R.string.toastZixi, R.string.toastJiayou,
				R.string.toastAnquan, R.string.toastShuijiao,
				R.string.toastBaozou };
		final int[] shorttoasts = { R.string.shorttoastPia,
				R.string.shorttoastCeng, R.string.shorttoastMaimeng,
				R.string.shorttoastQin, R.string.shorttoastBao,
				R.string.shorttoastChi, R.string.shorttoastZixi,
				R.string.shorttoastJiayou, R.string.shorttoastAnquan,
				R.string.shorttoastShuijiao, R.string.shorttoastBaozou };
		for (int i = 0; i < btns.length; i++) {
			final int shorttoast = shorttoasts[i];
			btns[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), shorttoast,
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		final String role = getSharedPreferences("SETTING", 0).getString(
				"role", "");
		final String tel = getSharedPreferences("SETTING", 0).getString("tel",
				"");
		if (role.equals("zhuren")) {
			for (int i = 0; i < btns.length; i++) {
				final int toast = toasts[i];
				final String finalsms = smss[i];
				btns[i].setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						String sms = smsDeal(finalsms);
						smsSend(getApplicationContext(), sms, toast);
						writeToDB(tel, sms);
						return true;
					}
				});
			}
		} else {
			for (int i = 0; i < btns.length; i++) {
				final int toast = toasts[i];
				final String finalsms = nsmss[i];
				btns[i].setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						String sms = smsDeal(finalsms);
						smsSend(getApplicationContext(), sms, toast);
						writeToDB(tel, sms);
						return true;
					}
				});
			}
		}
	}

	public void listInit(View view) {
		talkView = (ListView) view.findViewById(R.id.list);
		list = new ArrayList<DetailEntity>();
		DetailEntity dtail;
		ContentResolver resolver = this.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor;
		String tel = getSharedPreferences("SETTING", 0).getString("tel", "");
		tel = "%" + tel + "%";
		cursor = resolver.query(uri, new String[] { "_id", "address", "date",
				"type", "body" }, "address like ? and read=?", new String[] {
				tel, "1" }, " date desc limit 20");

		if (cursor.moveToLast()) {
			while (cursor.moveToPrevious()) {
				int type = cursor.getInt(3);
				String body = cursor.getString(4);
				if (type == 1)
					dtail = new DetailEntity(body, R.layout.list_say_me_item);
				else
					dtail = new DetailEntity(body, R.layout.list_say_he_item);
				list.add(dtail);
			}

			cursor.close();

			talkView.setAdapter(new DetailAdapter(getApplicationContext(), list));
			// 去掉分割线
			talkView.setDivider(null);
		}
	}

	/**
	 * 发送短信同时给出Toast
	 * 
	 * @param context
	 * @param msg
	 * @param toast
	 */
	public void smsSend(Context context, String msg, int toast) {

		sms.sendTextMessage(
				getSharedPreferences("SETTING", 0).getString("tel", ""), null,
				msg, null, null);
		Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();

	}

	/**
	 * 写入短信数据库
	 * 
	 * @param tel
	 * @param msg
	 */
	public void writeToDB(String tel, String msg) {
		ContentValues values = new ContentValues();
		values.put("address", tel);
		values.put("date", System.currentTimeMillis());
		values.put("body", msg);
		values.put("type", "2");
		values.put("read", "1");
		getContentResolver().insert(Uri.parse("content://sms"), values);
	}

	/**
	 * 生成随机短信
	 * 
	 * @param sms
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String smsDeal(String sms) {
		String[] result = sms.split("\\|\\|");
		int count = result.length;
		Date t = new Date();
		return result[t.getSeconds() % count];
	}

	/**
	 * 返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			if (mp.isPlaying())
				mp.stop();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			final String role = getSharedPreferences("SETTING", 0).getString(
					"role", "");
			if (role.equals("zhuren"))
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			else
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
		}
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Toast.makeText(this, "这里用来修改设置的喵！", Toast.LENGTH_LONG).show();
		// 如果返回false，此方法就把用户点击menu的动作给消费了，onCreateOptionsMenu方法将不会被调用
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "修改").setIcon(
				android.R.drawable.ic_menu_edit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:

			this.finish();
			if (mp.isPlaying())
				mp.stop();
			Intent intent = new Intent(this, ModifyTargetTel.class);
			startActivity(intent);
			break;
		}
		return false;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
	}

	/**
	 * 为ViewPager提供Adapter
	 * 
	 */
	public class MyViewPagerAdapter extends PagerAdapter {

		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}
