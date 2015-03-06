

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text_cityname = (TextView) findViewById(R.id.text_cityname);

		text_temperature = (TextView) findViewById(R.id.text_temperature);

		text_humidity = (TextView) findViewById(R.id.text_humidity);

		text_weather = (TextView) findViewById(R.id.text_weather);

		text_tomo = (TextView) findViewById(R.id.text_tomo);

		text_ceshi = (TextView) findViewById(R.id.text_ceshi);

		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new Date());
		// 显示当前日期。
		text_ceshi.setText(date);

		edit_in = (EditText) findViewById(R.id.edit_in);
		// 从文件中读取城市ID。
		cid = InfoText.getCityInfo(this);
		// 如果是刚刚安装或者是没有读取到城市ID
		if (cid == null) {
			// 直接访问当前网络供应商所确定的地理位置，可能会出错。但是可以设置。

			new MyTask(MainActivity.this)
					.execute("http://weather.xcyh.org/json");

		}
		// 读取到文件中的城市对于ID。
		else {

			new MyTask(MainActivity.this).execute("http://weather.xcyh.org/"
					+ cid.trim() + "/json");
		}
	}

	public void clickBu(View view) {

		switch (view.getId()) {
		case R.id.button_ti:

			// 获得文本框中的城市名称。
			String city = edit_in.getText().toString().trim();

			// 如果，为空。就相当于刷新。
			if (city.equals("")) {

				// 直接获取文件中的ID 来刷新。
				cid = InfoText.getCityInfo(this);

				// 文件中没有数据。
				if (cid == null) {

					// 直接访问当前网络位置。
					new MyTask(MainActivity.this)
							.execute("http://weather.xcyh.org/json");
				}
				// 访问数据中提供的城市。
				else {

					new MyTask(MainActivity.this)
							.execute("http://weather.xcyh.org/" + cid.trim()
									+ "/json");
				}
			}
			// 城市名称不为空。
			else {

				// 通过城市名称，在数据库中找对于的城市ID

				cid = XmlWheather.getCityId(city, this).trim();

				// 城市名称不存在，返回的城市ID就是空，
				if (cid.equals("")) {

					// 提醒，城市不在数据库中。
					Toast toast = Toast.makeText(MainActivity.this,
							"抱歉，未收录此城市", Toast.LENGTH_LONG);
					// 三个参数分别表示(起点位置,水平向右位移,垂直向下位移)
					toast.setGravity(Gravity.TOP, 0, 400);
					toast.show();

				}
				// 城市名称合格。就把取到的城市ID存入文件中。并把对应的天气呈现。
				else {
					InfoText.setCityInfo(this, cid);
					new MyTask(MainActivity.this)
							.execute("http://weather.xcyh.org/" + cid.trim()
									+ "/json");
				}

			}
			// 清空文本框。
			edit_in.setText("");

			break;

		default:
			break;
		}

	}

	class MyTask extends AsyncTask<String, Void, byte[]> {
		private Context context;

		private ProgressDialog pDialog;

		public MyTask(Context context) {
			this.context = context;
			pDialog = new ProgressDialog(context);
			pDialog.setTitle("提示：)");
			pDialog.setMessage("Loading.......");
			pDialog.setIcon(R.drawable.ic_launcher);

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.show();

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected byte[] doInBackground(String... params) {

			return OpenHttp.getByte(params[0]);
		}

		@Override
		protected void onPostExecute(byte[] result) {
			super.onPostExecute(result);
			if (result != null) {

				try {

					String json = new String(result, "utf-8");
					// 调用JSon解析方法。并返回所需要的数据。
					Map<String, String> map = getJsonMap(json);
					// 将得到的数据显示在APP的页面上。
					text_cityname.setText(map.get("cityname"));
					text_temperature.setText(map.get("temperature"));
					text_humidity.setText("湿度：" + map.get("humidity"));
					text_weather.setText(map.get("weather"));

					text_tomo.setText(map.get("weather2") + "      "
							+ map.get("temperature2"));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				Toast.makeText(context, "抱歉，数据加载失败！", Toast.LENGTH_LONG).show();
			}
			pDialog.dismiss();
		}
	}

	private Map<String, String> getJsonMap(String json) {

		map = new HashMap<String, String>();
		try {
			JSONObject jsonObj = new JSONObject(json);

			map.put("cityname", jsonObj.getString("cityname"));

			JSONArray jsonArray = jsonObj.getJSONArray("data");

			JSONObject jsonObject2 = jsonArray.getJSONObject(0);

			JSONObject jsonObject3 = jsonArray.getJSONObject(1);

			map.put("weather2", jsonObject3.getString("weather"));

			map.put("temperature2", jsonObject3.getString("temperature"));

			map.put("weather", jsonObject2.getString("weather"));

			map.put("temperature", jsonObject2.getString("temperature"));

			JSONObject jsonObj2 = jsonObj.getJSONObject("live");

			map.put("humidity", jsonObj2.getString("humidity"));

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
