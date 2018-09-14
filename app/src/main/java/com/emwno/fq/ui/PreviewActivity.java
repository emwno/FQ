package com.emwno.fq.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.emwno.fq.R;
import com.emwno.fq.util.DeviceOrientation;
import com.emwno.fq.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created on 09 Jun 2018.
 */
public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int orientation = getIntent().getIntExtra("imageOrientation", 0);
        boolean camFront = getIntent().getBooleanExtra("camFront", false);
        String fqt = getIntent().getStringExtra("fqTitle");
        String fqst = getIntent().getStringExtra("fqSubTitle");
        int textStyle = getIntent().getIntExtra("textStyle", 0);
        float textSize = getIntent().getFloatExtra("textSize", 25f);

        ImageView view = findViewById(R.id.image_preview);
        TextView mQuoteTitle = findViewById(R.id.fqTitle);
        TextView mQuoteSubtitle = findViewById(R.id.fqSubtitle);

        int rotation = 0;
        if (orientation == DeviceOrientation.ORIENTATION_LANDSCAPE) {
            rotation = 90;
        } else if (orientation == DeviceOrientation.ORIENTATION_LANDSCAPE_REVERSE) {
            rotation = -90;
        } else if (orientation == DeviceOrientation.ORIENTATION_PORTRAIT_REVERSE) {
            rotation = 180;
        }

        Matrix matrix = new Matrix();
        if (camFront) matrix.postScale(-1.0f, 1.0f);
        if (rotation != 0) matrix.setRotate(rotation);

        try {
            FileInputStream fis = openFileInput("cam.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            fis.close();

            if (camFront || rotation != 0)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


            Glide.with(this).load(bitmap).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Toast.makeText(PreviewActivity.this, "Unable to share image. Please try again or report this via Google Play", Toast.LENGTH_LONG).show();
//                onBackPressed();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    generateFQ();
                    shareFQ();
                    return false;
                }
            }).into(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mQuoteTitle.setTextSize(textSize);

        mQuoteTitle.setText(Util.fromHtml(fqt));
        mQuoteSubtitle.setText(Util.fromHtml(fqst));

        mQuoteTitle.setTypeface(Typeface.DEFAULT, textStyle);
        mQuoteSubtitle.setTypeface(Typeface.DEFAULT, textStyle);
    }

    private void generateFQ() {
        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        try {
            FileOutputStream fos = openFileOutput("image.jpg", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareFQ() {
        File newFile = new File(getFilesDir(), "image.jpg");
        Uri uri = FileProvider.getUriForFile(this, "com.emwno.fq.fileprovider", newFile);

        Intent shareIntent = new Intent();
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "~ FQ");
        shareIntent.setType("image/*");

        try {
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "There is no app that can handle sharing", Toast.LENGTH_SHORT).show();
        }
    }

}
