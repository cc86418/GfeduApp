//package cn.jun.courseinfo.course_adapter;
//
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.jun.bean.PackageClassTypeBean;
//import cn.jun.courseinfo.bean.AddressBean;
//import cn.jun.courseinfo.bean.ExameDateBean;
//import cn.jun.courseinfo.bean.TeachBean;
//import cn.jun.courseinfo.bean.TypeBean;
//import cn.jun.courseinfo.j_course_adapter.J_AddressRecyclerAdapter;
//import cn.jun.courseinfo.j_course_adapter.J_ExameDateRecyclerAdapter;
//import cn.jun.courseinfo.j_course_adapter.J_TeachRecyclerAdapter;
//import cn.jun.courseinfo.j_course_adapter.J_TypeRecyclerAdapter;
//import jc.cici.android.R;
//import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
//import jc.cici.android.atom.view.MyGridLayoutManager;
//
//import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgTypeClick;
//
//
//public class MealCourseAdapter extends BaseRecycleerAdapter<PackageClassTypeBean.Body.ProductList, MealCourseAdapter.MyHolder> {
//    private Context mCtx;
//    private ArrayList<PackageClassTypeBean.Body.ProductList> mData;
//    private ArrayList<String> TeachTypeList;
//    private ArrayList<String> ClassTypeTypeList;
//    private ArrayList<String> ExameDateList;
//    private ArrayList<String> ShoolPlaceList;
//
//    private TeachBean teachBean;
//    private ArrayList<TeachBean> teachList;
//    private J_TeachRecyclerAdapter teachAdapter;
//    private ExameDateBean exameDateBean;
//    private ArrayList<ExameDateBean> examList;
//    private J_ExameDateRecyclerAdapter exameAdapter;
//    private TypeBean typeBean;
//    private ArrayList<TypeBean> typeList;
//    private J_TypeRecyclerAdapter typeAdapter;
//    private AddressBean addressBean;
//    private ArrayList<AddressBean> addressList = new ArrayList<>();
//    private J_AddressRecyclerAdapter addressAdapter;
//
//    //Message传递过来的
//    private ArrayList<TeachBean> mTeachDate;
//    private ArrayList<ExameDateBean> mExameDate;
//    private ArrayList<TypeBean> mTypeDate;
//    private ArrayList<AddressBean> mAddressDate;
//
//    private Map<Integer, ArrayList<TeachBean>> tecah_maps = new HashMap<>();
//    private Map<Integer, ArrayList<ExameDateBean>> exame_maps = new HashMap<>();
//    private Map<Integer, ArrayList<TypeBean>> type_maps = new HashMap<>();
//    private Map<Integer, ArrayList<AddressBean>> address_maps = new HashMap<>();
//
//    private Map<Integer, ArrayList<TeachBean>> msgTecah_maps;
//    private Map<Integer, ArrayList<ExameDateBean>> msgExame_maps;
//    private Map<Integer, ArrayList<TypeBean>> msgType_maps;
//    private Map<Integer, ArrayList<AddressBean>> msgAddress_maps;
//
//    public static boolean msgTeachClick = false;
//    public static boolean msgExameClick = false;
//    public static boolean msgTypeClick = false;
//    public static boolean msgAddressClick = false;
//
//    private int parent_pos;
//    private int child_pos;
//
//    public MealCourseAdapter(Context context, List items) {
//        super(context, items);
//        this.mCtx = context;
//        this.mData = (ArrayList<PackageClassTypeBean.Body.ProductList>) items;
//    }
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case -2: //传递列表的父类position
//                    child_pos = (int) msg.obj;
//                    Log.i("子类的pos  -- > ", "" + child_pos);
//                    break;
//                case -1: //传递列表的父类position
//                    parent_pos = (int) msg.obj;
//                    Log.i("父类的pos  -- > ", "" + parent_pos);
//                    break;
//                case 0://授课方式
////                    HandleTeachDate(msg);
////                    for (int j = 0; j < tecah_maps.get(parent_pos).size(); j++) {
////                        if (tecah_maps.get(parent_pos).get(j).isTypeSelect()) {
////                            J_msgTeachClick = true;
////                            break;
////                        }
////                    }
//                    break;
//                case 1: //考试年月选择
////                    HandleExameDate(msg);
//                    break;
//                case 2: //课程类型选择
//                    HandleClassType(msg);
//                    break;
//                case 3: //上课地点选择
////                    HandleAddressDate(msg);
//                    break;
//            }
//        }
//    };
//
////    /**
////     * 考试年月处理
////     **/
////    public void HandleExameDate(Message msg) {
////        mExameDate = (ArrayList<ExameDateBean>) msg.obj;
////        for (int i = 0; i < mExameDate.size(); i++) {
////            if (mExameDate.get(i).isTypeSelect()) {
////                //点击的日期
////                String clickDate = mExameDate.get(i).getTypeName();
//////                Log.i("clickDate == ", "" + clickDate);
////                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
////                    //大数据中的日期
////                    String mDataExamDateName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
//////                    Log.i("大数据中 ", "" + mDataExamDateName);
////                    if (clickDate.equals(mDataExamDateName)) {
////                        //对比之后大数据中的课程类型
////                        String mDataTypeName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
////                        //对比之后大数据中的上课地点
////                        String mDataPlaceName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();
//////                        Log.i("对比之后类型 == ", "" + mDataTypeName);
//////                        Log.i("对比之后地点 == ", "" + mDataPlaceName);
////                        /**课程类型**/
////                        ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
////                        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
////                            int ClassTypeSize = ClassTypeTypeList.size();
////                            for (int k = 0; k < ClassTypeSize; k++) {
////                                if (mDataTypeName.equals(ClassTypeTypeList.get(k))) {
////                                    typeList.get(k).setChecked(true);
////                                } else {
////                                    typeList.get(k).setChecked(false);
////                                }
////                            }
////                            if (null != typeAdapter) {
////                                typeAdapter.notifyDataSetChanged();
////                            }
////                        }
////                        /**考试地点**/
////                        ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
////                        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
////                            int ShoolSize = ShoolPlaceList.size();
////                            for (int a = 0; a < ShoolSize; a++) {
////                                if (mDataPlaceName.equals(ShoolPlaceList.get(a))) {
////                                    addressList.get(a).setChecked(true);
////                                } else {
////                                    addressList.get(a).setChecked(false);
////                                }
////                            }
////                            if (null != addressAdapter) {
////                                addressAdapter.notifyDataSetChanged();
////                            }
////                        }
////                    } else {
//////                        Log.i("点击的和大数据中的不同 -- ", "点击的和大数据中的不同 -- ");
////                    }
////                }
////            } else {
//////                Log.i("日期取消选择 -- > ", "日期取消选择");
////
////            }
////        }
////        for (int j = 0; j < mExameDate.size(); j++) {
////            if (mExameDate.get(j).isTypeSelect()) {
////                msgExameClick = true;
////                break;
////            }
////        }
////        Log.i("日期操作完成之后", "" + msgExameClick);
////        //如果三个选项都没有选中的状态,则恢复重置按钮
////        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
////            /**考试年月**/
////            ExameDateList = mData.get(0).getBody().getExameDateList();
////            if (ExameDateList != null && !"".equals(ExameDateList)) {
////                int ExameSize = ExameDateList.size();
////                for (int k = 0; k < ExameSize; k++) {
////                    examList.get(k).setChecked(true);
////                }
////                if (null != exameAdapter) {
////                    exameAdapter.notifyDataSetChanged();
////                }
////            }
////            /**课程类型**/
////            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
////            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
////                int ClassTypeSize = ClassTypeTypeList.size();
////                for (int k = 0; k < ClassTypeSize; k++) {
////                    typeList.get(k).setChecked(true);
////                }
////                if (null != typeAdapter) {
////                    typeAdapter.notifyDataSetChanged();
////                }
////            }
////            /**考试地点**/
////            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
////            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
////                int ShoolSize = ShoolPlaceList.size();
////                for (int a = 0; a < ShoolSize; a++) {
////                    addressList.get(a).setChecked(true);
////                }
////                if (null != addressAdapter) {
////                    addressAdapter.notifyDataSetChanged();
////                }
////            }
////        }
////
////    }
////
//    /**
//     * 课程类型处理
//     **/
//    public void HandleClassType(Message msg) {
//        msgType_maps = (Map<Integer, ArrayList<TypeBean>>) msg.obj;
////        mTypeDate = (ArrayList<TypeBean>) msg.obj;
//
//        for (int i = 0; i < msgType_maps.get(parent_pos).size(); i++) {
//            if (msgType_maps.get(parent_pos).get(i).isTypeSelect()) {
//                //点击的类型
//                String clickType = msgType_maps.get(parent_pos).get(i).getTypeName();
//                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
//                    //大数据中的类型
//                    String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
//                    if (clickType.equals(mTypeName)) {
//                        //对比之后大数据中的考试时间
//                        String mExamDateName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
//                        //对比之后大数据中的上课地点
//                        String mDataPlaceName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
//                        /**考试年月**/
//                        ExameDateList = mData.get(parent_pos).getExameDateList();
//                        if (ExameDateList != null && !"".equals(ExameDateList)) {
//                            int ExameDateSize = ExameDateList.size();
//                            for (int a = 0; a < ExameDateSize; a++) {
//                                if (mExamDateName.equals(ExameDateList.get(a))) {
////                                    msgType_maps.get(parent_pos).get(a).setChecked(true);
//                                    type_maps.get(parent_pos).get(a).setChecked(true);
////                                    examList.get(a).setChecked(true);
//                                } else {
//                                    type_maps.get(parent_pos).get(a).setChecked(false);
////                                    examList.get(a).setChecked(false);
//                                }
//                            }
//                            if (null != exameAdapter) {
//                                exameAdapter.notifyDataSetChanged();
//                            }
//                        }
//                        /**考试地点**/
//                        ShoolPlaceList = mData.get(parent_pos).getShoolPlaceList();
//                        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
//                            int ShoolSize = ShoolPlaceList.size();
//                            if ("在线".equals(clickType)) {
//                                for (int a = 0; a < ShoolSize; a++) {
//                                    Log.i("在线", "mDataPlaceName " + mDataPlaceName);
//                                    Log.i("在线", "ShoolPlaceList.get(a) " + ShoolPlaceList.get(a));
//                                    address_maps.get(parent_pos).get(a).setChecked(false);
//                                }
//                                Gson s = new Gson();
//                                Log.i("add刷新之前数据 -- ", "" + s.toJson(address_maps).toString());
////                                Message f_msg = new Message();
////                                f_msg.what = 3;
////                                f_msg.obj = parent_pos;
////                                mHandler.sendMessage(f_msg);
//
////                                notifyItemChanged(parent_pos);
//
////                                notifyItemChanged(parent_pos);
//                                addressAdapter.notifyDataSetChanged();
//                                addressAdapter.notifyItemChanged(child_pos);
//
//////                                addressAdapter.notifyItemChanged(child_pos);
//
////                                addressAdapter.notifyDataSetChanged();
////                                addressAdapter.setCheck(parent_pos,child_pos);
//
//                            } else {
//                                Log.i("在线 eee ", "在线eee === > ");
//                                for (int a = 0; a < ShoolSize; a++) {
//                                    if (mDataPlaceName.equals(ShoolPlaceList.get(a))) {
//                                        address_maps.get(parent_pos).get(a).setChecked(true);
//                                    } else {
//                                        address_maps.get(parent_pos).get(a).setChecked(false);
//                                    }
//                                }
//                            }
////                            if (null != addressAdapter) {
////                                addressAdapter.notifyItemChanged(parent_pos);
//////                                addressAdapter.notifyDataSetChanged();
////                            }
//                        }
//                    }
//                }
//            }
//        }
//        for (int j = 0; j < msgType_maps.get(parent_pos).size(); j++) {
//            if (msgType_maps.get(parent_pos).get(j).isTypeSelect()) {
//                J_msgTypeClick = true;
//                break;
//            }
//        }
//        Log.i("类型操作完成之后", "" + J_msgTypeClick);
//        //如果三个选项都没有选中的状态,则恢复重置按钮
////        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
////            /**考试年月**/
////            ExameDateList = mData.get(0).getBody().getExameDateList();
////            if (ExameDateList != null && !"".equals(ExameDateList)) {
////                int ExameSize = ExameDateList.size();
////                for (int k = 0; k < ExameSize; k++) {
////                    examList.get(k).setChecked(true);
////                }
////                if (null != exameAdapter) {
////                    exameAdapter.notifyDataSetChanged();
////                }
////            }
////            /**课程类型**/
////            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
////            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
////                int ClassTypeSize = ClassTypeTypeList.size();
////                for (int k = 0; k < ClassTypeSize; k++) {
////                    typeList.get(k).setChecked(true);
////                }
////                if (null != typeAdapter) {
////                    typeAdapter.notifyDataSetChanged();
////                }
////            }
////            /**考试地点**/
////            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
////            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
////                int ShoolSize = ShoolPlaceList.size();
////                for (int a = 0; a < ShoolSize; a++) {
////                    addressList.get(a).setChecked(true);
////                }
////                if (null != addressAdapter) {
////                    addressAdapter.notifyDataSetChanged();
////                }
////            }
////        }
//    }
////
////    /**
////     * 上课地点处理
////     **/
////    public void HandleAddressDate(Message msg) {
////        mAddressDate = (ArrayList<AddressBean>) msg.obj;
////        Gson s = new Gson();
////        Log.i("是否有选中 -- > ", "" + s.toJson(mAddressDate).toString());
////        for (int i = 0; i < mAddressDate.size(); i++) {
////            if (mAddressDate.get(i).isTypeSelect()) {
////                Log.i("上课地点选中了 -- > ", "" + mAddressDate.get(i).getTypeName());
////                //点击的地点
////                String clickAddress = mAddressDate.get(i).getTypeName();
////                for (int j = 0; j < mData.get(0).getBody().getClassTypeList().size(); j++) {
////                    //大数据中的地点
////                    String mAddressName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_PlaceName();
////                    if (clickAddress.equals(mAddressName)) {
////                        //对比之后大数据中的考试时间
////                        String mExamDateName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_ExamDateName();
////                        //对比之后大数据中的课程类型
////                        String mTypeName = mData.get(0).getBody().getClassTypeList().get(j).getClassType_TypeName();
////                        /**考试年月**/
////                        ExameDateList = mData.get(0).getBody().getExameDateList();
////                        if (ExameDateList != null && !"".equals(ExameDateList)) {
////                            int ExameDateSize = ExameDateList.size();
////                            for (int a = 0; a < ExameDateSize; a++) {
////                                if (mExamDateName.equals(ExameDateList.get(a))) {
////                                    examList.get(a).setChecked(true);
////                                } else {
////                                    examList.get(a).setChecked(false);
////                                }
////                            }
////                            if (null != exameAdapter) {
////                                exameAdapter.notifyDataSetChanged();
////                            }
////                        }
////                        /**课程类型**/
////                        ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
////                        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
////                            int ClassTypeSize = ClassTypeTypeList.size();
////                            for (int k = 0; k < ClassTypeSize; k++) {
////                                if (mTypeName.equals(ClassTypeTypeList.get(k))) {
////                                    typeList.get(k).setChecked(true);
////                                } else {
////                                    typeList.get(k).setChecked(false);
////                                }
////                            }
////                            if (null != typeAdapter) {
////                                typeAdapter.notifyDataSetChanged();
////                            }
////                        }
////                    } else {
//////                        Log.i("点击的和大数据中的不同 -- ", "点击的和大数据中的不同 -- ");
////                    }
////                }
////            } else {
//////                Log.i("上课地点取消选择 -- > ", "" + mAddressDate.get(i).getTypeName());
////            }
////        }
////        for (int j = 0; j < mAddressDate.size(); j++) {
////            if (mAddressDate.get(j).isTypeSelect()) {
////                msgAddressClick = true;
////                break;
////            }
////        }
////        Log.i("上课地点操作完成之后", "" + msgAddressClick);
////        //如果三个选项都没有选中的状态,则恢复重置按钮
////        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
////            /**考试年月**/
////            ExameDateList = mData.get(0).getBody().getExameDateList();
////            if (ExameDateList != null && !"".equals(ExameDateList)) {
////                int ExameSize = ExameDateList.size();
////                for (int k = 0; k < ExameSize; k++) {
////                    examList.get(k).setChecked(true);
////                }
////                if (null != exameAdapter) {
////                    exameAdapter.notifyDataSetChanged();
////                }
////            }
////            /**课程类型**/
////            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
////            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
////                int ClassTypeSize = ClassTypeTypeList.size();
////                for (int k = 0; k < ClassTypeSize; k++) {
////                    typeList.get(k).setChecked(true);
////                }
////                if (null != typeAdapter) {
////                    typeAdapter.notifyDataSetChanged();
////                }
////            }
////            /**考试地点**/
////            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
////            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
////                int ShoolSize = ShoolPlaceList.size();
////                for (int a = 0; a < ShoolSize; a++) {
////                    addressList.get(a).setChecked(true);
////                }
////                if (null != addressAdapter) {
////                    addressAdapter.notifyDataSetChanged();
////                }
////            }
////        }
////    }
//
//    @Override
//    public MealCourseAdapter.MyHolder onCreateViewHolder(View view, int viewType) {
//        return new MealCourseAdapter.MyHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(final MealCourseAdapter.MyHolder holder, PackageClassTypeBean.Body.ProductList item, final int position) {
//        /**授课方式**/
//        teachList = new ArrayList<>();
//        teachBean = new TeachBean();
//        teachBean.setTypeName("限购");
//        teachList.add(teachBean);
//        teachBean = new TeachBean();
//        teachBean.setTypeName("预购");
//        teachList.add(teachBean);
//        holder.teachRecyclerView.setLayoutManager(new MyGridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
//        holder.teachRecyclerView.setNestedScrollingEnabled(false);
////        teachAdapter = new J_TeachRecyclerAdapter(context, teachList, handler, 0);
////        holder.teachRecyclerView.setAdapter(teachAdapter);
//
//        /**授课方式**/
//
//        /**预购还是现购**/
//        int Link_BuyType = mData.get(position).getLink_BuyType();
//        teachList = new ArrayList<>();
//        //判断现购还是预购
//        //0：自选 1：现购 2：预购
//        if (0 == Link_BuyType) {
//            teachBean = new TeachBean();
//            teachBean.setTypeName("限购");
//            teachList.add(teachBean);
//            teachBean = new TeachBean();
//            teachBean.setTypeName("预购");
//            teachList.add(teachBean);
//            tecah_maps.put(position, teachList);
//        } else if (1 == Link_BuyType) { //现购
//            teachBean = new TeachBean();
//            teachBean.setTypeName("限购");
//            teachBean.setTypeSelect(true);
//            teachList.add(teachBean);
//            teachBean = new TeachBean();
//            teachBean.setTypeName("预购");
//            teachBean.setChecked(false);
//            teachList.add(teachBean);
//            tecah_maps.put(position, teachList);
//        } else { //预购
//            teachBean = new TeachBean();
//            teachBean.setTypeName("限购");
//            teachBean.setChecked(false);
//            teachList.add(teachBean);
//            teachBean = new TeachBean();
//            teachBean.setTypeName("预购");
//            teachBean.setTypeSelect(true);
//            teachList.add(teachBean);
//            tecah_maps.put(position, teachList);
//        }
//
//        holder.teachRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//        holder.teachRecyclerView.setNestedScrollingEnabled(false);
//        teachAdapter = new J_TeachRecyclerAdapter(mCtx, tecah_maps, handler, position, Link_BuyType);
//        holder.teachRecyclerView.setAdapter(teachAdapter);
//
////        }else{
////            Log.i("不是空","不是空");
////        }
//
//        /**考试年月**/
//        examList = new ArrayList<>();
//        ExameDateList = mData.get(position).getExameDateList();
//        //0：自选 1：现购 2：预购
//        if (2 == Link_BuyType) {
//            if (ExameDateList != null && !"".equals(ExameDateList)) {
//                int ExameSize = ExameDateList.size();
//                for (int i = 0; i < ExameSize; i++) {
//                    exameDateBean = new ExameDateBean();
//                    exameDateBean.setTypeName(ExameDateList.get(i));
//                    exameDateBean.setChecked(false);
//                    examList.add(exameDateBean);
//                }
//            }
//            exame_maps.put(position, examList);
//        } else {
//            if (ExameDateList != null && !"".equals(ExameDateList)) {
//                int ExameSize = ExameDateList.size();
//                for (int i = 0; i < ExameSize; i++) {
//                    exameDateBean = new ExameDateBean();
//                    exameDateBean.setTypeName(ExameDateList.get(i));
//                    examList.add(exameDateBean);
//                }
//            }
//            exame_maps.put(position, examList);
//        }
//
//        holder.featureRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//        holder.featureRecyclerView.setNestedScrollingEnabled(false);
//        exameAdapter = new J_ExameDateRecyclerAdapter(mCtx, exame_maps, handler, position);
//        holder.featureRecyclerView.setAdapter(exameAdapter);
//
//        /**课程类型**/
//        typeList = new ArrayList<>();
//        ClassTypeTypeList = mData.get(position).getClassTypeTypeList();
//        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
//            int ClassTypeSize = ClassTypeTypeList.size();
//            for (int i = 0; i < ClassTypeSize; i++) {
//                typeBean = new TypeBean();
//                typeBean.setTypeName(ClassTypeTypeList.get(i));
//                typeBean.setTypeSelect(false);
//                typeList.add(typeBean);
//            }
//        }
//        type_maps.put(position, typeList);
//        holder.typeRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//        holder.typeRecyclerView.setNestedScrollingEnabled(false);
//        typeAdapter = new J_TypeRecyclerAdapter(mCtx, type_maps, handler, position);
//        holder.typeRecyclerView.setAdapter(typeAdapter);
//
//
//
//        /**考试地点**/
//        addressList = new ArrayList<>();
//        ShoolPlaceList = mData.get(position).getShoolPlaceList();
//        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
//            int ShoolSize = ShoolPlaceList.size();
//            for (int i = 0; i < ShoolSize; i++) {
//                addressBean = new AddressBean();
//                addressBean.setTypeName(ShoolPlaceList.get(i));
//                addressList.add(addressBean);
//            }
//        }
//        address_maps.put(position, addressList);
//        holder.addressRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//        holder.addressRecyclerView.setNestedScrollingEnabled(false);
//        addressAdapter = new J_AddressRecyclerAdapter(mCtx, address_maps, handler, position);
////            addressAdapter = new JJ_AddressRecyclerAdapter(mCtx, address_maps, handler, pos);
//        holder.addressRecyclerView.setAdapter(addressAdapter);
//
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.meal_list_items;
//    }
//
//    @Override
//    public int getItemCount() {
//        Log.i("getItemCount", "" + mData.size());
//        return mData.size();
//    }
//
//    public class MyHolder extends RecyclerView.ViewHolder {
//        //图片
//        @BindView(R.id.items_im)
//        ImageView items_im;
//        //标题
//        @BindView(R.id.items_content_tv)
//        TextView items_content_tv;
//        //价格
//        @BindView(R.id.items_price_tv)
//        TextView items_price_tv;
//        //授课方式
//        @BindView(R.id.teachRecyclerView)
//        RecyclerView teachRecyclerView;
//        // 年月
//        @BindView(R.id.featureRecyclerView)
//        RecyclerView featureRecyclerView;
//        // 类型
//        @BindView(R.id.typeRecyclerView)
//        RecyclerView typeRecyclerView;
//        // 地点
//        @BindView(R.id.addressRecyclerView)
//        RecyclerView addressRecyclerView;
//        //课程时长
//        @BindView(R.id.items_kssc_tv)
//        TextView items_kssc_tv;
//
//
//        public MyHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//}
