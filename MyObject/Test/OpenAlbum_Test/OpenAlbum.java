package com.example.java.api25.Test.OpenAlbum_Test;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.java.api25.R;

public class OpenAlbum extends AppCompatActivity {

    final static int CROP_PHOTO = 3;
    
    private Button mButton;
    private ImageView mImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_album);

        mImage = (ImageView) findViewById(R.id.image_content);
        mButton = (Button) findViewById(R.id.test_but_open_photo);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission
                        (OpenAlbum.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                    Toast.makeText(OpenAlbum.this, "nihao", Toast.LENGTH_SHORT).show();
                } else {
                    openAlbum();
                }
            }
        });
    }

    /**
     * 开启相册intent
     */
    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 2) {
            hanldeImage(data);
            Toast.makeText(this, "result", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == CROP_PHOTO) {
            cropPhoto(data.getData());
            Bitmap bit = data.getParcelableExtra("data");
            mImage.setImageBitmap(bit);
            Toast.makeText(this, "crop", Toast.LENGTH_SHORT).show();
        }
        }

    public void hanldeImage(Intent data) {
        String path = null;
        Uri uri=data.getData();
        Log.d("wer", "1: "+uri.toString());
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d("wer", "2: "+docId);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                Log.d("wer","3: "+ id);
                String selection = MediaStore.Images.Media._ID + "=" + id;
                Log.d("wer", "4: "+selection);
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                Log.d("wer", "5: "+path);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contenturi, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //all the pictures picked from phone are always "content" type.
            Log.d("wer", "6: " + uri.getScheme());
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.d("wer", "6: " + uri.getScheme());
            path = uri.getPath();
        }
        Log.d("wer", "7: " + path);
        displayImage(path);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        cropPhoto(uri);  //获得图片后剪切
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bit = BitmapFactory.decodeFile(imagePath);
            mImage.setImageBitmap(bit);
        } else {
            Toast.makeText(this, "fail to get the image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开剪切功能
     * @param uri 图片的地址
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 700);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO);
    }
}
