package com.aligohershabir.photocollage.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aligohershabir.photocollage.R;
import com.aligohershabir.photocollage.model.Path;
import com.aligohershabir.photocollage.utils.ResourceManager;
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_READ_PERMISSION = 101;

//    private Button bStart;

//    private void getInfo(){
//        bStart = findViewById(R.id.bStart);
//    }
//
//    private void setInfo(){
//        bStart.setOnClickListener(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getInfo();
//        setInfo();
    }

//    @Override
//    public void onClick(View view) {
//        int i = view.getId();
//        if (i == R.id.bStart) {
//            requestStoragePermission();
//
//        }
//    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            //startActivity(new Intent(ChooserActivity.this, MultiImagePickerActivity.class));
            startMultiImagePickerActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //startActivity(new Intent(ChooserActivity.this, MultiImagePickerActivity.class));
            startMultiImagePickerActivity();
        }
    }

    private void startMultiImagePickerActivity(){
        Intent mIntent = new Intent(this, PickImageActivity.class);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 9);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
//            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
//            // do your logic here...
//            ResourceManager.images = images;
//            startActivity(new Intent(ChooserActivity.this, CollageActivity.class));
//        }
        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode

        if(resultCode != RESULT_OK){
            return;
        }

        ArrayList<String> pathList;
        if (requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            pathList = data.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            if (pathList != null && !pathList.isEmpty()) {
                StringBuilder sb=new StringBuilder("");
                for(int i=0;i<pathList.size();i++) {
                    sb.append("Photo"+(i+1)+":"+pathList.get(i));
                    sb.append("\n");
                }
//                Log.i(TAG, "pathList: " + sb.toString());
            }
            ArrayList<Path> paths = new ArrayList<>();
            for(String s:pathList){
                paths.add(new Path(s,false));
            }
            ResourceManager.paths = paths;

//            for(int i = 0; i < paths.size(); i++){
//                Log.e(TAG, "Paths ("+ i +") : "+ String.valueOf(paths.get(i)));
//            }
            startActivity(new Intent(MainActivity.this, CollageActivity.class));
        }
    }
}
