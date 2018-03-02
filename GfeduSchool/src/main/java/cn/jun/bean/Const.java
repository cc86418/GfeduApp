package cn.jun.bean;



public class Const {
//    public static int  USERID=26146;
//    public static String USERID_S="123898";
//    public static String USERTEL="";

    public static String CLIENT = "ANDROID";
    public static String VERSION = "5.0.3";
    public static String APPNAME = "JINCHENGWANGXIAO";
    // 测试服务器地址
    //public static String URL = "http://mapitest.gfedu.org/";
    //上线服务器地址
    public static final String URL = "http://mapi.gfedu.cn/";
    /** 个人信息接口 **/
    public static final String GetRenXinIxAPI = "api/user/getuserdetail/v500";
    /** 更换手机获取验证码接口 **/
    public static final String GetMobleCodeAPI = "api/user/getupdatemobilecode/v500";
    /** 更换手机接口 **/
    public static final String GetChangeMobleAPI = "api/user/updateuserphone/v500";
    /** 获取用户绑定信息定接口 **/
    public static final String GetThridBingAPI = "api/user/getbindinfo/v500";
    /** 绑定第三方接口 **/
    public static final String BindUserAPI = "http://m.gfedu.cn/StudentWebService.asmx/BindUserThirdAccount?Student=";
    /** 解除第三方绑定接口 **/
    public static final String UnBingAPI = "api/user/delbind/v500";
    /** 修改密码接口 **/
    public static final String ChangePassAPI = "http://m.gfedu.cn/StudentWebService.asmx/ChangeUserPass?Student=";
    /** 修改个人信息接口 **/
    public static final String ChangeInfoAPI = "api/user/updateuserdetail/v500";
    /** 发送绑定邮箱验证码 **/
    public static final String SendBindeMailAPI = "api/user/sendbindemailurl/v500";
    /** 检查密码 **/
    public static final String CheckPwdAPI = "api/user/checkpwd/v500";
    /** 课程目录（在线） **/
    public static final String TonLineListAPI = "api/class/getonlinelist/v500";
    /** 在线课程详情 **/
    public static final String OnLineInfoAPI = "api/class/getonlineinfo/v500";
    /** 在线课程详情笔记/问题列表（在线） **/
    public static final String NotesListAPI = "api/notes/getnoteslist/v500";
    public static final String QuesListAPI = "api/classques/getqueslist/v500";
    /** 已结束课程 **/
    public static final String OverClassListAPI = "api/class/getendlist/v500";
    /**观看视频学会了**/
    public static final String StudyEndAPI = "api/class/studyend/v500";
    // 客户端登录标示
    public static final String VideoTime_FLAG ="video_time";
    /**离线视频进度**/
    public static final String AddVideoTimeAPI="api/class/addvideostudylog/v501";
    /**在线状态下上传视频的进度**/
    public static final String AddVideoLogAPI="api/class/addvideostudylog/v500";
    /**上传头像**/
    public static final String UserPhotoAPI="http://www.gfedu.cn/function/AppApi.ashx?Student=";
    /**解除绑定用户**/
    public static final String UnBindDeviceAPI="api/user/unbinddevice/v500";
    /**获取省市信息**/
    public static final String ProvinceCityAPI="api/user/getprovincecity/v500";
    /**帮助中心**/
    public static final String HelpCenterAPI="http://mapi.gfedu.cn/html/helpCenter.html";
    /**建议反馈**/
    public static final String FeedBackAPI="http://mapi.gfedu.cn/gfeducn/vp-apph5/feedBack-upload.html";
    /**建议反馈**/
    //http://mapi.gfedu.cn/html/feedback.html?client=android&version=1.0.1&userid=115227
    /**版本检测接口**/
    public static final String UpgradeAppAPI="http://mapi.gfedu.cn/api/plan/checkifupgradeapp/v500";
    /**查询是否绑定过设备**/
    public static final String CheckHasBindAPI="http://mapi.gfedu.cn/api/user/checkhasbind/v500";


