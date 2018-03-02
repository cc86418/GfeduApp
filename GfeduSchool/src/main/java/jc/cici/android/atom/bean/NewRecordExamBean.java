package jc.cici.android.atom.bean;

/**
 * 新做题记录实体
 * Created by atom on 2018/1/12.
 */

public class NewRecordExamBean {

    public CommonBean<TikuHomeBean> tikuHomeBean;
    public CommonBean<HistoryExamChoseBean> testPaperBeanCommonBean;

    public NewRecordExamBean(CommonBean<TikuHomeBean> tikuHomeBean, CommonBean<HistoryExamChoseBean> testPaperBeanCommonBean) {
        this.tikuHomeBean = tikuHomeBean;
        this.testPaperBeanCommonBean = testPaperBeanCommonBean;
    }
}
