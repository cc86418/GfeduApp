package cn.jun.utils;


import cn.jun.bean.AddVodRecordBean;
import cn.jun.bean.AddapPraiseBean;
import cn.jun.bean.ClassScheduleBean;
import cn.jun.bean.LiveRoomBean;
import cn.jun.bean.MyAssessment;
import cn.jun.bean.PackageClassTypeBean;
import cn.jun.bean.ProductListBean;
import cn.jun.bean.ProjectListBean;
import cn.jun.bean.ProjectList_NoAllBean;
import cn.jun.bean.VodRoomBean;
import cn.jun.bean.WeakCourseWare;
import cn.jun.polyv.VideoTimeLog;
import jc.cici.android.atom.bean.CommonBean;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface HttpPostServer {
    /**获取直播间**/
    @POST("live/getliverequest/v500")
    Observable<CommonBean<LiveRoomBean>> getLiveRoom(@Body RequestBody requestBody);

    /**获取点播**/
    @POST("live/getvodrequest/v500")
    Observable<CommonBean<VodRoomBean>> getVodRoom(@Body RequestBody requestBody);

    @POST("class/getproductlist/v500")
    Observable<CommonBean<ProductListBean>> getProductList(@Body RequestBody requestBody);

    @POST("class/getprojectlist/v500")
    Observable<CommonBean<ProjectListBean>> getProductListAll(@Body RequestBody requestBody);

    @POST("class/getproductlist/v500")
    Observable<CommonBean<ProjectList_NoAllBean>> getProductListNoAll(@Body RequestBody requestBody);

    /**获取师资评议**/
    @POST("class/getclassschedulelist/v500")
    Observable<CommonBean<ClassScheduleBean>> getClassScheduleBean(@Body RequestBody requestBody);

    /**提交师资评议**/
    @POST("class/addappraise/v500")
    Observable<CommonBean<AddapPraiseBean>> getAddapPraiseBean(@Body RequestBody requestBody);

    /**添加点播记录**/
    @POST("live/addvodrecord/v500")
    Observable<CommonBean<AddVodRecordBean>> getAddVodRecord(@Body RequestBody requestBody);

    @POST("class/getpackagecartclasstypelist/v500")
    Observable<CommonBean<PackageClassTypeBean>> getPackageClassType(@Body RequestBody requestBody);

//    @POST("class/getpackagecartclasstypelist/v500")
//    Observable<CommonBean<PackageClassTypeBean>> getPackageClassType(@Body RequestBody requestBody);

    /**能力评估**/
    @POST("exam/myassessment/v500")
    Observable<CommonBean<MyAssessment>> getMyAssessment(@Body RequestBody requestBody);

    /**薄弱知识点**/
    @POST("exam/weakcourseware/v500")
    Observable<CommonBean<WeakCourseWare>> getWeakCourseWare(@Body RequestBody requestBody);

    /**本地学习进度**/
    @POST("class/addvideostudylog/v501")
    Observable<CommonBean<VideoTimeLog>> getVideoTimeLog(@Body RequestBody requestBody);

    /**在线学习进度**/
    @POST("class/addvideostudylog/v500")
    Observable<CommonBean<VideoTimeLog>> getVideoTimeLog_live(@Body RequestBody requestBody);
}
