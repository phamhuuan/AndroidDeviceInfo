package com.phamhuuan.deviceinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class DeviceInfo {
	/**
	 * Get device language
	 * @return device language
	 */
	public static String getDeviceLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * Get device time zone
	 * @return device time zone
	 */
	public static String getDeviceTimeZone() {
		return TimeZone.getDefault().getID();
	}

	/**
	 * Get device local country code
	 * @param context use context for getting resouces
	 * @return device local country code
	 */
	public static String getDeviceLocalCountryCode(Context context) {
		return context.getResources().getConfiguration().locale.getCountry();
	}

	/**
	 * Get device current year
	 * @return device current year
	 */
	public static int getDeviceCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * Get device current time in milliseconds
	 * @return device current time in milliseconds
	 */
	public static long getCurrentDateTime() {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		return calendar.getTimeInMillis();
	}

	/**
	 * Get current time in milliseconds
	 * @param timeZone time zone (example "GMT+7")
	 * @return time in milliseconds
	 */
	public static long getCurrentDateTime(String timeZone) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone), Locale.getDefault());
		return calendar.getTimeInMillis();
	}

	public static String getHardwareModel() {
		return getDeviceName();
	}

	public static int getNumberOfProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

		public static String getDeviceLocal() {
		return Locale.getDefault().getISO3Country();
	}

	public static String getDeviceIpV4() {
		return getIpAddress(true);
	}

	public static String getDeviceIpV6() {
		return getIpAddress(false);
	}

	public static String getDeviceMacAddress() {
		String mac = getMACAddress("wlan0");
		if (TextUtils.isEmpty(mac)) {
			mac = getMACAddress("eth0");
		}
		if (TextUtils.isEmpty(mac)) {
			mac = "DU:MM:YA:DD:RE:SS";
		}
		return mac;
	}

	public static long getDeviceTotalMemory(Context context) {
		if (Build.VERSION.SDK_INT >= 16) {
			return getTotalMemory(context);
		}
		return 0;
	}

	public static long getDeviceFreeMemory(Context context) {
		return getFreeMemory(context);
	}

	public static long getDeviceUsedMemory(Context context) {
		if (Build.VERSION.SDK_INT >= 16) {
			return getTotalMemory(context) - getFreeMemory(context);
		}
		return 0;
	}

	public static int getDeviceTotalCpuUsage() {
		int[] cpu = getCpuUsageStatistic();
		if (cpu != null) {
			return cpu[0] + cpu[1] + cpu[2] + cpu[3];
		}
		return 0;
	}

	public static int getDeviceTotalCpuUsageSystem() {
		int[] cpu_sys = getCpuUsageStatistic();
		if (cpu_sys != null) {
			return cpu_sys[1];
		}
		return 0;
	}

	public static int getDeviceTotalCpuUsageUser() {
		int[] cpu_usage = getCpuUsageStatistic();
		if (cpu_usage != null) {
			return cpu_usage[0];
		}
		return 0;
	}

	public static int getDeviceTotalCpuUsageIdle() {
		int[] cpu_idle = getCpuUsageStatistic();
		if (cpu_idle != null) {
			return cpu_idle[2];
		}
		return 0;
	}

	public static String getDeviceManufacture() {
		return Build.MANUFACTURER;
	}

	public static String getDeviceSystemVersion() {
		return getDeviceName();
	}

	public static int getDeviceVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static double getDeviceInInch(Context context) {
		return getDeviceInch(context);
	}

	public static String getDeviceNetworkType(Context context) {
		return getNetworkType(context);
	}

	public static String getDeviceNetwork(Context context) {
		return checkNetworkStatus(context);
	}

	public static boolean isMobile(Context context) {
		if (isTablet(context)) {
			return !getDeviceMoreThan5Inch(context);
		} else {
			return true;
		}
	}

	public static String getDeviceId(Context context) {
		@SuppressLint("HardwareIds") String deviceUuid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		return deviceUuid == null ? "00000000" : deviceUuid;
	}

	private static long getFreeMemory(Context activity) {
		try {
			ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
			long availableMegs = mi.availMem / 1048576L; // in megabyte (mb)
			ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
			activityManager.getMemoryInfo(mi);

			return availableMegs;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	private static final String IPV4_BASIC_PATTERN_STRING =
			"(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" + // initial 3 fields, 0-255 followed by .
					"([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"; // final field, 0-255
	private static final Pattern IPV4_PATTERN =
			Pattern.compile("^" + IPV4_BASIC_PATTERN_STRING + "$");

	private static boolean isIPv4Address(final String input) {
		return IPV4_PATTERN.matcher(input).matches();
	}

	private static String getIpAddress(boolean useIpV4) {
		List<NetworkInterface> interfaces = null;
		try {
			interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

			for (NetworkInterface networkInterface : interfaces) {
				List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
				for (InetAddress address : inetAddresses) {
					if (!address.isLoopbackAddress()) {
						String sAddr = address.getHostAddress().toUpperCase();
						//TODO 3.0.0
						boolean isIpV4 = isIPv4Address(sAddr);
						if (useIpV4) {
							if (isIpV4)
								return sAddr;
						} else {
							if (!isIpV4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port
								// suffix
								return delim < 0 ? sAddr : sAddr.substring(0, delim);
							}
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String getMACAddress(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName))
						continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					return "";
				StringBuilder buf = new StringBuilder();
				for (byte b : mac) buf.append(String.format("%02X:", b));
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
			return "";
		} // for now eat exceptions
		return "";
		/*
		 * try { // this is so Linux hack return
		 * loadFileAsString("/sys/class/net/" +interfaceName +
		 * "/address").toUpperCase().trim(); } catch (IOException ex) { return
		 * null; }
		 */
	}

	private static long getTotalMemory(Context context) {
		try {
			ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
			long availableMegs = mi.totalMem / 1048576L; // in megabyte (mb)
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityManager.getMemoryInfo(mi);
			return availableMegs;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static int[] getCpuUsageStatistic() {
		try {
			String tempString = executeTop();

			tempString = tempString.replaceAll(",", "");
			tempString = tempString.replaceAll("User", "");
			tempString = tempString.replaceAll("System", "");
			tempString = tempString.replaceAll("IOW", "");
			tempString = tempString.replaceAll("IRQ", "");
			tempString = tempString.replaceAll("%", "");
			for (int i = 0; i < 10; i++) {
				tempString = tempString.replaceAll(" {2}", " ");
			}
			tempString = tempString.trim();
			String[] myString = tempString.split(" ");
			int[] cpuUsageAsInt = new int[myString.length];
			for (int i = 0; i < myString.length; i++) {
				myString[i] = myString[i].trim();
				cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
			}
			return cpuUsageAsInt;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String executeTop() {
		java.lang.Process p = null;
		BufferedReader in = null;
		String returnString = null;
		try {
			p = Runtime.getRuntime().exec("top -n 1");
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while (returnString == null || returnString.contentEquals("")) {
				returnString = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				p.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}

	private static double getDeviceInch(Context context) {
		try {
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

			float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
			float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;
			return Math.sqrt(xInches * xInches + yInches * yInches);
		} catch (Exception e) {
			return -1;
		}
	}

	private static String getNetworkType(final Context activity) {
		String networkStatus = "";

		final ConnectivityManager connMgr = (ConnectivityManager)
				activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		// check for wifi
		final android.net.NetworkInfo wifi =
				connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// check for mobile data
		final android.net.NetworkInfo mobile =
				connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable()) {
			networkStatus = "Wifi";
		} else if (mobile.isAvailable()) {
			networkStatus = getDataType(activity);
		} else {
			networkStatus = "noNetwork";
		}
		return networkStatus;
	}

	private static String checkNetworkStatus(Context activity) {
		String networkStatus = "";
		try {
			// Get connect manager
			final ConnectivityManager connMgr = (ConnectivityManager)
					activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			// // check for wifi
			final android.net.NetworkInfo wifi =
					connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			// // check for mobile data
			final android.net.NetworkInfo mobile =
					connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifi.isAvailable()) {
				networkStatus = "Wifi";
			} else if (mobile.isAvailable()) {
				networkStatus = getDataType(activity);
				if (networkStatus == null) return networkStatus;
			} else {
				networkStatus = "noNetwork";
			}
		} catch (Exception e) {
			e.printStackTrace();
			networkStatus = "noNetwork";
		}
		return networkStatus;
	}

	private static String getDataType(Context context) {
		String type = "Mobile Data";
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return null;
		}
		switch (tm.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				type = "Mobile Data 3G";
				// for 3g HSDPA networktype will be return as
				// per testing(real) in device with 3g enable
				// data
				// and speed will also matters to decide 3g network type
				break;
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				type = "Mobile Data 4G";
				// No specification for the 4g but from wiki
				// i found(HSPAP used in 4g)
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				type = "Mobile Data GPRS";
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				type = "Mobile Data EDGE 2G";
				break;
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				type = "Mobile Data 1xRTT";
				break;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				type = "Mobile Data CDMA: Either IS95A or IS95B";
				break;
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				type = "Mobile Data eHRPD";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				type = "Mobile Data EVDO revision 0";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				type = "Mobile Data EVDO revision A";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				type = "Mobile Data EVDO revision B";
				break;
			case TelephonyManager.NETWORK_TYPE_GSM:
				type = "Mobile Data GSM";
				break;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				type = "Mobile Data HSPA";
				break;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				type = "Mobile Data HSUPA";
				break;
			case TelephonyManager.NETWORK_TYPE_IDEN:
				type = "Mobile Data iDen";
				break;
			case TelephonyManager.NETWORK_TYPE_IWLAN:
				type = "Mobile Data IWLAN";
				break;
			case TelephonyManager.NETWORK_TYPE_LTE:
				type = "Mobile Data LTE";
				break;
			case TelephonyManager.NETWORK_TYPE_NR:
				type = "Mobile Data NR(New Radio) 5G";
				break;
			case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
				type = "Mobile Data TD_SCDMA";
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				type = "Mobile Data UMTS";
				break;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				type = "Mobile Data unknown";
				break;
		}
		return type;
	}

	private static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	private static boolean getDeviceMoreThan5Inch(Context activity) {
		try {
			DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
			// int width = displayMetrics.widthPixels;
			// int height = displayMetrics.heightPixels;

			float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
			float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;
			double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
			// 5inch device or bigger
			// smaller device
			return diagonalInches >= 7;
		} catch (Exception e) {
			return false;
		}
	}
}
