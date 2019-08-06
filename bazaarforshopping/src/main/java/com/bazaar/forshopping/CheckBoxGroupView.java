package com.bazaar.forshopping;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CheckBoxGroupView extends GridLayout {

    List<CheckBox> checkboxes = new ArrayList<>();

    public CheckBoxGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void put(CheckBox checkBox) {
        checkboxes.add( checkBox);
        invalidate();
        requestLayout();
    }

    public void remove(Integer id) {
        // TODO: Remove items from ArrayList
    }

    public List<?> getCheckboxesChecked(){

        List<CheckBox> checkeds = new ArrayList<>();
        for (CheckBox c : checkboxes){
            if(c.isChecked())
                checkeds.add(c);
        }

        return checkeds;
    }

    public List<Object> getCheckedIds(){

        List<Object> checkeds = new ArrayList<>();
        for (CheckBox c : checkboxes){
            if(c.isChecked())
                checkeds.add(c.getTag());
        }
        return checkeds;
    }

    public String getCheckedNames(){
        String names = "";
        for (CheckBox c : checkboxes){
            if(c.isChecked())
                names = names + c.getText().toString() + ",";
        }

        return names.substring(0, names.length()-1);
    }

    public int getCheckedCount(){
        int count = 0;
        for (CheckBox c : checkboxes){
            if(c.isChecked())
                count++;
        }
        return count;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        for(CheckBox c: checkboxes) {
            addView(c);
        }

        invalidate();
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}