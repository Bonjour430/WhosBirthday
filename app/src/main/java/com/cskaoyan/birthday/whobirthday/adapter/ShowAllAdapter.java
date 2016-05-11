package com.cskaoyan.birthday.whobirthday.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.utils.DayOfYear;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by lamchaohao on 2016/4/22.
 */
public class ShowAllAdapter extends BaseAdapter{

    Context mContext;
    private final List<BmobObject> personInfos;
    private final BitmapUtils mBU;

    public ShowAllAdapter(Context ctx ,List<BmobObject> infos) {
        mContext=ctx;
        personInfos = infos;
        mBU = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {
        return personInfos.size();
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
        LinearLayout ll_itemShowAll=null;
        Holder holder;
        if (convertView!=null){
            ll_itemShowAll= (LinearLayout) convertView;
            holder = (Holder) ll_itemShowAll.getTag();
        }else{
            ll_itemShowAll= (LinearLayout) View.inflate(mContext, R.layout.item_listview_friends_content, null);
            holder=new Holder();
            holder.iv_itemAll_head= (ImageView) ll_itemShowAll.findViewById(R.id.iv_itemAll_head);
            holder.iv_cake= (ImageView) ll_itemShowAll.findViewById(R.id.iv_cake);
            holder.tv_itemAll_name=(TextView) ll_itemShowAll.findViewById(R.id.tv_itemAll_name);
            holder.tv_itemAll_birthday=(TextView) ll_itemShowAll.findViewById(R.id.tv_itemAll_birthday);
            holder.tv_itemAll_daysleft=(TextView) ll_itemShowAll.findViewById(R.id.tv_itemAll_daysleft);
            ll_itemShowAll.setTag(holder);
        }
        PersonInfo personInfo = (PersonInfo) personInfos.get(position);
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

    class Holder{
        ImageView iv_itemAll_head;
        ImageView iv_cake;
        TextView tv_itemAll_name;
        TextView tv_itemAll_birthday;
        TextView tv_itemAll_daysleft;
    }
}
