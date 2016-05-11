package com.cskaoyan.birthday.whobirthday.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.Dao.PreDateDao;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by whb on 2016/4/23.
 */
public class SyncUtils {


     List<PersonInfo> personInfoListFromServer;
     List<BmobObject> personInfoListFromLocal;

    List<PersonInfo> preDateUpdatePersonInfoList;

    Context mContext;
    private HashMap<String, Integer> simplePersonInfoListFromLocal;
    private HashMap<String, Integer> simplePersonInfoListFromServer;
    private String TAG = "SyncUtils";

    public SyncUtils(Context mContext) {
        preDateUpdatePersonInfoList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void syncPersonInfo()
    {
        getPersonInfosFromServer(MyApplication.getCurrentUsername(mContext));
    }

    /**
     * 从服务器上查询指定用户的所有联系人信息
     */
    private   void getPersonInfosFromServer(String username)
    {
        personInfoListFromServer = new ArrayList<>();
        BmobQuery<PersonInfo> query = new BmobQuery<PersonInfo>();
        query.addWhereEqualTo("username", username);
        query.findObjects(mContext, new FindListener<PersonInfo>() {
            @Override
            public void onSuccess(List<PersonInfo> object) {
                personInfoListFromServer.addAll(object);
                Log.i(TAG,object.size()+"getPersonInfosFromServer");
                //从服务器上查询成功之后，在从本地查询联系人信息
                getPersonInfoListFromLocal();

            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i("getPersonInfos",code+msg);
                getPersonInfoListFromLocal();

            }
        });
    }

    /**
     * 从数据库中读出指定用户的所有联系人信息
     */
    private void getPersonInfoListFromLocal()
    {
        PersonInfoDao personInfoDao=new PersonInfoDao(mContext);
        personInfoListFromLocal = personInfoDao.queryAllPerson();
        Log.i(TAG,personInfoListFromLocal.size()+"getPersonInfoListFromLocal");

        //从本地也获取联系人信息后，开始处理
        processPersonInfo();
    }

    /**
     * 处理服务器上联系人信息和本地联系人信息
     */
    private void processPersonInfo()
    {
        //<联系人姓名，联系人在List中的位置>，方便后面通过姓名找到联系人其他信息
        simplePersonInfoListFromLocal = new HashMap<>();
        simplePersonInfoListFromServer = new HashMap<>();

        //本地和数据库中联系人姓名列表
        List<String> localPersonInfoNames = new ArrayList<>();
        List<String> serverPersonInfoNames = new ArrayList<>();

        for (int i=0; i<personInfoListFromLocal.size();i++)
        {
            PersonInfo personInfo = (PersonInfo) personInfoListFromLocal.get(i);
            String name = personInfo.getName();
            simplePersonInfoListFromLocal.put(name,i);
            localPersonInfoNames.add(name);
        }

        for (int i=0; i<personInfoListFromServer.size();i++)
        {
            String name = personInfoListFromServer.get(i).getName();
            simplePersonInfoListFromServer.put(name,i);
            serverPersonInfoNames.add(name);
        }

        ArrayList<String> downloadListNames = new ArrayList<String>();
        ArrayList<String> uploadListNames = new ArrayList<String>();
        ArrayList<String> updateListNames = new ArrayList<String>();

        //从服务器上要下拉的PersonInfo的姓名
        downloadListNames.addAll(serverPersonInfoNames);
        downloadListNames.removeAll(localPersonInfoNames);

        //从本地要上传的PersonInfo的姓名
        uploadListNames.addAll(localPersonInfoNames);
        uploadListNames.removeAll(serverPersonInfoNames);

        //本地和服务器name一致的name列表,根据updateTime确定哪一个更新
        updateListNames.addAll(localPersonInfoNames);
        updateListNames.retainAll(serverPersonInfoNames);

        //更新到服务器上的联系人信息列表
        List<BmobObject> updatePersonsToServer = new ArrayList<BmobObject>();

        //更新到本地的联系人信息列表
        List<PersonInfo> updatePersonsToLocal = new ArrayList<PersonInfo>();

        //下拉到本地的联系人信息PersonInfo
        List<PersonInfo> downloadPersonsToLocal = new ArrayList<PersonInfo>();

        //上传到服务器的联系人信息PersonInfo
        List<BmobObject> uploadPersonsToServer = new ArrayList<BmobObject>();
        /**
         * 这个for循环用来确定--服务器更新名单，本地更新名单
         */
        for (int i=0; i<updateListNames.size();i++)
        {
            String name = updateListNames.get(i);
            //通过姓名拿到联系人的更新时间
            Integer localloc = simplePersonInfoListFromLocal.get(name);
            Integer serverloc = simplePersonInfoListFromServer.get(name);

            PersonInfo personServer = personInfoListFromServer.get(serverloc);
            PersonInfo personLocal = (PersonInfo) personInfoListFromLocal.get(localloc);

            String updatedServer = personServer.getUpdateTime();
            String updatedLocal = personLocal.getUpdateTime();


            //比较更新时间，确定是更新本地还是更新服务器
            if (updatedLocal.compareTo(updatedServer)>0)
            {
                /**
                 * private String name;
                 private String gender;
                 private String birth;
                 private String phone;
                 private String image;
                 private String atGroup;
                 private int remind ;
                 */
                //本地信息比服务器信息更新，更新到服务器
                PersonInfo personInfo = new PersonInfo();
                personInfo.setObjectId(personServer.getObjectId());
                personInfo.setName(personLocal.getName());
                personInfo.setGender(personLocal.getGender());
                personInfo.setBirth(personLocal.getBirth());
                personInfo.setPhone(personLocal.getPhone());
                personInfo.setImage(personLocal.getImage());
                personInfo.setAtGroup(personLocal.getAtGroup());
                personInfo.setRemind(personLocal.getRemind());
                updatePersonsToServer.add(personInfo);

                
            }
            else if (updatedLocal.compareTo(updatedServer)<0)
            {
                //服务器信息比本地信息更新，更新到本地
                updatePersonsToLocal.add(personServer);
            }

        }

        //更新服务器
        updateToServer(updatePersonsToServer);
        //更新本地
        updateToLocal(updatePersonsToLocal);

        //下拉本地没有的联系人信息
        for (int i=0;i<downloadListNames.size();i++)
        {
            String name = downloadListNames.get(i);
            Integer location = simplePersonInfoListFromServer.get(name);
            PersonInfo personInfo = personInfoListFromServer.get(location);
            downloadPersonsToLocal.add(personInfo);
        }

        //上传服务器没有的联系人信息
        for (int i=0;i<uploadListNames.size();i++)
        {
            String name = uploadListNames.get(i);
            Integer location = simplePersonInfoListFromLocal.get(name);
            PersonInfo personInfo = (PersonInfo) personInfoListFromLocal.get(location);
            uploadPersonsToServer.add(personInfo);
        }

        syncPreDate(downloadPersonsToLocal,uploadPersonsToServer);

    }
    public void syncPreDate(List<PersonInfo> downloadPersonsToLocal, List<BmobObject> uploadPersonsToServer)
    {
        uploadToServer(uploadPersonsToServer);
        downloadToLocal(downloadPersonsToLocal);

        Log.i(TAG,uploadPersonsToServer.size()+"syncPreDate");
        Log.i(TAG,downloadPersonsToLocal.size()+"syncPreDate");

        PreDateDao pdDao=new PreDateDao(mContext);
        for (int i=0;i<preDateUpdatePersonInfoList.size();i++){
            pdDao.insertPreDate(preDateUpdatePersonInfoList.get(i).getName(),preDateUpdatePersonInfoList.get(i).getBirth());
        }

    }
    /**
     * 1.向服务器上传本地新建的联系人的修改
     * 注意：每次只支持最大50条记录的操作。
     * @param uploadListNames
     */
    private void uploadToServer(List<BmobObject> uploadListNames)
    {
        new BmobObject().insertBatch(mContext, uploadListNames, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("批量添加成功");
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("批量添加失败:"+msg);
            }
        });

    }

    /**
     * 2.从服务器下拉本地没有的联系人信息，
     * 其实在getPersonInfosFromServer()方法中已经拉下来了，这里插入到数据库中就可以了
     * @param downloadPersonsToLocal
     */
    private void downloadToLocal(List<PersonInfo> downloadPersonsToLocal) {
        //将downloadPersonsToLocal中的PersonInfo插入到本地数据库
        PersonInfoDao personInfoDao = new PersonInfoDao(mContext);
        for (int i=0;i<downloadPersonsToLocal.size();i++)
        {
            personInfoDao.insertPerson(downloadPersonsToLocal.get(i));
            //更新Pre_date
            preDateUpdatePersonInfoList.addAll(downloadPersonsToLocal);
        }

    }

    /**
     * 3.服务器同步本地
     * @param updatePersonsToLocal
     */
    private void updateToLocal(List<PersonInfo> updatePersonsToLocal) {
        //更新本地数据库中的联系人信息
        PersonInfoDao personInfoDao = new PersonInfoDao(mContext);
        for (int i=0;i<updatePersonsToLocal.size();i++)
        {
            personInfoDao.updatePersonByName(updatePersonsToLocal.get(i));

            //更新Pre_date
            preDateUpdatePersonInfoList.addAll(updatePersonsToLocal);

        }
    }

    /**
     * 4.本地同步到服务器
     * @param updatePersonsToServer
     */
    private void updateToServer(List<BmobObject> updatePersonsToServer) {

        new BmobObject().updateBatch(mContext, updatePersonsToServer, new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("updateToServer成功");
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("updateToServer失败");
            }
        });
    }

    private void toast(String s)
    {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }


    /**
     * 只能在同步之后再调用
     * @return
     */
    public List<PersonInfo> getPreDateUpdatePersonInfoList()
    {
        return preDateUpdatePersonInfoList;
    }


}
