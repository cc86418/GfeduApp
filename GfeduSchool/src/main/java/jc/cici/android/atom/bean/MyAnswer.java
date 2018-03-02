package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 我的回答内容
 * Created by atom on 2017/6/24.
 */

public class MyAnswer {
    // 问题id
    private int AnswerId;
    // 问题内容
    private String AnswerContent;
    // 问题添加时间
    private String AnswerAddTime;
    // 答案状态
    private int AnswerStatus;
    // 图片数量
    private int ImageCount;
    // 答案图片
    private ArrayList<String> AnswerImageUrl;
    // 追问数量
    private int AfterQuesCount;
    // 评论数量
    private int CommentsCount;
    // 点赞数量
    private int PraiseCount;
    // 是否点赞 1.已赞   2.未赞
    private int UserPraiseStatus;
    // 所有追问追答数量
    private int AfterCount;
    // 是否删除
    private int IsDelete;
    // 删除理由
    private String DeleteDetail;

    public int getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(int isDelete) {
        IsDelete = isDelete;
    }

    public String getDeleteDetail() {
        return DeleteDetail;
    }

    public void setDeleteDetail(String deleteDetail) {
        DeleteDetail = deleteDetail;
    }

    public int getAfterCount() {
        return AfterCount;
    }

    public void setAfterCount(int afterCount) {
        AfterCount = afterCount;
    }

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public String getAnswerContent() {
        return AnswerContent;
    }

    public void setAnswerContent(String answerContent) {
        AnswerContent = answerContent;
    }

    public String getAnswerAddTime() {
        return AnswerAddTime;
    }

    public void setAnswerAddTime(String answerAddTime) {
        AnswerAddTime = answerAddTime;
    }

    public int getAnswerStatus() {
        return AnswerStatus;
    }

    public void setAnswerStatus(int answerStatus) {
        AnswerStatus = answerStatus;
    }

    public int getImageCount() {
        return ImageCount;
    }

    public void setImageCount(int imageCount) {
        ImageCount = imageCount;
    }

    public ArrayList<String> getAnswerImageUrl() {
        return AnswerImageUrl;
    }

    public void setAnswerImageUrl(ArrayList<String> answerImageUrl) {
        AnswerImageUrl = answerImageUrl;
    }

    public int getAfterQuesCount() {
        return AfterQuesCount;
    }

    public void setAfterQuesCount(int afterQuesCount) {
        AfterQuesCount = afterQuesCount;
    }

    public int getCommentsCount() {
        return CommentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        CommentsCount = commentsCount;
    }

    public int getPraiseCount() {
        return PraiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        PraiseCount = praiseCount;
    }

    public int getUserPraiseStatus() {
        return UserPraiseStatus;
    }

    public void setUserPraiseStatus(int userPraiseStatus) {
        UserPraiseStatus = userPraiseStatus;
    }
}
