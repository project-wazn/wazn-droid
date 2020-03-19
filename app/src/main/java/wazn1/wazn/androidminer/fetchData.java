package wazn1.wazn.androidminer;


import android.os.AsyncTask;
import android.view.View;
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

public class fetchData extends AsyncTask<Void,Void,Void> {
    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    int Error = 0;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //Change this API address
            URL url = new URL("https://wallet.wazn.io/o.php?r=stats&wallet=" + StatsFragment.wallet);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONArray JA = new JSONArray(data);
            for(int i = 0; i < JA.length(); i++ ){
                JSONObject JO = (JSONObject) JA.get(i);
                singleParsed = "Total Due: " + JO.get("balance") + " WAZN\n" +
                               "Total Paid: " + JO.get("totalPaid") + " WAZN\n" +
                               "Total Hashes: " + JO.get("totalHashes") + "\n" +
                               "Minimum Pay: " + JO.get("minPay") + " WAZN\n";
                dataParsed = dataParsed + singleParsed;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Error = 1;
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(this.dataParsed!=null && Error==0) {
            StatsFragment.data.setText(this.dataParsed);
        }else{
            StatsFragment.data.setText("No stats found. You may need to start mining first. If you have already started mining, check back in a few minutes.");
        }
    }
}
