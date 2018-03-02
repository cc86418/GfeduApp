package jc.cici.android.atom.bean;

/**
 * 新组卷模考，模拟测试实体
 * Created by atom on 2018/1/23.
 */

public class NewOrganExamBean {

    public CommonBean<TikuHomeBean> tikuHomeBean;
    public CommonBean<TestPaperBean> historyExamChoseBean;

    public NewOrganExamBean(CommonBean<TikuHomeBean> tikuHomeBean, CommonBean<TestPaperBean> historyExamChoseBean) {
        this.tikuHomeBean = tikuHomeBean;
        this.historyExamChoseBean = historyExamChoseBean;
    }
}
