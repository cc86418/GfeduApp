package cn.jun.courseinfo.course_adapter;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jun.bean.PackageClassTypeBean;
import cn.jun.bean.PackageProduct;
import cn.jun.courseinfo.bean.AddressBean;
import cn.jun.courseinfo.bean.ExameDateBean;
import cn.jun.courseinfo.bean.TeachBean;
import cn.jun.courseinfo.bean.TypeBean;
import cn.jun.courseinfo.j_course_adapter.J_AddressRecyclerAdapter;
import cn.jun.courseinfo.j_course_adapter.J_ExameDateRecyclerAdapter;
import cn.jun.courseinfo.j_course_adapter.J_TeachRecyclerAdapter;
import cn.jun.courseinfo.j_course_adapter.J_TypeRecyclerAdapter;
import jc.cici.android.R;
import jc.cici.android.atom.view.MyGridLayoutManager;


public class J_RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mCtx;
    //    private ArrayList<PackageClassTypeBean.Body.ProductList> mData;
//    private ArrayList<CommonBean<PackageClassTypeBean>> mData;
    public static ArrayList<PackageClassTypeBean.ProductList> mData;

    private ArrayList<String> TeachTypeList;
    private ArrayList<String> ClassTypeTypeList;
    private ArrayList<String> ExameDateList;
    private ArrayList<String> ShoolPlaceList;

    private TeachBean teachBean;
    private ArrayList<TeachBean> teachList;
    //    private J_TeachRecyclerAdapter teachAdapter;
    private ExameDateBean exameDateBean;
    private ArrayList<ExameDateBean> examList;
    //    private J_ExameDateRecyclerAdapter exameAdapter;
    private TypeBean typeBean;
    private ArrayList<TypeBean> typeList;
    //    private J_TypeRecyclerAdapter typeAdapter;
    private AddressBean addressBean;
    private ArrayList<AddressBean> addressList = new ArrayList<>();

    private J_TeachRecyclerAdapter change_teachAdapter;
    private J_ExameDateRecyclerAdapter change_exameAdapter;
    private J_TypeRecyclerAdapter change_typeAdapter;
    private J_AddressRecyclerAdapter change_addressAdapter;


    //Message传递过来的
//    private ArrayList<TeachBean> mTeachDate;
//    private ArrayList<ExameDateBean> mExameDate;
//    private ArrayList<TypeBean> mTypeDate;
//    private ArrayList<AddressBean> mAddressDate;
    private Map<Integer, ArrayList<TeachBean>> msgTecah_maps;
    private Map<Integer, ArrayList<ExameDateBean>> msgExame_maps;
    private Map<Integer, ArrayList<TypeBean>> msgType_maps;
    private Map<Integer, ArrayList<AddressBean>> msgAddress_maps;

    private int parent_pos;
    private int child_pos;
    //    private RecyclerView.ViewHolder mViewHolder;
    //价格
    private String price = "";

    String HodleClickDate = "";
    private Map<Integer, ArrayList<TeachBean>> tecah_maps = new HashMap<>();
    private Map<Integer, ArrayList<ExameDateBean>> exame_maps = new HashMap<>();
    private Map<Integer, ArrayList<TypeBean>> type_maps = new HashMap<>();
    public static Map<Integer, ArrayList<AddressBean>> address_maps = new HashMap<>();


    public static boolean J_msgTeachClick = false;
    public static boolean J_msgExameClick = false;
    public static boolean J_msgTypeClick = false;
    public static boolean J_msgAddressClick = false;

    public static boolean Time_msgExameClick = false;
    public static boolean Time_msgTypeClick = false;
    public static boolean Time_msgAddressClick = false;

    //刷新价格的二次标识符
//    private boolean priceflag = false;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private View mHeaderView;

    private String ClickDate;
    public static String ClickType;
    public static String Time_ClickType;
    private String ClickAddress;
    private Handler returnId;
    private int classtypeid;

    private OnItemClickListener mOnItemClickListener = null;
    private PackageProduct packageProduct;
    //    public static ArrayList<PackageProduct> packageProductList = new ArrayList<>();
    private Set<PackageProduct> HashSet;

    //    private Holder myHolder;
    private RecyclerView.ViewHolder myHolder;
    private TextView myTextView;

    private String HodlePrice;

    private String HodleSalePrice;

    private boolean HodleViewFlag = true;

    //是否已经做了对比
    public static boolean Is_TeachContrast = false;
    public static boolean Is_ExameContrast = false;
    public static boolean Is_TypeContrast = false;

    public static boolean Is_AddressContrast = false;
