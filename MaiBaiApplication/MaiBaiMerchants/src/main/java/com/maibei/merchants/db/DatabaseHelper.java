package com.maibei.merchants.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.model.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2016/11/2.
 */

public class DatabaseHelper {
    public static DatabaseHelper databaseHelper;
    String f;
    Context context;
    public DatabaseHelper(Context context){
        this.context=context;
        f = context.getFilesDir()+"\\databases\\"+GlobalParams.DB_NAME; //此处如果是放在应用包名的目录下,自动放入“databases目录下
    }
    public List<ProvinceBean> getProvince(){
        List<ProvinceBean> provinceBeenList=new ArrayList<ProvinceBean>();
        SQLiteDatabase db = context.openOrCreateDatabase(f, MODE_PRIVATE, null);
        Cursor c = db.query(GlobalParams.TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            ProvinceBean provinceBean = new ProvinceBean();
            provinceBean.setProvince_id(c.getString(c.getColumnIndex(GlobalParams.DB_PROVINCE_ID)));
            provinceBean.setProvince_name(c.getString(c.getColumnIndex(GlobalParams.DB_PROVINCE_NAME)));
            provinceBeenList.add(provinceBean);
        }
        c.close();
        db.close();
        return provinceBeenList;
    }
}
