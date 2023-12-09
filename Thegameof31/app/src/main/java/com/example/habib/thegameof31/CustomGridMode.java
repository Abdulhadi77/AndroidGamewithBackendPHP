package com.example.habib.thegameof31;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ASUS on 5/20/2016.
 */
public class CustomGridMode extends BaseAdapter {

    private Context mContext;
    private final String[] nameModes;
    private final int[] ImageModesId;
    ImageView imageView;
    TextView textViewNameMode;
    TextView textViewNumLevelsWins;

    public CustomGridMode(Context c, String[] nameModes, int[] ImageModesId ) {
        mContext = c;
        this.nameModes = new String[22];
        this.ImageModesId = new int [22];
        for (int i=0;i<22;i++)
            this.nameModes[i] = nameModes[i];
        for (int i=0;i<22;i++)
            this.ImageModesId[i] = ImageModesId[i];
    }

    @Override
    public int getCount() {
        return nameModes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_mode_single, parent,false);
        } else {
            grid = (View) convertView;
        }
        textViewNameMode = (TextView) grid.findViewById(R.id.mode_name);
        imageView = (ImageView)grid.findViewById(R.id.grid_mode_image);
        textViewNumLevelsWins = (TextView) grid.findViewById(R.id.num_levels_wins);

        textViewNameMode.setText(nameModes[position]);
        imageView.setImageResource(ImageModesId[position]);
        textViewNumLevelsWins.setText(String.valueOf(lastLevelAndWinLevelsArray.lastLevel[position]) +"/22");
        return grid;
    }
}