//

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public J_RecyclerAdapter(Context context, List items, Handler returnId) {
        this.mCtx = context;
        this.mData = (ArrayList<PackageClassTypeBean.ProductList>) items;
//        this.mData = (ArrayList<PackageClassTypeBean.Body.ProductList>) items;
        this.returnId = returnId;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -2: //传递列表的父类position
                    child_pos = (int) msg.obj;
//                    Log.i("子类的pos  -- > ", "" + child_pos);
                    break;
                case -1: //传递列表的父类position
                    parent_pos = (int) msg.obj;
//                    Log.i("父类的pos  -- > ", "" + parent_pos);
                    break;
                case 0://授课方式
                    HandleTeachDate(msg);
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

                case 4://上课课时修改
                    parent_pos = (int) msg.obj;
//                    Log.i("上课课时修改 ", " 上课课时修改 ");
                    notifyItemChanged(parent_pos + 1, "1");

                    break;

                case 5://课程价格修改
//                    Log.i("课程价格修改 ", " ==> " + parent_pos);
//                    Log.i("课程价格修改 price ", " ==> " + HodleSalePrice);
                    parent_pos = (int) msg.obj;
                    notifyItemChanged(parent_pos + 1, "2");

                    break;
            }
        }
    };


    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_list_items, parent, false);
        //将创建的View注册点击事件
        layout.setOnClickListener(this);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Log.i("大类刷新OnBindView", " ========= 无参数");
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position, List payloads) {
//        Log.i("大类刷新OnBindView", " ====payloads " + payloads);
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(viewHolder);

        if (payloads.isEmpty()) {
            onBindViewHolder(viewHolder, position);

//        if (HodleViewFlag) {

            if (viewHolder instanceof Holder) {
//            myHolder = viewHolder;

                //将position保存在itemView的Tag中，以便点击时进行获取
                viewHolder.itemView.setTag(pos);

                /**预购还是现购**/
                final int Link_BuyType = mData.get(pos).getLink_BuyType();

                /**课程图片**/
                String Class_MobileImage = mData.get(pos).getClassInfo().getClass_MobileImage();
                Glide.with(mCtx)
                        .load(Class_MobileImage)
                        .placeholder(R.drawable.pic_kong_banner)
                        .into(((Holder) viewHolder).items_im);
                /**课程标题**/
                String titile = mData.get(pos).getClassInfo().getClass_Name();
                titile = titile.replaceAll("&nbsp;", " ");
                ((Holder) viewHolder).items_content_tv.setText(titile);


                /**课程售价**/
                ((Holder) viewHolder).items_price_tv.setTag(pos);
                Log.i("控件ID----- >>>>> ", "" + ((Holder) viewHolder).items_price_tv);
                String minprice = mData.get(pos).getClassInfo().getClass_MinSalePrice();
                String maxprice = mData.get(pos).getClassInfo().getClass_MaxSalePrice();
                if (maxprice.equals(minprice)) {
                    ((Holder) viewHolder).items_price_tv.setText(minprice);
                } else {
                    ((Holder) viewHolder).items_price_tv.setText(minprice + " - " + maxprice);
                }
//            ((Holder) viewHolder).items_price_tv.setText(price);
                /**授课方式**/
//        if("".equals(teachList) && null==teachList) {
                teachList = new ArrayList<>();
                //判断现购还是预购
                //0：自选 1：限购 2：预购
                if (0 == Link_BuyType) {
//                    teachBean = new TeachBean();
//                    teachBean.setTypeName("自选");
//                    teachBean.setTypeSelect(true);
//                    teachList.add(teachBean);
                    teachBean = new TeachBean();
                    teachBean.setTypeName("现购");
                    teachBean.setTypeSelect(false);
                    teachList.add(teachBean);
                    teachBean = new TeachBean();
                    teachBean.setTypeName("预购");
                    teachBean.setTypeSelect(false);
                    teachList.add(teachBean);
                    tecah_maps.put(pos, teachList);
                } else if (1 == Link_BuyType) { //限购
                    teachBean = new TeachBean();
                    teachBean.setTypeName("现购");
                    teachBean.setTypeSelect(true);
                    teachList.add(teachBean);
//                    teachBean = new TeachBean();
//                    teachBean.setTypeName("预购");
//                    teachBean.setChecked(false);
//                    teachList.add(teachBean);
                    tecah_maps.put(pos, teachList);
                } else { //预购
//                    teachBean = new TeachBean();
//                    teachBean.setTypeName("限购");
//                    teachBean.setChecked(false);
//                    teachList.add(teachBean);
                    teachBean = new TeachBean();
                    teachBean.setTypeName("预购");
                    teachBean.setTypeSelect(true);
                    teachList.add(teachBean);
                    tecah_maps.put(pos, teachList);
                }

                ((Holder) viewHolder).teachRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
                ((Holder) viewHolder).teachRecyclerView.setNestedScrollingEnabled(false);
                final J_TeachRecyclerAdapter teachAdapter = new J_TeachRecyclerAdapter(mCtx, tecah_maps, handler, pos, Link_BuyType);
                ((Holder) viewHolder).teachRecyclerView.setAdapter(teachAdapter);
//        }else{
//            Log.i("不是空","不是空");
//        }

                /**考试年月**/
                examList = new ArrayList<>();
                ExameDateList = mData.get(pos).getExameDateList();
                //0：自选 1：现购 2：预购
                if (2 == Link_BuyType) {
                    if (ExameDateList != null && !"".equals(ExameDateList)) {
                        int ExameSize = ExameDateList.size();
                        for (int i = 0; i < ExameSize; i++) {
                            exameDateBean = new ExameDateBean();
                            exameDateBean.setTypeName(ExameDateList.get(i));
                            exameDateBean.setChecked(false);
                            examList.add(exameDateBean);
                        }
                    }
                    exame_maps.put(pos, examList);
                } else {
                    if (ExameDateList != null && !"".equals(ExameDateList)) {
                        int ExameSize = ExameDateList.size();
                        for (int i = 0; i < ExameSize; i++) {
                            exameDateBean = new ExameDateBean();
                            exameDateBean.setTypeName(ExameDateList.get(i));
                            examList.add(exameDateBean);
                        }
                    }
                    exame_maps.put(pos, examList);
                }

                ((Holder) viewHolder).featureRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
                ((Holder) viewHolder).featureRecyclerView.setNestedScrollingEnabled(false);
                final J_ExameDateRecyclerAdapter exameAdapter = new J_ExameDateRecyclerAdapter(mCtx, exame_maps, handler, pos, Link_BuyType);
                ((Holder) viewHolder).featureRecyclerView.setAdapter(exameAdapter);


                /**考试地点**/
                addressList = new ArrayList<>();
                ShoolPlaceList = mData.get(pos).getShoolPlaceList();
                if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                    int ShoolSize = ShoolPlaceList.size();
                    for (int i = 0; i < ShoolSize; i++) {
                        addressBean = new AddressBean();
                        addressBean.setTypeName(ShoolPlaceList.get(i));
                        addressList.add(addressBean);
                    }
                }
                address_maps.put(pos, addressList);
                ((Holder) viewHolder).addressRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
                ((Holder) viewHolder).addressRecyclerView.setNestedScrollingEnabled(false);
                final J_AddressRecyclerAdapter addressAdapter = new J_AddressRecyclerAdapter(mCtx, address_maps, handler, pos);
                // addressAdapter = new JJ_AddressRecyclerAdapter(mCtx, address_maps, handler, pos);
                ((Holder) viewHolder).addressRecyclerView.setAdapter(addressAdapter);


                /**课程类型**/
                typeList = new ArrayList<>();
                ClassTypeTypeList = mData.get(pos).getClassTypeTypeList();

                if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                    int ClassTypeSize = ClassTypeTypeList.size();
                    for (int i = 0; i < ClassTypeSize; i++) {
                        typeBean = new TypeBean();
                        typeBean.setTypeName(ClassTypeTypeList.get(i));
                        typeBean.setTypeSelect(false);
                        typeList.add(typeBean);
                    }
                }
                type_maps.put(pos, typeList);
                ((Holder) viewHolder).typeRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
                ((Holder) viewHolder).typeRecyclerView.setNestedScrollingEnabled(false);
                final J_TypeRecyclerAdapter typeAdapter = new J_TypeRecyclerAdapter(mCtx, type_maps, handler, pos);
                ((Holder) viewHolder).typeRecyclerView.setAdapter(typeAdapter);


                /**点击刷新**/
                //授课类型刷新
                teachAdapter.buttonSetOnclick(new J_TeachRecyclerAdapter.ButtonInterface() {
                    @Override
                    public void onclick(View view, int parent_pos, int chlid_pos) {
                        change_exameAdapter = exameAdapter;
                        change_typeAdapter = typeAdapter;
                        change_addressAdapter = addressAdapter;

                    }
                });

                //考试年月点击刷新
                exameAdapter.buttonSetOnclick(new J_ExameDateRecyclerAdapter.ButtonInterface() {
                    @Override
                    public void onclick(View view, int parent_pos, int chlid_pos) {
                        change_typeAdapter = typeAdapter;
                        change_addressAdapter = addressAdapter;

//                    if (2 == Link_BuyType) {
//                        if (Time_msgTypeClick) {
//                            if ("在线".equals(Time_ClickType)) {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_Date() + "");
//                            } else {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_StudyDay() + "");
//                            }
//                        }
//                    } else {
//                        if (Time_msgTypeClick && Time_msgExameClick) {
//                            if ("在线".equals(ClickType)) {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_Date() + "");
//                            } else {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_StudyDay() + "");
//                            }
//                        }
//                    }

                    }
                });
                //课程类型点击刷新
                typeAdapter.buttonSetOnclick(new J_TypeRecyclerAdapter.ButtonInterface() {
                    @Override
                    public void onclick(View view, int parent_pos, int chlid_pos) {
                        change_addressAdapter = addressAdapter;
                        change_exameAdapter = exameAdapter;

//                    if (2 == Link_BuyType) {
//                        if (Time_msgTypeClick) {
//                            if ("在线".equals(Time_ClickType)) {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_Date() + "");
//                            } else {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_StudyDay() + "");
//                            }
//                        }
//                    } else {
//                        if (Time_msgTypeClick && Time_msgExameClick) {
//                            if ("在线".equals(ClickType)) {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_Date() + "");
//                            } else {
//                                ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_StudyDay() + "");
//                            }
//                        }
//                    }
                    }
                });
                //考试地点点击刷新
                addressAdapter.buttonSetOnclick(new J_AddressRecyclerAdapter.ButtonInterface() {
                    @Override
                    public void onclick(View view, int parent_pos, int chlid_pos) {

                    }
                });
                /**课程时长**/
////            ((Holder) viewHolder).items_kssc_tv.setText(mData.get(pos).getClassTypeList().get(0).getClassType_StudyDay() + "");
                ((Holder) viewHolder).items_kssc_tv.setText("0");
            }
        } else {
            final int Link_BuyType = mData.get(pos).getLink_BuyType();
            //0：自选 1：现购 2：预购
            if (2 == Link_BuyType) {
                ((Holder) viewHolder).items_kssc_tv.setText("--");
            } else {
                ((Holder) viewHolder).items_kssc_tv.setText(HodlePrice);
            }

            ((Holder) viewHolder).items_price_tv.setText(HodleSalePrice);
//            Bundle payload = (Bundle) payloads.get(0);
//            if (payload.containsKey("1")) {
//                ((Holder) viewHolder).items_kssc_tv.setText(HodlePrice);
//            }else {
//                ((Holder) viewHolder).items_price_tv.setText(HodleSalePrice);
//            }


        }

