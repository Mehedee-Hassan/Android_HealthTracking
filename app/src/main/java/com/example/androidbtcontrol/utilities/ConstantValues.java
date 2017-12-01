package com.example.androidbtcontrol.utilities;

/**
 * Created by REFACTOR on 10/26/2016.
 */

public class ConstantValues {
    public static boolean CONNECTED_TO_DEVICE = false;
    public static String BASE_API_URL = "https://ehealthju.com/";
//    public static String BASE_API_URL = "http://androidtime.net/health_tracking/";
    //public static String BASE_API_URL = "http://localhost:8080/HealthTracking/";
    public static boolean PRODUCTION_READY = true;

    public static String SENSOR_BLOOD_PRESSURE = "1";
    public static String SENSOR_ECG = "2";
    public static String SENSOR_SPO = "3";
    public static String SENSOR_AIR_FLOW = "4";
    public static String SENSOR_BODY_POSITION = "5";
    public static String SENSOR_GL_METER = "6";
    public static String SENSOR_TEMPERATURE = "7";
//   new added sensor
    public static String SENSOR_HEIGHT = "8";
    public static String SENSOR_WEIGHT = "9";
//  device
    public static String BLUETOOTH_DEVICE_1  = "HC-05";
//  password for encryption
    public static final String ENCRYPTION_PASS = "password";

    public static String SHARED_PREF_KEY="connected";
}
