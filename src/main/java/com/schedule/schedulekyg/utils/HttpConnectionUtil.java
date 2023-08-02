package com.schedule.schedulekyg.utils;

import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpConnectionUtil {

	public static String connectToServer(String data, String reqUrl) {
		return connectToServer(data, reqUrl, null);
	}

	public static String connectToServer(String data, String reqUrl, Map<String, String> header) {
		HttpURLConnection conn = null;
		BufferedReader resultReader = null;
		PrintWriter pw = null;
		URL url = null;
		int statusCode = 0;
		StringBuffer recvBuffer = new StringBuffer();
		try {
			url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(25000);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", String.valueOf(MediaType.APPLICATION_JSON));
			if (header != null) {
				for (String key : header.keySet()) {
					conn.setRequestProperty(key, header.get(key));
				}
			}

			pw = new PrintWriter(conn.getOutputStream());
			pw.write(data);
			pw.flush();

			statusCode = conn.getResponseCode();
			resultReader = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			for (String temp; (temp = resultReader.readLine()) != null;) {
				recvBuffer.append(temp).append("\n");
			}

			if (!(statusCode == HttpURLConnection.HTTP_OK)) {
				throw new Exception("ERROR");
			}

			return recvBuffer.toString().trim();
		} catch (Exception e) {
			return "ERROR";
		} finally {
			recvBuffer.setLength(0);

			try {
				if (resultReader != null) {
					resultReader.close();
				}
			} catch (Exception ex) {
				resultReader = null;
			}

			try {
				if (pw != null) {
					pw.close();
				}
			} catch (Exception ex) {
				pw = null;
			}

			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception ex) {
				conn = null;
			}
		}
	}
}
