package com.greatsky.kitcheninpocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by fangwenli on 13/11/2016.
 */

public class AddRecipeActivity extends Activity implements DialogInterface.OnClickListener{

    ArrayList<Ingredients> items = new ArrayList<>();
    ListView listView;
    IngredientListAdapter ingredientListAdapter;
    String ingredient1;
    String amount1;
    Button btn_submit;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        Ingredients i1 = new Ingredients("apple", "1");
        Ingredients i2 = new Ingredients("egg", "1");
        Ingredients i3 = new Ingredients("egg", "1");
        items.add(i1);
        items.add(i2);
        items.add(i3);
        btn_submit = (Button)findViewById(R.id.btn_submit);


        listView = (ListView)findViewById(R.id.ingredient_list);
        ingredientListAdapter = new IngredientListAdapter(AddRecipeActivity.this, items);

        //to deal with the problem that a listview on a scrollview cannot show the whole data
        refreshListview();

        listView.setAdapter(ingredientListAdapter);
    }

    public void addCoverPicture(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        startActivity(intent);
    }

    public void addStep1Picture(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        startActivity(intent);
    }

    public void addStep2Picture(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        startActivity(intent);
    }

    public void addStep3Picture(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        startActivity(intent);
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

    public void onClickSubmit(View view){       // function of "SUBMIT" button
        // do stuff...
    }


}
