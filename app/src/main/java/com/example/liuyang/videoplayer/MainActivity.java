package com.example.liuyang.videoplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoview;
    private TextView telephone;
    private Button jump;
    private Button qqlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoview = (VideoView) findViewById(R.id.video_view);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button replay = (Button) findViewById(R.id.replay);
        jump = (Button)findViewById(R.id.jump);
        qqlogin = (Button)findViewById(R.id.login_qq);
        telephone = (TextView)findViewById(R.id.telephone_num);

        //设置视频控制器
        videoview.setMediaController(new MediaController(this));

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);
        jump.setOnClickListener(this);
        qqlogin.setOnClickListener(this);

        final SpannableStringBuilder style = new SpannableStringBuilder();
        final String telephone_number = "13066078790";
        //设置文字
        style.append(telephone_number);

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(MainActivity.this, "触发点击事件!", Toast.LENGTH_SHORT).show();
                callPhone(telephone_number);
            }
        };
        style.setSpan(clickableSpan, 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        telephone.setText(style);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        style.setSpan(foregroundColorSpan, 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        telephone.setMovementMethod(LinkMovementMethod.getInstance());
        telephone.setText(style);




        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initVideoPath();
        }
    }

    //打电话函数
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private void initVideoPath(){
//        String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";//
//        Uri uri = Uri.parse( videoUrl );
//        videoview.setVideoURI(uri);
//
//        videoview.start();

//        //设置视频路径
//        videoview.setVideoPath(videoUrl);

        File file = new File(Environment.getExternalStorageDirectory(),"movie.mp4");
        if(file==null){
            Toast.makeText(this,"找不到文件",Toast.LENGTH_LONG).show();
        }
        videoview.setVideoPath(file.getPath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED){
                    initVideoPath();
                }else{
                    Toast.makeText(this,"拒绝权限无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.play:
                if(!videoview.isPlaying()){
                    videoview.start();
                }
                break;
            case R.id.pause:
                if(videoview.isPlaying()){
                    videoview.pause();
                }
                break;
            case R.id.replay:
                initData();
                if(videoview.isPlaying()){
                    videoview.resume();
                }
                break;
            case R.id.jump:
                Intent intent =new Intent(MainActivity.this,CoachInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.login_qq:
                Intent intent1 =new Intent(MainActivity.this,Login.class);
                startActivity(intent1);
        }
    }


    private void initData() {
        String filePath = "/sdcard/Shesaid/";
        String fileName = "log.txt";
        Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
        writeTxtToFile("txt content", filePath, fileName);

        Resources res = this.getResources();
        BitmapDrawable d = (BitmapDrawable) res.getDrawable(R.drawable.coach_ad);
        Bitmap img = d.getBitmap();
        String fn = "default_icon.png";
        String path = "/sdcard/Shesaid/" + fn;
        try{
            OutputStream os = new FileOutputStream(path);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        }catch(Exception e){
            Log.e("TAG", "", e);
        }


        //String fp = getResourcesUri(R.drawable.coach_ad);//"/sdcard/DCIM/Camera/IMG_20180807_201639.jpg"
        //System.out.println(fp);
        File f = new File(path);
        if(!f.exists()){
            Toast.makeText(this,"文件不存在",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"文件存在",Toast.LENGTH_SHORT).show();
        }
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹    //之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();

            Toast.makeText(this,"创建文件成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Toast.makeText(this,"找不到文件",Toast.LENGTH_SHORT).show();
            Log.i("error:", e+"");
        }
    }
}


