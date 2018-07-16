package com.emwno.fq.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emwno.fq.R;
import com.emwno.fq.util.Util;

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

        byte[] image = getIntent().getByteArrayExtra("image");
        String fqt = getIntent().getStringExtra("fqTitle");
        String fqst = getIntent().getStringExtra("fqSubTitle");
        int textStyle = getIntent().getIntExtra("textStyle", 0);
        float textSize = getIntent().getFloatExtra("textSize", 25f);

        ImageView view = findViewById(R.id.image_preview);
        TextView mQuoteTitle = findViewById(R.id.fqTitle);
        TextView mQuoteSubtitle = findViewById(R.id.fqSubtitle);

        Glide.with(this).load(image).apply(RequestOptions.centerCropTransform()).into(view);

        mQuoteTitle.setTextSize(textSize);

        mQuoteTitle.setText(Util.fromHtml(fqt));
        mQuoteSubtitle.setText(Util.fromHtml(fqst));

        mQuoteTitle.setTypeface(Typeface.DEFAULT, textStyle);
        mQuoteSubtitle.setTypeface(Typeface.DEFAULT, textStyle);
    }
}
