package com.greatsky.kitcheninpocket.AddRecipe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.HerokuImageService;
import com.greatsky.kitcheninpocket.HerokuService;
import com.greatsky.kitcheninpocket.LoginActivity;
import com.greatsky.kitcheninpocket.MainActivity;
import com.greatsky.kitcheninpocket.R;
import com.greatsky.kitcheninpocket.RegisterActivity;
import com.greatsky.kitcheninpocket.object.Authorization;
import com.greatsky.kitcheninpocket.object.Ingredients;
import com.greatsky.kitcheninpocket.object.Recipe;
import com.greatsky.kitcheninpocket.object.Registration;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.platform.win32.WinGDI;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by fangwenli on 13/11/2016.
 */

public class AddRecipeActivity extends Activity implements DialogInterface.OnClickListener{

    ArrayList<Ingredients_list> items = new ArrayList<>();
    ArrayList<Steps> steps = new ArrayList<>();
    ListView listView;
    ListView step_list;
    IngredientListAdapter ingredientListAdapter;
    StepListAdapter stepListAdapter;
    String ingredient1;
    String amount1;
    String upload_recipe_name;
    ArrayList<Ingredients> upload_ingredients = new ArrayList<>();
    ArrayList<String> upload_steps = new ArrayList<>();
    String recipe_url;
    String upload_recipe_result;
    String upload_img_result;
    EditText recipe_name;
    Uri selectedImage;
    SharedPreferences shared;
    String access_token = "";
    String returned_url;

    //Button btn_submit;

    int click = 0;
    private int LOAD_COVER_IMAGE = 1;
    private int REQUEST_PERMISSION = 2;
    private int LOAD_STEP_IMAGE = 3;


    public void refreshListview(){

        int totalHeight = 0;
        for (int i = 0, len = ingredientListAdapter.getCount(); i < len; i++) {
            View listItem = ingredientListAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (ingredientListAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    public void refreshListview2(){

        int totalHeight = 0;
        for (int i = 0, len = stepListAdapter.getCount(); i < len; i++) {
            View listItem = stepListAdapter.getView(i, null, step_list);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = step_list.getLayoutParams();
        params.height = totalHeight + (step_list.getDividerHeight() * (stepListAdapter.getCount() - 1));

        step_list.setLayoutParams(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);
        recipe_name = (EditText)findViewById(R.id.recipe_name);


        Ingredients_list i1 = new Ingredients_list("Add ingredients", "Add amount");

        items.add(i1);


        listView = (ListView)findViewById(R.id.ingredient_list);
        ingredientListAdapter = new IngredientListAdapter(AddRecipeActivity.this, items);

        //to deal with the problem that a listview on a scrollview cannot show the whole data
        refreshListview();

        listView.setAdapter(ingredientListAdapter);

        Steps step1 = new Steps("steps", "Add description");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == LOAD_COVER_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
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

            Ingredients_list tempData = new Ingredients_list();
            tempData.setIngredient(t1.getText().toString());
            tempData.setAmount(t2.getText().toString());
            items.add(tempData);

            ingredientListAdapter.notifyDataSetChanged();

            refreshListview();
            
            Toast.makeText(this, t1.getText().toString(), Toast.LENGTH_LONG).show();
        }

    }


//    public void onClickSubmit(View view){       // function of "SUBMIT" button
//        // do stuff...
//    }



    public void addSteps(View view){
        click++;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.step_dialog, (ViewGroup) findViewById(R.id.step_dialog));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add more steps.");

        builder.setView(layout);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                AlertDialog ad = (AlertDialog) dialog;
                EditText e1 = (EditText) ad.findViewById(R.id.dialog_step_edit1);

                Steps tempData = new Steps();
                tempData.setStep_num(Integer.toString(click));
                tempData.setDescription(e1.getText().toString());
                steps.add(tempData);

                stepListAdapter.notifyDataSetChanged();

                refreshListview2();
                Toast.makeText(getBaseContext(), e1.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void submitRecipe(View view) throws IOException {
        uploadFile(selectedImage);

        upload_recipe_name = recipe_name.getText().toString();
        //recipe_url

        for (int i = 1; i<items.size();i++)
        {
            Ingredients ingredients = new Ingredients();
            ingredients.setName(items.get(i).getIngredient());
            ingredients.setAmount(items.get(i).getAmount());
            upload_ingredients.add(ingredients);
        }

        for (int i = 1; i<steps.size(); i++)
        {
            upload_steps.add(steps.get(i).getDescription());
        }

    }

    protected void uploadRecipeRequest()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);

        shared = getSharedPreferences("login", Context.MODE_PRIVATE);
        access_token = shared.getString("access_token", "");

        Recipe recipe = new Recipe(access_token, upload_recipe_name, returned_url, upload_ingredients, upload_steps);
        Call<ResponseBody> call = restAPI.uploadRecipe(recipe, access_token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Log.e("===","return:" + response.body().toString());
                BufferedSource source = response.body().source();
                try {
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Buffer buffer = source.buffer();
                upload_recipe_result = buffer.clone().readString(Charset.forName("UTF-8"));
                afterUploadRecipe();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });

    }

    public void afterUploadRecipe()
    {
        if(upload_recipe_result.contains("success")) {
            Intent intent = new Intent(AddRecipeActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(AddRecipeActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
        }
        else
        if(upload_recipe_result.contains("error"))
        {
            String[] msg = upload_recipe_result.split("\"");
            Toast.makeText(AddRecipeActivity.this, msg[7], Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadFile(Uri fileUri) throws IOException {
        // create upload service client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .build();
        HerokuImageService service = retrofit.create(HerokuImageService.class);

        String real_path = getRealFilePath(this, selectedImage);
        File file = new File(real_path);
        long length = file.length();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        String descriptionString = "hello";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString );

        Call<ResponseBody> call = service.upload_recipe_picture(requestFile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");

                BufferedSource source = response.body().source();
                try {
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Buffer buffer = source.buffer();
                upload_img_result = buffer.clone().readString(Charset.forName("UTF-8"));
                afterUploadImage();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void afterUploadImage()
    {
        if(upload_img_result.contains("success")) {

            String temp = upload_img_result.replaceAll("\"url\":\"", "lalalala");
            String[] xyz = temp.split("lalalala");
            returned_url = xyz[1].substring(0, xyz[1].length()-3);
            uploadRecipeRequest();

        }
        else if(upload_img_result.contains("error"))
        {
            String[] msg = upload_recipe_result.split("\"");
            Toast.makeText(AddRecipeActivity.this, msg[7], Toast.LENGTH_SHORT).show();
        }
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
