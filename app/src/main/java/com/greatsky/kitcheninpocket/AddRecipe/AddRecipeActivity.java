package com.greatsky.kitcheninpocket.AddRecipe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fangwenli on 13/11/2016.
 */

public class AddRecipeActivity extends Activity implements DialogInterface.OnClickListener{

    ArrayList<Ingredients> items = new ArrayList<>();
    ArrayList<Steps> steps = new ArrayList<>();
    ListView listView;
    ListView step_list;
    IngredientListAdapter ingredientListAdapter;
    StepListAdapter stepListAdapter;
    String ingredient1;
    String amount1;

    //Button btn_submit;

    int click = 1;
    private int LOAD_COVER_IMAGE = 1;
    private int REQUEST_PERMISSION = 2;
    private int LOAD_STEP_IMAGE = 3;


    public void refreshListview(){

        int totalHeight = 0;
        for (int i = 0, len = ingredientListAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = ingredientListAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (ingredientListAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public void refreshListview2(){

        int totalHeight = 0;
        for (int i = 0, len = stepListAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = stepListAdapter.getView(i, null, step_list);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = step_list.getLayoutParams();
        params.height = totalHeight + (step_list.getDividerHeight() * (stepListAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        step_list.setLayoutParams(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        Ingredients i1 = new Ingredients("Add ingredients", "Add amount");
//        Ingredients i2 = new Ingredients("egg", "1");
//        Ingredients i3 = new Ingredients("egg", "1");
        items.add(i1);


        //btn_submit = (Button)findViewById(R.id.btn_submit);

//        items.add(i2);
//        items.add(i3);




        listView = (ListView)findViewById(R.id.ingredient_list);
        ingredientListAdapter = new IngredientListAdapter(AddRecipeActivity.this, items);

        //to deal with the problem that a listview on a scrollview cannot show the whole data
        refreshListview();

        listView.setAdapter(ingredientListAdapter);

        Steps step1 = new Steps("1", R.drawable.step_picture);
        steps.add(step1);

        step_list = (ListView)findViewById(R.id.step_list);
        stepListAdapter = new StepListAdapter(AddRecipeActivity.this, steps);
        refreshListview2();
        step_list.setAdapter(stepListAdapter);


    }

    public void addCoverPicture(View view){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            return;
        }

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, LOAD_COVER_IMAGE);
    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == REQUEST_PERMISSION){
//            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                //用户同意使用write
//                startGetImageThread();
//            }else{
//                //用户不同意，自行处理即可
//                finish();
//            }
//        }
//    }

    public void addStepPicture(View view){
//        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, LOAD_STEP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == LOAD_COVER_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageButton cover_pic = (ImageButton) findViewById(R.id.cover_pic);
            Bitmap original_bitmap = BitmapFactory.decodeFile(picturePath);
            Bitmap scaled_bitmap = Bitmap.createScaledBitmap(original_bitmap,1000,180,false);
            cover_pic.setImageBitmap(scaled_bitmap);
            cover_pic.setScaleType(ImageButton.ScaleType.FIT_XY);
        }

//        else if (requestCode == LOAD_STEP_IMAGE && resultCode == RESULT_OK && null != data){
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            ImageButton cover_pic = (ImageButton) findViewById(R.id.step_image);
//            Bitmap original_bitmap = BitmapFactory.decodeFile(picturePath);
//            Bitmap scaled_bitmap = Bitmap.createScaledBitmap(original_bitmap,1000,180,false);
//            cover_pic.setImageBitmap(scaled_bitmap);
//            cover_pic.setScaleType(ImageButton.ScaleType.FIT_XY);
//        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void addIngredient(View view){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog_layout));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add more ingredients");
//        builder.setMessage("result");
//        EditText editText1 = new EditText(this);
//        EditText editText2 = new EditText(this);
//
//        builder.setView(editText1);
//        builder.setView(editText2);

        builder.setView(layout);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                dialog.cancel();
            }
        });

//        builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog, int which) {
//                // here you can add functions
////                dialog.cancel();
//                ingredient1 =
//            }
//        });
        builder.setPositiveButton("Add", this);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if(which == AlertDialog.BUTTON_POSITIVE){

            AlertDialog ad = (AlertDialog) dialog;
            EditText t1 = (EditText) ad.findViewById(R.id.dialog_edit1);
            EditText t2 = (EditText) ad.findViewById(R.id.dialog_edit2);

            Ingredients tempData = new Ingredients();
            tempData.setIngredient(t1.getText().toString());
            tempData.setAmount(t2.getText().toString());
            items.add(tempData);

            ingredientListAdapter.notifyDataSetChanged();

            refreshListview();
            
            Toast.makeText(this, t1.getText().toString(), Toast.LENGTH_LONG)
                    .show();
        }

    }


//    public void onClickSubmit(View view){       // function of "SUBMIT" button
//        // do stuff...
//    }



    public void addSteps(View view){
        click++;
        Steps tempData = new Steps();
        tempData.setStep_num(Integer.toString(click));
        tempData.setImage_source(R.drawable.step_picture);

        steps.add(tempData);

        stepListAdapter.notifyDataSetChanged();
        refreshListview2();
    }

<<<<<<< HEAD
    public void submitRecipe(View view){

    }
=======
>>>>>>> b7311af018a207df533c39c686d32bf6269aaced
}
