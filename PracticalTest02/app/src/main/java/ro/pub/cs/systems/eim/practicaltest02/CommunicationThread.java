package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class CommunicationThread extends Thread
{
    private Socket socket = null;

    public CommunicationThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        try
        {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            String pokeName = bufferedReader.readLine();
            Log.d(Constants.TAG, "[COMMUNICATION THREAD] Got : " + pokeName);

            String stringUrl = Constants.API_V_2_POKEMON + pokeName + "/";
            URL url = new URL(stringUrl);
            //HttpClient httpClient = new DefaultHttpClient();  //deprecated
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader httpBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer inputBuffer = new StringBuffer();
            String inputLine = "";

            while ((inputLine = httpBufferedReader.readLine()) != null)
            {
                inputBuffer.append(inputLine);
            }

            Log.d(Constants.TAG, "[COMMUNICATION THREAD] Got from REMOTE API : " + inputBuffer);
            try
            {
                JSONObject jsonResponse = new JSONObject(inputBuffer.toString());
                JSONArray abilitiesArray = jsonResponse.getJSONArray(Constants.ABILITIES);
                JSONArray typesArray = jsonResponse.getJSONArray(Constants.TYPES);

                StringBuffer finalResult = new StringBuffer();
                for (int i = 0; i < abilitiesArray.length(); i++)
                {
                    JSONObject abilityIter = abilitiesArray.getJSONObject(i);
                    String abilityName = abilityIter.getJSONObject(Constants.ABILITY).getString(Constants.NAME);
                    finalResult.append(abilityName).append(", ");
                    Log.d(Constants.TAG, "[COMMUNICATION THREAD] name : " + abilityName);
                }
                for (int i = 0; i < typesArray.length(); i++)
                {
                    JSONObject typeIter = typesArray.getJSONObject(i);
                    String typeName = typeIter.getJSONObject(Constants.TYPE).getString(Constants.NAME);
                    finalResult.append(typeName).append(", ");
                    Log.d(Constants.TAG, "[COMMUNICATION THREAD] name : " + typeName);
                }
                printWriter.println(finalResult.toString());
                printWriter.flush();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}