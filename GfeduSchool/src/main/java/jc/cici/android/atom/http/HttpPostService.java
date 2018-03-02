package jc.cici.android.atom.http;

import jc.cici.android.atom.bean.AddressBean;
import jc.cici.android.atom.bean.AddressManagerBean;
import jc.cici.android.atom.bean.AdsInfo;
import jc.cici.android.atom.bean.Answer;
import jc.cici.android.atom.bean.BillBean;
import jc.cici.android.atom.bean.BindJCInfo;
import jc.cici.android.atom.bean.BindedInfo;
import jc.cici.android.atom.bean.BookBean;
import jc.cici.android.atom.bean.ClassInfoBean;
import jc.cici.android.atom.bean.CommentBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.ContentExamHomeBean;
import jc.cici.android.atom.bean.DeiverInfo;
import jc.cici.android.atom.bean.EnsureOrderBean;
import jc.cici.android.atom.bean.ErrorOrFavorBean;
import jc.cici.android.atom.bean.ErrorOrFavorTypeBean;
import jc.cici.android.atom.bean.EveryDayAndMothBean;
import jc.cici.android.atom.bean.ExamChoseBean;
import jc.cici.android.atom.bean.ExamChosePaper;
import jc.cici.android.atom.bean.ExamKnowledgeBean;
import jc.cici.android.atom.bean.Express;
import jc.cici.android.atom.bean.FaceCourseBean;
import jc.cici.android.atom.bean.HistoryExamChoseBean;
import jc.cici.android.atom.bean.HistoryInfo;
import jc.cici.android.atom.bean.KnowledgeQuesBean;
import jc.cici.android.atom.bean.LessInfo;
import jc.cici.android.atom.bean.LessonChildInfo;
import jc.cici.android.atom.bean.LessonInfo;
import jc.cici.android.atom.bean.LiveContent;
import jc.cici.android.atom.bean.LiveProduct;
import jc.cici.android.atom.bean.LiveProject;
import jc.cici.android.atom.bean.LiveSelectBean;
import jc.cici.android.atom.bean.MessageBean;
import jc.cici.android.atom.bean.MyAnswerBean;
import jc.cici.android.atom.bean.NextTaskBean;
import jc.cici.android.atom.bean.NoteBean;
import jc.cici.android.atom.bean.NoteDetailsBean;
import jc.cici.android.atom.bean.NoteOrQuesStatus;
import jc.cici.android.atom.bean.OrderBean;
import jc.cici.android.atom.bean.OrderDetailBean;
import jc.cici.android.atom.bean.PasswordInfo;
import jc.cici.android.atom.bean.PayInfo;
import jc.cici.android.atom.bean.PayOrderBean;
import jc.cici.android.atom.bean.ProductInfo;
import jc.cici.android.atom.bean.QRInageBean;
import jc.cici.android.atom.bean.QuesAnalysisBean;
import jc.cici.android.atom.bean.QuesDetailBean;
import jc.cici.android.atom.bean.Question;
import jc.cici.android.atom.bean.QuestionBean;
import jc.cici.android.atom.bean.QuestionInfoBean;
import jc.cici.android.atom.bean.Register;
import jc.cici.android.atom.bean.SelectBean;
import jc.cici.android.atom.bean.ShopcartBean;
import jc.cici.android.atom.bean.StageInfo;
import jc.cici.android.atom.bean.SubCourseBean;
import jc.cici.android.atom.bean.SubjectBean;
import jc.cici.android.atom.bean.TestBean;
import jc.cici.android.atom.bean.TestPaperBean;
import jc.cici.android.atom.bean.TikuHomeBean;
import jc.cici.android.atom.bean.UserInfo;
import jc.cici.android.atom.bean.ZhuiWenBean;
import jc.cici.android.atom.ui.tiku.SubmitQuesAnswer;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 网络请求方法接口
 * Created by atom on 2017/4/17.
 */

public interface HttpPostService {

    /**
     * 登录请求
     *
     * @param requestBody
     * @return
     */
    @POST("user/login/v500")
    Observable<UserInfo> getUserInfo(@Body RequestBody requestBody);

    /**
     * 绑定设备
     *
     * @param requestBody
     * @return
     */
    @POST("user/binddevice/v500")
    Observable<DeiverInfo> getDeiverInfo(@Body RequestBody requestBody);

