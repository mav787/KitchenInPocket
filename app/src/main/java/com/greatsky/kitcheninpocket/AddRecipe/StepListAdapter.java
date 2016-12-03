package com.greatsky.kitcheninpocket.AddRecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greatsky.kitcheninpocket.R;

import java.util.ArrayList;

/**
 * Created by fangwenli on 01/12/2016.
 */

public class StepListAdapter extends BaseAdapter {

    Context context;

    ArrayList<Steps> steps;


    public StepListAdapter(Context context, ArrayList<Steps> arrayList){
        this.context = context;
//        sample.add("apple");
//        sample.add("banana");
        this.steps = arrayList;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public Object getItem(int i) {
        return steps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.step_list, viewGroup, false);

//        TextView ingre1 = (TextView)row.findViewById(R.id.ingre1);
//        TextView amount1 = (TextView)row.findViewById(R.id.amount1);

        TextView step_num = (TextView)row.findViewById(R.id.step_num);
        ImageButton step_image = (ImageButton)row.findViewById(R.id.step_image);
        EditText step_description = (EditText)row.findViewById(R.id.step_description);

        Steps temp = steps.get(i);

        step_num.setText(temp.getStep_num());
        step_image.setBackgroundResource(temp.getImage_source());
        step_description.setText(temp.getDescription());

        return row;
    }


}