    //获取试卷信息
    public static final String ExamPaperInfo="api/exam/getpaperinfo/v500";
    //提交答案（单题）
    public static final String SubmitPaperAPI="api/exam/submituserquesanswer/v500";
    //提交答案(答题卡全部)
    public static final String SubmitQuesanswerAPI="api/exam/submituserpaper/v500";
    //提获取用户试卷报告
    public static final String GetUserPaperReportAPI="api/exam/getuserpaperreport/v500";
    //错题解析
    public static final String GetUserPaperCardErrorAPI = "api/exam/getuserpapercarderrorlist/v500";


    /**二期接口***/
    /**APP首页接口**/
    public static final String GetProjectListAPI = "api/class/getprojectlist/v500";
    /**轮播图片接口**/
    public static final String GetAdsListAPI = "api/class/getadslist/v500";
    /**产品列表接口**/
    public static final String GetProductListAPI = "api/class/getproductlist/v500";
    /**套餐详情接口**/
    public static final String GetPackageDetailAPI = "api/class/getpackagedetail/v500";
    /**班级详情接口**/
    public static final String GetClassDetailAPI = "api/class/getclassdetail/v500";
    /**班级大纲接口**/
    public static final String GetClassOutLineAPI = "api/class/getclassoutline/v500";
    /**系列直播大纲接口**/
    public static final String GetLiveClassOutLineAPI = "api/class/getclassoutline/v501";
    /**课程详情收藏接口**/
    public static final String GetProductCollectionAPI = "api/class/productcollection/v500";
    /**客服H5地址**/
    public static final String ServerH5 = "http://looyuoms7623.looyu.com/chat/chat/p.do?c=20000653&f=10050794&g=10056807&refer=zjfbaidu&u=f0c5d80117937320b3a9ed0d734b3c1816&v=e3f1cb37f77f53b2b2ea5b03e123beeb21&command=&_d=1502417453270";
    /**课程详情页面常见问题**/
    public static final String GetEaluateAPI = "api/class/getealuatelist/v500";

    /*三期接口*/
    /**班级下版型**/
    public static final String GetClassClassTypeAPI = "api/class/getclassclasstypelist/v500";
    /**套餐下版型**/
    public static final String GetPackageClassTypeAPI = "api/class/getpackagecartclasstypelist/v500";
    /**加入购物车(班级)**/
    public static final String AddClassCartAPI = "api/order/addclasscart/v500";
    /**加入购物车(套餐)**/
    public static final String AddPackageCart = "api/order/addpackagecart/v500";
    /**收藏列表**/
    public static final String GetCollectionListAPI = "api/class/getcollectionlist/v500";
    /**师资评议**/
    public static final String GetClassScheduleAPI="api/class/getclassschedulelist/v500";
    /**师资评议问题列表**/
    public static final String GetRaiseQuestionAPI="api/class/getappraisequestion/v500";
    /**添加评议**/
    public static final String GetAddAppraiseAPI="api/class/addappraise/v500";
    /**保存讲师综合评价**/
    public static final String SetOverAllMerit="api/class/setoverallmerit/v500";
    /**推送通知**/
    public static final String GetPusAPI="api/notify/getpushandroid/v500";
    /**获取已读/未读通知**/
    public static final String GetNotifyByIsReadAPI="api/notify/getnotifybyisread/v500";
    /**通知设置为已读**/
    public static final String SetNotifyIsRead="api/notify/setnotifyisread/v500";
    /**直播详情页**/
    public static final String GetLiveDetailAPI="api/live/getlivedetail/v500";
    /**我的直播页**/
    public static final String GetMyLiveAPI="api/live/getmylive/v500";
    /**直播课程大纲**/
    public static final String GetClassLiveOutAPI="api/class/getclassliveoutline/v500";
    /**获取直播或点播H5地址**/
    public static final String GetLiveUrlAPI = "api/live/getliveurl/v500";
    /**直播预约**/
    public static final String AddLiveBookAPI = "api/live/addlivebook/v500";
    /**直播预约(系列)**/
    public static final String ClassLiveBookAPI = "api/live/classlivebook/v500";
    /**获取直播间**/
    public static final String GetLiveRequestAPI = "api/live/getliverequest/v500";




}
