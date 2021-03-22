package com.phamhuuan.androiddeviceinfo;

import java.util.Locale;
import java.util.TimeZone;

public class DeviceInfo {
	public static String getDeviceLanguage() {
		return Locale.getDefault().getLanguage();
	}

	public static String getDeviceTimeZone() {
		return TimeZone.getDefault().getID();
	}
}
