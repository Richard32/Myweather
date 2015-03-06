package com.forever.info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

public class InfoText {

	public static boolean setCityInfo(Context context, String cityid) {

		try {

			File file = new File(context.getFilesDir(), "info.txt");

			FileOutputStream fos = new FileOutputStream(file);

			fos.write(cityid.getBytes());

			fos.close();
			return true;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static String getCityInfo(Context context) {

		try {
			File file = new File(context.getFilesDir(), "info.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));

			String city = br.readLine();
			br.close();
			return city;

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
