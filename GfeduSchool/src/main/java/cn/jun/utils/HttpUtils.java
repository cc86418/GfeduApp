package cn.jun.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import cn.jun.bean.AddClassCartPackageBean;
import cn.jun.bean.AddLiveBook;
import cn.jun.bean.AddapPraiseBean;
import cn.jun.bean.AddvideostudylogBean;
import cn.jun.bean.AdsListBean;
import cn.jun.bean.AllUserAreasBean;
import cn.jun.bean.BindUser;
import cn.jun.bean.ChangeInfo;
import cn.jun.bean.ChangeTelBean;
import cn.jun.bean.CheckPwdBean;
import cn.jun.bean.CheckhAsBindBean;
import cn.jun.bean.ClassClassTypeBean;
import cn.jun.bean.ClassDetailBean;
import cn.jun.bean.ClassOutLineBean;
import cn.jun.bean.ClassScheduleBean;
import cn.jun.bean.Const;
import cn.jun.bean.EaluateBean;
import cn.jun.bean.GetAddVideoStudyLog;
import cn.jun.bean.GetLiveDetailBean;
import cn.jun.bean.GetLiveUrlBean;
import cn.jun.bean.GetMyLiveBean;
import cn.jun.bean.GetNotifybyIsRead;
import cn.jun.bean.GetPushAndroid;
import cn.jun.bean.GetThridBing;
import cn.jun.bean.GetUserDetail;
import cn.jun.bean.MyCollectionBean;
import cn.jun.bean.NotesListBean;
import cn.jun.bean.OnLineInfoBean;
import cn.jun.bean.OverClassListBean;
import cn.jun.bean.PackageClassTypeBean;
import cn.jun.bean.PackageDetailBean;
import cn.jun.bean.PackageProduct;
import cn.jun.bean.ProductCollection;
import cn.jun.bean.ProductListBean;
import cn.jun.bean.ProjectListBean;
import cn.jun.bean.ProjectList_NoAllBean;
import cn.jun.bean.QuesListBean;
import cn.jun.bean.RaiseQuestionBean;
import cn.jun.bean.SetNotifyIsRead;
import cn.jun.bean.StageLessonListBean;
import cn.jun.bean.StudyEndBean;
import cn.jun.bean.UnBindPhone;
import cn.jun.bean.UnBindUser;
import cn.jun.bean.UpgradeAppBean;
import cn.jun.bean.UserPhotoBean;
import jc.cici.android.atom.utils.ToolUtils;


public class HttpUtils {
    public final static String MD5_KEY = "android!%@%$@#$";
    private HttpClient httpClient = new DefaultHttpClient();
    private HttpResponse httpResponse;