    /**
     * 新用户注册
     *
     * @param requestBody
     * @return
     */
    @POST("user/register/v500")
    Observable<CommonBean<Register>> getRegisterInfo(@Body RequestBody requestBody);

//    /**
//     * 发送短信接口
//     *
//     * @param mobile
//     * @param md5
//     * @return
//     */
//    @GET("SendRegCheckMsg")
//    Observable<String> getSendMsgInfo(@Query("Mobile") String mobile, @Query("MD5_Code") String md5);

    /**
     * 发送短信接口
     *
     * @param requestBody5
     * @return
     */
    @POST("user/getregistercode/v500")
    Observable<CommonBean> getSendMsgInfo(@Body RequestBody requestBody5);

    /**
     * 找回密码短信码验证
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("SNameOrSTelephoneExistence")
    Observable<PasswordInfo> sendFindPwdMessage(@Query("Student") String student, @Query("MD5_Code") String md5);

    /**
     * 重置密码
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("ChangeUserPass")
    Observable<Integer> getResetPwdInfo(@Query("Student") String student, @Query("MD5_Code") String md5);

    /**
     * 快速登录短信发送
     *
     * @param requestBody
     * @return
     */
    @POST("user/getlogincode/v500")
    Observable<MessageBean> sendFastLoginMsg(@Body RequestBody requestBody);

    /**
     * 快速登录
     *
     * @param requestBody
     * @return
     */
    @POST("user/codelogin/v500")
    Observable<UserInfo> getFastLoginInfo(@Body RequestBody requestBody);

    /**
     * 检测第三方登录是否绑定设备号
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("CheckThirdAccountLogin")
    Observable<BindJCInfo> checkBindJCInfo(@Query("Student") String student, @Query("MD5code") String md5);

    /**
     * 第三方未绑定金程账号登录绑定
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("BindNewUserThirdAccount")
    Observable<BindJCInfo> getBindJCInfo(@Query("Student") String student, @Query("MD5code") String md5);

    /**
     * 第三方已绑定金程账号登录情况
     *
     * @param student
     * @param md5
     * @return
     */
    @GET("BindUserThirdAccount")
    Observable<BindedInfo> getBindedInfo(@Query("Student") String student, @Query("MD5code") String md5);

    /**
     * 获取班级列表信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getmyclasslist/v500")
    Observable<ClassInfoBean> getClassInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclassstagelist/v500")
    Observable<CommonBean<StageInfo>> getStageInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段课程信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasslessonlist/v500")
    Observable<LessInfo> getLessInfo(@Body RequestBody requestBody);

    /**
     * 获取签到信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/setclasslessonsignin/v500")
    Observable<CommonBean> getSignInfo(@Body RequestBody requestBody);

    /**
     * 获取阶段历史信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasshistorylessonlist/v500")
    Observable<CommonBean<HistoryInfo>> getHistoryLessonInfo(@Body RequestBody requestBody);

    /**
     * 课程详情页
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasslessondetail/v500")
    Observable<CommonBean<LessonInfo>> getLessonDetailInfo(@Body RequestBody requestBody);

    /**
     * 笔记列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/getnoteslist/v500")
    Observable<CommonBean<NoteBean>> getNotesListInfo(@Body RequestBody requestBody);

    /**
     * 科目列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getsubjectlist/v500")
    Observable<CommonBean<SubjectBean>> getSubjectListInfo(@Body RequestBody requestBody);

    /**
     * 添加笔记接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/addnotes/v500")
    Observable<CommonBean> getAddNotesInfo(@Body RequestBody requestBody);

    /**
     * 笔记详情接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/getnotesinfo/v500")
    Observable<CommonBean<NoteDetailsBean>> getNotesDetailsInfo(@Body RequestBody requestBody);

    /**
     * 删除笔记接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/delnotes/v500")
    Observable<CommonBean> getDelNotesInfo(@Body RequestBody requestBody);

    /**
     * 笔记点赞接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/addnotespraise/v500")
    Observable<CommonBean> getNotesPraiseInfo(@Body RequestBody requestBody);

    /**
     * 笔记公开或私人设置接口
     *
     * @param requestBody
     * @return
     */
    @POST("notes/settempval/v500")
    Observable<CommonBean> getTempValInfo(@Body RequestBody requestBody);

