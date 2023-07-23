package com.example.navigatorappandroid;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onHelpClick(View view) {
        
        Intent intent = new Intent(this, StartVideoActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
    }

    public void onRegistrationClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("language", )
        startActivity(intent);
    }
}