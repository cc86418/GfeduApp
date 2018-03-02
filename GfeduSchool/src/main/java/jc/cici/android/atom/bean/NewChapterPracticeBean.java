package jc.cici.android.atom.bean;

/**
 * 构建后的知识点实体
 * Created by atom on 2018/1/4.
 */

public class NewChapterPracticeBean {
    public CommonBean<TikuHomeBean> tikuHomeBean;
    public CommonBean<ExamKnowledgeBean> examKnowledgeBean;

    public NewChapterPracticeBean(CommonBean<TikuHomeBean> tikuHomeBean, CommonBean<ExamKnowledgeBean> examKnowledgeBean) {
        this.tikuHomeBean = tikuHomeBean;
        this.examKnowledgeBean = examKnowledgeBean;
    }
}
