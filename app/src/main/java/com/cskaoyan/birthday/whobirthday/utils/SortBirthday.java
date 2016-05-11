package com.cskaoyan.birthday.whobirthday.utils;

import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 对距离生日剩余天数进行排序
 * Created by lamchaohao on 2016/4/22.
 */
public class SortBirthday {

    public static List<BmobObject> sortLeftDay(List<BmobObject> personInfos){
        List<Integer> daysLeftList=new ArrayList<>();
        for(int i=0;i<personInfos.size();i++){
            PersonInfo personInfo = (PersonInfo) personInfos.get(i);
            int howManyDayLeft = DayOfYear.getHowManyDayLeft(personInfo.getBirth());
            daysLeftList.add(howManyDayLeft);
        }

        //直接排序
        for (int i=0;i<daysLeftList.size();i++){
            int min=i;
            for (int j=i+1;j<daysLeftList.size();j++){
                if (daysLeftList.get(j)<daysLeftList.get(min)){
                    min=j;
                }
            }
            if (min!=i){
                int temp=daysLeftList.get(i);
                PersonInfo tempPerson = (PersonInfo) personInfos.get(i);
                //personInfos也依照daysLeftList排序
                daysLeftList.set(i,daysLeftList.get(min));
                personInfos.set(i,personInfos.get(min));

                daysLeftList.set(min,temp);
                personInfos.set(min,tempPerson);

                String s = personInfos.toString();
            }

        }
        String s = personInfos.toString();
        return personInfos;
    }

}
