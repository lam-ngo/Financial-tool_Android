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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import lamngo.financialtool.Transaction.TransactionEvent;

/**
 * Displays Dashboard with 10 last transactions
 */
public class DashboardActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = DashboardActivity.class.getSimpleName();

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
    private void updateUi(ArrayList<TransactionEvent> transactionArray) {
        // Get the table view
        TableLayout table = (TableLayout) findViewById(R.id.dashboard_table);
        Double totalSale = 0.0;
        Log.i("Array length", String.valueOf(transactionArray.size()));

//        TransactionEvent singleTransaction = new TransactionEvent(123456, 1515446378272L, "Test product");
//        idText.setText("test ID");
//        dateText.setText("test date");
//        productText.setText("test product");

        for (int i = 0; i < 10; i++) {
            // Create new row
            TableRow newRow = new TableRow(this);

            // Display the id in the UI
            TextView idText = new TextView(this);
            idText.setLayoutParams(new TableRow.LayoutParams(1));
            idText.setPadding(40,40,40,40);
            // Display the date in the UI
            TextView dateText = new TextView(this);
            dateText.setLayoutParams(new TableRow.LayoutParams(2));
            dateText.setPadding(40,40,40,40);
            // Display product name in the UI
            TextView productText = new TextView(this);
            productText.setLayoutParams(new TableRow.LayoutParams(3));
            productText.setPadding(40,40,40,40);
            TransactionEvent singleTransaction;

            singleTransaction = transactionArray.get(i);
            idText.setText(getIdString(singleTransaction.getTransactionId()));
            dateText.setText(getDateString(singleTransaction.getDate()));
            productText.setText(singleTransaction.getProductName());

            newRow.addView(idText);
            newRow.addView(dateText);
            newRow.addView(productText);
            table.addView(newRow);
            totalSale += singleTransaction.getPrice();
        }

        TextView totalSaleText = (TextView) findViewById(R.id.dashboard_total_sale);
        totalSaleText.setText("Total sales for the last 10 transactions was € " + new DecimalFormat("##.##").format(totalSale));
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
    private class TransactionAsyncTask extends AsyncTask<URL, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(TRANSACTION_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create an {@link TransactionEvent} object
            ArrayList transactionArray = extractFeatureFromJson(jsonResponse);

            // Return the {@link TransactionEvent} object as the result fo the {@link TransactionAsyncTask}
            return transactionArray;
        }

        /**
         * Update the screen with the given transaction (which was the result of the
         * {@link TransactionAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList transactionArray) {
            if (transactionArray == null) {
                return;
            }

            updateUi(transactionArray);
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
                Log.e(LOG_TAG, "Problem retrieving the transaction JSON results.", e);
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
        private ArrayList<TransactionEvent> extractFeatureFromJson(String allDataJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(allDataJSON)) {
                return new ArrayList<TransactionEvent>();
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(allDataJSON);
                JSONArray transactionJSONArray = baseJsonResponse.getJSONArray("transaction");

                // If there are results in the features array
                if (transactionJSONArray.length() > 0) {
                    ArrayList<TransactionEvent> transactionArray = new ArrayList<>();
                    JSONObject JSONTransaction;
                    int transactionId;
                    long date;
                    String productName;
                    Double productPrice;
                    JSONObject product;
                    TransactionEvent transactionEvent;

                    for(int i = 0; i < 10; i++) {
                        // Extract out each transaction and save data into transactionEvent
                        JSONTransaction = transactionJSONArray.getJSONObject(i);

                        // Extract out the title, time, and transaction values
                        transactionId = JSONTransaction.getInt("transactionId");
                        date = Long.valueOf(JSONTransaction.getLong("inputDate"));
                        product = JSONTransaction.getJSONObject("product");
                        productName = product.getString("productName");
                        productPrice = product.getDouble("price");

                        transactionEvent = new TransactionEvent(transactionId, date, productName, productPrice);
                        transactionArray.add(transactionEvent);
                    }
                    return transactionArray;
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the transaction JSON results", e);
            }
            return new ArrayList<TransactionEvent>();
        }
    }
}
