package com.example.habib.thegameof31;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentRandom extends Fragment {

    public FragmentRandom() {
    }
    TextView userNameTextView;
    TextView scoreTextView;
    public Context context = getActivity();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_random, container, false);
        //mySocket.init(context);
        //Intent intent = getActivity().getIntent();
        //View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.activity_fragment_random, null);
        /*userNameTextView = (TextView) inflatedView.findViewById(R.id.userNameOfRandomPlayer);
        scoreTextView = (TextView) inflatedView.findViewById(R.id.scoreOfRandomPlayer);
        Button searchButton = (Button) rootView.findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameTextView.setVisibility(View.VISIBLE);
                scoreTextView.setVisibility(View.VISIBLE);
                JSONObject searchRandomPlayerRequest = new JSONObject();
                try {
                    searchRandomPlayerRequest.put("cmd" , "searchRandomPlayerRequest");
                    searchRandomPlayerRequest.put("AAA","ABC");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mySocket.mWebSocketClient.send(searchRandomPlayerRequest.toString());
            }
        });*/
        return rootView;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Context context = getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(STATE_CURRENT_SCENE, mCurrentScene);
    }

}
