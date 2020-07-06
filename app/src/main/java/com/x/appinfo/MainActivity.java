package com.x.appinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    HashMap<String, String> activityMap = new HashMap();
    ListView lv_rom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("导出应用信息列表");
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }
        lv_rom=(ListView)findViewById(R.id.listViewAppInfo);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RomAdapter adapter = new RomAdapter(getApplicationContext(), getData(true));
                lv_rom.setAdapter(adapter);
            }
        });

    }

    public List<HashMap<String,String>> getData(boolean isSave)
    {
        List<HashMap<String,String>> data=new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        List localList1 = getPackageManager().getInstalledPackages(0);

        String str1 = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "ApkVersion.csv";
        File localFile1 = new File(str1);
        if (localFile1.exists())
            localFile1.delete();
        try
        {
            localFile1.createNewFile();
            Intent localIntent = new Intent("android.intent.action.MAIN", null);
            localIntent.addCategory("android.intent.category.LAUNCHER");
            List localList2 = getPackageManager().queryIntentActivities(localIntent, 0);
            for (int i = 0; i < localList2.size(); i++)
            {
                String str9 = ((ResolveInfo)localList2.get(i)).activityInfo.name;
                String str10 = ((ResolveInfo)localList2.get(i)).activityInfo.packageName;
                this.activityMap.put(str10, str9);
            }
            if(isSave){
                saveToSD("编号,Apk Name,中文名,版本号,文件大小(KB),包名,进程名,Activity,安装路径", localFile1);
            }

            for (int j = 0; j < localList1.size(); j++)
            {
                File localFile2 = new File(((PackageInfo)localList1.get(j)).applicationInfo.publicSourceDir);
                String apkName = localFile2.getName();
                String appName = ((PackageInfo)localList1.get(j)).applicationInfo.loadLabel(getPackageManager()).toString();
                String versionName = ((PackageInfo)localList1.get(j)).versionName;
                File localFile3 = new File(((PackageInfo)localList1.get(j)).applicationInfo.publicSourceDir);
                int k = Integer.valueOf((int)localFile3.length()).intValue() / 1024;
                String packageName = ((PackageInfo)localList1.get(j)).packageName;
                String processName = ((PackageInfo)localList1.get(j)).applicationInfo.processName;
                String sourceDir = ((PackageInfo)localList1.get(j)).applicationInfo.publicSourceDir;
                String ActivityName = "NULL";
                if (this.activityMap.containsKey(packageName))
                    ActivityName = (String)this.activityMap.get(packageName);
                map=new HashMap<String,String>();
                map.put("app",appName);
                map.put("version",versionName);
                map.put("fileSize",k+"KB");
                map.put("pkg",packageName);
                map.put("process",processName);
                map.put("activity",ActivityName);
                map.put("path",sourceDir);
                data.add(map);
                String line=j + "," + apkName + "," + appName + "," + versionName + "," + k + "," + packageName + "," + processName + "," + ActivityName + "," + sourceDir;
                Log.v("APPINFO",line);

                if(isSave){
                    saveToSD(line, localFile1);
                }
            }
            if(isSave){
                Toast.makeText(getApplicationContext(), "结果已经保存在" + str1, Toast.LENGTH_LONG).show();
            }

        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }

        return  data;
    }



    public static void saveToSD(String text, File runFile) {
        BufferedWriter fw = null;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(runFile, true),"GBK"));

            fw.append(text);
            fw.newLine();
            fw.flush();

            fw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_save://监听菜单按钮
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RomAdapter adapter = new RomAdapter(getApplicationContext(), getData(true));
                        lv_rom.setAdapter(adapter);
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
