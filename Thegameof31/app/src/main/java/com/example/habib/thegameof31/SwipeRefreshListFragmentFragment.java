package com.example.habib.thegameof31;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

public class SwipeRefreshListFragmentFragment extends FragmentFacebook {

    private static final String LOG_TAG = SwipeRefreshListFragmentFragment.class.getSimpleName();

    private static final int LIST_ITEM_COUNT = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Notify the system to allow an options menu for this fragment.
        setHasOptionsMenu(true);
    }

    // BEGIN_INCLUDE (setup_views)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    // END_INCLUDE (setup_views)

    private void initiateRefresh() {
        Log.i(LOG_TAG, "initiateRefresh");

        new DummyBackgroundTask().execute();
    }
    private void onRefreshComplete(List<String> result) {
        Log.i(LOG_TAG, "onRefreshComplete");

        // Remove all items from the ListAdapter, and then replace them with the new items
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
        adapter.clear();
        for (String cheese : result) {
            adapter.add(cheese);
        }

        // Stop the refreshing indicator
        setRefreshing(false);
    }
    private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>> {

        static final int TASK_DURATION = 1 * 1000; // 3 seconds

        @Override
        protected List<String> doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	super.onPreExecute();
        	System.out.println("Started");
        }


        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            System.out.println("Stopped");
            onRefreshComplete(result);
        }
    }
}
