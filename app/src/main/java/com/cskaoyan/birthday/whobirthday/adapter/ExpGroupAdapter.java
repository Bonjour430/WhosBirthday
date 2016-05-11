package com.cskaoyan.birthday.whobirthday.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cskaoyan.birthday.whobirthday.Dao.GroupDao;
import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.bean.GroupBean;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.utils.DayOfYear;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * 此adapter用于主界面的分组
 * Created by lamchaohao on 2016/4/22.
 */
public class ExpGroupAdapter extends BaseExpandableListAdapter {

    private List<GroupBean> mGrouplist;
    private Context mContext;
    private final BitmapUtils mBU;
    public ExpGroupAdapter(Context ctx, List<GroupBean> groupBeen) {
        mContext=ctx;
        mGrouplist=groupBeen;
        GroupDao gd=new GroupDao(mContext);
        mBU = new BitmapUtils(mContext);
        List<String> groups = gd.getGroups();
    }

    @Override
    public int getGroupCount() {
        return mGrouplist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //子bean在GroupBean中
        return mGrouplist.get(groupPosition).getChildList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGrouplist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGrouplist.get(groupPosition).getChildList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_group, null);
        TextView tv_groupName = (TextView) view.findViewById(R.id.tv_group_groupname);
        ImageView iv_group_icon = (ImageView) view.findViewById(R.id.iv_group_icon);
        tv_groupName.setText(mGrouplist.get(groupPosition).getGroupName());
        if (isExpanded) {
            iv_group_icon.setImageResource(R.drawable.rounds_open);
        } else {
            iv_group_icon.setImageResource(R.drawable.rounds_close);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LinearLayout ll_itemShowAll=null;
        ChildHolder holder;
        if (convertView!=null){
            ll_itemShowAll= (LinearLayout) convertView;
            holder = (ChildHolder) ll_itemShowAll.getTag();
        }else{
            ll_itemShowAll= (LinearLayout) View.inflate(mContext, R.layout.item_listview_friends_content, null);
            holder=new ChildHolder();
            holder.iv_itemAll_head= (ImageView) ll_itemShowAll.findViewById(R.id.iv_itemAll_head);
            holder.iv_cake= (ImageView) ll_itemShowAll.findViewById(R.id.iv_cake);
            holder.tv_itemAll_name=(TextView) ll_itemShowAll.findViewById(R.id.tv_itemAll_name);
            holder.tv_itemAll_birthday=(TextView) ll_itemShowAll.findViewById(R.id.tv_itemAll_birthday);
            holder.tv_itemAll_daysleft=(TextView) ll_itemShowAll.findViewById(R.id.tv_itemAll_daysleft);
            ll_itemShowAll.setTag(holder);
        }
        PersonInfo personInfo = (PersonInfo) mGrouplist.get(groupPosition).getChildList().get(childPosition);
        String imagePath = personInfo.getImage();

        if (personInfo.getGender().equals("女")){
            mBU.configDefaultLoadFailedImage(R.drawable.black_widow_pic);
        }
        else{
            mBU.configDefaultLoadFailedImage(R.drawable.hawkeye_pic);
        }
        mBU.display(holder.iv_itemAll_head,imagePath);
        holder.tv_itemAll_name.setText(personInfo.getName());
        holder.tv_itemAll_birthday.setText(personInfo.getBirth());

        holder.tv_itemAll_name.setText(personInfo.getName());
        holder.tv_itemAll_birthday.setText(personInfo.getBirth());
        int howManyDayLeft = DayOfYear.getHowManyDayLeft(personInfo.getBirth());
        if (howManyDayLeft==0){
            holder.tv_itemAll_daysleft.setText("今");
            holder.iv_cake.setVisibility(View.VISIBLE);
        }else if(howManyDayLeft>7){
            holder.iv_cake.setVisibility(View.GONE);
            holder.tv_itemAll_daysleft.setText(howManyDayLeft+"");
        }else{
            holder.iv_cake.setVisibility(View.VISIBLE);
            holder.tv_itemAll_daysleft.setText(howManyDayLeft+"");
        }
        return ll_itemShowAll;
    }

    /**
     * 如果子条目需要响应click事件,必需返回true
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ChildHolder{
        ImageView iv_cake;
        ImageView iv_itemAll_head;
        TextView tv_itemAll_name;
        TextView tv_itemAll_birthday;
        TextView tv_itemAll_daysleft;
    }

}
