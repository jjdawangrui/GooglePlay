package com.itheima.googleplay.bean;

import java.util.List;

/**
 * Created by raynwang on 2017/7/11.
 */

public class ItemBean {
    /**
     * id : 1525489
     * name : 黑马程序员
     * packageName : com.itheima.www
     * iconUrl : app/com.itheima.www/icon.jpg
     * stars : 5
     * size : 91767
     * downloadUrl : app/com.itheima.www/com.itheima.www.apk
     * des : 产品介绍：google市场app测试。
     */

    public long id;
    public String name;
    public String packageName;
    public String iconUrl;
    public float stars;
    public long size;
    public String downloadUrl;
    public String des;

       /*--------------- 添加详情页面里面的额外字段 ---------------*/

    public String author;// 黑马程序员
    public String date;//  2015-06-10
    public String downloadNum;//	40万+
    public String version;// 1.1.0605.0
    public List<ItemSafeBean> safe;// Array
    public List<String> screen;//Array

    public class ItemSafeBean {
        public String safeDes;// 已通过安智市场安全检测，请放心使用
        public int safeDesColor;//	0
        public String safeDesUrl;//	app/com.itheima.www/safeDesUrl0.jpg
        public String safeUrl;// app/com.itheima.www/safeIcon0.jpg

        @Override
        public String toString() {
            return "ItemSafeBean{" +
                    "safeDes='" + safeDes + '\'' +
                    ", safeDesColor=" + safeDesColor +
                    ", safeDesUrl='" + safeDesUrl + '\'' +
                    ", safeUrl='" + safeUrl + '\'' +
                    '}';
        }
    }
}