    /**
     * 获取我的问题(OR 大家的问题)
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getqueslist/v500")
    Observable<CommonBean<QuestionBean>> getQuesListInfo(@Body RequestBody requestBody);

    /**
     * 获取我的回答列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getanswerlist/v500")
    Observable<CommonBean<MyAnswerBean>> getAnswerListInfo(@Body RequestBody requestBody);

    /**
     * 添加问题接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/addclassquesinfo/v500")
    Observable<CommonBean<Question>> getAddClassQuesInfo(@Body RequestBody requestBody);

    /**
     * 添加我的回答接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/addclassquesinfo/v500")
    Observable<CommonBean<Answer>> getAddAnswerInfo(@Body RequestBody requestBody);

    /**
     * 问题详情接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getquesdetail/v500")
    Observable<CommonBean<QuesDetailBean>> getQuesDetailInfo(@Body RequestBody requestBody);

    /**
     * 答案点赞功能
     *
     * @param requestBody
     * @return
     */
    @POST("classques/adduserpraise/v500")
    Observable<CommonBean> addUserPraiseInfo(@Body RequestBody requestBody);

    /**
     * 答案设置为最佳
     *
     * @param requestBody
     * @return
     */
    @POST("classques/setclassquesstatus/v500")
    Observable<CommonBean> setClassQuesStatusInfo(@Body RequestBody requestBody);

    /**
     * 追问(追答,评论)列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getafterquesanswerlist/v500")
    Observable<CommonBean<ZhuiWenBean>> getAfterQuesAnswerListInfo(@Body RequestBody requestBody);

    /**
     * 评论列表接口
     *
     * @param requestBody
     * @return
     */
    @POST("classques/getafterquesanswerlist/v500")
    Observable<CommonBean<CommentBean>> getCommentListInfo(@Body RequestBody requestBody);

    /**
     * 笔记答疑开放权限
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclassstageservicestatus/v500")
    Observable<CommonBean<NoteOrQuesStatus>> getClassStageServiceStatus(@Body RequestBody requestBody);

    /**
     * 下一个任务
     *
     * @param requestBody
     * @return
     */
    @POST("class/downstudyinfo/v500")
    Observable<CommonBean<NextTaskBean>> getDownStudyInfo(@Body RequestBody requestBody);

    /**
     * 完善信息
     *
     * @param requestBody
     * @return
     */
    @POST("user/checkusercompleteinfo/v500")
    Observable<CommonBean> checkUserCompleteInfo(@Body RequestBody requestBody);

    /**
     * 获取课表课件内容
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasslessonchildinfo/v500")
    Observable<CommonBean<LessonChildInfo>> getClassLessonChildInfo(@Body RequestBody requestBody);

    /**
     * 获取轮播图列表信息
     *
     * @param requestBody
     * @return
     */
    @POST("class/getadslist/v500")
    Observable<CommonBean<AdsInfo>> getAdsListInfo(@Body RequestBody requestBody);

    /**
     * 获取产品列表
     *
     * @param requestBody
     * @return
     */
    @POST("class/index/v500")
    Observable<CommonBean<SubCourseBean>> getProductListInfo(@Body RequestBody requestBody);

    /**
     * 产品搜索列表
     *
     * @param requestBody
     * @return
     */
    @POST("class/getproductlist/v500")
    Observable<CommonBean<ProductInfo>> getSearchProductListInfo(@Body RequestBody requestBody);

    /**
     * 产品分类筛选
     *
     * @param requestBody
     * @return
     */
    @POST("class/getselectprojectlist/v500")
    Observable<CommonBean<SelectBean>> getSelectProjectListInfo(@Body RequestBody requestBody);

    /**
     * 订单列表
     *
     * @param requestBody
     * @return
     */
    @POST("order/orderlist/v500")
    Observable<CommonBean<OrderBean>> getOrderListInfo(@Body RequestBody requestBody);

    /**
     * 订单详情
     *
     * @param requestBody
     * @return
     */
    @POST("order/orderdetail/v500")
    Observable<CommonBean<OrderDetailBean>> getOrderDetailInfo(@Body RequestBody requestBody);

    /**
     * 取消订单
     *
     * @param requestBody
     * @return
     */
    @POST("order/cancelorder/v500")
    Observable<CommonBean> getCancelOrderInfo(@Body RequestBody requestBody);

    /**
     * 添加收货地址
     *
     * @param requestBody
     * @return
     */
    @POST("user/insertaddress/v500")
    Observable<CommonBean> getInsertAddressInfo(@Body RequestBody requestBody);

