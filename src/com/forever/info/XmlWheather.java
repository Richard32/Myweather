package com.forever.info;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.forever.simplewerther.R;

public class XmlWheather {

	public static String getCityId(String cityname, Context context) {

		try {

			XmlResourceParser PullParser = context.getResources().getXml(
					R.xml.citys_weather);
			int type = PullParser.getEventType();
			String str = "";
			while (type != 1) {
				String name = PullParser.getName();
				switch (type) {
				case 2:

					if ("d".equals(name)) {

						String id = PullParser.getAttributeValue(0);
						String city = PullParser.nextText();

						if (cityname.equals(city)) {

							str = id;
							// 找到对应的数据，就无需再进行解析了。直接打断循环。
							type = 1;
						}

					}

					break;

				}
				type = PullParser.next();
				// Log.i(TAG, list.toString());
			}
			return str;

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

}