//        }
//        else {
//            Log.i("else > HodleViewFlag > ", "" + HodleViewFlag);
//            if (viewHolder instanceof Holder) {
//                ((Holder) viewHolder).items_kssc_tv.setText(HodlePrice);
//            }
//
//        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();

        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mData.size() : mData.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public class Holder extends RecyclerView.ViewHolder {
        //图片
        ImageView items_im;
        //标题
        TextView items_content_tv;
        //价格
        TextView items_price_tv;
        //授课方式
        RecyclerView teachRecyclerView;
        // 年月
        RecyclerView featureRecyclerView;
        // 类型
        RecyclerView typeRecyclerView;
        // 地点
        RecyclerView addressRecyclerView;
        //课程时长
        TextView items_kssc_tv;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            items_im = (ImageView) itemView.findViewById(R.id.items_im);
            items_content_tv = (TextView) itemView.findViewById(R.id.items_content_tv);
            items_price_tv = (TextView) itemView.findViewById(R.id.items_price_tv);
            teachRecyclerView = (RecyclerView) itemView.findViewById(R.id.teachRecyclerView);
            featureRecyclerView = (RecyclerView) itemView.findViewById(R.id.featureRecyclerView);
            typeRecyclerView = (RecyclerView) itemView.findViewById(R.id.typeRecyclerView);
            addressRecyclerView = (RecyclerView) itemView.findViewById(R.id.addressRecyclerView);
            items_kssc_tv = (TextView) itemView.findViewById(R.id.items_kssc_tv);

        }
    }

    /**
     * 限购预购处理
     **/
    public void HandleTeachDate(Message msg) {
        msgTecah_maps = (Map<Integer, ArrayList<TeachBean>>) msg.obj;
        int Link_ProductType = mData.get(parent_pos).getLink_BuyType();
        if (0 == Link_ProductType) {
            for (int i = 0; i < msgTecah_maps.get(parent_pos).size(); i++) {
                if (msgTecah_maps.get(parent_pos).get(i).isTypeSelect()) {
                    //点击的购买方式
                    String clickTeach = msgTecah_maps.get(parent_pos).get(i).getTypeName();
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        //大数据中的方式
                        String mDataTeachName = null;
                        //0：自选 1：现购 2：预购
                        int mTeachName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                        mDataTeachName = mTeachName+"";
                        if (0 == mTeachName) {
                            mDataTeachName = "自选";
                        } else if (1 == mTeachName) {
                            mDataTeachName = "现购";
                        } else if (2 == mTeachName) {
                            mDataTeachName = "预购";
                        }
                        //大数据中的日期
                        String mDataExamDateName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        //大数据中的类型
                        String mDataTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        //大数据中的地点
                        String mDataAddressName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();

//                        String mConstTypeName = null;
//                        String mConstAddressName = null;
//                        if (clickTeach.equals(mDataTeachName) && "预购".equals(clickTeach) && "预购".equals(mDataTeachName)) {
                        if (clickTeach.equals(mDataTeachName)) {
                            //对比之后大数据中的上课时间
                            String mConstExamName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                            //对比之后大数据中的课程类型
                            String mConstTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                            //对比之后大数据中的上课地点
                            String mConstAddressName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();


                            /**考试年月**/
                            ExameDateList = mData.get(parent_pos).getExameDateList();
                            //0：自选 1：限购 2：预购
                            if ("预购".equals(clickTeach)) {
                                if (ExameDateList != null && !"".equals(ExameDateList)) {
                                    int ExameDateSize = ExameDateList.size();
                                    for (int a = 0; a < ExameDateSize; a++) {
                                        if (mConstExamName.equals(ExameDateList.get(a))) {
                                            exame_maps.get(parent_pos).get(a).setChecked(false);
                                        } else {
                                            exame_maps.get(parent_pos).get(a).setChecked(false);
                                        }
                                    }
                                    change_exameAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (ExameDateList != null && !"".equals(ExameDateList)) {
                                    int ExameDateSize = ExameDateList.size();
                                    for (int a = 0; a < ExameDateSize; a++) {
                                        if (mConstExamName.equals(ExameDateList.get(a))) {
                                            exame_maps.get(parent_pos).get(a).setChecked(true);
                                        } else {
                                            exame_maps.get(parent_pos).get(a).setChecked(false);
                                        }
                                    }
                                    change_exameAdapter.notifyDataSetChanged();
                                }
                            }


                            /**课程类型**/
                            ClassTypeTypeList = mData.get(parent_pos).getClassTypeTypeList();
                            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                                int ClassTypeSize = ClassTypeTypeList.size();
                                if (ClassTypeSize > 1) {

                                    for (int k = 0; k < ClassTypeSize; k++) {
                                        if (mConstTypeName.equals(ClassTypeTypeList.get(k))) {
                                            type_maps.get(parent_pos).get(k).setChecked(true);
                                        }
                                        /**在线和面授同时出现（暂时屏蔽）**/
                                        else {
                                            type_maps.get(parent_pos).get(k).setChecked(false);
                                        }
                                        change_typeAdapter.notifyDataSetChanged();
                                    }

                                } else {

                                    for (int k = 0; k < ClassTypeSize; k++) {
                                        if (mDataTypeName.equals(ClassTypeTypeList.get(k))) {
                                            type_maps.get(parent_pos).get(k).setChecked(true);
                                        }
                                        /**在线和面授同时出现（暂时屏蔽）**/
//                                    else {
//                                        type_maps.get(parent_pos).get(k).setChecked(false);
//                                    }
                                        change_typeAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            /**考试地点**/
                            ShoolPlaceList = mData.get(parent_pos).getShoolPlaceList();
                            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                                int ShoolSize = ShoolPlaceList.size();
                                for (int a = 0; a < ShoolSize; a++) {
                                    if (mConstAddressName.equals(ShoolPlaceList.get(a))) {
                                        address_maps.get(parent_pos).get(a).setChecked(true);
//                                        Is_AddressContrast = true;
                                    } else {
                                        address_maps.get(parent_pos).get(a).setChecked(false);
                                    }
                                }
                                if (null != change_addressAdapter) {
                                    change_addressAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }
                }
            }
            for (int j = 0; j < tecah_maps.get(parent_pos).size(); j++) {
                if (tecah_maps.get(parent_pos).get(j).isTypeSelect()) {
                    J_msgTeachClick = true;
                    break;
                }
            }


//            if (J_msgTeachClick == false) {
//                int ExameDateSize = ExameDateList.size();
//                for (int a = 0; a < ExameDateSize; a++) {
//                    exame_maps.get(parent_pos).get(a).setChecked(true);
//                }
//                change_exameAdapter.notifyDataSetChanged();
//            }
        }

    }

    /**
     * 考试年月处理
     **/
    public void HandleExameDate(Message msg) {
        msgExame_maps = (Map<Integer, ArrayList<ExameDateBean>>) msg.obj;
        Gson s = new Gson();
        String HodleClickType = null;
        for (int i = 0; i < msgExame_maps.get(parent_pos).size(); i++) {
            if (msgExame_maps.get(parent_pos).get(i).isTypeSelect()) {
                //点击的日期
                String clickDate = msgExame_maps.get(parent_pos).get(i).getTypeName();
                HodleClickType = clickDate;
                HodleClickDate = clickDate;
                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    price = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                    //大数据中的日期
                    String mDataExamDateName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                    if (clickDate.equals(mDataExamDateName)) {
                        //对比之后大数据中的课程类型
                        String mDataTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        //对比之后大数据中的上课地点
                        String mDataPlaceName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();

                        /**课程类型**/
                        ClassTypeTypeList = mData.get(parent_pos).getClassTypeTypeList();
                        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                            int ClassTypeSize = ClassTypeTypeList.size();
                            if (ClassTypeSize > 1) {

                                for (int k = 0; k < ClassTypeSize; k++) {
                                    if (mDataTypeName.equals(ClassTypeTypeList.get(k))) {
                                        type_maps.get(parent_pos).get(k).setChecked(true);
                                    }
                                    /**在线和面授同时出现（暂时屏蔽）**/
                                    //2017/12/21
//                                    else {
//                                        type_maps.get(parent_pos).get(k).setChecked(false);
//                                    }
                                    change_typeAdapter.notifyDataSetChanged();
                                }
                            } else {

                                for (int k = 0; k < ClassTypeSize; k++) {
                                    if (mDataTypeName.equals(ClassTypeTypeList.get(k))) {
                                        type_maps.get(parent_pos).get(k).setChecked(true);
                                    }
                                    /**在线和面授同时出现（暂时屏蔽）**/
//                                    else {
//                                        type_maps.get(parent_pos).get(k).setChecked(false);
//                                    }
                                    change_typeAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        /**考试地点**/
                        ShoolPlaceList = mData.get(parent_pos).getShoolPlaceList();
                        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                            int ShoolSize = ShoolPlaceList.size();
                            for (int a = 0; a < ShoolSize; a++) {
                                if (mDataPlaceName.equals(ShoolPlaceList.get(a))) {
                                    address_maps.get(parent_pos).get(a).setChecked(true);
                                    Is_AddressContrast = true;
                                } else {
                                    address_maps.get(parent_pos).get(a).setChecked(false);
                                }
                            }
                            if (null != change_addressAdapter) {
                                change_addressAdapter.notifyDataSetChanged();
                            }
                        }

//                        /**考试地点**/
//                        String clickType_show = null;
//                        ShoolPlaceList = mData.get(parent_pos).getShoolPlaceList();
//                        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
//                            int ShoolSize = ShoolPlaceList.size();
//                            if (J_msgTypeClick) {
//                                for (int a = 0; a < msgType_maps.get(parent_pos).size(); a++) {
//                                    if (msgType_maps.get(parent_pos).get(a).isTypeSelect()) {
//                                        //点击的类型
//                                        clickType_show = msgType_maps.get(parent_pos).get(a).getTypeName();
//                                    }
//                                }
//                                for (int y = 0; y < mData.get(parent_pos).getClassTypeList().size(); y++) {
//                                    //对比之后大数据中时间
//                                    String ExamName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDate();
//                                    //对比之后大数据中的类型
//                                    String TypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
//                                    if (clickType_show.equals(TypeName) && clickDate.equals(ExamName)) {
//                                        String Placce_show = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
//                                        for (int a = 0; a < ShoolSize; a++) {
//                                          String name=  address_maps.get(parent_pos).get(a).getTypeName();
//                                            if(Placce_show.equals(name)){
//                                                address_maps.get(parent_pos).get(a).setChecked(false);
//                                            }
//                                        }
//                                        change_addressAdapter.notifyDataSetChanged();
//
//
//                                    }
//                                }
//                            }
//                        }

                    } else {
//                        Log.i("点击的和大数据中的不同 -- ", "点击的和大数据中的不同 -- ");
                    }
                }
            } else {
//                Log.i("日期取消选择 -- > ", "日期取消选择");
            }
        }
        for (int j = 0; j < msgExame_maps.get(parent_pos).size(); j++) {
            if (msgExame_maps.get(parent_pos).get(j).isTypeSelect()) {
                J_msgExameClick = true;
                break;
            }
        }

        if (J_msgExameClick == false) {
            int ClassTypeSize = ClassTypeTypeList.size();
            for (int a = 0; a < ClassTypeSize; a++) {
                type_maps.get(parent_pos).get(a).setChecked(true);
            }
            change_typeAdapter.notifyDataSetChanged();
        }

        //判断3个选择是否都选中了
        if (J_msgExameClick && J_msgTypeClick && J_msgAddressClick) {

        }

        if (J_msgExameClick) {
            for (int j = 0; j < msgExame_maps.get(parent_pos).size(); j++) {
                if (msgExame_maps.get(parent_pos).get(j).isTypeSelect()) {
                    ClickDate = msgExame_maps.get(parent_pos).get(j).getTypeName();

                }
            }
        }
        if (J_msgExameClick && J_msgTypeClick) {
            if ("在线".equals(ClickType)) {
                String clickType = "";
                for (int i = 0; i < msgType_maps.get(parent_pos).size(); i++) {
                    if (msgType_maps.get(parent_pos).get(i).isTypeSelect()) {
                        clickType = msgType_maps.get(parent_pos).get(i).getTypeName();
                    }
                }
                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                    /**选中的课程时长**/
                    String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                    if (HodleClickType.equals(date) && clickType.equals(mTypeName)) {
                        HodlePrice = mData.get(parent_pos).getClassTypeList().get(j).getExpireDate();

                    }
                    String ClassType_Date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Date();
                    HodleViewFlag = false;
                    Message notify_msg = new Message();
                    notify_msg.what = 4;
                    notify_msg.obj = parent_pos;
                    handler.sendMessage(notify_msg);

                    /**选中的课程价格**/
                    String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                    HodleSalePrice = ClassType_salePrice;
                    HodleViewFlag = false;
                    Message SalePrice_msg = new Message();
                    SalePrice_msg.what = 5;
                    SalePrice_msg.obj = parent_pos;
                    handler.sendMessage(SalePrice_msg);


                    String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                    int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                    //0：自选 1：现购 2：预购

                    if (1 == Link_BuyType) {
                        if (date.equals(ClickDate) && type.equals(ClickType)) {
                            classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //班级ID
                            int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                            //版型ID
                            int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //Kid	Int	NO	套餐产品关联ID
                            int kid = mData.get(parent_pos).getLink_PKID();
                            //限购预购
                            Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                            int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                            packageProduct = new PackageProduct();
//                            packageProduct.setKid(kid);
//                            packageProduct.setClassId(classid);
//                            packageProduct.setClassTypeId(classtypeId);
//                            packageProduct.setBuyType(classBuyType);
//                            packageProductList.add(packageProduct);
//
//                            Message return_msg = new Message();
//                            return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                            return_msg.obj = packageProductList;
//                            returnId.sendMessage(return_msg);

                        }
                    } else {
                        if (type.equals(ClickType)) {
                            classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //班级ID
                            int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                            //版型ID
                            int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //Kid	Int	NO	套餐产品关联ID
                            int kid = mData.get(parent_pos).getLink_PKID();
                            //限购预购
                            Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                            int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                            packageProduct = new PackageProduct();
//                            packageProduct.setKid(kid);
//                            packageProduct.setClassId(classid);
//                            packageProduct.setClassTypeId(classtypeId);
//                            packageProduct.setBuyType(classBuyType);
//                            packageProductList.add(packageProduct);
//
//                            Message return_msg = new Message();
//                            return_msg.what = 0;
//                            return_msg.obj = packageProductList;
//                            returnId.sendMessage(return_msg);

                        }
                    }

                }
            } else {//
                if (J_msgAddressClick) {
                    String clickType = "";
                    if (!"".equals(msgType_maps) && null != msgType_maps) {
                        if (!"".equals(msgType_maps.get(parent_pos)) && null != msgType_maps.get(parent_pos)) {
                            for (int i = 0; i < msgType_maps.get(parent_pos).size(); i++) {
                                if (msgType_maps.get(parent_pos).get(i).isTypeSelect()) {
                                    clickType = msgType_maps.get(parent_pos).get(i).getTypeName();
                                }
                            }
                        }
                    }
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        String address = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                        /**选中的课程时长**/
                        String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        if (HodleClickType.equals(date) && clickType.equals(mTypeName)) {
                            HodlePrice = mData.get(parent_pos).getClassTypeList().get(j).getExpireDate();
                        }

                        String ClassType_Date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Date();

                        HodleViewFlag = false;
                        Message notify_msg = new Message();
                        notify_msg.what = 4;
                        notify_msg.obj = parent_pos;
                        handler.sendMessage(notify_msg);

                        /**选中的课程价格**/
                        String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                        HodleSalePrice = ClassType_salePrice;
                        HodleViewFlag = false;
                        Message SalePrice_msg = new Message();
                        SalePrice_msg.what = 5;
                        SalePrice_msg.obj = parent_pos;
                        handler.sendMessage(SalePrice_msg);

                        int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                        //0：自选 1：现购 2：预购
                        if (1 == Link_BuyType) {
                            if (date.equals(ClickDate) && type.equals(ClickType) && address.equals(ClickAddress)) {
                                classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //班级ID
                                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                                //版型ID
                                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //Kid	Int	NO	套餐产品关联ID
                                int kid = mData.get(parent_pos).getLink_PKID();
                                //限购预购
                                Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                                packageProduct = new PackageProduct();
//                                packageProduct.setKid(kid);
//                                packageProduct.setClassId(classid);
//                                packageProduct.setClassTypeId(classtypeId);
//                                packageProduct.setBuyType(classBuyType);
//                                packageProductList.add(packageProduct);
//
//                                Message return_msg = new Message();
//                                return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                                return_msg.obj = packageProductList;
//                                returnId.sendMessage(return_msg);
                            }
                        } else {
                            if (type.equals(ClickType) && address.equals(ClickAddress)) {
                                classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //班级ID
                                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                                //版型ID
                                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //Kid	Int	NO	套餐产品关联ID
                                int kid = mData.get(parent_pos).getLink_PKID();
                                //限购预购
                                Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                                packageProduct = new PackageProduct();
//                                packageProduct.setKid(kid);
//                                packageProduct.setClassId(classid);
//                                packageProduct.setClassTypeId(classtypeId);
//                                packageProduct.setBuyType(classBuyType);
//                                packageProductList.add(packageProduct);
//
//                                Message return_msg = new Message();
//                                return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                                return_msg.obj = packageProductList;
//                                returnId.sendMessage(return_msg);
                            }
                        }
                    }
                }
            }
        }


        int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
        //0：自选 1：现购 2：预购
        if (2 == Link_BuyType) {//预购(不记录考试年月)
            Log.i("预购项目 == > ", "不需要任何处理 ===> ");
        } else {//其他(记录考试年月)
            if (J_msgExameClick) {
                if (!"".equals(msgExame_maps) && null != msgExame_maps) {
                    for (int j = 0; j < msgExame_maps.get(parent_pos).size(); j++) {
                        if (msgExame_maps.get(parent_pos).get(j).isTypeSelect()) {
                            ClickDate = msgExame_maps.get(parent_pos).get(j).getTypeName();

                        }
                    }
                }

            }
            if (J_msgTypeClick) {
                if (!"".equals(msgType_maps) && null != msgType_maps) {
                    for (int j = 0; j < msgType_maps.get(parent_pos).size(); j++) {
                        if (msgType_maps.get(parent_pos).get(j).isTypeSelect()) {
                            ClickType = msgType_maps.get(parent_pos).get(j).getTypeName();

                        }
                    }
                }
            }
            if (J_msgAddressClick) {
                if (!"".equals(msgAddress_maps) && null != msgAddress_maps) {
                    for (int j = 0; j < msgAddress_maps.get(parent_pos).size(); j++) {
                        if (msgAddress_maps.get(parent_pos).get(j).isTypeSelect()) {
                            ClickAddress = msgAddress_maps.get(parent_pos).get(j).getTypeName();

                        }
                    }
                }

            }

            if (J_msgTypeClick) {
                if ("在线".equals(ClickType)) {
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        String Date_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        if (ClickDate.equals(Date_s) && ClickType.equals(type_s)) {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                        } else {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                        }
                    }
                } else {
                    if (J_msgAddressClick) {
                        for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                            String Date_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                            String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                            String Address_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                            if (ClickDate.equals(Date_s) && ClickType.equals(type_s) && ClickAddress.equals(Address_s)) {
                                mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                            } else {
                                mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                            }
                        }
                    }
                }
            }
        }


        for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
            if (mData.get(parent_pos).getClassTypeList().get(j).isChoose()) {
//                //班级ID
//                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
//                //套餐产品关联ID
//                int kid = mData.get(parent_pos).getLink_PKID();
//                //版型ID
//                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
//                //限购预购
//                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                packageProduct = new PackageProduct();
//                packageProduct.setKid(kid);
//                packageProduct.setClassId(classid);
//                packageProduct.setClassTypeId(classtypeId);
//                packageProduct.setBuyType(classBuyType);
//                packageProductList.add(packageProduct);
//
//                Message return_msg = new Message();
//                return_msg.what = 0;
//                return_msg.obj = packageProductList;
//                returnId.sendMessage(return_msg);
            }
        }

        Gson sdsa = new Gson();
        Log.i("日期新逻辑选择之后 ===> ", "" + sdsa.toJson(mData.get(parent_pos).getClassTypeList()));
    }

    /**
     * 课程类型处理
     **/
    public void HandleClassType(Message msg) {
        msgType_maps = (Map<Integer, ArrayList<TypeBean>>) msg.obj;
//        mTypeDate = (ArrayList<TypeBean>) msg.obj;
        String HodleClickType = "";
//        String DataClickmTypeName = null;
        for (int i = 0; i < msgType_maps.get(parent_pos).size(); i++) {
            if (msgType_maps.get(parent_pos).get(i).isTypeSelect()) {
                //点击的类型
                String clickType = msgType_maps.get(parent_pos).get(i).getTypeName();
                HodleClickType = clickType;
                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    //大数据中的类型
                    String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
//                    DataClickmTypeName = mTypeName;
                    if (clickType.equals(mTypeName)) {
                        //对比之后大数据中的课程类型
                        String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Name();
                        //对比之后大数据中的考试时间
                        String mExamDateName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        //对比之后大数据中的上课地点
                        String mDataPlaceName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();


                        /**考试年月**/
                        ExameDateList = mData.get(parent_pos).getExameDateList();
                        int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                        //0：自选 1：限购 2：预购
//                        if (1 == Link_BuyType) {
//                            if (ExameDateList != null && !"".equals(ExameDateList)) {
//                                int ExameDateSize = ExameDateList.size();
//                                for (int a = 0; a < ExameDateSize; a++) {
//                                    if (mExamDateName.equals(ExameDateList.get(a))) {
//                                        exame_maps.get(parent_pos).get(a).setChecked(true);

//                                    }
////                                else {
////                                    exame_maps.get(parent_pos).get(a).setChecked(false);
////                                }
//                                }
//                                change_exameAdapter.notifyDataSetChanged();
//                            }
//                        }
//                        if (ExameDateList != null && !"".equals(ExameDateList)) {
//                            int ExameDateSize = ExameDateList.size();
//                            for (int a = 0; a < ExameDateSize; a++) {
//                                if (mExamDateName.equals(ExameDateList.get(a))) {
//                                    exame_maps.get(parent_pos).get(a).setChecked(true);
//                                }
////                                else {
////                                    exame_maps.get(parent_pos).get(a).setChecked(false);
////                                }
//                            }
//                            change_exameAdapter.notifyDataSetChanged();
//                        }
                        /**考试地点**/
                        ShoolPlaceList = mData.get(parent_pos).getShoolPlaceList();
                        if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
                            int ShoolSize = ShoolPlaceList.size();
                            if ("在线".equals(clickType)) {
                                for (int a = 0; a < ShoolSize; a++) {
                                    address_maps.get(parent_pos).get(a).setChecked(false);
                                }
                                change_addressAdapter.notifyDataSetChanged();
                            } else {
//                                if (Is_AddressContrast == false) {
                                for (int a = 0; a < ShoolSize; a++) {
                                    if (mDataPlaceName.equals(ShoolPlaceList.get(a))) {
                                        address_maps.get(parent_pos).get(a).setChecked(true);
                                    }
                                }
//                              }

                                change_addressAdapter.notifyDataSetChanged();
                            }

                        }
                    } else {//没选中当前的
                        //对比之后大数据中的考试时间
                        String mExamDateName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        /**考试年月**/
                        ExameDateList = mData.get(parent_pos).getExameDateList();
//                        if (ExameDateList != null && !"".equals(ExameDateList)) {
//                            int ExameDateSize = ExameDateList.size();
//                            for (int a = 0; a < ExameDateSize; a++) {
//                                if (mExamDateName.equals(ExameDateList.get(a))) {
//                                    exame_maps.get(parent_pos).get(a).setChecked(false);
//                                }
//                            }
//                            change_exameAdapter.notifyDataSetChanged();
//                        }
                    }
                }
            }
        }
        for (int j = 0; j < msgType_maps.get(parent_pos).size(); j++) {
            if (msgType_maps.get(parent_pos).get(j).isTypeSelect()) {
                J_msgTypeClick = true;
                break;
            }
        }
        if (J_msgTypeClick) {
            for (int j = 0; j < msgType_maps.get(parent_pos).size(); j++) {
                if (msgType_maps.get(parent_pos).get(j).isTypeSelect()) {
                    ClickType = msgType_maps.get(parent_pos).get(j).getTypeName();

                }
            }
        }

        if (J_msgTypeClick == false) {
            int ShoolSize = ShoolPlaceList.size();
            if (Is_AddressContrast == false) {
                for (int a = 0; a < ShoolSize; a++) {
                    address_maps.get(parent_pos).get(a).setChecked(true);
                }
            }
            change_addressAdapter.notifyDataSetChanged();
            int ExameDateSize = ExameDateList.size();
            int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
            //0：自选 1：现购 2：预购
            if (1 == Link_BuyType) {
                for (int a = 0; a < ExameDateSize; a++) {
                    exame_maps.get(parent_pos).get(a).setChecked(true);
                }
                change_exameAdapter.notifyDataSetChanged();
            }

        }


        if (J_msgExameClick && J_msgTypeClick) {
            if ("在线".equals(ClickType)) {
                String clickData = "";
                for (int i = 0; i < msgExame_maps.get(parent_pos).size(); i++) {
                    if (msgExame_maps.get(parent_pos).get(i).isTypeSelect()) {
                        clickData = msgExame_maps.get(parent_pos).get(i).getTypeName();
                    }
                }
                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                    String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                    //大数据中的类型
                    String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                    /**选中的课程时长**/
                    int Link_BuyType_ = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
                    //0：自选 1：现购 2：预购
                    if (2 == Link_BuyType_) {
                        if (HodleClickType.equals(mTypeName)) {
                            HodlePrice = mData.get(parent_pos).getClassTypeList().get(j).getExpireDate();
                        }
                    } else {
                        if (HodleClickType.equals(mTypeName) && clickData.equals(date)) {
                            HodlePrice = mData.get(parent_pos).getClassTypeList().get(j).getExpireDate();
                        }
                    }

                    String ClassType_Date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Date();
                    HodleViewFlag = false;
                    Message notify_msg = new Message();
                    notify_msg.what = 4;
                    notify_msg.obj = parent_pos;
                    handler.sendMessage(notify_msg);

                    /**选中的课程价格**/
                    String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                    if (HodleClickType.equals(mTypeName) && clickData.equals(date)) {
                        String iii = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                        HodleSalePrice = iii;
                    }

//                    HodleSalePrice = ClassType_salePrice;
                    HodleViewFlag = false;
                    Message SalePrice_msg = new Message();
                    SalePrice_msg.what = 5;
                    SalePrice_msg.obj = parent_pos;
                    handler.sendMessage(SalePrice_msg);


                    int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                    //0：自选 1：现购 2：预购
                    if (1 == Link_BuyType) {
                        if (date.equals(ClickDate) && type.equals(ClickType)) {
                            classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //班级ID
                            int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                            //版型ID
                            int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //Kid	Int	NO	套餐产品关联ID
                            int kid = mData.get(parent_pos).getLink_PKID();
                            //限购预购
                            Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                            int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                            packageProduct = new PackageProduct();
//                            packageProduct.setKid(kid);
//                            packageProduct.setClassId(classid);
//                            packageProduct.setClassTypeId(classtypeId);
//                            packageProduct.setBuyType(classBuyType);
//                            packageProductList.add(packageProduct);
//                            Message return_msg = new Message();
//                            return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                            return_msg.obj = packageProductList;
//                            returnId.sendMessage(return_msg);

                        }
                    } else {
                        if (type.equals(ClickType)) {
                            classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //班级ID
                            int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                            //版型ID
                            int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //Kid	Int	NO	套餐产品关联ID
                            int kid = mData.get(parent_pos).getLink_PKID();
                            //限购预购
                            Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                            int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                            packageProduct = new PackageProduct();
//                            packageProduct.setKid(kid);
//                            packageProduct.setClassId(classid);
//                            packageProduct.setClassTypeId(classtypeId);
//                            packageProduct.setBuyType(classBuyType);
//                            packageProductList.add(packageProduct);
//                            Message return_msg = new Message();
//                            return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                            return_msg.obj = packageProductList;
//                            returnId.sendMessage(return_msg);

                        }
                    }

                }
            } else {//
                if (J_msgAddressClick) {
                    String clickData = "";
                    for (int i = 0; i < msgExame_maps.get(parent_pos).size(); i++) {
                        if (msgExame_maps.get(parent_pos).get(i).isTypeSelect()) {
                            clickData = msgExame_maps.get(parent_pos).get(i).getTypeName();
                        }
                    }
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        String address = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                        //大数据中的类型
                        String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        /**选中的课程时长**/
                        if (HodleClickType.equals(mTypeName) && clickData.equals(date)) {
                            HodlePrice = mData.get(parent_pos).getClassTypeList().get(j).getExpireDate();
                        }
                        String ClassType_Date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Date();

                        HodleViewFlag = false;
                        Message notify_msg = new Message();
                        notify_msg.what = 4;
                        notify_msg.obj = parent_pos;
                        handler.sendMessage(notify_msg);

                        /**选中的课程价格**/
                        String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                        if (HodleClickType.equals(mTypeName) && clickData.equals(date)) {
                            String iii = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                            HodleSalePrice = iii;
                        }
//                        HodleSalePrice = ClassType_salePrice;
                        HodleViewFlag = false;
                        Message SalePrice_msg = new Message();
                        SalePrice_msg.what = 5;
                        SalePrice_msg.obj = parent_pos;
                        handler.sendMessage(SalePrice_msg);

                        int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                        //0：自选 1：现购 2：预购
                        if (1 == Link_BuyType) {
                            if (date.equals(ClickDate) && type.equals(ClickType) && address.equals(ClickAddress)) {
                                classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //班级ID
                                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                                //版型ID
                                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //Kid	Int	NO	套餐产品关联ID
                                int kid = mData.get(parent_pos).getLink_PKID();
                                //限购预购
                                Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                                packageProduct = new PackageProduct();
//                                packageProduct.setKid(kid);
//                                packageProduct.setClassId(classid);
//                                packageProduct.setClassTypeId(classtypeId);
//                                packageProduct.setBuyType(classBuyType);
//                                packageProductList.add(packageProduct);
//
//                                Message return_msg = new Message();
//                                return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                                return_msg.obj = packageProductList;
//                                returnId.sendMessage(return_msg);
                            }
                        } else {
                            if (type.equals(ClickType) && address.equals(ClickAddress)) {
                                classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //班级ID
                                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                                //版型ID
                                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //Kid	Int	NO	套餐产品关联ID
                                int kid = mData.get(parent_pos).getLink_PKID();
                                //限购预购
                                Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                                packageProduct = new PackageProduct();
//                                packageProduct.setKid(kid);
//                                packageProduct.setClassId(classid);
//                                packageProduct.setClassTypeId(classtypeId);
//                                packageProduct.setBuyType(classBuyType);
//                                packageProductList.add(packageProduct);
//
//                                Message return_msg = new Message();
//                                return_msg.what = 0;
//                                return_msg.obj = packageProductList;
//                                returnId.sendMessage(return_msg);
                            }
                        }

                    }
                }
            }
        } else {
            //预购的价格选择
            for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                String address = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                //大数据中的类型
                String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                /**选中的课程价格**/
                String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                if (HodleClickType.equals(mTypeName)) {
                    String iii = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                    HodleSalePrice = iii;
                }
//                        HodleSalePrice = ClassType_salePrice;
                HodleViewFlag = false;
                Message SalePrice_msg = new Message();
                SalePrice_msg.what = 5;
                SalePrice_msg.obj = parent_pos;
                handler.sendMessage(SalePrice_msg);
            }

        }

        //如果三个选项都没有选中的状态,则恢复重置按钮
