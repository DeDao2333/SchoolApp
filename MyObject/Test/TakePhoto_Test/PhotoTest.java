package com.example.java.api25.Test.TakePhoto_Test;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.java.api25.MyObject.ToolSet.HeadImage;
import com.example.java.api25.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PhotoTest extends AppCompatActivity {

    private Button mButton,mBut_get;
    private ImageView mImageView;
    private Uri mUri;
    public static final int CHOOSE_PHOTO=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_test);

        mImageView = (ImageView) findViewById(R.id.image_photo);
        mButton = (Button) findViewById(R.id.but_take_photo);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    mUri = FileProvider.getUriForFile(PhotoTest.this, "phototest", outputImage);
                } else {
                    mUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                Toast.makeText(PhotoTest.this, ""+mUri.toString(), Toast.LENGTH_SHORT).show();
                startActivityForResult(intent,1);
            }
        });
        mBut_get = (Button) findViewById(R.id.but_get_photo);
        mBut_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PhotoTest.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PhotoTest.this,new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUri));
                        HeadImage.setImage(bitmap);
                        mImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    hanldeImage(data);
                }
            default:
                break;
        }
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
                break;
            default:
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
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bit = BitmapFactory.decodeFile(imagePath);
            HeadImage.setImage(bit);
            mImageView.setImageBitmap(bit);
        } else {
            Toast.makeText(this, "fail to get the image", Toast.LENGTH_SHORT).show();
        }
    }
}
