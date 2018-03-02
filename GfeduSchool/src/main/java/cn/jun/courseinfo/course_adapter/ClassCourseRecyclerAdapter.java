package cn.jun.courseinfo.course_adapter;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.bean.ClassClassTypeBean;
import cn.jun.courseinfo.bean.AddressBean;
import cn.jun.courseinfo.bean.ExameDateBean;
import cn.jun.courseinfo.bean.TypeBean;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.view.MyGridLayoutManager;


public class ClassCourseRecyclerAdapter extends BaseRecycleerAdapter<ClassClassTypeBean, ClassCourseRecyclerAdapter.MyHolder> {
    private Context mCtx;
    private ArrayList<ClassClassTypeBean> mData;
    private ArrayList<String> ClassTypeTypeList;
    private ArrayList<String> ExameDateList;
    private ArrayList<String> ShoolPlaceList;
    private ExameDateBean exameDateBean;
    private ArrayList<ExameDateBean> examList = new ArrayList<>();
    private ExameDateRecyclerAdapter exameAdapter;
    private TypeBean typeBean;
    private ArrayList<TypeBean> typeList = new ArrayList<>();
    private TypeRecyclerAdapter typeAdapter;
    private AddressBean addressBean;
    private ArrayList<AddressBean> addressList = new ArrayList<>();
    private AddressRecyclerAdapter addressAdapter;

    //Message传递过来的
    private ArrayList<ExameDateBean> mExameDate;
    private ArrayList<TypeBean> mTypeDate;
    private ArrayList<AddressBean> mAddressDate;

//    private Map<String, ArrayList<ExameDateBean>> mExameDate;
//    private Map<Integer,ArrayList<TypeBean>> mTypeDate;
//    private Map<Integer,ArrayList<AddressBean>> mAddressDate;

    public static boolean msgExameClick = false;
    public static boolean msgTypeClick = false;
    public static boolean msgAddressClick = false;


    private String ClickDate;
    public static String ClickType;
    private String ClickAddress;

    private Handler returnId;
    private int classtypeid;
    private MyHolder myHolder;

