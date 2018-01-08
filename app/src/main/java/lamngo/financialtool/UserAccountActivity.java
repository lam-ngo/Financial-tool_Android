package lamngo.financialtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import lamngo.financialtool.UserAccount.User;

public class UserAccountActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = UserAccountActivity.class.getSimpleName();

    /** URL to query user info */
    private static final String USER_ACCOUNT_URL =
            "http://financial-tool-env.td5uvdyeme.eu-west-1.elasticbeanstalk.com/user/getUser?userName=user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        // Kick off an {@link AsyncTask} to perform the network request
        UserAccountAsyncTask task = new UserAccountAsyncTask();
        task.execute();
    }

    /**
     * Update the screen to display information from the given {@link User}.
     */
    private void updateUi(User user) {
        // Set userId textView
        TextView userId = (TextView) findViewById(R.id.user_account_id);
        userId.setText(String.valueOf(user.getUserId()));

        // Set userName textView
        TextView userName = (TextView) findViewById(R.id.user_account_name);
        userName.setText(user.getUserName());

        // Set email textView
        TextView email = (TextView) findViewById(R.id.user_account_email);
        email.setText(user.getEmail());

        // Set role textView
        TextView role = (TextView) findViewById(R.id.user_account_role);
        role.setText(user.getUserRole());
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the user account info in the response.
     */
    private class UserAccountAsyncTask extends AsyncTask<URL, Void, User> {

        @Override
        protected User doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(USER_ACCOUNT_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create an {@link User} object
            User user = extractFeatureFromJson(jsonResponse);

            // Return the {@link User} object as the result fo the {@link User}
            return user;
        }

        /**
         * Update the screen with the given user (which was the result of the
         * {@link UserAccountAsyncTask}).
         */
        @Override
        protected void onPostExecute(User user) {
            if (user == null) {
                return;
            }
            updateUi(user);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "Loading...";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTE2ODc1MzM5fQ.aj084NY73hzS8fczRvxV3FLwvUKts-ReogFMNrD-oYgYbWf3oLX-MStsAk28n2eXsWf3aY77x1W2m_JOWGT4fA");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                Log.i("makeHttpRequest", "Connection sent!");
                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    Log.i("makeHttpRequest", "Response received ok!");
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the user JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link User} object by parsing out information
         * about the user from the userJSON string.
         */
        private User extractFeatureFromJson(String allDataJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(allDataJSON)) {
                return new User();
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(allDataJSON);
                int userId = baseJsonResponse.getInt("userId");
                String userName = baseJsonResponse.getString("userName");
                String email = baseJsonResponse.getString("email");
                String role = baseJsonResponse.getString("role");

                return new User(userId, userName, email, role);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the user JSON results", e);
            }
            return new User();
        }
    }
}
