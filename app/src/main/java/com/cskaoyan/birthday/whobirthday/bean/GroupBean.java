package com.cskaoyan.birthday.whobirthday.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by lamchaohao on 2016/4/22.
 */
public class GroupBean {

    private String groupName;
    private List<BmobObject> childList;

    public GroupBean() {

    }

    public GroupBean(String groupName, List<BmobObject> childList) {
        this.groupName = groupName;
        this.childList = childList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<BmobObject> getChildList() {
        return childList;
    }

    public void setChildList(List<BmobObject> childList) {
        this.childList = childList;
    }
}
