package lamngo.financialtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.TextUtils;
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

import lamngo.financialtool.Transaction.TransactionEvent;

/**
 * Displays information about a single earthquake.
 */
public class DashboardActivity extends AppCompatActivity {

    /** URL to query all the transactions */
    private static final String TRANSACTION_URL =
            "http://financial-tool-env.td5uvdyeme.eu-west-1.elasticbeanstalk.com/user/getAllData?userId=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Kick off an {@link AsyncTask} to perform the network request
        TransactionAsyncTask task = new TransactionAsyncTask();
        task.execute();
    }

    /**
     * Update the screen to display information from the given {@link TransactionEvent}.
     */
    private void updateUi(TransactionEvent transaction) {
        // Display the id in the UI
        TextView idText = (TextView) findViewById(R.id.transaction_id);
//        idText.setText(getIdString(transaction.getTransactionId()));
        idText.setText("test ID");

        // Display the date in the UI
        TextView dateText = (TextView) findViewById(R.id.transaction_date);
//        dateText.setText(getDateString(transaction.getDate()));
        dateText.setText("test date");

        // Display product name in the UI
        TextView productText = (TextView) findViewById(R.id.transaction_product);
//        productText.setText(transaction.getProductName());
        productText.setText("test product");
    }

    private String getIdString(long transactionId) {
        return String.valueOf(transactionId);
    }

    /**
     * Returns a formatted date string for transaction.
     */
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(timeInMilliseconds);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the transaction in the response.
     */
    private class TransactionAsyncTask extends AsyncTask<URL, Void, TransactionEvent> {

        @Override
        protected TransactionEvent doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(TRANSACTION_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
//                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create an {@link TransactionEvent} object
            TransactionEvent transaction = extractFeatureFromJson(jsonResponse);

            // Return the {@link TransactionEvent} object as the result fo the {@link TransactionAsyncTask}
            return transaction;
        }

        /**
         * Update the screen with the given transaction (which was the result of the
         * {@link TransactionAsyncTask}).
         */
        @Override
        protected void onPostExecute(TransactionEvent transaction) {
            if (transaction == null) {
                return;
            }

            updateUi(transaction);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
//                Log.e(LOG_TAG, "Error with creating URL", exception);
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

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
//                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
//                Log.e(LOG_TAG, "Problem retrieving the transaction JSON results.", e);
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
         * Return an {@link TransactionEvent} object by parsing out information
         * about the first transaction from the input transactionJSON string.
         */
        private TransactionEvent extractFeatureFromJson(String allDataJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(allDataJSON)) {
                return new TransactionEvent(123456, 1513382400000L, "This is a test");
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(allDataJSON);
                JSONArray transactionArray = baseJsonResponse.getJSONArray("transaction");

                // If there are results in the features array
                if (transactionArray.length() > 0) {
                    // Extract out the first object (which is an transaction)
                    JSONObject firstTransaction = transactionArray.getJSONObject(0);
                    //JSONObject properties = firstTransaction.getJSONObject("properties");

                    // Extract out the title, time, and transaction values
                    int transactionId = firstTransaction.getInt("transactionId");
                    long date = firstTransaction.getLong("inputDate");
                    String product = firstTransaction.getString("product");

                    // Create a new {@link TransactionEvent} object
                    return new TransactionEvent(transactionId, date, product);
                }
            } catch (JSONException e) {
//                Log.e(LOG_TAG, "Problem parsing the transaction JSON results", e);
            }
            return new TransactionEvent(123456, 1513382400000L, "This is a test");
        }
    }
}
