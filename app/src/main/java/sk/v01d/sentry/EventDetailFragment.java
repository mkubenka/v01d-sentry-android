package sk.v01d.sentry;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

import org.acra.ACRA;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private int mEventID;

    private TextView abstractTextView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mEventID = getArguments().getInt(ARG_ITEM_ID);

            if(mEventID > 0) {
                EventDetailTask task = new EventDetailTask();
                task.execute(new Integer[]{mEventID});
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);

        abstractTextView = (TextView) rootView.findViewById(R.id.event_detail);

        return rootView;
    }

    private class EventDetailTask extends AsyncTask<Integer, Void, String> {
        private static final String TAG = "EventDetailTask";

        @Override
        protected String doInBackground(Integer... eventID) {
            try {
                OkHttpClient client = new OkHttpClient();

                Uri.Builder builder = Uri.parse(EventApplication.API_SERVER).buildUpon();
                builder.path("/api/v1/events/" + eventID[0]);

                HttpURLConnection connection = client.open(new URL(builder.build().toString()));
                InputStream is = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");

                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null;) {
                    stringBuilder.append(line).append("\n");
                }

                JSONObject jsonResult = new JSONObject(stringBuilder.toString());
                JSONObject event = jsonResult.getJSONObject("event");

                return event.getString("abstract");
            } catch(MalformedURLException e) {
                Log.e(TAG, "MalformedURLException", e);
                ACRA.getErrorReporter().handleSilentException(e);
            } catch(IOException e) {
                Log.e(TAG, "IOException", e);
                ACRA.getErrorReporter().handleSilentException(e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
                ACRA.getErrorReporter().handleSilentException(e);
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            abstractTextView.setText(result);
        }
    }
}
