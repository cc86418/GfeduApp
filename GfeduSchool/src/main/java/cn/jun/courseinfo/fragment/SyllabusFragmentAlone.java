//package cn.jun.courseinfo.fragment;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ExpandableListView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//
//import cn.jun.courseinfo.adapter.ExpandableListAdapter;
//import cn.jun.courseinfo.ui.MyScrollView;
//import cn.jun.courseinfo.ui.PublicStaticClass;
//import jc.cici.android.R;
//
//import static cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivity.SyllabusDetailsFragmentAloneContent;
//
//
//public class SyllabusFragmentAlone extends Fragment implements ExpandableListView.OnGroupExpandListener,
//        ExpandableListAdapter.OnChildTreeViewClickListener {
//    private View mView;
//    private ExpandableListView expandableListView;
//    private ExpandableListAdapter adapter;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        mView = inflater.inflate(R.layout.fragment_two_alone, container, false);
////        initView();
//        initDate();
//        return mView;
//    }
//
//    private void initView() {
//        MyScrollView twoScrollView = (MyScrollView) mView.findViewById(R.id.twoScrollview);
//        twoScrollView.setScrollListener(new MyScrollView.ScrollListener() {
//            @Override
//            public void onScrollToBottom() {
//
//            }
//
//            @Override
//            public void onScrollToTop() {
//
//            }
//
//            @Override
//            public void onScroll(int scrollY) {
//                if (scrollY == 0) {
//                    PublicStaticClass.IsTop = true;
//                } else {
//                    PublicStaticClass.IsTop = false;
//                }
//            }
//
//            @Override
//            public void notBottom() {
//
//            }
//
//        });
//    }
//
//    private void initDate() {
//        expandableListView = (ExpandableListView) mView.findViewById(R.id.myList);
//        expandableListView.setDivider(null);
//        expandableListView.setGroupIndicator(null);
//        Gson s =  new Gson();
//        Log.i("数据------》 ","" +s.toJson(SyllabusDetailsFragmentAloneContent).toString());
//        if (!"".equals(SyllabusDetailsFragmentAloneContent) && null != SyllabusDetailsFragmentAloneContent) {
//            adapter = new ExpandableListAdapter(getActivity(), SyllabusDetailsFragmentAloneContent);
//            expandableListView.setAdapter(adapter);
////            for (int i = 0; i < SyllabusDetailsFragmentAloneContent.size(); i++) {
////                expandableListView.expandGroup(i);
////            }
////            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
////                @Override
////                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
////                    return true;
////                }
////            });
//
//            adapter.setOnChildTreeViewClickListener(SyllabusFragmentAlone.this);
//
//        }
//
//    }
//
//
//    //展开一项，关闭其他项，保证每次只能展开一项
//    @Override
//    public void onGroupExpand(int groupPosition) {
//        Log.i("onGroupExpand ", "onGroupExpand");
//        if (!"".equals(SyllabusDetailsFragmentAloneContent) && null != SyllabusDetailsFragmentAloneContent) {
//            if (!"".equals(SyllabusDetailsFragmentAloneContent) && null != SyllabusDetailsFragmentAloneContent) {
//                adapter = new ExpandableListAdapter(getActivity(), SyllabusDetailsFragmentAloneContent);
//                expandableListView.setAdapter(adapter);
//                adapter.setOnChildTreeViewClickListener(SyllabusFragmentAlone.this);
//            }
//        }
//
//    }
//
//    //击子ExpandableListView的子项时，回调本方法，根据下标获取值来做相应的操作
//    @Override
//    public void onClickPosition(int parentPosition, int groupPosition, int childPosition) {
//        String childName = SyllabusDetailsFragmentAloneContent.get(0).getBody().getOutLineList().get(parentPosition)
//                .getLevelTwo().get(groupPosition).getList().get(childPosition).getLevel_ShowName();
//        Toast.makeText(
//                getActivity(),
//                "点击的下标为： parentPosition=" + parentPosition
//                        + "   groupPosition=" + groupPosition
//                        + "   childPosition=" + childPosition + "\n点击的是："
//                        + childName, Toast.LENGTH_SHORT).show();
//        Log.i("点击的parentPosition ==> ", "" + parentPosition);
//        Log.i("点击的groupPosition ==> ", "" + groupPosition);
//        Log.i("点击的childPosition ==> ", "" + childPosition);
//        Log.i("点击的内容 ==> ", "" + childName);
//
//    }
//}
