package com.example.habib.thegameof31;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final String[] nameLevel;
    private final int[] Imageid;
    ImageView imageView;
    TextView textView;
    public static int modePos;

    public CustomGrid(Context c,String[] nameLevel,int[] Imageid ) {
        mContext = c;
        this.nameLevel = new String[22];
        this.Imageid = new int [22];
        for (int i=0;i<22;i++)
            this.nameLevel[i] = nameLevel[i];
        for (int i=0;i<22;i++)
            this.Imageid[i] = Imageid[i];

        //if i want to add textview or anything to a grid item
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nameLevel.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //String userNameForKeepLogin = sharedpreferences.getString("userNameForKeepLogin",null);


        //this doesn't repeat items when scroll the layout
        View grid;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, parent,false);
        } else {
            grid = (View) convertView;
        }

        textView = (TextView) grid.findViewById(R.id.grid_text);
        imageView = (ImageView)grid.findViewById(R.id.grid_image);

        textView.setText(nameLevel[position]);
        if (position != 0 && position != 1 && position > lastLevelAndWinLevelsArray.lastLevel[modePos])
            imageView.setImageResource(R.drawable.lockpurple);
        else
            imageView.setImageResource(Imageid[position]);
        isEnabled(position);

        return grid;
    }

    @Override
    public boolean isEnabled(int position) {
        //return false ==> lock
        //return true ==> unlock
        //check the position to be locked or not
        //if yes then return true, else return false
        if (position != 0 && position != 1 && position > lastLevelAndWinLevelsArray.lastLevel[modePos]){
            return false;
        }
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}