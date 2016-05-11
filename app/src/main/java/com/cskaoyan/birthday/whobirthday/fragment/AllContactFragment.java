package com.cskaoyan.birthday.whobirthday.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.activity.FriendDetailActivity;
import com.cskaoyan.birthday.whobirthday.activity.MainActivity;
import com.cskaoyan.birthday.whobirthday.activity.MyAccountActivity;
import com.cskaoyan.birthday.whobirthday.adapter.ShowAllAdapter;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.utils.SortBirthday;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 *  create by lamchaohao on 2016/4/21
 */
public class AllContactFragment extends Fragment {


    public static final int DETAIL_REQUEST = 105;
    private ListView mLvShowAll;
    private String content;
    private View view;
    private List<BmobObject> personInfos;
    private ShowAllAdapter mShowAllAdapter;
    private PersonInfoDao ptDao;
    private List<BmobObject> tempInfo;
    private int mPosition;
    public AllContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_contact, container, false);
        mLvShowAll = (ListView) view.findViewById(R.id.lv_allCotact_show);
        String currentUsername = MyApplication.getCurrentUsername(getActivity());
        if (currentUsername!=null){

        }
        ptDao = new PersonInfoDao(getActivity());
        tempInfo = ptDao.queryAllPerson();
        //排序过后的数据集
        personInfos = SortBirthday.sortLeftDay(tempInfo);
        mShowAllAdapter = new ShowAllAdapter(getActivity(), personInfos);
        mLvShowAll.setAdapter(mShowAllAdapter);
        mLvShowAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mPosition=position;
                Intent intent=new Intent(getActivity(), FriendDetailActivity.class);

                PersonInfo person = (PersonInfo) personInfos.get(position);
                intent.putExtra("name",person.getName());
                intent.putExtra("birthday",person.getBirth());
                intent.putExtra("phone",person.getPhone());
                intent.putExtra("gender",person.getGender());
                intent.putExtra("headpic",person.getImage());
                intent.putExtra("group",person.getAtGroup());
                startActivityForResult(intent,DETAIL_REQUEST);
            }
        });
        return view;
    }

    private void refresh(){
        personInfos.clear();
        tempInfo = ptDao.queryAllPerson();
        //排序过后的数据集
        List<BmobObject> bmobObjects = SortBirthday.sortLeftDay(tempInfo);
        personInfos.addAll(bmobObjects);
        mShowAllAdapter.notifyDataSetChanged();
    }

    public void setDataSet(PersonInfo person,int flag){
        switch (flag){
            case MainActivity.CREATE_REQUEST_CODE:
                refresh();
                break;
            case FriendDetailActivity.UPDATE_PERSON:
                refresh();
                break;
            case FriendDetailActivity.DELETE_PERSON:
                refresh();
                break;
        }

    }

}
