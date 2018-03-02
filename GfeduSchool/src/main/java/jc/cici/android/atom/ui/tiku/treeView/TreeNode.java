package jc.cici.android.atom.ui.tiku.treeView;

import java.util.ArrayList;
import java.util.List;

import jc.cici.android.atom.ui.tiku.bean.ChildNode;

/**
 * 树形结构
 * Created by atom on 2018/1/3.
 */

public class TreeNode<T extends LayoutItemType> implements Cloneable {

    // 布局
    private T content;
    // 父类节点
    private TreeNode parent;
    // 子类列表
    private List<TreeNode> childList;
    // 是否展开
    private boolean isExpand;
    //the tree high
    private int height = UNDEFINE;

    private static final int UNDEFINE = -1;

    /**
     * 构造函数获取上下文信息
     *
     * @param content
     */
    public TreeNode(T content) {
        this.content = content;
    }

    /**
     * 如果为根节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 获取节点高度
     *
     * @return
     */
    public int getHeight() {
        if (isRoot()) {
            height = 0;
        } else if (height == UNDEFINE) {
            height = parent.getHeight() + 1;
        }
        return height;
    }

    /**
     * 判断叶子节点
     *
     * @return
     */
    public boolean isLeaf() {
        return childList == null || childList.isEmpty();
    }

    /**
     * 设置布局
     *
     * @param content
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * 获取布局
     *
     * @return
     */
    public T getContent() {
        return content;
    }

    /**
     * 获取子类节点
     *
     * @return
     */
    public List<TreeNode> getChildList() {
        return childList;
    }

    /**
     * 设置子类节点
     *
     * @param childList
     */
    public void setChildList(List<TreeNode> childList) {
        this.childList = childList;
    }

    /**
     * 添加子类节点
     *
     * @param node
     * @return
     */
    public TreeNode addChild(TreeNode node) {
        if (childList == null)
            childList = new ArrayList<>();
        childList.add(node);
        node.parent = this;
        return this;
    }

    /**
     * 可见情况
     *
     * @return
     */
    public boolean toggle() {
        isExpand = !isExpand;
        return isExpand;
    }

    /**
     * 折叠情况
     */
    public void collapse() {
        if (!isExpand)
            isExpand = false;
    }

    /**
     * 展开情况
     */
    public void expand() {
        if (isExpand)
            isExpand = true;
    }

    /**
     * 是否展开
     *
     * @return
     */
    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 设置父类节点
     *
     * @param parent
     */
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * 获取父类节点
     *
     * @return
     */
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "content=" + this.content +
                ", parent=" + (parent == null ? "null" : parent.getContent().toString()) +
                ", childList=" + (childList == null ? "null" : childList.toString()) +
                ", isExpand=" + isExpand +
                '}';
    }

    @Override
    protected TreeNode<T> clone() throws CloneNotSupportedException {
        TreeNode<T> clone = new TreeNode<>(this.content);
        clone.isExpand = this.isExpand;
        return clone;
    }

}
