package com.forever.info;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenHttp {

	public static byte[] getByte(String httpurl) {

		ByteArrayOutputStream baos = null;
		BufferedInputStream bis = null;

		try {
			URL url = new URL(httpurl);

			HttpURLConnection httpconn = (HttpURLConnection) url
					.openConnection();
			httpconn.setDoInput(true);

			httpconn.connect();

			if (httpconn.getResponseCode() == 200) {

				bis = new BufferedInputStream(httpconn.getInputStream());
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024 * 8];
				int len = 0;
				while (-1 != (len = bis.read(buffer))) {

					baos.write(buffer, 0, len);
					baos.flush();

				}
				return baos.toByteArray();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 必须判断是否为null,不然在无网络的时候，没有进行实例化相应的流对象，APP会挂掉。
				if (baos != null && bis != null) {
					baos.close();
					bis.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;

	}

}
