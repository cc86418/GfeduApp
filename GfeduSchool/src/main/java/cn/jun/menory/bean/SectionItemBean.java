package cn.jun.menory.bean;



public class SectionItemBean {
    public boolean editMode = false; // true 编辑模式，false 非编辑模式
    public boolean isSectionHeader = false; // true section 头部，false 普通节点
    public int totalItem; // 该分类的普通节点数

    public VideoClassStageBean videoClassStageBean; // 头部节点
    public VideoItemBean videoItemBean; // 普通节点

    // 头部
    public SectionItemBean(VideoClassStageBean videoClassStageBean) {
        this.videoClassStageBean = videoClassStageBean;
        isSectionHeader = true;
    }

    // 普通节点
    public SectionItemBean(VideoItemBean videoItemBean) {
        isSectionHeader = false;
        this.videoItemBean = videoItemBean;
    }
}
