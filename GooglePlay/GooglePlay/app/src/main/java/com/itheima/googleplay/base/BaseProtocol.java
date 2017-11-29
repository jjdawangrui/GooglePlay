package com.itheima.googleplay.base;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.FileUtils;
import com.itheima.googleplay.utils.HttpUtils;
import com.itheima.googleplay.utils.IOUtils;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * 针对具体的protocol封装的基类
 * 抽取基类的方法：
 * 如果你现在有具体的类
 * 直接拷贝具体类的代码到你的基类，然后站在基类的角度增删改就行了
 */
public abstract class BaseProtocol<T> {
    /**
     * 加载数据
     * 1.从内存-->返回
     * 2.从磁盘-->返回,存内存
     * 3.从网络-->返回,存内存,存磁盘
     */

    /**
     * 考虑异常是抛出还是捕获?
     * 异常如果抛出去是抛到哪里去了？
     * 抛到方法的调用处
     * 什么时候抛什么时候try catch？
     * 如果抛出的异常，在方法调用处会根据异常处理相应的逻辑时，就应该抛出去
     */
    public T loadData(int index) throws Exception {//所以这个异常就可以被调用处捕获
        T result = null;
        result = loadDataFromMem(index);
        if (result != null) {
            LogUtils.s("从内存加载了数据--" + generateOnlyKey(index));
            return result;
        }
        result = loadDataFromLocal(index);
        if (result != null) {
            LogUtils.s("从本地加载了数据--" + getCacheFile(index).getAbsolutePath());
            return result;
        }
        return loadDataFromNet(index);
    }

    /**
     * 从内存加载数据
     *
     * @param index
     * @return
     */
    private T loadDataFromMem(int index) {
        T result;
        //找到存储结构
        MyApplication myApplication = (MyApplication) UIUtils.getContext();//上下文转成Application什么意思？
        Map<String, String> memProtocolCacheMap = myApplication.getMemProtocolCacheMap();

        //判断存储结构中是否有缓存
        String key = generateOnlyKey(index);
        if (memProtocolCacheMap.containsKey(key)) {
            String memCacheJsonString = memProtocolCacheMap.get(key);
            result = parseJson(memCacheJsonString);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 从本地加载数据
     *
     * @param index
     * @return
     */
    private T loadDataFromLocal(int index) {
        BufferedReader reader = null;
        //找到缓存文件
        File file = getCacheFile(index);
        //判断是否存在
        if (file.exists()) {
            try {
                reader = new BufferedReader(new FileReader(file));
                //读取缓存的生成时间
                String firstLine = reader.readLine();
                long cacheInsertTime = Long.parseLong(firstLine);//转成long类型

                //判断是否过期
                if (System.currentTimeMillis() - cacheInsertTime < Constants.PROTOCOLTIMEOUT) {
                    //有效的缓存
                    String diskCacheJsonString = reader.readLine();//一共就两行，第一行是时间

                    /*----------保存数据到内存---------*/
                    MyApplication myApplication = (MyApplication) UIUtils.getContext();
                    Map<String, String> memProtocolCacheMap = myApplication.getMemProtocolCacheMap();
                    memProtocolCacheMap.put(generateOnlyKey(index), diskCacheJsonString);
                    LogUtils.s("保存磁盘数据到内存--" + generateOnlyKey(index));

                    return parseJson(diskCacheJsonString);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

    /**
     * 得到缓存文件
     */
    private File getCacheFile(int index) {
        String dir = FileUtils.getDir("json");//优先保存到外置sdcard,应用程序的缓存目录(sdcard/Android/data/包目录/json)
        String fileName = generateOnlyKey(index);//唯一命中的问题 interfaceKey+"."+index
        return new File(dir, fileName);
    }

    /**
     * 从网络加载数据
     *
     * @param index
     * @return
     * @throws Exception
     */
    public T loadDataFromNet(int index) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        // 拼接URL：http://10.0.2.2:8080/GooglePlayServer/home
//        String url = Constants.URLS.BASEURL + interfaceKey;
        /**
         * interfaceKey如果交给外部决定，有几种方式
         *      1.通过参数传递进来
         *      2.定义成为成员变量，通过构造方法传进来
         *      3.定义成为抽象方法，交给子类必须实现
         */
        String url = Constants.URLS.BASEURL + getInterfaceKey();

        Map<String, Object> params = new HashMap<>();
//        params.put("index", index);//暂时不考虑分页，0

        params = getParamsMap(index);
        //参数部分的url
        String urlParamsByMap = HttpUtils.getUrlParamsByMap(params);
        LogUtils.s(urlParamsByMap);
        //前后进行拼接
        url = url + "?" + urlParamsByMap;
        LogUtils.s(url);
        //请求对象
        Request request = new Request.Builder().get().url(url).build();
        //开始请求
        Response response = okHttpClient.newCall(request).execute();
        //解析响应结果
        if (response.isSuccessful()) {//自带方法，返回code>=200&&<300
            String json = response.body().string();//不是toString吗？？？？？

            LogUtils.s("json-->" + json);
            /*--------------- 保存数据到内存 ---------------*/
            MyApplication application = (MyApplication) UIUtils.getContext();
            Map<String, String> memProtocolCacheMap = application.getMemProtocolCacheMap();
            memProtocolCacheMap.put(generateOnlyKey(index), json);
            LogUtils.s("保存网络数据到内存-->" + generateOnlyKey(index));

            /*--------------- 保存数据到本地磁盘 ---------------*/
            LogUtils.s("保存网络数据到本地-->" + getCacheFile(index).getAbsolutePath());
            BufferedWriter writer = null;
            try {
                File cacheFile = getCacheFile(index);
                writer = new BufferedWriter(new FileWriter(cacheFile));
                //写第一行
                writer.write(System.currentTimeMillis() + "");
                //换行，厉害了
                writer.newLine();
                //写第二行
                writer.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(writer);
            }
            return parseJson(json);
        } else {
            return null;
        }
    }

    /**
     * 生成缓存的唯一索引的key
     */
    public String generateOnlyKey(int index) {
        return getInterfaceKey() + "." + index;
    }

    /**
     * @des 得到协议的关键字
     * @des 在BaseProtocl中, 不知道协议关键字具体是啥, 交给子类
     * @des 子类是必须实现, 所以定义成为抽象方法, 交给子类具体实现
     */
    public abstract String getInterfaceKey();

    /**
     * @des 负责解析网络请求回来的jsonString
     * @des 一共有3种解析情况(结点解析, Bean解析, 泛型解析)
     */
    protected T parseJson(String json) {
        Gson gson = new Gson();

        //这一行可以获取子类的数据类型，所以又可以抽取了
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T object = gson.fromJson(json, type);
        return object;
    }


    /**
     * json解析的3种情况
     * 1.结点解析--jsonObject JSONArray去完成
     * 2.bean解析 jsonString-->bean
     *      HomeBean homeBean = gson.fromJson(json, HomeBean.class);
     * 3.泛型解析   jsonString-->List Map
     *      gson.fromJson(json,new TypeToken<>(){}.getType());
     */



    /**
     * @des 得到请求参数所对应的HashMap
     * @des 子类可以覆写该方法, 返回不同的参数
     */
    @NonNull
    public Map<String, Object> getParamsMap(int index) {
        Map<String, Object> params = new HashMap<>();
        params.put("index", index);//暂时不考虑分页
        return params;//默认参数是index
    }
}
