//package cn.jun.courseinfo.adapter.base;
//
//import android.content.Context;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.jun.bean.PackageClassTypeBean;
//import cn.jun.courseinfo.bean.AddressBean;
//import cn.jun.courseinfo.bean.ExameDateBean;
//import cn.jun.courseinfo.bean.TeachBean;
//import cn.jun.courseinfo.bean.TypeBean;
//import cn.jun.courseinfo.course_adapter.AddressRecyclerAdapter;
//import cn.jun.courseinfo.course_adapter.ExameDateRecyclerAdapter;
//import cn.jun.courseinfo.j_course_adapter.J_TeachRecyclerAdapter;
//import cn.jun.courseinfo.course_adapter.TypeRecyclerAdapter;
//import jc.cici.android.R;
//import jc.cici.android.atom.view.MyGridLayoutManager;
//
//import static jc.cici.android.atom.ui.tiku.TiKuContentFragment.handler;
//
//
//public class J_RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    public static final int TYPE_HEADER = 0;  //说明是带有Header的
//    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
//    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
//
//    //获取从Activity中传递过来每个item的数据集合
////    private List<String> mDatas;
//    //HeaderView, FooterView
//    private View mHeaderView;
//    private View mFooterView;
//
//    /****/
//    private Context mCtx;
//    private ArrayList<PackageClassTypeBean.Body.ProductList> mData;
//    private ArrayList<String> TeachTypeList;
//    private ArrayList<String> ClassTypeTypeList;
//    private ArrayList<String> ExameDateList;
//    private ArrayList<String> ShoolPlaceList;
//
//    private TeachBean teachBean;
//    private ArrayList<TeachBean> teachList = new ArrayList<>();
//    private J_TeachRecyclerAdapter teachAdapter;
//    private ExameDateBean exameDateBean;
//    private ArrayList<ExameDateBean> examList = new ArrayList<>();
//    private ExameDateRecyclerAdapter exameAdapter;
//    private TypeBean typeBean;
//    private ArrayList<TypeBean> typeList = new ArrayList<>();
//    private TypeRecyclerAdapter typeAdapter;
//    private AddressBean addressBean;
//    private ArrayList<AddressBean> addressList = new ArrayList<>();
//    private AddressRecyclerAdapter addressAdapter;
//
//    //Message传递过来的
//    private ArrayList<TeachBean> mTeachDate;
//    private ArrayList<ExameDateBean> mExameDate;
//    private ArrayList<TypeBean> mTypeDate;
//    private ArrayList<AddressBean> mAddressDate;
//
//
//    public static boolean msgTeachClick = false;
//    public static boolean msgExameClick = false;
//    public static boolean msgTypeClick = false;
//    public static boolean msgAddressClick = false;
//
//
//    //构造函数
//    public J_RecyclerAdapter(Context context, List items) {
//        this.mCtx = context;
//        this.mData = (ArrayList<PackageClassTypeBean.Body.ProductList>) items;
//    }
//
//    //HeaderView和FooterView的get和set函数
//    public View getHeaderView() {
//        return mHeaderView;
//    }
//
//    public void setHeaderView(View headerView) {
//        mHeaderView = headerView;
//        notifyItemInserted(0);
//    }
//
//    /**
//     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
//     */
//    @Override
//    public int getItemViewType(int position) {
//        if (mHeaderView == null && mFooterView == null) {
//            return TYPE_NORMAL;
//        }
//        if (position == 0) {
//            //第一个item应该加载Header
//            return TYPE_HEADER;
//        }
//        if (position == getItemCount() - 1) {
//            //最后一个,应该加载Footer
//            return TYPE_FOOTER;
//        }
//        return TYPE_NORMAL;
//    }
//
//    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (mHeaderView != null && viewType == TYPE_HEADER) {
//            return new ListHolder(mHeaderView);
//        }
//        if (mFooterView != null && viewType == TYPE_FOOTER) {
//            return new ListHolder(mFooterView);
//        }
//        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_list_items, parent, false);
//        return new ListHolder(layout);
//    }
//
//    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (getItemViewType(position) == TYPE_NORMAL) {
//            if (holder instanceof ListHolder) {
//                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
////                ((ListHolder) holder).tv.setText(mData.get(position - 1));
//                /**授课方式**/
//                teachBean = new TeachBean();
//                teachBean.setTypeName("限购");
//                teachList.add(teachBean);
//                teachBean = new TeachBean();
//                teachBean.setTypeName("预购");
//                teachList.add(teachBean);
//                ((ListHolder) holder).teachRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//                ((ListHolder) holder).teachRecyclerView.setNestedScrollingEnabled(false);
//                teachAdapter = new J_TeachRecyclerAdapter(mCtx, teachList, handler,0);
//                ((ListHolder) holder).teachRecyclerView.setAdapter(teachAdapter);
//                /**考试年月**/
//                ExameDateList = mData.get(position - 1).getExameDateList();
//                if (ExameDateList != null && !"".equals(ExameDateList)) {
//                    int ExameSize = ExameDateList.size();
//                    for (int i = 0; i < ExameSize; i++) {
//                        exameDateBean = new ExameDateBean();
//                        exameDateBean.setTypeName(ExameDateList.get(i));
//                        examList.add(exameDateBean);
//                    }
//                }
//                ((ListHolder) holder).featureRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//                ((ListHolder) holder).featureRecyclerView.setNestedScrollingEnabled(false);
//                exameAdapter = new ExameDateRecyclerAdapter(mCtx, examList, handler);
//                ((ListHolder) holder).featureRecyclerView.setAdapter(exameAdapter);
//
//                /**课程类型**/
//                ClassTypeTypeList = mData.get(position - 1).getClassTypeTypeList();
//                if (ClassTypeTypeList != null && !"".equals(ClassTypeTypeList)) {
//                    int ClassTypeSize = ClassTypeTypeList.size();
//                    for (int i = 0; i < ClassTypeSize; i++) {
//                        typeBean = new TypeBean();
//                        typeBean.setTypeName(ClassTypeTypeList.get(i));
//                        typeBean.setTypeSelect(false);
//                        typeList.add(typeBean);
//                    }
//                }
//                ((ListHolder) holder).typeRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//                ((ListHolder) holder).typeRecyclerView.setNestedScrollingEnabled(false);
//                typeAdapter = new TypeRecyclerAdapter(mCtx, typeList, handler);
//                ((ListHolder) holder).typeRecyclerView.setAdapter(typeAdapter);
//
//                /**考试地点**/
//                ShoolPlaceList = mData.get(position - 1).getShoolPlaceList();
//                if (ShoolPlaceList != null && !"".equals(ShoolPlaceList)) {
//                    int ShoolSize = ShoolPlaceList.size();
//                    for (int i = 0; i < ShoolSize; i++) {
//                        addressBean = new AddressBean();
//                        addressBean.setTypeName(ShoolPlaceList.get(i));
//                        addressList.add(addressBean);
//                    }
//                }
//                ((ListHolder) holder).addressRecyclerView.setLayoutManager(new MyGridLayoutManager(mCtx, 3, GridLayoutManager.VERTICAL, false));
//                ((ListHolder) holder).addressRecyclerView.setNestedScrollingEnabled(false);
//                addressAdapter = new AddressRecyclerAdapter(mCtx, addressList, handler);
//                ((ListHolder) holder).addressRecyclerView.setAdapter(addressAdapter);
//                return;
//            }
//            return;
//        } else if (getItemViewType(position) == TYPE_HEADER) {
//            return;
//        } else {
//            return;
//        }
//    }
//
//    //在这里面加载ListView中的每个item的布局
//    public class ListHolder extends RecyclerView.ViewHolder {
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
//        TextView tv;
//
//        public ListHolder(View itemView) {
//            super(itemView);
//            //如果是headerview或者是footerview,直接返回
//            if (itemView == mHeaderView) {
//                return;
//            }
//            if (itemView == mFooterView) {
//                return;
//            }
//            ButterKnife.bind(this, itemView);
//        }
//    }
//
//    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
//    @Override
//    public int getItemCount() {
//        if (mHeaderView == null && mFooterView == null) {
//            return mData.size();
//        } else if (mHeaderView == null && mFooterView != null) {
//            return mData.size() + 1;
//        } else if (mHeaderView != null && mFooterView == null) {
//            return mData.size() + 1;
//        } else {
//            return mData.size() + 2;
//        }
//    }
//}
//
