package com.example.examenreversi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton arquero1,arquero2,ninja1,ninja2,robot1,robot2;
    String pieza1="",pieza2="";
    Bundle data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arquero1 = findViewById(R.id.arquero1);
        arquero2 = findViewById(R.id.arquero2);
        ninja1 = findViewById(R.id.ninja1);
        ninja2 = findViewById(R.id.ninja2);
        robot1 = findViewById(R.id.robot1);
        robot2 = findViewById(R.id.robot2);
        data = new Bundle();
    }

    public void arquero1Boton(View view){
        arquero1.setAlpha((float) 1);
        pieza1 = "Arquero";
        ninja1.setAlpha((float) .4);
        robot1.setAlpha((float) .4);
    }

    public void arquero2Boton(View view){
        arquero2.setAlpha((float) 1);
        pieza2 = "Arquero";
        ninja2.setAlpha((float) .4);
        robot2.setAlpha((float) .4);
    }

    public void ninja1Boton(View view){
        ninja1.setAlpha((float) 1);
        pieza1 = "Ninja";
        arquero1.setAlpha((float) .4);
        robot1.setAlpha((float) .4);
    }

    public void ninja2Boton(View view){
        ninja2.setAlpha((float) 1);
        pieza2 = "Ninja";
        arquero2.setAlpha((float) .4);
        robot2.setAlpha((float) .4);
    }

    public void robot1Boton(View view){
        robot1.setAlpha((float) 1);
        pieza1 = "Robot";
        arquero1.setAlpha((float) .4);
        ninja1.setAlpha((float) .4);
    }

    public void robot2Boton(View view){
        robot2.setAlpha((float) 1);
        pieza2 = "Robot";
        arquero2.setAlpha((float) .4);
        ninja2.setAlpha((float) .4);
    }

    public void iniciar(View view){
        if (pieza1.equals(pieza2)){
            Toast.makeText(this, "Selecciona piezas distintas", Toast.LENGTH_SHORT).show();
        }else{
            Intent juego = new Intent(this,Partida.class);
            data.putString("nombre1",pieza1);
            data.putString("nombre2",pieza2);
            juego.putExtra("data",data);
            startActivity(juego);
            finish();
        }
    }

}