//        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
//            /**考试年月**/
//            ExameDateList = mData.get(0).getBody().getExameDateList();
//            if (ExameDateList != null && !"".equals(ExameDateList)) {
//                int ExameSize = ExameDateList.size();
//                for (int k = 0; k < ExameSize; k++) {
//                    examList.get(k).setChecked(true);
//                }
//                if (null != exameAdapter) {
//                    exameAdapter.notifyDataSetChanged();
//                }
//            }
//            /**课程类型**/
//            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
//            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
//                int ClassTypeSize = ClassTypeTypeList.size();
//                for (int k = 0; k < ClassTypeSize; k++) {
//                    typeList.get(k).setChecked(true);
//                }
//                if (null != typeAdapter) {
//                    typeAdapter.notifyDataSetChanged();
//                }
//            }
//            /**考试地点**/
//            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
//            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
//                int ShoolSize = ShoolPlaceList.size();
//                for (int a = 0; a < ShoolSize; a++) {
//                    addressList.get(a).setChecked(true);
//                }
//                if (null != addressAdapter) {
//                    addressAdapter.notifyDataSetChanged();
//                }
//            }
//        }

        int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
        //0：自选 1：现购 2：预购
        if (2 == Link_BuyType) {//预购(不记录考试年月)
            Log.i("预购(不记录考试年月)", " ==== ");
            Log.i("J_msgTypeClick  ", "" + J_msgTypeClick);
            Log.i("J_msgAddressClick  ", "" + J_msgAddressClick);
            if (J_msgTypeClick) {
                for (int j = 0; j < msgType_maps.get(parent_pos).size(); j++) {
                    if (msgType_maps.get(parent_pos).get(j).isTypeSelect()) {
                        ClickType = msgType_maps.get(parent_pos).get(j).getTypeName();

                    }
                }
            }
            if ("在线".equals(ClickType)) {
                Log.i("选择在线 == ", " == > ");
                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                    Log.i("ClickType = ", "" + ClickType);
                    if (ClickType.equals(type_s)) {
                        mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                    } else {
                        mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                    }

                }
            } else {
                Log.i("选择面授 == ", " == > ");
                if (J_msgAddressClick) {
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        String Address_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                        Log.i("ClickType = ", "" + ClickType);
                        Log.i("ClickAddress = ", "" + ClickAddress);
                        if (ClickType.equals(type_s) && ClickAddress.equals(Address_s)) {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                        } else {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                        }

                    }
                } else {
                    Log.i("选择面授 == ", " 但是地址未选择 == > ");
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                    }
                }

            }
        } else {//其他(记录考试年月)
            Log.i("其他(记录考试年月) ", " ==== ");
            if (J_msgExameClick && J_msgTypeClick) {
                if ("在线".equals(ClickType)) {
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        String Date_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        if (ClickDate.equals(Date_s) && ClickType.equals(type_s)) {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                        } else {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                        }
                    }
                } else {
                    if (J_msgAddressClick) {
                        for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                            String Date_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                            String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                            String Address_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                            if (ClickDate.equals(Date_s) && ClickType.equals(type_s) && ClickAddress.equals(Address_s)) {
                                mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                            } else {
                                mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                            }
                        }
                    }
                }

            }

        }
        for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
            if (mData.get(parent_pos).getClassTypeList().get(j).isChoose()) {
//                //班级ID
//                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
//                //套餐产品关联ID
//                int kid = mData.get(parent_pos).getLink_PKID();
//                //版型ID
//                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
//                //限购预购
//                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                packageProduct = new PackageProduct();
//                packageProduct.setKid(kid);
//                packageProduct.setClassId(classid);
//                packageProduct.setClassTypeId(classtypeId);
//                packageProduct.setBuyType(classBuyType);
//                packageProductList.add(packageProduct);
//
//                Message return_msg = new Message();
//                return_msg.what = 0;
//                return_msg.obj = packageProductList;
//                returnId.sendMessage(return_msg);
            }


        }

        Gson sdsa = new Gson();
        Log.i("新逻辑课程类型选择之后 --- ", "" + sdsa.toJson(mData.get(parent_pos).getClassTypeList()));
    }


    /**
     * 上课地点处理
     **/
    public void HandleAddressDate(Message msg) {
        msgAddress_maps = (Map<Integer, ArrayList<AddressBean>>) msg.obj;
//        mAddressDate = (ArrayList<AddressBean>) msg.obj;
//        Log.i("是否有选中 -- > ", "" + s.toJson(msgAddress_maps).toString());
        String HodleClickAdd = "";
        for (int i = 0; i < msgAddress_maps.get(parent_pos).size(); i++) {
            if (msgAddress_maps.get(parent_pos).get(i).isTypeSelect()) {
//                Log.i("上课地点选中了 -- > ", "" + msgAddress_maps.get(parent_pos).get(i).getTypeName());
                //点击的地点
                String clickAddress = msgAddress_maps.get(parent_pos).get(i).getTypeName();
                HodleClickAdd = clickAddress;

                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    //大数据中的地点
                    String mAddressName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                    if (clickAddress.equals(mAddressName)) {
                        //对比之后大数据中的考试时间
                        String mExamDateName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        //对比之后大数据中的课程类型
                        String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
//                        Log.i("mTypeName -- ", "" + mTypeName);
                        /**考试年月**/
                        ExameDateList = mData.get(parent_pos).getExameDateList();
                        if (ExameDateList != null && !"".equals(ExameDateList)) {
                            int ExameDateSize = ExameDateList.size();
//                            for (int a = 0; a < ExameDateSize; a++) {
//                                if (mExamDateName.equals(ExameDateList.get(a))) {
//                                    examList.get(a).setChecked(true);
//                                } else {
//                                    examList.get(a).setChecked(false);
//                                }
//                            }
//                            if (null != change_exameAdapter) {
//                                change_exameAdapter.notifyDataSetChanged();
//                            }
                        }
                        /**课程类型**/
                        ClassTypeTypeList = mData.get(parent_pos).getClassTypeTypeList();
                        if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
                            int ClassTypeSize = ClassTypeTypeList.size();
                            for (int k = 0; k < ClassTypeSize; k++) {
                                if (mTypeName.equals(ClassTypeTypeList.get(k))) {
//                                    typeList.get(k).setChecked(true);
//                                    type_maps.get(parent_pos).get(k).setChecked(true);
                                } else {
//                                    typeList.get(k).setChecked(false);
//                                    type_maps.get(parent_pos).get(k).setChecked(false);
                                }
                            }
//                            if (null != typeAdapter) {
//                                typeAdapter.notifyDataSetChanged();
//                            }
                        }
                    } else {
//                        Log.i("点击的和大数据中的不同 -- ", "点击的和大数据中的不同 -- ");
                    }
                }
            } else {
//                Log.i("上课地点取消选择 -- > ", "" + mAddressDate.get(i).getTypeName());
            }
        }
        for (int j = 0; j < msgAddress_maps.get(parent_pos).size(); j++) {
            if (msgAddress_maps.get(parent_pos).get(j).isTypeSelect()) {
                J_msgAddressClick = true;
                break;
            }
        }

        if (J_msgAddressClick) {
            for (int j = 0; j < msgAddress_maps.get(parent_pos).size(); j++) {
                if (msgAddress_maps.get(parent_pos).get(j).isTypeSelect()) {
                    ClickAddress = msgAddress_maps.get(parent_pos).get(j).getTypeName();
                }
            }
        }