    /**
     * 管理收货地址
     *
     * @param requestBody
     * @return
     */
    @POST("user/getaddresslist/v500")
    Observable<CommonBean<AddressManagerBean>> getAddressListInfo(@Body RequestBody requestBody);

    /**
     * 设置默认管理地址
     *
     * @param requestBody
     * @return
     */
    @POST("user/setdefaultaddress/v500")
    Observable<CommonBean> setDefaultAddressInfo(@Body RequestBody requestBody);

    /**
     * 删除地址
     *
     * @param requestBody
     * @return
     */
    @POST("user/deleteaddress/v500")
    Observable<CommonBean> deleteAddressInfo(@Body RequestBody requestBody);

    /**
     * 修改地址
     *
     * @param requestBody
     * @return
     */
    @POST("user/updateaddress/v500")
    Observable<CommonBean> updateAddressInfo(@Body RequestBody requestBody);

    /**
     * 添加发票
     *
     * @param requestBody
     * @return
     */
    @POST("order/addorderinvoice/v500")
    Observable<CommonBean<BillBean>> addOrderInvoiceInfo(@Body RequestBody requestBody);

    /**
     * 发票内容
     *
     * @param requestBody
     * @return
     */
    @POST("order/getorderinvoice/v500")
    Observable<CommonBean<BillBean>> getOrderInvoiceInfo(@Body RequestBody requestBody);

    /**
     * 获取购物车信息
     *
     * @param requestBody
     * @return
     */
    @POST("order/getcartlist/v500")
    Observable<CommonBean<ShopcartBean>> getCartListInfo(@Body RequestBody requestBody);

    /**
     * 提交简答题答案
     *
     * @param requestBody
     * @return
     */
    @POST("api/exam/submituserquesanswer/v500")
    Observable<CommonBean<SubmitQuesAnswer>> getSubmitUserqQuesAnswer(@Body RequestBody requestBody);

    /**
     * 移除购物车
     *
     * @param requestBody
     * @return
     */
    @POST("order/removecart/v500")
    Observable<CommonBean> getRemoveCartInfo(@Body RequestBody requestBody);

    /**
     * 获取默认地址
     *
     * @param requestBody
     * @return
     */
    @POST("user/getaddresssingle/v500")
    Observable<CommonBean<AddressBean>> getAddressSingleInfo(@Body RequestBody requestBody);

    /**
     * 确认订单
     *
     * @param requestBody
     * @return
     */
    @POST("order/createorder/v500")
    Observable<CommonBean<EnsureOrderBean>> getCreateOrderInfo(@Body RequestBody requestBody);

    /**
     * 获取支付订单
     *
     * @param requestBody
     * @return
     */
    @POST("order/getpayorder/v500")
    Observable<CommonBean<PayOrderBean>> getPayOrderInfo(@Body RequestBody requestBody);

    /**
     * 支付宝接口请求
     *
     * @param requestBody
     * @return
     */
    @POST("order/applyaplipay/v500")
    Observable<CommonBean<PayInfo>> getApplyAliPayInfo(@Body RequestBody requestBody);

    /**
     * 微信接口请求
     *
     * @param requestBody
     * @return
     */
    @POST("order/applywechatpay/v500")
    Observable<CommonBean<PayInfo>> getApplyWeChatPayInfo(@Body RequestBody requestBody);

    /**
     * 物流信息
     *
     * @param requestBody
     * @return
     */
    @POST("order/getorderexpress/v500")
    Observable<CommonBean<Express>> getOrderExpressInfo(@Body RequestBody requestBody);

    /**
     * 面授列表
     *
     * @param requestBody
     * @return
     */
    @POST("class/getclasslessonlist/v501")
    Observable<CommonBean<FaceCourseBean>> getClassLessListInfo(@Body RequestBody requestBody);

    /**
     * 直播项目列表
     *
     * @param requestBody
     * @return
     */
    @POST("live/getliveprojectlist/v500")
    Observable<CommonBean<LiveProject>> getLiveProjectListInfo(@Body RequestBody requestBody);

    /**
     * 直播首页
     *
     * @param requestBody
     * @return
     */
    @POST("live/getliveindex/v500")
    Observable<CommonBean<LiveContent>> getLiveIndexInfo(@Body RequestBody requestBody);

    /**
     * 直播立即预约
     *
     * @param requestBody
     * @return
     */
    @POST("live/addlivebook/v500")
    Observable<CommonBean<BookBean>> addLiveBookInfo(@Body RequestBody requestBody);

