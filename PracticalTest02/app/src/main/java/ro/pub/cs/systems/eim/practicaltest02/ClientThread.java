package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread
{
    private String pokemonName;
    private Socket socket;
    private TextView abilityTextView, abilityTextType;
    private ImageView imageViewPokemon;

    public ClientThread(String pokemonName, TextView abilityTextView, TextView abilityTextType, ImageView imageViewPokemon)
    {
        this.pokemonName = pokemonName;
        this.abilityTextView = abilityTextView;
        this.abilityTextType = abilityTextType;
        this.imageViewPokemon = imageViewPokemon;
    }

    public void run()
    {
        try
        {
            socket = new Socket(Constants.SERVER_ADDRESS, Constants.SERVER_PORT);

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            printWriter.println(pokemonName);
            printWriter.flush();

            String finalResult = "";
            while ((finalResult = bufferedReader.readLine()) != null)
            {
                Log.d(Constants.TAG, "[CLIENT THREAD] Got from COMMUNICATION THREAD : " + finalResult);
                final String abilityResult = finalResult;
                abilityTextView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        abilityTextView.setText(abilityResult);
                    }
                });
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}