//        Log.i("上课地点操作完成之后Address", "" + J_msgAddressClick);
//        Log.i("上课地点操作完成之后Type", "" + J_msgTypeClick);
//        Log.i("上课地点操作完成之后Exame", "" + J_msgExameClick);

        if (J_msgExameClick && J_msgTypeClick) {
//            Log.i("地点", "111");
//            Log.i("ClickType", " === > " + ClickType);
//            Log.i("ClickType", " === > " + ClickType);
            if ("在线".equals(ClickType)) {
                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                    String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                    /**选中的课程时长**/
                    String ClassType_Date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Date();
                    HodlePrice = mData.get(parent_pos).getClassTypeList().get(j).getExpireDate();
                    HodleViewFlag = false;
                    Message notify_msg = new Message();
                    notify_msg.what = 4;
                    notify_msg.obj = parent_pos;
                    handler.sendMessage(notify_msg);

                    /**选中的课程价格**/
                    String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
//                    Log.i("地点在线--价格 == > ", "编号 == > " + mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID());
//                    Log.i("地点在线--价格 == > ", "" + ClassType_salePrice);
                    HodleSalePrice = ClassType_salePrice;
                    HodleViewFlag = false;
                    Message SalePrice_msg = new Message();
                    SalePrice_msg.what = 5;
                    SalePrice_msg.obj = parent_pos;
                    handler.sendMessage(SalePrice_msg);


                    int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
//                    Log.i("地点--在线--预购还是现购 ", "" + Link_BuyType);
                    //0：自选 1：现购 2：预购
                    if (1 == Link_BuyType) {
                        if (date.equals(ClickDate) && type.equals(ClickType)) {
                            classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //班级ID
                            int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                            //版型ID
                            int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //Kid	Int	NO	套餐产品关联ID
                            int kid = mData.get(parent_pos).getLink_PKID();
                            //限购预购
//                            Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                            int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                            packageProduct = new PackageProduct();
//                            packageProduct.setKid(kid);
//                            packageProduct.setClassId(classid);
//                            packageProduct.setClassTypeId(classtypeId);
//                            packageProduct.setBuyType(classBuyType);
//                            packageProductList.add(packageProduct);
////                            Log.i("地点", "" + classtypeid);
//                            Message return_msg = new Message();
//                            return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                            return_msg.obj = packageProductList;
//                            returnId.sendMessage(return_msg);

                        }
                    } else {
                        if (type.equals(ClickType)) {
                            classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //班级ID
                            int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                            //版型ID
                            int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                            //Kid	Int	NO	套餐产品关联ID
                            int kid = mData.get(parent_pos).getLink_PKID();
                            //限购预购
                            Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                            int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                            packageProduct = new PackageProduct();
//                            packageProduct.setKid(kid);
//                            packageProduct.setClassId(classid);
//                            packageProduct.setClassTypeId(classtypeId);
//                            packageProduct.setBuyType(classBuyType);
//                            packageProductList.add(packageProduct);
////                            Log.i("地点", "" + classtypeid);
//                            Message return_msg = new Message();
//                            return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                            return_msg.obj = packageProductList;
//                            returnId.sendMessage(return_msg);

                        }
                    }

                }
            } else {//
                if (J_msgAddressClick) {
                    String clickDate = "";
                    String clickType = "";
                    String clickAdd = "";
                    for (int i = 0; i < msgExame_maps.get(parent_pos).size(); i++) {
                        if (msgExame_maps.get(parent_pos).get(i).isTypeSelect()) {
                            clickDate = msgExame_maps.get(parent_pos).get(i).getTypeName();
                        }
                    }
                    for (int i = 0; i < msgType_maps.get(parent_pos).size(); i++) {
                        if (msgType_maps.get(parent_pos).get(i).isTypeSelect()) {
                            clickType = msgType_maps.get(parent_pos).get(i).getTypeName();
                        }
                    }
                    for (int i = 0; i < msgAddress_maps.get(parent_pos).size(); i++) {
                        if (msgAddress_maps.get(parent_pos).get(i).isTypeSelect()) {
                            clickAdd = msgAddress_maps.get(parent_pos).get(i).getTypeName();
                        }
                    }

                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                        String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        String address = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                        /**选中的课程时长**/
                        if (clickDate.equals(date) && clickType.equals(type) && clickAdd.equals(address)) {
                            HodlePrice = mData.get(parent_pos).getClassTypeList().get(j).getExpireDate();
                        }
                        String ClassType_Date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Date();

                        HodleViewFlag = false;
                        Message notify_msg = new Message();
                        notify_msg.what = 4;
                        notify_msg.obj = parent_pos;
                        handler.sendMessage(notify_msg);

                        /**选中的课程价格**/
                        String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                        if (clickDate.equals(date) && clickType.equals(type) && clickAdd.equals(address)) {
                            String iii = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
//                            Log.i("选中的面授课程价格***", "" + iii);
                            HodleSalePrice = iii;
                        }
//                        Log.i("地点面授--价格 == > ", "编号 == > " + mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID());
//                        Log.i("地点面授--价格 == > ", "" + ClassType_salePrice);
//                        HodleSalePrice = ClassType_salePrice;
                        HodleViewFlag = false;
                        Message SalePrice_msg = new Message();
                        SalePrice_msg.what = 5;
                        SalePrice_msg.obj = parent_pos;
                        handler.sendMessage(SalePrice_msg);


                        int Link_BuyType = mData.get(parent_pos).getLink_BuyType();

                        //0：自选 1：现购 2：预购
                        if (1 == Link_BuyType) {
                            if (date.equals(ClickDate) && type.equals(ClickType) && address.equals(ClickAddress)) {
                                classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //班级ID
                                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                                //版型ID
                                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //Kid	Int	NO	套餐产品关联ID
                                int kid = mData.get(parent_pos).getLink_PKID();
                                //限购预购
                                Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                                packageProduct = new PackageProduct();
//                                packageProduct.setKid(kid);
//                                packageProduct.setClassId(classid);
//                                packageProduct.setClassTypeId(classtypeId);
//                                packageProduct.setBuyType(classBuyType);
//                                packageProductList.add(packageProduct);
//
//                                Message return_msg = new Message();
//                                return_msg.what = 0;
////                        return_msg.obj = classtypeid;
//                                return_msg.obj = packageProductList;
//                                returnId.sendMessage(return_msg);
                            }
                        } else {
                            if (type.equals(ClickType) && address.equals(ClickAddress)) {
                                classtypeid = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //班级ID
                                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
                                //版型ID
                                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
                                //Kid	Int	NO	套餐产品关联ID
                                int kid = mData.get(parent_pos).getLink_PKID();
                                //限购预购
                                Link_BuyType = mData.get(parent_pos).getLink_BuyType();
                                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                                packageProduct = new PackageProduct();
//                                packageProduct.setKid(kid);
//                                packageProduct.setClassId(classid);
//                                packageProduct.setClassTypeId(classtypeId);
//                                packageProduct.setBuyType(classBuyType);
//                                packageProductList.add(packageProduct);
//
//                                Message return_msg = new Message();
//                                return_msg.what = 0;
//                                return_msg.obj = packageProductList;
//                                returnId.sendMessage(return_msg);
                            }
                        }

                    }
                }
            }
        } else {
            //预购的价格选择
//            String clickDate = "";
            String clickType = "";
            String clickAdd = "";
//            for (int i = 0; i < msgExame_maps.get(parent_pos).size(); i++) {
//                if (msgExame_maps.get(parent_pos).get(i).isTypeSelect()) {
//                    clickDate = msgExame_maps.get(parent_pos).get(i).getTypeName();
//                }
//            }
            if (J_msgTypeClick) {
                for (int i = 0; i < msgType_maps.get(parent_pos).size(); i++) {
                    if (msgType_maps.get(parent_pos).get(i).isTypeSelect()) {
                        clickType = msgType_maps.get(parent_pos).get(i).getTypeName();
                    }
                }
            }

            if (J_msgAddressClick) {
                for (int i = 0; i < msgAddress_maps.get(parent_pos).size(); i++) {
                    if (msgAddress_maps.get(parent_pos).get(i).isTypeSelect()) {
                        clickAdd = msgAddress_maps.get(parent_pos).get(i).getTypeName();
                    }
                }
            }

            for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                String date = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                String type = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                String address = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                //大数据中的类型
                String mTypeName = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                /**选中的课程价格**/
                String ClassType_salePrice = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
                if (clickType.equals(type) && clickAdd.equals(address)) {
                    String iii = mData.get(parent_pos).getClassTypeList().get(j).getClassType_SalePrice();
//                    Log.i("选中的课程面授价格**** ", "" + iii);
                    HodleSalePrice = iii;
                }
//                Log.i("课程类型面授价格 == > ", "编号 == > " + mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID());
//                Log.i("课程类型面授价格 == > ", "" + ClassType_salePrice);
//                        HodleSalePrice = ClassType_salePrice;
                HodleViewFlag = false;
                Message SalePrice_msg = new Message();
                SalePrice_msg.what = 5;
                SalePrice_msg.obj = parent_pos;
                handler.sendMessage(SalePrice_msg);
            }

        }
        //如果三个选项都没有选中的状态,则恢复重置按钮
