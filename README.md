# Android device info

## 1. Installation

In file build.gradle (project level) add this line to allprojects.repositories
```groovy
allprojects {
    repositories {
        ...
        maven {url 'https://jitpack.io'}
    }
}
```

In file build.gradle (app level) add this line to dependencies

**Note:** replace TAG with version tag you want to install (for example 0.0.1)

```groovy
dependencies {
    ...
    implement 'com.github.phamhuuan:AndroidDeviceInfo:TAG'
}
```

In AndroidManifest.xml do not forget to add permission

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## 2. Usage
| Function | Return type |
| -------- | ----------- |
| [getDeviceLanguage()](#getdevicelanguage) | String |
| [getDeviceTimeZone()](#getdevicetimezone) | String |
| [getDeviceLocalCountryCode(Context context)](#getdevicelocalcountrycode) | String |
| [getDeviceCurrentYear()](#getdevicecurrentyear) | int |
| [getCurrentDateTime()](#getcurrentdatetime) | long |
| [getCurrentDateTime(String timeZone)](#getcurrentdatetime) | long |
| [getHardwareModel()](#gethardwaremodel) | String |
| [getNumberOfProcessors()](#getnumberofprocessors) | int |
| [getDeviceLocal()](#getdevicelocal) | String |
| [getDeviceIpV4()](#getdeviceipv4) | String |
| [getDeviceIpV6()](#getdeviceipv6) | String |
| [getDeviceMacAddress()](#getdevicemacaddress) | String |
| [getDeviceTotalMemory(Context context)](#getdevicetotalmemory) | long |
| [getDeviceFreeMemory(Context context)](#getdevicefreememory) | long |
| [getDeviceUsedMemory(Context context)](#getdeviceusedmemory) | long |
| [getDeviceTotalCpuUsage()](#getdevicetotalcpuusage) | int |
| [getDeviceTotalCpuUsageSystem()](#getdevicetotalcpuusagesystem) | int |
| [getDeviceTotalCpuUsageUser()](#getdevicetotalcpuusageuser) | int |
| [getDeviceTotalCpuUsageIdle()](#getdevicetotalcpuusageidle) | int |
| [getDeviceManufacture()](#getdevicemanufacture) | String |
| [getDeviceSystemVersion()](#getdevicesystemversion) | String |
| [getDeviceVersion()](#getdeviceversion) | int |
| [getDeviceInInch(Context context)](#getdeviceininch) | double |
| [getDeviceNetworkType(Context context)](#getdevicenetworktype) | String |
| [getDeviceNetwork(Context context)](#getdevicenetwork) | String |
| [isMobile(Context context)](#isMobile) | boolean |