    public ClassCourseRecyclerAdapter(Context context, List items, Handler returnId) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<ClassClassTypeBean>) items;
        this.returnId = returnId;

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    msgExameDate = (ArrayList<ExameDateBean>) msg.obj;
                    break;
                case 1: //考试年月选择
                    HandleExameDate(msg);
                    break;
                case 2: //课程类型选择
                    HandleClassType(msg);
                    break;
                case 3: //上课地点选择
                    HandleAddressDate(msg);
                    break;
            }
        }
    };

    /**
     * 考试年月处理
     **/
    public void HandleExameDate(Message msg) {
        mExameDate = (ArrayList<ExameDateBean>) msg.obj;
        for (int i = 0; i < mExameDate.size(); i++) {
            if (mExameDate.get(i).isTypeSelect()) {
                //点击的日期
                String clickDate = mExameDate.get(i).getTypeName();
//                Log.i("clickDate == ", "" + clickDate);
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    //大数据中的日期
                    String mDataExamDateName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
//                    Log.i("大数据中 ", "" + mDataExamDateName);
                    if (clickDate.equals(mDataExamDateName)) {
                        //对比之后大数据中的课程类型
                        String mDataTypeName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                        //对比之后大数据中的上课地点
                        String mDataPlaceName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();
//                        Log.i("对比之后类型 == ", "" + mDataTypeName);
//                        Log.i("对比之后地点 == ", "" + mDataPlaceName);
                        /**课程类型**/
                        ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
                        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                            int ClassTypeSize = ClassTypeTypeList.size();
//                            if (ClassTypeSize > 1) {
//                                for (int k = 0; k < ClassTypeSize; k++) {
//                                    if (mDataTypeName.equals(ClassTypeTypeList.get(k))) {
//                                        typeList.get(k).setChecked(true);
//                                    }
//                                    else {
//                                        typeList.get(k).setChecked(false);
//                                    }
//                                }
//                            }
                            for (int k = 0; k < ClassTypeSize; k++) {
                                if (mDataTypeName.equals(ClassTypeTypeList.get(k))) {
                                    typeList.get(k).setChecked(true);
                                }
//                                    else {
//                                        typeList.get(k).setChecked(false);
//                                    }
                            }

                            if (null != typeAdapter) {
                                typeAdapter.notifyDataSetChanged();
                            }
                        }
                        /**考试地点**/
                        ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
                        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                            int ShoolSize = ShoolPlaceList.size();
                            for (int a = 0; a < ShoolSize; a++) {
                                if (mDataPlaceName.equals(ShoolPlaceList.get(a))) {
                                    addressList.get(a).setChecked(true);
                                } else {
                                    addressList.get(a).setChecked(false);
                                }
                            }
                            if (null != addressAdapter) {
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
//                        Log.i("点击的和大数据中的不同 -- ", "点击的和大数据中的不同 -- ");
                    }
                }
            } else {
//                Log.i("日期取消选择 -- > ", "日期取消选择");

            }
        }
        for (int j = 0; j < mExameDate.size(); j++) {
            if (mExameDate.get(j).isTypeSelect()) {
                msgExameClick = true;
                break;
            }
        }
        Log.i("日期操作完成之后", "" + msgExameClick);
        if (msgExameClick) {
            for (int i = 0; i < examList.size(); i++) {
                if (examList.get(i).isTypeSelect()) {
                    ClickDate = examList.get(i).getTypeName();
                }
            }
        }

        if (msgExameClick && msgTypeClick) {
            if ("在线".equals(ClickType)) {
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                    String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                    if (date.equals(ClickDate) && type.equals(ClickType)) {
                        classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                        String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                        Message return_msg = new Message();
                        return_msg.what = 0;
                        return_msg.obj = classtypeid;
                        returnId.sendMessage(return_msg);

                        /**选中在线更改价格**/
                        Message price_msg = new Message();
                        price_msg.what = 1;
                        price_msg.obj = price;
                        returnId.sendMessage(price_msg);


                        /**选中的课程时长**/
                        String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(j).getExpireDate();
                        ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);
                    }
                }

            } else if ("直播".equals(ClickType)) {
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                    String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                    if (date.equals(ClickDate) && type.equals(ClickType)) {
                        classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                        String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                        Message return_msg = new Message();
                        return_msg.what = 0;
                        return_msg.obj = classtypeid;
                        returnId.sendMessage(return_msg);

                        /**选中在线更改价格**/
                        Message price_msg = new Message();
                        price_msg.what = 1;
                        price_msg.obj = price;
                        returnId.sendMessage(price_msg);


                        /**选中的课程时长**/
                        String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(j).getExpireDate();
                        ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);
                    }
                }
            } else {//
                if (msgAddressClick) {
                    for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                        String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                        String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                        String address = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();

                        if (date.equals(ClickDate) && type.equals(ClickType) && address.equals(ClickAddress)) {
                            classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                            String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                            Message return_msg = new Message();
                            return_msg.what = 0;
                            return_msg.obj = classtypeid;
                            returnId.sendMessage(return_msg);

                            /**选中在线更改价格**/
                            Message price_msg = new Message();
                            price_msg.what = 1;
                            price_msg.obj = price;
                            returnId.sendMessage(price_msg);


                            /**选中的课程时长**/
                            String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(j).getExpireDate();
                            ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);
                        }
                    }
                }
            }
        }


        //如果三个选项都没有选中的状态,则恢复重置按钮
        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
            /**考试年月**/
            ExameDateList = mData.get(0).getBody().getExameDateList();
            if (ExameDateList != null && !"".equals(ExameDateList)) {
                int ExameSize = ExameDateList.size();
                for (int k = 0; k < ExameSize; k++) {
                    examList.get(k).setChecked(true);
                }
                if (null != exameAdapter) {
                    exameAdapter.notifyDataSetChanged();
                }
            }
            /**课程类型**/
            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                int ClassTypeSize = ClassTypeTypeList.size();
                for (int k = 0; k < ClassTypeSize; k++) {
                    typeList.get(k).setChecked(true);
                }
                if (null != typeAdapter) {
                    typeAdapter.notifyDataSetChanged();
                }
            }
            /**考试地点**/
            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                int ShoolSize = ShoolPlaceList.size();
                for (int a = 0; a < ShoolSize; a++) {
                    addressList.get(a).setChecked(true);
                }
                if (null != addressAdapter) {
                    addressAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    /**
     * 课程类型处理
     **/
    public void HandleClassType(Message msg) {
        mTypeDate = (ArrayList<TypeBean>) msg.obj;
        Gson s = new Gson();
        Log.i("类型是否有选中 -- > ", "" + s.toJson(mTypeDate).toString());
        for (int i = 0; i < mTypeDate.size(); i++) {
            if (mTypeDate.get(i).isTypeSelect()) {
                //点击的类型
                String clickType = mTypeDate.get(i).getTypeName();
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    //大数据中的类型
                    String mTypeName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                    Log.i("大数据中 mTypeName == > ", "" + mTypeName);

                    if (clickType.equals(mTypeName)) {
                        //对比之后大数据中的考试时间
                        String mExamDateName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                        //对比之后大数据中的上课地点
                        String mDataPlaceName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();
                        /**考试年月**/
                        ExameDateList = mData.get(0).getBody().getExameDateList();
                        if (ExameDateList != null && !"".equals(ExameDateList)) {
                            Log.i("考试年月== > ", " === 考试年月");
                            int ExameDateSize = ExameDateList.size();
                            for (int a = 0; a < ExameDateSize; a++) {
                                if (mExamDateName.equals(ExameDateList.get(a))) {
                                    examList.get(a).setChecked(true);
                                }
//                                else {
//                                    examList.get(a).setChecked(false);
//                                }
                            }
                            if (null != exameAdapter) {
                                exameAdapter.notifyDataSetChanged();
                            }
                        }

                        /**考试地点**/
                        ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
                        if ("在线".equals(clickType)) {
                            if (!"".equals(ShoolPlaceList) && null !=ShoolPlaceList) {
                                int ShoolSize = ShoolPlaceList.size();
                                for (int a = 0; a < ShoolSize; a++) {
                                    addressList.get(a).setChecked(false);
                                }
                                if (null != addressAdapter) {
                                    addressAdapter.notifyDataSetChanged();
                                }
                            }

                        } else if ("面授".equals(clickType)) {
                            int ShoolSize = ShoolPlaceList.size();
                            for (int a = 0; a < ShoolSize; a++) {
                                addressList.get(a).setChecked(true);
                            }
                            if (null != addressAdapter) {
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
//                            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
//                                int ShoolSize = ShoolPlaceList.size();
//                                if (ShoolSize > 1) {
//                                    for (int a = 0; a < ShoolSize; a++) {
//                                        if (mDataPlaceName.equals(ShoolPlaceList.get(a))) {
////                                        addressList.get(a).setChecked(true);
//                                        }
////                                    else {
////                                        addressList.get(a).setChecked(false);
////                                    }
//                                    }
//                                    if (null != addressAdapter) {
//                                        addressAdapter.notifyDataSetChanged();
//                                    }
//                                } else {
//                                    for (int a = 0; a < ShoolSize; a++) {
//                                        if (mDataPlaceName.equals(ShoolPlaceList.get(a))) {
//                                            addressList.get(a).setChecked(true);
//                                        } else {
//                                            addressList.get(a).setChecked(false);
//                                        }
//                                    }
//                                    if (null != addressAdapter) {
//                                        addressAdapter.notifyDataSetChanged();
//                                    }
//                                }
//
//                            }
                    }
                }
            }
        }
        for (int j = 0; j < mTypeDate.size(); j++) {
            if (mTypeDate.get(j).isTypeSelect()) {
                msgTypeClick = true;
                break;
            }
        }
        Log.i("类型操作完成之后", "" + msgTypeClick);
        if (msgTypeClick) {
            for (int i = 0; i < typeList.size(); i++) {
                if (typeList.get(i).isTypeSelect()) {
                    ClickType = typeList.get(i).getTypeName();

                    Log.i("ClickType == ", "" + ClickType);
                }
            }
        }

        if (msgExameClick && msgTypeClick) {
            if ("在线".equals(ClickType)) {
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                    String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                    if (date.equals(ClickDate) && type.equals(ClickType)) {
                        classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                        Message return_msg = new Message();
                        return_msg.what = 0;
                        return_msg.obj = classtypeid;
                        returnId.sendMessage(return_msg);

                        /**选中在线更改价格**/
                        String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                        Message price_msg = new Message();
                        price_msg.what = 1;
                        price_msg.obj = price;
                        returnId.sendMessage(price_msg);


                        /**选中的课程时长**/
                        String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(j).getExpireDate();
                        ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);

                    }
                }
            } else if ("直播".equals(ClickType)) {
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                    String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                    if (date.equals(ClickDate) && type.equals(ClickType)) {
                        classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                        String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                        Message return_msg = new Message();
                        return_msg.what = 0;
                        return_msg.obj = classtypeid;
                        returnId.sendMessage(return_msg);

                        /**选中在线更改价格**/
                        Message price_msg = new Message();
                        price_msg.what = 1;
                        price_msg.obj = price;
                        returnId.sendMessage(price_msg);


                        /**选中的课程时长**/
                        String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(j).getExpireDate();
                        ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);
                    }
                }
            } else {//
                if (msgAddressClick) {
                    for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                        String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                        String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                        String address = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();
                        if (date.equals(ClickDate) && type.equals(ClickType) && address.equals(ClickAddress)) {
                            classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                            Message return_msg = new Message();
                            return_msg.what = 0;
                            return_msg.obj = classtypeid;
                            returnId.sendMessage(return_msg);

                            /**选中在线更改价格**/
                            String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                            Message price_msg = new Message();
                            price_msg.what = 1;
                            price_msg.obj = price;
                            returnId.sendMessage(price_msg);

                            /**选中的课程时长**/
                            String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(j).getExpireDate();
                            ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);
                        }
                    }
                }
            }
        }

        //如果三个选项都没有选中的状态,则恢复重置按钮
        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
            /**考试年月**/
            ExameDateList = mData.get(0).getBody().getExameDateList();
            if (ExameDateList != null && !"".equals(ExameDateList)) {
                int ExameSize = ExameDateList.size();
                for (int k = 0; k < ExameSize; k++) {
                    examList.get(k).setChecked(true);
                }
                if (null != exameAdapter) {
                    exameAdapter.notifyDataSetChanged();
                }
            }
            /**课程类型**/
            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                int ClassTypeSize = ClassTypeTypeList.size();
                for (int k = 0; k < ClassTypeSize; k++) {
                    typeList.get(k).setChecked(true);
                }
                if (null != typeAdapter) {
                    typeAdapter.notifyDataSetChanged();
                }

            }
            /**考试地点**/
            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                int ShoolSize = ShoolPlaceList.size();
                for (int a = 0; a < ShoolSize; a++) {
                    addressList.get(a).setChecked(true);
                }
                if (null != addressAdapter) {
                    addressAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 上课地点处理
     **/

    public void HandleAddressDate(Message msg) {
        mAddressDate = (ArrayList<AddressBean>) msg.obj;
        Gson s = new Gson();
        Log.i("是否有选中 -- > ", "" + s.toJson(mAddressDate).toString());
        for (int i = 0; i < mAddressDate.size(); i++) {
            if (mAddressDate.get(i).isTypeSelect()) {
                Log.i("上课地点选中了 -- > ", "" + mAddressDate.get(i).getTypeName());
                //点击的地点
                String clickAddress = mAddressDate.get(i).getTypeName();
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    //大数据中的地点
                    String mAddressName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();
                    if (clickAddress.equals(mAddressName)) {
                        //对比之后大数据中的考试时间
                        String mExamDateName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                        //对比之后大数据中的课程类型
                        String mTypeName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                        Log.i("mTypeName -- ", "" + mTypeName);
                        /**考试年月**/
                        ExameDateList = mData.get(0).getBody().getExameDateList();
                        if (ExameDateList != null && !"".equals(ExameDateList)) {
                            int ExameDateSize = ExameDateList.size();

                            if (ExameDateSize > 1) {
                                for (int a = 0; a < ExameDateSize; a++) {
                                    if (mExamDateName.equals(ExameDateList.get(a))) {
                                        examList.get(a).setChecked(true);
                                    }
//                                    else {
//                                        examList.get(a).setChecked(false);
//                                    }
                                }
                                if (null != exameAdapter) {
                                    exameAdapter.notifyDataSetChanged();
                                }
                            } else {
                                for (int a = 0; a < ExameDateSize; a++) {
                                    if (mExamDateName.equals(ExameDateList.get(a))) {
                                        examList.get(a).setChecked(true);
                                    } else {
                                        examList.get(a).setChecked(false);
                                    }
                                }
                                if (null != exameAdapter) {
                                    exameAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                        /**课程类型**/
                        ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
                        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                            int ClassTypeSize = ClassTypeTypeList.size();
                            for (int k = 0; k < ClassTypeSize; k++) {
                                if (mTypeName.equals(ClassTypeTypeList.get(k))) {
                                    typeList.get(k).setChecked(true);
                                } else {
                                    typeList.get(k).setChecked(false);
                                }
                            }
                            if (null != typeAdapter) {
                                typeAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
//                        Log.i("点击的和大数据中的不同 -- ", "点击的和大数据中的不同 -- ");
                    }
                }
            } else {
//                Log.i("上课地点取消选择 -- > ", "" + mAddressDate.get(i).getTypeName());
            }
        }
        for (int j = 0; j < mAddressDate.size(); j++) {
            if (mAddressDate.get(j).isTypeSelect()) {
                msgAddressClick = true;
                break;
            }
        }
        Log.i("上课地点操作完成之后Address", "" + msgAddressClick);
        if (msgAddressClick) {
            for (int i = 0; i < addressList.size(); i++) {
                if (addressList.get(i).isTypeSelect()) {
                    ClickAddress = addressList.get(i).getTypeName();
                }
            }
        }

        if (msgExameClick && msgTypeClick) {
            if ("在线".equals(ClickType)) {
                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                    String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                    String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                    if (date.equals(ClickDate) && type.equals(ClickType)) {
                        classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                        Message return_msg = new Message();
                        return_msg.what = 0;
                        return_msg.obj = classtypeid;
                        returnId.sendMessage(return_msg);

                        /**选中在线更改价格**/
                        String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                        Message price_msg = new Message();
                        price_msg.what = 1;
                        price_msg.obj = price;
                        returnId.sendMessage(price_msg);

                        /**选中的课程时长**/
                        String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(0).getExpireDate();
                        ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);

                    }
                }
            } else {//
                if (msgAddressClick) {
                    for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
                        String date = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
                        String type = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
                        String address = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();

                        if (date.equals(ClickDate) && type.equals(ClickType) && address.equals(ClickAddress)) {
                            classtypeid = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PKID();
                            Message return_msg = new Message();
                            return_msg.what = 0;
                            return_msg.obj = classtypeid;
                            returnId.sendMessage(return_msg);

                            /**选中在线更改价格**/
                            String price = mData.get(0).getBody().getClassTypeList().get(j).getClassType_SalePrice();
                            Message price_msg = new Message();
                            price_msg.what = 1;
                            price_msg.obj = price;
                            returnId.sendMessage(price_msg);

                            /**选中的课程时长**/
                            String ClassType_Date = mData.get(0).getBody().getClassTypeList().get(j).getExpireDate();
                            ((TextView) myHolder.items_kssc_tv.findViewWithTag(1)).setText(ClassType_Date);
                        }
                    }
                }
            }
        }

        //如果三个选项都没有选中的状态,则恢复重置按钮
        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
            /**考试年月**/
            ExameDateList = mData.get(0).getBody().getExameDateList();
            if (ExameDateList != null && !"".equals(ExameDateList)) {
                int ExameSize = ExameDateList.size();
                for (int k = 0; k < ExameSize; k++) {
                    examList.get(k).setChecked(true);
                }
                if (null != exameAdapter) {
                    exameAdapter.notifyDataSetChanged();
                }
            }
            /**课程类型**/
            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                int ClassTypeSize = ClassTypeTypeList.size();
                for (int k = 0; k < ClassTypeSize; k++) {
                    typeList.get(k).setChecked(true);
                }
                if (null != typeAdapter) {
                    typeAdapter.notifyDataSetChanged();
                }
            }
            /**考试地点**/
            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                int ShoolSize = ShoolPlaceList.size();
                for (int a = 0; a < ShoolSize; a++) {
                    addressList.get(a).setChecked(true);
                }
                if (null != addressAdapter) {
                    addressAdapter.notifyDataSetChanged();
                }
            }
        }
    }


