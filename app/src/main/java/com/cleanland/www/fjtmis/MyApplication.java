package com.cleanland.www.fjtmis;

import android.app.Application;

/**
 * Created by Administrator on 2014/10/17.
 * 用于保存一些全局变量，以免频繁查询本地INI文件。。。
 *
 * 全局变量：
 * Stirng dummy=((MyApplication) getApplication()).getDummy();
 * Stirng domain=((MyApplication) getApplication()).getSiteUrl();
 */
public class MyApplication extends Application {
    /**
     * 全局变量：软件要访问的域名
     */
    private static String SiteUrl;
    private String dummy;

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public static String getSiteUrl() {
        return SiteUrl;
    }

    /**
     * 保存软件要访问的域名
     *
     * @param c
     */
    public void setSiteUrl(String c) {
        this.SiteUrl = c;
    }

    @Override
    public void onCreate() {
        SiteUrl = "";
        dummy = "blablabla";
        super.onCreate();
    }
}