    /**
     * 直播列表
     *
     * @param requestBody
     * @return
     */
    @POST("live/getlivelist/v500")
    Observable<CommonBean<LiveProduct>> getLiveListInfo(@Body RequestBody requestBody);

    /**
     * 直播筛选列表
     *
     * @param requestBody
     * @return
     */
    @POST("live/getselectprojectlist/v500")
    Observable<CommonBean<LiveSelectBean>> getSelectProListInfo(@Body RequestBody requestBody);

    /**
     * 题库项目列表
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getselectprojectlist/v500")
    Observable<CommonBean<TikuHomeBean>> getExamListInfo(@Body RequestBody requestBody);

    /**
     * 题库项目列表
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getexamindex/v500")
    Observable<CommonBean<ContentExamHomeBean>> getExamIndexInfo(@Body RequestBody requestBody);


    /**
     * 智能组卷信息
     *
     * @param requestBody
     * @return
     */
    @POST("exam/examchose/v500")
    Observable<CommonBean<ExamChoseBean>> getExamChoseInfo(@Body RequestBody requestBody);

    /**
     * 智能组卷生成试卷信息
     *
     * @param requestBody
     * @return
     */
    @POST("exam/setexamchosetpapger/v500")
    Observable<CommonBean<ExamChosePaper>> setExamChosePagerInfo(@Body RequestBody requestBody);

    /**
     * 智能组卷组卷历史信息
     *
     * @param requestBody
     * @return
     */
    @POST("exam/examchosehistory/v500")
    Observable<CommonBean<HistoryExamChoseBean>> examChoseHistoryInfo(@Body RequestBody requestBody);

    /**
     * 知识点做题信息
     *
     * @param requestBody
     * @return
     */
    @POST("exam/examknowledge/v500")
    Observable<CommonBean<ExamKnowledgeBean>> examKnowledgeInfo(@Body RequestBody requestBody);

    /**
     * 错题集or收藏类型
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getmyquestypelist/v500")
    Observable<CommonBean<ErrorOrFavorTypeBean>> getMyQuestTypeListInfo(@Body RequestBody requestBody);

    /**
     * 错题集or收藏内容
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getmyqueslist/v500")
    Observable<CommonBean<ErrorOrFavorBean>> getMyQuestListInfo(@Body RequestBody requestBody);

    /**
     * 获取知识点试题信息
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getknowledgeques/v500")
    Observable<CommonBean<KnowledgeQuesBean>> getKnowledgeQuestInfo(@Body RequestBody requestBody);

    /**
     * 获取试题信息
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getquesinfo/v500")
    Observable<CommonBean<QuestionInfoBean>> getQuestInfo(@Body RequestBody requestBody);

    /**
     * 添加试题收藏
     *
     * @param requestBody
     * @return
     */
    @POST("exam/collectques/v500")
    Observable<CommonBean> getCollQuestInfo(@Body RequestBody requestBody);

    /**
     * 取消试题收藏
     *
     * @param requestBody
     * @return
     */
    @POST("exam/cancelcollectques/v500")
    Observable<CommonBean> cancelCollQuestInfo(@Body RequestBody requestBody);

    /**
     * 提交试卷
     *
     * @param requestBody
     * @return
     */
    @POST("exam/submittpaper/v500")
    Observable<CommonBean> submitPaperInfo(@Body RequestBody requestBody);

    /**
     * 获取做题记录
     *
     * @param requestBody
     * @return
     */
    @POST("exam/gettestpaperlist/v500")
    Observable<CommonBean<TestPaperBean>> getTestPaperListInfo(@Body RequestBody requestBody);

    /**
     * 每日一题or每月一测
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getquestion/v500")
    Observable<CommonBean<EveryDayAndMothBean>> getQuesInfo(@Body RequestBody requestBody);

    /**
     * 每日一题解析
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getquesanalysisinfo/v500")
    Observable<CommonBean<QuesAnalysisBean>> getQuesAnalysisInfo(@Body RequestBody requestBody);

    /**
     * 生成试卷
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getpaperques/v500")
    Observable<CommonBean<TestBean>> getPaperquesInfo(@Body RequestBody requestBody);


    /**
     * 获取二维码图片
     *
     * @param requestBody
     * @return
     */
    @POST("exam/getprojectqrcode/v500")
    Observable<CommonBean<QRInageBean>> getProjectQrcodeInfo(@Body RequestBody requestBody);

}
