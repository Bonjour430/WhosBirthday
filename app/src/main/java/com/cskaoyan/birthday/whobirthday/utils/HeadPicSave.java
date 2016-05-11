package com.cskaoyan.birthday.whobirthday.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 此类用于保存bitmap头像到sdcard的WhoBirthday文件夹
 * Created by lamchaohao on 2016/4/21.
 */
public class HeadPicSave {

    public static String saveBitMap(Context ctx,String bitName, Bitmap mBitmap){
        File folder=new File(Environment.getExternalStorageDirectory()+"/whoBirthday/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File picFile = new File(folder, bitName + ".JPEG");
        Log.i("HeadPicSave",picFile.getAbsolutePath());
        if (picFile.exists()){
           picFile.delete();
        }
        FileOutputStream fOut = null;
        try {
//            picFile.createNewFile();
            fOut = new FileOutputStream(picFile);
            int rowBytes = mBitmap.getRowBytes();
            Log.i("HeadPicSave","rowBytes:"+rowBytes);
            if (rowBytes>4000){
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
            }else{
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            }
            Log.i("HeadPicSave",bitName);
            fOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fOut!=null)
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return picFile.toString();
    }


}