//    private void initSelect() {
//        for (int i = 0; i < mData.size(); i++) {
//            if (mData.get(i).isTypeSelect()) {
//                mSelectedPos = i;
//            }
//        }
//    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, ClassClassTypeBean item, final int position) {
        Log.i("父类", "onBindViewHolder");
        /**考试年月**/
        ExameDateList = mData.get(0).getBody().getExameDateList();
        if (ExameDateList != null && !"".equals(ExameDateList)) {
            int ExameSize = ExameDateList.size();
            for (int i = 0; i < ExameSize; i++) {
                exameDateBean = new ExameDateBean();
                exameDateBean.setTypeName(ExameDateList.get(i));
                examList.add(exameDateBean);
            }
        }
        holder.featureRecyclerView.setLayoutManager(new MyGridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        holder.featureRecyclerView.setNestedScrollingEnabled(false);
        exameAdapter = new ExameDateRecyclerAdapter(context, examList, handler, 0);
        holder.featureRecyclerView.setAdapter(exameAdapter);

        /**课程类型**/
        ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
            int ClassTypeSize = ClassTypeTypeList.size();
            for (int i = 0; i < ClassTypeSize; i++) {
                typeBean = new TypeBean();
                typeBean.setTypeName(ClassTypeTypeList.get(i));
                typeBean.setTypeSelect(false);
                typeList.add(typeBean);
            }
        }
        holder.typeRecyclerView.setLayoutManager(new MyGridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        holder.typeRecyclerView.setNestedScrollingEnabled(false);
        typeAdapter = new TypeRecyclerAdapter(context, typeList, handler, 0);
        holder.typeRecyclerView.setAdapter(typeAdapter);

        /**考试地点**/
        ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
            int ShoolSize = ShoolPlaceList.size();
            for (int i = 0; i < ShoolSize; i++) {
//            for (int i = 0; i < 5; i++) {
                addressBean = new AddressBean();
                addressBean.setTypeName(ShoolPlaceList.get(i));
//                addressBean.setTypeName("ShoolPlace " + i);
                addressList.add(addressBean);
            }
        }
        holder.addressRecyclerView.setLayoutManager(new MyGridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
        holder.addressRecyclerView.setNestedScrollingEnabled(false);
        addressAdapter = new AddressRecyclerAdapter(context, addressList, handler, 0);
        holder.addressRecyclerView.setAdapter(addressAdapter);

        /**课程时长**/
        holder.items_kssc_tv.setText(mData.get(0).getBody().getClassTypeList().get(position).getExpireDate() + "");
        holder.items_kssc_tv.setTag(1);
        myHolder = holder;
    }

    @Override
    public int getLayoutId() {
        return R.layout.class_list_items;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        // 年月
        @BindView(R.id.featureRecyclerView)
        RecyclerView featureRecyclerView;
        // 类型
        @BindView(R.id.typeRecyclerView)
        RecyclerView typeRecyclerView;
        // 地点
        @BindView(R.id.addressRecyclerView)
        RecyclerView addressRecyclerView;
        //课程时长
        @BindView(R.id.items_kssc_tv)
        TextView items_kssc_tv;


        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
