package com.cskaoyan.birthday.whobirthday.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cskaoyan.birthday.whobirthday.Dao.GroupDao;
import com.cskaoyan.birthday.whobirthday.R;

import java.util.List;

/**
 * 该adapter用于在增加联系人的界面的 spinner
 * Created by lamchaohao on 2016/4/21.
 */
public class GroupAdapter extends BaseAdapter{

    Context mContext;
    private List<String> groups;

    public GroupAdapter(Context ctx) {
        mContext=ctx;
        GroupDao groupDao=new GroupDao(ctx);
        groups = groupDao.getGroups();
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_group, null);
        TextView tv_group_groupname = (TextView) view.findViewById(R.id.tv_group_groupname);
        tv_group_groupname.setText(groups.get(position));
        return view;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