    // 流转String
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    //获取时间戳
    public String getTimeStamp() {
        long time = System.currentTimeMillis() / 1000;// 获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }

    //MD5加密
    public static String getMD5Str(int userid) {
        String MD5_String = userid + MD5_KEY;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(MD5_String.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        // 16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toString().toUpperCase();
    }

    //MD5加密
    public static String getMD5Str2(String userid) {
        String MD5_String = userid + MD5_KEY;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(MD5_String.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        // 16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toString().toUpperCase();
    }


    public String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * wifi网络
     **/
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否联网
     **/
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 获取全部感兴趣项目
    public AllUserAreasBean getAllUserAreasBeanList(String url) {
        AllUserAreasBean mAllUserAreasBean = null;
        try {
            HttpGet get = new HttpGet(url);
            HttpClient client = new DefaultHttpClient();
            // 设置超时
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            HttpResponse response;
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity(),
                        "utf-8");

                mAllUserAreasBean = JSON.parseObject(result,
                        AllUserAreasBean.class);
                return mAllUserAreasBean;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //提交用户感兴趣项目
    public String SumbitIntensest(String url) {
        String res = "0";
        HttpGet httpRequest = new HttpGet(url);
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                String result = EntityUtils.toString(httpResponse.getEntity(),
                        "utf-8");
                JSONObject obj = new JSONObject(result);
                res = obj.getString("ResultState");

            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    //获取第三方绑定信息
    public GetThridBing getbindinfo(String Path, int UserId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("userId", UserId);

            String jsonString = obj.toString();
            System.out.println("jsonString -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                GetThridBing GetThridBing = JSON.parseObject(response, GetThridBing.class);

                return GetThridBing;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取用户绑定信息
    public GetUserDetail getUserDetail(String Path, String Client, int UserId, String Oauth) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("client", Client);
            obj.put("userId", UserId);
            obj.put("oauth", Oauth);

            String jsonString = obj.toString();
            System.out.println("jsonString -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                GetUserDetail UserDetail = JSON.parseObject(response, GetUserDetail.class);

                return UserDetail;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 第三方直接绑定
     */
    public BindUser BindUserList(String url) {
        try {
            HttpGet get = new HttpGet(url);
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity(),
                        "utf-8");
                BindUser data = JSON.parseObject(result, BindUser.class);
                System.out.println("第三方 直接绑定--- " + result);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解除第三方绑定
     */
    public UnBindUser GetUnBindUser(String Path, String Client, int UserId, String Oauth, String BindType) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("client", Client);
            obj.put("userId", UserId);
            obj.put("oauth", Oauth);
            obj.put("bindtype", BindType);

            String jsonString = obj.toString();
            System.out.println("jsonString -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                UnBindUser UnBindUser = JSON.parseObject(response, UnBindUser.class);

                return UnBindUser;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 修改密码
     * 返回的为String字符串
     */
    public String ChangePassWordMsg(String url) {
        String res = "0";
        HttpGet httpRequest = new HttpGet(url);
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // System.out.println("HttpStatus.SC_OK :  ");
                StringBuilder builder = new StringBuilder();
                BufferedReader bufferedReader2 = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity()
                                .getContent()));
                for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
                        .readLine()) {
                    builder.append(s);
                }
                res = builder.toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 修改个人信息
     */
    public ChangeInfo GetChangeInfo(String Path, int UserId, String Client, String Oauth, String S_Type, String parameter) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
//            String ParameterEncoding = URLEncoder.encode(parameter,
//                    "UTF-8");
//            System.out.println("ParameterEncoding -- " + ParameterEncoding );
            JSONObject obj = new JSONObject();
            obj.put("userId", UserId);
            obj.put("client", Client);
            obj.put("oauth", Oauth);
            if ("city".equals(S_Type)) {
                String Province = "";
                String Country = "";
                String[] city = parameter.split(",");
                Province = city[0];
                Country = city[1];
                obj.put("SN_Province", Province);
                obj.put("SN_Country", Country);
            } else {
                obj.put(S_Type, parameter);
            }


            System.out.println("修改信息key -- " + S_Type);
            System.out.println("修改信息value -- " + parameter);

            String jsonString = obj.toString();
            System.out.println("jsonString -- " + Path + " : + " + jsonString);
            wr.write(jsonString.toString().getBytes("UTF-8"));

            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                ChangeInfo changeInfo = JSON.parseObject(response, ChangeInfo.class);

                return changeInfo;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取短信验证码
    public String getMsg(String url) {
        String res = "0";
        HttpGet httpRequest = new HttpGet(url);
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // System.out.println("HttpStatus.SC_OK :  ");
                StringBuilder builder = new StringBuilder();
                BufferedReader bufferedReader2 = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity()
                                .getContent()));
                for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
                        .readLine()) {
                    builder.append(s);
                }
                res = builder.toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    public String EncapsulationJson
            (String client, String version, String appname, String userId, String classId, String lessonId) {
        JSONObject obj = new JSONObject();
        String jsonString = "";
        try {
            obj.put("client", client);
            obj.put("version", version);
//            obj.put("deviceid", deviceid);
            obj.put("appname", appname);
            obj.put("userId", userId);
            obj.put("timeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + getTimeStamp() + "android!%@%$@#$"));
            //次要条件
            obj.put("classId", classId);
            obj.put("lessonId", lessonId);
            jsonString = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    //修改手机短信获取
    public ChangeTelBean getChangeTelMsg(String Path, String Mobile, String Oauth, String Client) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("mobile", Mobile);
            obj.put("oauth", Oauth);
            obj.put("client", Client);
            String jsonString = obj.toString();
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                ChangeTelBean ChangeTelBean = JSON.parseObject(response, ChangeTelBean.class);

                return ChangeTelBean;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //更换手机号
    public ChangeTelBean getChangeTelPhone(String Path, String Mobile, String Code) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("mobile", Mobile);
            obj.put("code", Code);
            String jsonString = obj.toString();
            System.out.println("jsonString -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                ChangeTelBean ChangeTelBean = JSON.parseObject(response, ChangeTelBean.class);

                return ChangeTelBean;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送邮箱验证码
     */
    public ChangeInfo SendMailInfo(String Path, int UserId, String Client, String Password, String Email, String Oauth) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("userId", UserId);
            obj.put("client", Client);
            obj.put("password", Password);
            obj.put("email", Email);
            obj.put("oauth", Oauth);

            String jsonString = obj.toString();
            System.out.println("jsonString -- " + Path + " : + " + jsonString);
            wr.write(jsonString.toString().getBytes("UTF-8"));

            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                ChangeInfo changeInfo = JSON.parseObject(response, ChangeInfo.class);

                return changeInfo;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //检查密码接口
    public CheckPwdBean getCheckPwdBean(String Path, String UserId, String Client, String PassWord, String Oauth) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("userId", UserId);
            obj.put("client", Client);
            obj.put("password", PassWord);
            obj.put("oauth", Oauth);
            String jsonString = obj.toString();
            System.out.println("jsonString -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                CheckPwdBean checkPwdBean = JSON.parseObject(response, CheckPwdBean.class);

                return checkPwdBean;
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //班级课程目录（在线）
    public StageLessonListBean getStagelessonlist
    (String Path,
     String client,
     String version,
     String appname,
     String userId,
     String ChildClassTypeID,
     String ClassTypeID) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("client", client);
            obj.put("version", version);
            obj.put("clientName", appname);
            obj.put("userId", userId);
            obj.put("timeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("ChildClassTypeID", ChildClassTypeID);
            obj.put("ClassTypeID", ClassTypeID);
            String jsonString = obj.toString();
            System.out.println("Stagelessonlist -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("StagelessonlistCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("Stagelessonlistresponse -- " + response);

                StageLessonListBean stageLessonListBean = JSON.parseObject(response, StageLessonListBean.class);

                return stageLessonListBean;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //课程详情问题列表（在线）
    public QuesListBean getQuesListBeanlist
    (String Path,
     String client,
     String version,
     String appname,
     String userId,
     String VideoId,
     String QuesType,
     int PageIndex,
     int PageSize) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("client", client);
            obj.put("version", version);
            obj.put("clientName", appname);
            obj.put("userId", userId);
            obj.put("timeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("VideoId", VideoId);
            obj.put("QuesType", QuesType);
            obj.put("PageIndex", PageIndex);
            obj.put("PageSize", PageSize);

            String jsonString = obj.toString();
            System.out.println("QuesListBean -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("QuesListBean -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("QuesListBean -- " + response);

                QuesListBean quesListBean = JSON.parseObject(response, QuesListBean.class);

                return quesListBean;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //班级课程目录（在线）
    public NotesListBean getNotesListBeanlist
    (String Path,
     String client,
     String version,
     String appname,
     String userId,
     String CSPKID,
     String Type,
     String VPKID,
     int PageIndex,
     int PageSize) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();

            obj.put("client", client);
            obj.put("version", version);
            obj.put("clientName", appname);
            obj.put("userId", userId);
            obj.put("timeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("CSPKID", CSPKID);//课表ID 在线课程详情列表页面无需用到,穿0即可
            obj.put("Type", Type);
            obj.put("VPKID", VPKID);

            obj.put("PageIndex", PageIndex);
            obj.put("PageSize", PageSize);

            String jsonString = obj.toString();
            System.out.println("NotesListBean -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("NotesListBean -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("NotesListBean -- " + response);

                NotesListBean notesListBean = JSON.parseObject(response, NotesListBean.class);

                return notesListBean;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //已结束课程
    public OverClassListBean getOverClassListBean
    (String Path,
     String client,
     String version,
     String appname,
     String userId
    ) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();

            obj.put("client", client);
            obj.put("version", version);
            obj.put("clientName", appname);
            obj.put("userId", userId);
            obj.put("timeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + getTimeStamp() + "android!%@%$@#$"));
            String jsonString = obj.toString();
            System.out.println("NotesListBean -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("NotesListBean -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("NotesListBean -- " + response);

                OverClassListBean overClassListBean = JSON.parseObject(response, OverClassListBean.class);

                return overClassListBean;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //在线课程详情接口
    public OnLineInfoBean getOnLineInfoBean
    (String Path,
     String client,
     String version,
     String appname,
     String userId,
     String ChildClassTypeID,
     String ClassTypeID,
     String Level_ParentID,
     String Level_PKID
    ) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("client", client);
            obj.put("version", version);
            obj.put("clientName", appname);
            obj.put("userId", userId);
            obj.put("ChildClassTypeID", ChildClassTypeID);
            obj.put("ClassTypeID", ClassTypeID);
            obj.put("Level_ParentID", Level_ParentID);
            obj.put("Level_PKID", Level_PKID);
            obj.put("timeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + getTimeStamp() + "android!%@%$@#$"));
            String jsonString = obj.toString();
            System.out.println("getOnLineInfoBean -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("getOnLineInfoBean -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                OnLineInfoBean LineInfoBean = JSON.parseObject(response, OnLineInfoBean.class);
                System.out.println("getOnLineInfoBean -- " + response);
                return LineInfoBean;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //学会了接口
    public StudyEndBean getStudyEndBean
    (String Path,
     String client,
     String version,
     String appname,
     String userId,
     String KeyValue) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("client", client);
            obj.put("version", version);
            obj.put("clientName", appname);
            obj.put("userId", userId);
            obj.put("KeyValue", KeyValue);
            obj.put("timeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(userId + getTimeStamp() + "android!%@%$@#$"));
            String jsonString = obj.toString();
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                StudyEndBean studyEndBean = JSON.parseObject(response, StudyEndBean.class);
                System.out.println("studyEndBean -- " + response);
                return studyEndBean;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //用户上传头像
    public UserPhotoBean getAddPhoto(String url, String userImgUrl) {
        MultipartEntity mpEntity = new MultipartEntity();
        try {
            if (!"".equals(userImgUrl) && null != userImgUrl) {
                System.out.println("imgUrl >>>：" + userImgUrl);
                ContentBody cbFile = new FileBody(new File(userImgUrl),
                        "image/jpg");
                mpEntity.addPart("UserHeadImgFile", cbFile);
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(mpEntity);
                HttpResponse httpResponse = httpClient.execute(httppost);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String result = EntityUtils.toString(
                            httpResponse.getEntity(), "utf-8");
                    UserPhotoBean PhotoBean = JSON.parseObject(result,
                            UserPhotoBean.class);
                    Log.i("PhotoBean ", "" + result);
                    return PhotoBean;
                }
            } else {
                try {
                    HttpGet get = new HttpGet(url);
                    HttpClient client = new DefaultHttpClient();
                    client.getParams().setParameter(
                            CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
                    HttpResponse response = client.execute(get);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(
                                response.getEntity(), "utf-8");
                        UserPhotoBean PhotoBean = JSON.parseObject(result,
                                UserPhotoBean.class);
                        return PhotoBean;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 解除绑定设备号
    public UnBindPhone unbinddevice
    (String Path,
     int UserId,
     String client,
     String version,
     String loginname,
     String password,
     String deviceid,
     String appname) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("UserId", UserId);
            obj.put("client", client);
            obj.put("version", version);
            obj.put("loginname", loginname);
            obj.put("password", password);
            obj.put("deviceid", deviceid);
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", appname);
            String jsonString = obj.toString();
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                UnBindPhone UnBindPhone = JSON.parseObject(response,
                        UnBindPhone.class);
                System.out.println("解除绑定 -- " + UnBindPhone.getMessage());
                return UnBindPhone;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 上传学习进度时间
    public AddvideostudylogBean addvideostudylog
    (String Path,
     int UserId,
     String client,
     String version,
     String appname,
     String KeyValue,
     String ViedoID,
     String ViedoTime) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("KeyValue", KeyValue);
            obj.put("VideoID", ViedoID);
            obj.put("ViedoTime", ViedoTime);
            String jsonString = obj.toString();

            Log.i("用户视频记录", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("用户视频记录 -- " + response);
                AddvideostudylogBean Addvideostudylog = JSON.parseObject(response,
                        AddvideostudylogBean.class);
//                System.out.println("用户视频记录 -- " + Addvideostudylog.getMeaaage());
                return Addvideostudylog;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //APP版本检测
    public UpgradeAppBean checkifupgradeapp(String Path) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("appname", Const.APPNAME);

            String jsonString = obj.toString();
            System.out.println("upgradeApp -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("upgradeApp -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("upgradeApp -- " + response);

                UpgradeAppBean upgradeApp = JSON.parseObject(response, UpgradeAppBean.class);

                return upgradeApp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 是否绑定过该设备
    public CheckhAsBindBean checkhasbind
    (String Path,
     String oauth,
     String client,
     String userId,
     String TimeStamp,
     String version,
     String AppName,
     String DeviceId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("oauth", oauth);
            obj.put("client", client);
            obj.put("userId", userId);
            obj.put("TimeStamp", TimeStamp);
            obj.put("version", version);
            obj.put("AppName", AppName);
            obj.put("DeviceId", DeviceId);
            String jsonString = obj.toString();
            System.out.println("jsonString -- " + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode -- " + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("response -- " + response);
                CheckhAsBindBean CheckhAsBindBean = JSON.parseObject(response,
                        CheckhAsBindBean.class);
                System.out.println("是否绑定过 -- " + CheckhAsBindBean.getMessage());
                return CheckhAsBindBean;
            }
        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * APP首页数据获取
     **/
    public ProjectListBean getProjectListBean
    (String Path,
     String projectId,
     String addAllOption) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("UserId", "0");
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
//            obj.put("TimeStamp", "1501496942");
//            obj.put("oauth","c8856b3245c8cdd7");

            obj.put("appname", Const.APPNAME);
            obj.put("projectId", projectId);
            obj.put("addAllOption", addAllOption);//是否添加全部按钮 1：是 0：否

            String jsonString = obj.toString();
            Log.i("首页数据参数传递-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                ProjectListBean projectListBean = JSON.parseObject(response,
                        ProjectListBean.class);
                System.out.println("首页数据获取 -- " + projectListBean.getCode());
                Gson s = new Gson();
                System.out.println("首页数据获取 -- " + s.toJson(projectListBean).toString());
                return projectListBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * APP首页数据获取(无全部)
     **/
    public ProjectList_NoAllBean getProjectList_NoAllBean
    (String Path,
     String projectId,
     String addAllOption) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("UserId", "0");
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
//            obj.put("TimeStamp", "1501496942");
//            obj.put("oauth","c8856b3245c8cdd7");

            obj.put("appname", Const.APPNAME);
            obj.put("projectId", projectId);
            obj.put("addAllOption", addAllOption);//是否添加全部按钮 1：是 0：否

            String jsonString = obj.toString();
            Log.i("首页数据参数传递(没有全部)-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                ProjectList_NoAllBean projectListBean = JSON.parseObject(response,
                        ProjectList_NoAllBean.class);
                System.out.println("首页数据获取(没有全部) -- " + projectListBean.getCode());
                Gson s = new Gson();
                System.out.println("首页数据获取(没有全部) -- " + s.toJson(projectListBean).toString());
                return projectListBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * APP首页轮播图接口
     **/
    public AdsListBean getAdsListBean
    (String Path,
     int projectId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("UserId", "0");
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("projectId", projectId);

            String jsonString = obj.toString();
            Log.i("轮播图-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                AdsListBean adsListBean = JSON.parseObject(response,
                        AdsListBean.class);
                System.out.println("轮播图 -- " + projectId + adsListBean.getCode());
                Gson s = new Gson();
                System.out.println("轮播图 -- " + projectId + s.toJson(adsListBean).toString());
                return adsListBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 产品列表
     **/
    public ProductListBean getProductListBean
    (String Path,
     int PageIndex,  //第几页
     int PageSizse, //每页条数 默认10
     int ProjectIdArr,
     int OrderType) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("UserId", "0");
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);
//            OrderType Int NO 1：推荐 2：价格 3：人气 4：热门
//            Direction Int NO 0：降序 1：升序
//            PageIndex Int NO 第几页
//            PageSizse Int NO 每页条数 默认10
//            Keywords String NO 关键字
//            ProjectIdArr Int NO 项目ID 多个逗号隔开
//            ClassType_TypeArr Int NO 班型类型：1:面授 2:在线 4:直播多个逗号隔开

            obj.put("OrderType", OrderType);
            obj.put("PageIndex", PageIndex);
            obj.put("PageSizse", "10");
            obj.put("ProjectIdArr", ProjectIdArr);

            String jsonString = obj.toString();
            Log.i("产品列表-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                ProductListBean productList = JSON.parseObject(response,
                        ProductListBean.class);
                System.out.println("产品列表 -- " + productList.getCode());
                Gson s = new Gson();
                System.out.println("产品列表 -- " + s.toJson(productList).toString());
                return productList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 套餐详情接口
     **/
    public PackageDetailBean getPackageDetailBean
    (String Path,
     int UserId,
     int packageid) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);

            obj.put("packageid", packageid);

            String jsonString = obj.toString();
            Log.i("套餐详情-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("套餐详情 100-- " + response);
                PackageDetailBean packageDetail = JSON.parseObject(response,
                        PackageDetailBean.class);
                return packageDetail;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 班级详情接口
     **/
    public ClassDetailBean getClassDetailBean
    (String Path,
     int UserId,
     int classid) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());

            obj.put("appname", Const.APPNAME);

            obj.put("classid", classid);

            String jsonString = obj.toString();
            Log.i("班级-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("班级 200-- " + response);
                ClassDetailBean classDetail = JSON.parseObject(response,
                        ClassDetailBean.class);
                System.out.println("班级 -- " + classDetail.getCode());
                Gson s = new Gson();
                System.out.println("班级 解析完成-- " + s.toJson(classDetail).toString());
                return classDetail;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 班级大纲接口
     **/
    public ClassOutLineBean getClassOutLineBean
    (String Path,
     int UserId,
     int classid) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);

            obj.put("classid", classid);

            String jsonString = obj.toString();
            Log.i("班级大纲-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                ClassOutLineBean classOutLineBean = JSON.parseObject(response,
                        ClassOutLineBean.class);
                Gson s = new Gson();
                System.out.println("班级大纲 解析完成-- " + s.toJson(classOutLineBean).toString());
                return classOutLineBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 班级详情收藏接口
     **/
    public ProductCollection getProductCollection
    (String Path,
     int UserId,
     int CollectionID,
     int CollectionType,
     int type) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("UserId", UserId);
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            if (1 == CollectionType) {
                obj.put("Classid", CollectionID);//班级ID
            } else if (0 == CollectionType) {
                obj.put("Packageid", CollectionID);//套餐ID
            }
            obj.put("type", type);//0:添加收藏 1：删除收藏

            String jsonString = obj.toString();
            Log.i("收藏-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                ProductCollection productCollection = JSON.parseObject(response,
                        ProductCollection.class);
                Gson s = new Gson();
                System.out.println("收藏-- " + s.toJson(productCollection).toString());
                return productCollection;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 课程详情常见问题接口
     **/
    public EaluateBean getEaluateList
    (String Path,
     int UserId,
     int CollectionType,//1班级,0套餐
     int CollectionID
    ) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);

            if (1 == CollectionType) {
                obj.put("Classid", CollectionID);//班级ID
            } else if (0 == CollectionType) {
                obj.put("Packageid", CollectionID);//套餐ID
            }
            obj.put("pageindex", 1);

            String jsonString = obj.toString();
            Log.i("常见问题-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                EaluateBean ealuateBean = JSON.parseObject(response,
                        EaluateBean.class);
                Gson s = new Gson();
                System.out.println("常见问题-- " + s.toJson(ealuateBean).toString());
                return ealuateBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 班级下的所有班型接口
     **/
    public ClassClassTypeBean getclassclasstypelist
    (String Path,
     int UserId,
     //班级ID
     int ClassID) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("ClassID", ClassID);

            String jsonString = obj.toString();
            Log.i("班级下版型-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                ClassClassTypeBean classClassTypeBean = JSON.parseObject(response,
                        ClassClassTypeBean.class);
                Gson s = new Gson();
                System.out.println("班级下版型-- " + s.toJson(classClassTypeBean).toString());
                return classClassTypeBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 套餐下的所有班型接口
     **/
    public PackageClassTypeBean getpackageClassTypeList
    (String Path,
     int UserId,
     //套餐ID
     int packageid,
     //班级列表,多个逗号隔开
     String classidlist) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("packageid", packageid);
            if (!"".equals(classidlist)) {
                obj.put("classidlist", classidlist);
            }


            String jsonString = obj.toString();
            Log.i("套餐型-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("套餐型-- " + response);
                PackageClassTypeBean packageClassTypeBean = JSON.parseObject(response,
                        PackageClassTypeBean.class);
                Gson s = new Gson();
                return packageClassTypeBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 收藏列表接口
     **/
    public MyCollectionBean getMyCollectionBeanList
    (String Path,
     int UserId,
     //页码
     int PageIndex,
     //每页条数
     int PageSize) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("PageIndex", PageIndex);
            if (0 == PageSize) {
                obj.put("PageSize", 10);
            } else {
                obj.put("PageSize", PageSize);
            }

            String jsonString = obj.toString();
            Log.i("收藏列表-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                System.out.println("收藏列表-- " + response);
                MyCollectionBean myCollection = JSON.parseObject(response,
                        MyCollectionBean.class);
                return myCollection;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 删除收藏接口
     **/
    public ProductCollection RemoveProductCollection
    (String Path,
     int UserId,
     int CollectionID,
     int CollectionType) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("UserId", UserId);
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            if (1 == CollectionType) {
                obj.put("Classid", CollectionID);//班级ID
            } else if (0 == CollectionType) {
                obj.put("Packageid", CollectionID);//套餐ID
            }
            obj.put("type", 1);//0:添加收藏 1：删除收藏

            String jsonString = obj.toString();
            Log.i("删除收藏-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                ProductCollection productCollection = JSON.parseObject(response,
                        ProductCollection.class);
                Gson s = new Gson();
                System.out.println("删除收藏-- " + s.toJson(productCollection).toString());
                return productCollection;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 未评议课表接口
     **/
    public ClassScheduleBean getclassschedulelist
    (String Path,
     int UserId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);


            String jsonString = obj.toString();
            Log.i("师资评议-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("师资评议-- ", "" + response);
                ClassScheduleBean classSchedule = JSON.parseObject(response,
                        ClassScheduleBean.class);
                return classSchedule;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 评议问题列表
     **/
    public RaiseQuestionBean getRaiseQuestion
    (String Path,
     int UserId,
     int ClassSchedule_ID) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("ClassSchedule_ID", ClassSchedule_ID);
            String jsonString = obj.toString();
            Log.i("评议问题-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("评议问题-- ", "" + response);
                RaiseQuestionBean classSchedule = JSON.parseObject(response,
                        RaiseQuestionBean.class);
                return classSchedule;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交评议问题
     **/
    public AddapPraiseBean getAddapPraiseBean
    (String Path,
     int UserId,
     int ClassSchedule_ID,
     ArrayList<String> Appraise_Content,
     String Appraise_Opinion,
     int TemplateId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("ClassSchedule_ID", ClassSchedule_ID);
            obj.put("TemplateId", TemplateId);
            JSONArray arr = new JSONArray();
            for (int i = 0; i < Appraise_Content.size(); i++) {
                arr.put(Appraise_Content.get(i));
            }

            obj.put("Appraise_Content", arr);

            obj.put("Appraise_Opinion", Appraise_Opinion);

            String jsonString = obj.toString();
            Log.i("提交评价-- ", "" + jsonString);
            wr.write(jsonString.getBytes());
//            wr.writeBytes(jsonString.getBytes());
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("提交评价-- ", "" + response);
                AddapPraiseBean classSchedule = JSON.parseObject(response,
                        AddapPraiseBean.class);
                return classSchedule;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 保存讲师综合
     **/
    public AddapPraiseBean getAddapPraiseAll
    (String Path,
     int UserId,
     int Appraise_PKIDint,
     int Appraise_OverallMerit
    ) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("Appraise_PKID", Appraise_PKIDint);
            obj.put("Appraise_OverallMerit", Appraise_OverallMerit);

            String jsonString = obj.toString();
            Log.i("保存讲师综合-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("保存讲师综合-- ", "" + response);
                AddapPraiseBean classSchedule = JSON.parseObject(response,
                        AddapPraiseBean.class);
                return classSchedule;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加入购物车（班级）
     **/
    public AddClassCartPackageBean getAddClassCart
    (String Path,
     int UserId,
     int classid,
     int classtypeid,
     int buyway) {//1：加入购物车 2：直接购买
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("classid", classid);
            obj.put("classtypeid", classtypeid);
            obj.put("buyway", buyway);

            String jsonString = obj.toString();
            Log.i("加入购物车（班级）-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("加入购物车（班级）-- ", "" + response);
                AddClassCartPackageBean classSchedule = JSON.parseObject(response,
                        AddClassCartPackageBean.class);
                return classSchedule;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加入购物车（套餐）
     **/
    public AddClassCartPackageBean getAddPackageCart
    (String Path,
     int UserId,
     int PackageId,
     int buyway,//1：加入购物车 2：直接购买
     ArrayList<PackageProduct> list) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("PackageId", PackageId);
            obj.put("buyway", buyway);
            JSONObject json;
            JSONArray arr = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                json = new JSONObject();
                json.put("Kid", list.get(i).getKid());
                json.put("ClassId", list.get(i).getClassId());
                json.put("ClassTypeId", list.get(i).getClassTypeId());
                json.put("BuyType", list.get(i).getBuyType());
                arr.put(json);
            }
            Gson s = new Gson();
            Log.i(" == ", "" + s.toJson(arr).toString());

            obj.put("PackageProduct", arr);


            String jsonString = obj.toString();
            Log.i("加入购物车（套餐）-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("加入购物车（套餐）-- ", "" + response);
                AddClassCartPackageBean classSchedule = JSON.parseObject(response,
                        AddClassCartPackageBean.class);
                return classSchedule;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取推送通知
     **/
    public GetPushAndroid getpushandroid
    (String Path,
     int UserId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            Log.i("推送时间戳 -- ",""+getTimeStamp());
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            Log.i("推送时间戳 2 -- ",""+getTimeStamp());
            obj.put("appname", Const.APPNAME);

            String jsonString = obj.toString();
            Log.i("推送-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            Log.i("推送-- ", "" + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("推送-- ", "" + response);
                GetPushAndroid pushAndroid = JSON.parseObject(response,
                        GetPushAndroid.class);
                return pushAndroid;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 视频记录
     **/
    public GetAddVideoStudyLog GetAddVideoStudyLog
    (String Path,
     int UserId,
     String KeyValue,
     int ViedoID,
     int ViedoTime) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            obj.put("appname", Const.APPNAME);

            obj.put("KeyValue", KeyValue);
            obj.put("ViedoID", ViedoID);
            obj.put("ViedoTime", ViedoTime);

            String jsonString = obj.toString();
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                GetAddVideoStudyLog addVideoStudyLog = JSON.parseObject(response,
                        GetAddVideoStudyLog.class);
                return addVideoStudyLog;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取已读/未读通知列表
     **/
    public GetNotifybyIsRead getNotifybyIsRead
    (String Path,
     int UserId,
     int Inform_IsRead,
     int PageIndex,
     int PageSize) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("Inform_IsRead", Inform_IsRead);//1.已读   0.未读
            obj.put("PageIndex", PageIndex);//页数 默认1
            obj.put("PageSize", PageSize);//数量  默认10

            String jsonString = obj.toString();
            Log.i("通知列表-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            Log.i("推送-- ", "" + statusCode);
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("通知列表-- ", "" + response);
                GetNotifybyIsRead NotifybyIsRead = JSON.parseObject(response,
                        GetNotifybyIsRead.class);
                return NotifybyIsRead;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通知设置为已读
     **/
    public SetNotifyIsRead setNotifyIsRead
    (String Path,
     int UserId,
     int Inform_PKID) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("Inform_PKID", Inform_PKID);

            String jsonString = obj.toString();
            Log.i("设置通知已读-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("设置通知已读-- ", "" + response);
                SetNotifyIsRead setNotifyIsRead = JSON.parseObject(response,
                        SetNotifyIsRead.class);
                return setNotifyIsRead;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 直播详情
     **/
    public GetLiveDetailBean getLiveDetail
    (String Path,
     int UserId,
     int ClassId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("ClassId", ClassId);

            String jsonString = obj.toString();
            Log.i("直播详情-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("直播详情-- ", "" + response);
                GetLiveDetailBean getLiveDetailBean = JSON.parseObject(response,
                        GetLiveDetailBean.class);
                return getLiveDetailBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 我的直播页面
     **/
    public GetMyLiveBean getMyLive
    (String Path,
     int UserId,
     int searchtype) {//1：未结束 2：已结束
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("searchtype", searchtype);

            String jsonString = obj.toString();
            Log.i("我的直播-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("我的直播-- ", "" + response);
                GetMyLiveBean getLiveDetailBean = JSON.parseObject(response,
                        GetMyLiveBean.class);
                return getLiveDetailBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取直播点播H5地址
     **/
    public GetLiveUrlBean getLiveUrl
    (String Path,
     int UserId,
     int scheduleId,    //课表IDID
     int classid,
     int searchType) {//0：直播URL  1:点播URL
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("scheduleId", scheduleId);
            obj.put("classid", classid);
            obj.put("searchType", searchType);

            String jsonString = obj.toString();
            Log.i("获取H5地址-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("获取H5地址 - ", "" + response);
                GetLiveUrlBean getLiveDetailBean = JSON.parseObject(response,
                        GetLiveUrlBean.class);
                return getLiveDetailBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 预约直播
     **/
    public AddLiveBook addlivebook
    (String Path,
     int UserId,
     int classid) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("classid", classid);


            String jsonString = obj.toString();
            Log.i("预约直播-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("预约直播 - ", "" + response);
                AddLiveBook getLiveDetailBean = JSON.parseObject(response,
                        AddLiveBook.class);
                return getLiveDetailBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 预约直播(系列)
     **/
    public AddLiveBook addlivebookXL
    (String Path,
     int UserId,
     int classid,
     int ClassTypeId,
     int ChildClassTypeId,
     int ScheduleId,
     int LevelId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            JSONObject obj = new JSONObject();
            if (0 != UserId) {
                obj.put("UserId", UserId);
                obj.put("Oauth", ToolUtils.getMD5Str(UserId + getTimeStamp() + "android!%@%$@#$"));
            } else {
                obj.put("UserId", "0");
                obj.put("Oauth", ToolUtils.getMD5Str("0" + getTimeStamp() + "android!%@%$@#$"));
            }
            obj.put("Client", Const.CLIENT);
            obj.put("version", Const.VERSION);
            obj.put("TimeStamp", getTimeStamp());
            obj.put("appname", Const.APPNAME);
            obj.put("classid", classid);

            obj.put("ClassTypeId", ClassTypeId);
            obj.put("ChildClassTypeId", ChildClassTypeId);
            obj.put("ScheduleId", ScheduleId);
            obj.put("LevelId", LevelId);



            String jsonString = obj.toString();
            Log.i("预约直播XL-- ", "" + jsonString);
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(
                        urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                Log.i("预约直播XL - ", "" + response);
                AddLiveBook getLiveDetailBean = JSON.parseObject(response,
                        AddLiveBook.class);
                return getLiveDetailBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
