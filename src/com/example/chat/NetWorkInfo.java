package com.example.chat;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @ClassName: NetWorkInfo.java
 * @Description: 通用网络信息处理类
 * @author Melvin
 * @version V1.0
 * @Date 2012-12-4 下午2:33:06
 */
public class NetWorkInfo {
	public NetWorkInfo() {

	}

	/**
	 * TODO 取得域名对应IP地址
	 * 
	 * @param url
	 *            域名
	 * @author Melvin
	 * @date 2013-4-25
	 * @return String
	 */
	public static String getIpByDomain(String url) {
		java.net.InetAddress x;
		try {
			URL url2 = new URL(url);
			x = java.net.InetAddress.getByName(url2.getHost());
			String ip = x.getHostAddress();
			return ip;
		} catch (UnknownHostException e) {
		} catch (MalformedURLException e) {
		}
		return null;
	}

	/**
	 * TODO 是否WIFI网络
	 * 
	 * @param context
	 *            {@link Context}
	 * @author Melvin
	 * @date 2013-4-25
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * TODO 是否3G网络
	 * 
	 * @param context
	 *            {@link Context}
	 * @author Melvin
	 * @date 2013-4-25
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			int subType = telephonyManager.getNetworkType();
			return isConnectionFast(subType);
		}
		return false;
	}
	
	/**
	 * TODO 获取网络类型
	 * 
	 * @param context
	 *            {@link Context}
	 * @author Melvin
	 * @date 2013-4-24
	 * @return int
	 */
	public static int getNetworkType(Context context) {
		if (context == null) {
			return 0;
		}
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getNetworkType();
	}

	/**
	 * TODO 获取网络类型名称，方法过期，请看getNetworkTypeName2 {@link getNetworkTypeName2}
	 * 
	 * @param context
	 *            {@link Context}
	 * @author Melvin
	 * @date 2013-4-24
	 * @return String
	 */
	@Deprecated
	public static String getNetworkTypeName(Context context) {
		if (context == null) {
			return null;
		}
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
		if (networkinfo == null)
			return null;
		int type = networkinfo.getType();
		if (type == ConnectivityManager.TYPE_WIFI) {
			return "WIFI";
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			String networkName = networkinfo.getExtraInfo();
			if (networkName != null && networkName.length() > 20) {
				networkName = networkName.substring(0, 20);
			}
			return networkName;
		} else {
			return null;
		}
	}

	/**
	 * TODO 根据速率返回网络类型
	 * 
	 * @param context
	 *            {@link Context}
	 * @author Melvin
	 * @date 2013-4-24
	 * @return String 联通3g为UMTS或者HSDPA,电信的3g为EVDO,移动和联通的2g为GPRS或者EGDE,电信的2G为CMDA
	 */
	public static String getNetworkTypeNameByPhone(Context context) {
		if (context == null) {
			return null;
		}
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
		if (networkinfo == null)
			return null;
		int type = networkinfo.getType();
		if (type == ConnectivityManager.TYPE_WIFI) {
			return "WIFI";
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			TelephonyManager teleMan = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			int networkType = teleMan.getNetworkType();
			switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "NETWORK_TYPE_1xRTT"; // 2G ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return "NETWORK_TYPE_CDMA"; // 2G ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "NETWORK_TYPE_EDGE"; // 2G ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "NETWORK_TYPE_GPRS"; // 2G ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return "NETWORK_TYPE_EVDO_0"; // 3G ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return "NETWORK_TYPE_EVDO_A"; // 3G ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return "NETWORK_TYPE_EVDO_B"; // 3G ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return "NETWORK_TYPE_HSDPA"; // 3G ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return "NETWORK_TYPE_HSPA"; // 3G ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return "NETWORK_TYPE_HSUPA"; // 3G ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "NETWORK_TYPE_UMTS"; // 4G ~ 50 -100 mbps
			case TelephonyManager.NETWORK_TYPE_LTE:
				return "NETWORK_TYPE_LTE";
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return "NETWORK_TYPE_UNKNOWN";
			default:
				return "UNKNOW";
			}
		}
		return "UNKNOW";
	}

	/**
	 * 取得网络类型，根据网络制式
	 * 
	 * @param context
	 * @return 返回分别是WIFI,2G,3G,4G,UNKNOW
	 */
	public static String getNetworkTypeName2(Context context) {

		if (context == null) {
			return null;
		}
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
		if (networkinfo == null)
			return null;
		int type = networkinfo.getType();
		if (type == ConnectivityManager.TYPE_WIFI) {
			return "WIFI";
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			TelephonyManager teleMan = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			int networkType = teleMan.getNetworkType();
			switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "2G";
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_IDEN:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return "3G";
			case TelephonyManager.NETWORK_TYPE_LTE:
				return "4G";

			}
			return "UNKNOW";
		}
		return "";
	}

	private static boolean isConnectionFast(int subType) {

		switch (subType) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // 3G ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // 3G ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // 3G ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // 3G ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
			// NOT AVAILABLE YET IN API LEVEL 7
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 峰值速率：下行100Mbps，上行50Mbps kbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}

	/**
	 * TODO 判断网络是否连接
	 * 
	 * @param context
	 *            {@link Context}
	 * @author Melvin
	 * @date 2013-4-25
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null)
			return networkInfo.isConnectedOrConnecting();
		return false;
	}

	/**
	 * TODO 打开无线设置界面
	 * 
	 * @param context
	 *            {@link Context}
	 * @author Melvin
	 * @date 2013-4-25
	 * @return void
	 */
	public static void startWirelessSetting(Context context) {
		if (context == null) {
			return;
		}
		Intent mIntent = null;
		if (Build.VERSION.SDK_INT >= 15) {
			mIntent = new Intent(Settings.ACTION_SETTINGS);
		} else {
			mIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		}
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mIntent);
	}

}
