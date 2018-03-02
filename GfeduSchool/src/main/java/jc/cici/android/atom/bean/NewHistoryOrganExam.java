package jc.cici.android.atom.bean;

/**
 * 组卷历史合并请求实体
 * Created by atom on 2018/1/2.
 */

public class NewHistoryOrganExam {

    public CommonBean<TikuHomeBean> tikuHomeBean;
    public CommonBean<HistoryExamChoseBean> historyExamChoseBean;

    public NewHistoryOrganExam(CommonBean<TikuHomeBean> tikuHomeBean, CommonBean<HistoryExamChoseBean> historyExamChoseBean) {
        this.tikuHomeBean = tikuHomeBean;
        this.historyExamChoseBean = historyExamChoseBean;
    }
}
