package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.io.IOException;
import java.net.ServerSocket;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText textInput;
    private Button submitButton;
    private ImageView imageViewPokemon;
    private TextView textType, textAbility;
    private ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        textInput = findViewById(R.id.textInput);
        submitButton = findViewById(R.id.submitButton);
        textAbility = findViewById(R.id.textAbility);
        textType = findViewById(R.id.textType);
        imageViewPokemon = findViewById(R.id.imageViewPokemon);

        serverThread = new ServerThread();
        serverThread.start();

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ClientThread clientThread = new ClientThread(textInput.getText().toString(), textAbility, textType, imageViewPokemon);
                clientThread.start();
            }
        });
    }

}