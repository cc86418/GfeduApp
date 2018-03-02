package jc.cici.android.atom.bean;

/**
 * 新构建错题or收藏实体
 * Created by atom on 2018/1/5.
 */

public class NewErrorOrFavorBean {

    public CommonBean<TikuHomeBean> tikuHomeBean;
    public CommonBean<ErrorOrFavorTypeBean> errorOrFavorTypeBean;

    public NewErrorOrFavorBean(CommonBean<TikuHomeBean> tikuHomeBean, CommonBean<ErrorOrFavorTypeBean> errorOrFavorTypeBean) {
        this.tikuHomeBean = tikuHomeBean;
        this.errorOrFavorTypeBean = errorOrFavorTypeBean;
    }
}
