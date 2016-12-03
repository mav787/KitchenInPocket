package com.greatsky.kitcheninpocket.AddRecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greatsky.kitcheninpocket.AddRecipe.Ingredients;
import com.greatsky.kitcheninpocket.R;

import java.util.ArrayList;

/**
 * Created by fangwenli on 29/11/2016.
 */

public class IngredientListAdapter extends BaseAdapter {

    Context context;

    ArrayList<Ingredients> ingredients;


    public IngredientListAdapter(Context context, ArrayList<Ingredients> arrayList){
        this.context = context;
//        sample.add("apple");
//        sample.add("banana");
        this.ingredients = arrayList;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Object getItem(int i) {
        return ingredients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.ingredient_list, viewGroup, false);

        TextView ingre1 = (TextView)row.findViewById(R.id.ingre1);
        TextView amount1 = (TextView)row.findViewById(R.id.amount1);
    
        Ingredients temp = ingredients.get(i);

        ingre1.setText(temp.getIngredient());
        amount1.setText(temp.getAmount());

        return row;
    }


}

