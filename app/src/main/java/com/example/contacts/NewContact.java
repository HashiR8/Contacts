package com.example.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewContact extends AppCompatActivity {
    EditText Name;
    EditText Phone;
    EditText WAddress;
    EditText HAddress;
    EditText Email;
    ImageView contactTitle;
    Bitmap profilePic;
    String filePath;
    private Boolean updateStatus;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcontact);
        Name=findViewById(R.id.newContactName);
        Phone=findViewById(R.id.newContactPhone);
        WAddress=findViewById(R.id.newContactWork);
        HAddress=findViewById(R.id.newContactHome);
        Email=findViewById(R.id.newContactEmail);
        contactTitle=findViewById(R.id.profilePicture);
        updateStatus=getIntent().getBooleanExtra("Status",false);
        if(updateStatus)
        {
            Name.setText(getIntent().getStringExtra("Name"));
            Phone.setText(getIntent().getStringExtra("PNumber"));
            WAddress.setText(getIntent().getStringExtra("WAddress"));
            HAddress.setText(getIntent().getStringExtra("HAddress"));
            Email.setText(getIntent().getStringExtra("Email"));
            if(!getIntent().getStringExtra("PicPath").equals("null"))
            {
                File f=new File(getIntent().getStringExtra("PicPath"));
                Picasso.with(this).load(f).transform(new PicassoCircleTransformation()).into(contactTitle);
            }
        }
        contactTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMedia=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openMedia,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            Uri ImageURI=data.getData();
            try {
                profilePic=MediaStore.Images.Media.getBitmap(getContentResolver(),ImageURI);
                contactTitle.setImageBitmap(profilePic);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(View view)
    {
        finish();
    }
    public void addContact(View view) throws FileNotFoundException {
        if(Name.getText().toString().equals("")&&Phone.getText().toString().equals("")&&WAddress.getText().toString().equals("")&&HAddress.getText().toString().equals("")&&Email.getText().toString().equals(""))
        {
            Toast.makeText(this, "Unable to save contact", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            if(updateStatus)
            {
                if(!(profilePic==null))
                {
                    saveBitmap();
                }
                ContentValues contentValues=new ContentValues();
                contentValues.put("Name",Name.getText().toString());
                contentValues.put("PNumber",Phone.getText().toString());
                contentValues.put("WAddress",WAddress.getText().toString());
                contentValues.put("HAddress",HAddress.getText().toString());
                contentValues.put("Email",Email.getText().toString());
                contentValues.put("PicPath",filePath);
                int ID=getIntent().getIntExtra("ID",0);
                String[] id= new String[]{Integer.toString(ID)};
                this.getContentResolver().update(ContactscontentProvider.contactsDBURI,contentValues,null,id);
                finish();
                MainActivity.recyclerViewAdapter.data=this.getContentResolver().query(ContactscontentProvider.contactsDBURI,null,null,null,null);
                MainActivity.recyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,ViewContact.class);
                intent.putExtra("ID",id);
                intent.putExtra("Name",contentValues.get("Name").toString());
                intent.putExtra("PNumber",contentValues.get("PNumber").toString());
                intent.putExtra("WAddress",contentValues.get("WAddress").toString());
                intent.putExtra("HAddress",contentValues.get("HAddress").toString());
                intent.putExtra("Email",contentValues.get("Email").toString());
                intent.putExtra("PicPath",contentValues.get("PicPath").toString());
                startActivity(intent);
            }
            else
            {
                if(!(profilePic==null))
                {
                    saveBitmap();
                }
                ContentValues contentValues=new ContentValues();
                contentValues.put("Name",Name.getText().toString());
                contentValues.put("PNumber",Phone.getText().toString());
                contentValues.put("WAddress",WAddress.getText().toString());
                contentValues.put("HAddress",HAddress.getText().toString());
                contentValues.put("Email",Email.getText().toString());
                contentValues.put("PicPath",filePath);
                this.getContentResolver().insert(ContactscontentProvider.contactsDBURI,contentValues);
                finish();
                MainActivity.recyclerViewAdapter.data=this.getContentResolver().query(ContactscontentProvider.contactsDBURI,null,null,null,null);
                MainActivity.recyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(this, "New Contact Created", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void saveBitmap() throws FileNotFoundException {
        String fileName=null;
        if(!Name.getText().toString().equals(null))
        {
            fileName=Name.getText().toString()+".jpg";
        }
        else if(!Phone.getText().toString().equals(null))
        {
            fileName=Phone.getText().toString()+".jpg";
        }
        else if(!WAddress.getText().toString().equals(null))
        {
            fileName=WAddress.getText().toString()+".jpg";
        }
        else if(!HAddress.getText().toString().equals(null))
        {
            fileName=HAddress.getText().toString()+".jpg";
        }
        else if(!Email.getText().toString().equals(null))
        {
            fileName=Email.getText().toString()+".jpg";
        }
        String path=getApplicationContext().getApplicationInfo().dataDir;
        ContextWrapper cw=new ContextWrapper(getApplicationContext());
        File directory=cw.getDir("profilePic", Context.MODE_PRIVATE);
        File mypath=new File(directory,fileName);
        FileOutputStream fos=null;
        fos=new FileOutputStream(mypath);
        profilePic.compress(Bitmap.CompressFormat.PNG,100,fos);
        filePath=directory.getAbsolutePath()+"/"+fileName;
    }
    void back(View view)
    {
        finish();
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
