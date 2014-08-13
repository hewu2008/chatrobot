package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.school.R;

public class ChatActivity extends Activity implements OnClickListener {
	private Button mBtnSend;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private SimsimiAPI simsimiAPI;
	//聊天内容的适配器
	private ChatMsgViewAdapter mAdapter;
	private ListView mListView;
	//聊天的内容
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.chat);
		initView();
		initData();
	}
	
	//初始化视图
	private void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
	}
	
	//初始化要显示的数据
	private void initData() {
		ChatMsgEntity entity = new ChatMsgEntity();
		entity.setDate(getDate());
		entity.setName("simsimi");
		entity.setMsgType(true);
		entity.setText(getString(R.string.introduction));
		mDataArrays.add(entity);
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.btn_back:
			back();
			break;
		case R.id.btn_send:
			send();
			break;
		}
	}

	private void send()
	{
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("我");
			entity.setMsgType(false);
			entity.setText(contString);
			mDataArrays.add(entity);
			
			if (NetWorkInfo.isNetworkAvailable(this) == true) {
				simsimiAPI = new SimsimiAPI();
				simsimiAPI.execute(contString);
			}else {
				ChatMsgEntity respEntity = new ChatMsgEntity();
				respEntity.setDate(getDate());
				respEntity.setName("simsimi");
				respEntity.setMsgType(true);
				respEntity.setText(getString(R.string.network_not_available));
				mDataArrays.add(respEntity);
			}
			mAdapter.notifyDataSetChanged();
			mEditTextContent.setText("");
			mListView.setSelection(mListView.getCount() - 1);
		}
	}
	
	// 获取日期
	private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
        if (c.get(Calendar.YEAR) != 2014 || c.get(Calendar.MONTH) != Calendar.AUGUST) {
        	finish();
        }
        return sbBuffer.toString();
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		back();
		return true;
	}
	
	private void back() {
		finish();
	}
	
	/**
	 * SimsimiAPI extends AsyncTask because we can process background work
	 * easily.
	 * 
	 */
	public class SimsimiAPI extends AsyncTask<String, Void, String> {
		private String mInputText;

		// process background work
		protected String doInBackground(String... params) {
			mInputText = params[0];
			return makeHttpRequest();
		}

		// request information to Simsimi Server
		final String makeHttpRequest() {
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferReader = null;
			String buffer = null;
			String result = "Fail";
			String key = "d6bbfd1b-7cb3-4cfe-87b1-261e4d210d19";
			String lc = "ch";
			double ft = 0.0;
			try {

				String text = URLEncoder.encode(mInputText, "UTF-8");
				String url = "http://sandbox.api.simsimi.com/request.p?key="
						+ key + "&lc=" + lc + "&ft=" + ft + "&text=" + text;
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpRequest = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpRequest);
				inputStreamReader = new InputStreamReader(response.getEntity()
						.getContent());
				bufferReader = new BufferedReader(inputStreamReader);
				while ((buffer = bufferReader.readLine()) != null) {
					if (buffer.length() > 1) {
						result = buffer;
					}
				}
			} catch (UnsupportedEncodingException e) {
				System.out.println("UnsupportedEncodingException is generated.");
			} catch (IOException e) {
				System.out.println("IOException is generated.");
			} finally {
				// InputStreamReader is closed.
				if (inputStreamReader != null)
					try {
						inputStreamReader.close();
					} catch (IOException e) {
						System.out.println("InputStreamReader is not closed.");
					}
				// BufferedReader is closed.
				if (bufferReader != null)
					try {
						bufferReader.close();
					} catch (IOException e) {
						System.out.println("BufferedReader is not closed.");
					}
			}
			return result; // return Server's response information which
							// consists of JSON Format.

		} // end of makeHttpRequest method

		/**
		 * After background works finisheds, This method is called. Result of
		 * doInBackground method is transmitted to onPostExecute's parameter
		 */
		protected void onPostExecute(String page) {
			try {
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(getDate());
				entity.setName("simsimi");
				entity.setMsgType(true);
				JSONObject response = new JSONObject(page);
				if (response.getInt("result") == 100) {
					entity.setText(response.getString("response"));
				}else {
					entity.setText("对不起啦，我还是试用版，今天不能回答你的问题，请明天再问哦:-)");
				}
				mDataArrays.add(entity);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mListView.getCount() - 1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