//        if (!msgExameClick && !msgTypeClick && !msgAddressClick) {
//            /**考试年月**/
//            ExameDateList = mData.get(0).getBody().getExameDateList();
//            if (ExameDateList != null && !"".equals(ExameDateList)) {
//                int ExameSize = ExameDateList.size();
//                for (int k = 0; k < ExameSize; k++) {
//                    examList.get(k).setChecked(true);
//                }
//                if (null != exameAdapter) {
//                    exameAdapter.notifyDataSetChanged();
//                }
//            }
//            /**课程类型**/
//            ClassTypeTypeList = mData.get(0).getBody().getClassTypeTypeList();
//            if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
//                int ClassTypeSize = ClassTypeTypeList.size();
//                for (int k = 0; k < ClassTypeSize; k++) {
//                    typeList.get(k).setChecked(true);
//                }
//                if (null != typeAdapter) {
//                    typeAdapter.notifyDataSetChanged();
//                }
//            }
//            /**考试地点**/
//            ShoolPlaceList = mData.get(0).getBody().getShoolPlaceList();
//            if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
//                int ShoolSize = ShoolPlaceList.size();
//                for (int a = 0; a < ShoolSize; a++) {
//                    addressList.get(a).setChecked(true);
//                }
//                if (null != addressAdapter) {
//                    addressAdapter.notifyDataSetChanged();
//                }
//            }
//        }

        int Link_BuyType = mData.get(parent_pos).getLink_BuyType();
        //0：自选 1：现购 2：预购
        if (2 == Link_BuyType) {//预购(不记录考试年月)
            if (J_msgTypeClick) {
                for (int j = 0; j < msgType_maps.get(parent_pos).size(); j++) {
                    if (msgType_maps.get(parent_pos).get(j).isTypeSelect()) {
                        ClickType = msgType_maps.get(parent_pos).get(j).getTypeName();
                    }
                }
            }
            Log.i("J_msgAddressClick == ", " >>> ");
            if (J_msgAddressClick) {
                Log.i("ClickType == ", " >>> " + ClickType);
                if ("面授".equals(ClickType)) {
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                        String Address_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                        if (ClickType.equals(type_s) && ClickAddress.equals(Address_s)) {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                        } else {
                            mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                        }
                    }
                } else {
                    Log.i("地点选择了 == > ", "但是在线的 ==>");
                }
            } else { //取消地址选择(清空false)
                if ("面授".equals(ClickType)) {
                    for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                        mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                    }
                }

            }
        } else {//其他(记录考试年月)
            if (J_msgExameClick && J_msgTypeClick && "面授".equals(ClickType) && J_msgAddressClick) {
                Log.i("日.类.面.地 ==> ", "都选中 == > ");
                for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
                    String Date_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_ExamDateName();
                    String type_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_TypeName();
                    String Address_s = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PlaceName();
                    if (ClickDate.equals(Date_s) && ClickType.equals(type_s) && ClickAddress.equals(Address_s)) {
                        mData.get(parent_pos).getClassTypeList().get(j).setChoose(true);
                    } else {
                        mData.get(parent_pos).getClassTypeList().get(j).setChoose(false);
                    }
                }
            }


        }
        for (int j = 0; j < mData.get(parent_pos).getClassTypeList().size(); j++) {
            if (mData.get(parent_pos).getClassTypeList().get(j).isChoose()) {
//                //班级ID
//                int classid = mData.get(parent_pos).getClassInfo().getClass_PKID();
//                //套餐产品关联ID
//                int kid = mData.get(parent_pos).getLink_PKID();
//                //版型ID
//                int classtypeId = mData.get(parent_pos).getClassTypeList().get(j).getClassType_PKID();
//                //限购预购
//                int classBuyType = mData.get(parent_pos).getClassTypeList().get(j).getClassType_Type();
//                packageProduct = new PackageProduct();
//                packageProduct.setKid(kid);
//                packageProduct.setClassId(classid);
//                packageProduct.setClassTypeId(classtypeId);
//                packageProduct.setBuyType(classBuyType);
//                packageProductList.add(packageProduct);
//
//
//                Message return_msg = new Message();
//                return_msg.what = 0;
//                return_msg.obj = packageProductList;
//                returnId.sendMessage(return_msg);
            }

        }

        Gson sdsa = new Gson();
        Log.i("地址新逻辑选择之后 ===>", "" + sdsa.toJson(mData.get(parent_pos).getClassTypeList()));
    }


}