package com.pmh.iotblog.Service;

public class APIService {
    private static String base_url = "http://api.openweathermap.org/data/2.5/";
    private static String base_url_iot = "http://binhmusic.tk/";

    public static DataService getService() {
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }

    public static DataService getServiceIOT() {
        return APIRetrofitClient.getClient(base_url_iot).create(DataService.class);
    }
}
