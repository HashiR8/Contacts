package com.example.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Cursor data;
    private Context Basecontext;
    private Activity BaseActivity;
    public static RecyclerViewAdapter adapter;
    public RecyclerViewAdapter(Cursor cursor, Context context, Activity activity)
    {
        this.data=cursor;
        this.Basecontext=context;
        this.BaseActivity=activity;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contactrow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, final int position) {
        data.moveToPosition(position);
        holder.contactName.setText(data.getString(1));
        if(!(data.getString(6).equals(null)))
        {
            String check=data.getString(6);
            if(check.equals("null"))
            {
                Log.d("Tag","No Profile Pic");
            }
            else {
                File f=new File(data.getString(6));
                Picasso.with(this.BaseActivity).load(f).transform(new PicassoCircleTransformation()).into(holder.contactPic);}
        }
        holder.contactName.setStateListAnimator(null);
        holder.contactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BaseActivity,ViewContact.class);
                data.moveToPosition(position);
                intent.putExtra("ID",data.getInt(0));
                intent.putExtra("Name",data.getString(1));
                intent.putExtra("PNumber",data.getString(2));
                intent.putExtra("WAddress",data.getString(3));
                intent.putExtra("HAddress",data.getString(4));
                intent.putExtra("Email",data.getString(5));
                intent.putExtra("PicPath",data.getString(6));
                BaseActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public Button contactName;
        public ImageView contactPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName=itemView.findViewById(R.id.contactNamerow);
            contactPic=itemView.findViewById(R.id.rowProfile);
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
