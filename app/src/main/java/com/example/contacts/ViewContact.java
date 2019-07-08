package com.example.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

public class ViewContact extends AppCompatActivity {
    TextView viewTitle1;
    TextView viewTitle2;
    TextView viewName;
    TextView viewPhone;
    TextView viewWAddress;
    TextView viewHAddress;
    TextView viewEmail;
    ImageView profile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontact);
        viewTitle1=findViewById(R.id.viewTitle1);
        viewTitle2=findViewById(R.id.viewTitle2);
        viewName=findViewById(R.id.viewName);
        viewPhone=findViewById(R.id.viewPhone);
        viewWAddress=findViewById(R.id.viewWAddress);
        viewHAddress=findViewById(R.id.viewHAddress);
        viewEmail=findViewById(R.id.viewEmail);
        profile=findViewById(R.id.viewProfile);
        setTitle();
        viewTitle2.setText(getIntent().getStringExtra("PNumber"));
        viewName.setText(getIntent().getStringExtra("Name"));
        viewPhone.setText(getIntent().getStringExtra("PNumber"));
        viewWAddress.setText(getIntent().getStringExtra("WAddress"));
        viewHAddress.setText(getIntent().getStringExtra("HAddress"));
        viewEmail.setText(getIntent().getStringExtra("Email"));
        if(!getIntent().getStringExtra("PicPath").equals("null"))
        {
            File f=new File(getIntent().getStringExtra("PicPath"));
            Picasso.with(this).load(f).transform(new PicassoCircleTransformation()).into(profile);
        }
    }
    public void editContact(View view)
    {
        Intent intent=new Intent(this,NewContact.class);
        intent.putExtra("Status",true);
        intent.putExtra("ID",getIntent().getIntExtra("ID",0));
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("PNumber",getIntent().getStringExtra("PNumber"));
        intent.putExtra("WAddress",getIntent().getStringExtra("HAddress"));
        intent.putExtra("HAddress",getIntent().getStringExtra("WAddress"));
        intent.putExtra("Email",getIntent().getStringExtra("Email"));
        intent.putExtra("PicPath",getIntent().getStringExtra("PicPath"));
        startActivity(intent);
        finish();
    }
    public void setTitle()
    {
        if(!getIntent().getStringExtra("Name").equals(null))
        {
            viewTitle1.setText(getIntent().getStringExtra("Name"));
        }
        else if(!getIntent().getStringExtra("PNumber").equals(null))
        {
            viewTitle1.setText(getIntent().getStringExtra("PNumber"));
        }
        else if(!getIntent().getStringExtra("WAddress").equals(null))
        {
            viewTitle1.setText(getIntent().getStringExtra("WAddress"));
        }
        else if(!getIntent().getStringExtra("HAddress").equals(null))
        {
            viewTitle1.setText(getIntent().getStringExtra("HAddress"));
        }
        else if(!getIntent().getStringExtra("Email").equals(null))
        {
            viewTitle1.setText(getIntent().getStringExtra("Email"));
        }
    }
    public class PicassoCircleTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
