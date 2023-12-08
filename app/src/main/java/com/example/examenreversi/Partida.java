package com.example.examenreversi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Partida extends AppCompatActivity {
    TextView highscore,turno;
    ImageView piezaCentral1, piezaCentral2, piezaCentral3, piezaCentral4,piezaGandora;
    int[][] matrizJuego = new int[8][8];
    ImageView[][] matrizCasillas = new ImageView[8][8];
    SharedPreferences sharedPreferences;
    int puntuacionMaxima;
    LinearLayout gridReversi;
    Bundle data;
    String nombre1,nombre2;
    int pieza1,pieza2,puntuacion1,puntuacion2;
    boolean player1 = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);
        data = getIntent().getExtras().getBundle("data");
        nombre1 = data.getString("nombre1");
        nombre2 = data.getString("nombre2");
        pieza1 = piezas(nombre1);
        pieza2 = piezas(nombre2);
        sharedPreferences = getSharedPreferences("puntos",MODE_PRIVATE);
        puntuacionMaxima = sharedPreferences.getInt("puntos",0);
        piezaGandora = findViewById(R.id.piezaGanadora);
        gridReversi = findViewById(R.id.tablero);
        highscore = findViewById(R.id.puntajeAlto);
        turno = findViewById(R.id.turnoActual);
        highscore.setText("Puntuacion mas alta: "+puntuacionMaxima);
        piezaCentral1 = findViewById(R.id.pieza28);
        piezaCentral2 = findViewById(R.id.pieza29);
        piezaCentral3 = findViewById(R.id.pieza36);
        piezaCentral4 = findViewById(R.id.pieza37);
        piezaCentral1.setImageResource(pieza2);
        piezaCentral4.setImageResource(pieza2);
        piezaCentral2.setImageResource(pieza1);
        piezaCentral3.setImageResource(pieza1);
        for (int i = 0; i < gridReversi.getChildCount(); i++) {
            // Linear Layout inside the main layout
            View columnas = gridReversi.getChildAt(i);
            if (columnas instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) columnas).getChildCount(); j++) {
                    // ImageView dentro de cada LinearLayout
                    View pieza = ((LinearLayout) columnas).getChildAt(j);
                    if (pieza instanceof ImageView) {
                        matrizCasillas[i][j] = (ImageView) pieza;
                        matrizJuego[i][j] = 0;
                    }
                }
            }
        }
        matrizJuego[3][3] = 2;
        matrizJuego[3][4] = 1;
        matrizJuego[4][3] = 1;
        matrizJuego[4][4] = 2;

        turno.setText(nombre1+" es tu turno");
        for (int i = 0; i < gridReversi.getChildCount(); i++){
            View columnas = gridReversi.getChildAt(i);
            if (columnas instanceof LinearLayout){
                for (int j=0; j < ((LinearLayout) columnas).getChildCount(); j++){
                    View pieza = ((LinearLayout) columnas).getChildAt(j);
                    if (pieza instanceof ImageView){
                        int finalI = i;
                        int finalJ = j;
                        pieza.setOnClickListener(v-> {
                            boolean piezaValida = checarPieza(finalI, finalJ);
                            if (piezaValida) {
                                ArrayList<int[]> indices = voltearPiezas(finalI, finalJ);
                                if(indices.size() >= 1){
                                    for (int[] indice:indices){
                                        actualizar(indice[0],indice[1],true);
                                    }
                                    actualizar(finalI,finalJ,true);
                                    if (player1){
                                        player1 = false;
                                        turno.setText(nombre2+" es tu turno");
                                    }else {
                                        player1 = true;
                                        turno.setText(nombre1+" es tu turno");
                                    }
                                }else{
                                    String jugadorCorrecto = player1 ? nombre1 : nombre2;
                                    turno.setText("Es el turno de "+jugadorCorrecto);
                                }
                            }else {
                                actualizar(finalI,finalJ,false);
                            }
                            if (hayMovimientosValidos()){
                                boolean tableroLleno = false;
                                puntuacion1 = 0;
                                puntuacion2 = 0;
                                for (int x=0; x < matrizJuego.length; x++){
                                    for (int y=0; y < matrizJuego[x].length; y++){
                                        if (matrizJuego[x][y] == 0){
                                            tableroLleno = true;
                                        }else {
                                            if (matrizJuego[x][y] == 1){
                                                puntuacion1++;
                                            }else{
                                                puntuacion2++;
                                            }
                                        }
                                    }
                                }
                                if (!tableroLleno){
                                    piezaGandora.setVisibility(View.VISIBLE);
                                    gridReversi.setVisibility(View.INVISIBLE);
                                    String nombre = puntuacion1 > puntuacion2 ? nombre1 : nombre2;
                                    piezaGandora.setImageResource(piezas(nombre));
                                    turno.setText(nombre+" es el ganador");
                                    if (puntuacion2 > puntuacion1){
                                        if (puntuacion2 > puntuacionMaxima){
                                            sharedPreferences.edit().putInt("puntos", puntuacion2).apply();
                                            highscore.setText("La puntuacion mas alta es: "+puntuacion2);
                                        }
                                    }else if (puntuacion1 > puntuacion2){
                                        if (puntuacion1 > puntuacionMaxima){
                                            sharedPreferences.edit().putInt("puntos", puntuacion1).apply();
                                            highscore.setText("La puntuacion mas alta es: "+puntuacion1);
                                        }
                                    }else{
                                        turno.setText("¡Tablas!");
                                    }
                                }
                            }else {
                                piezaGandora.setVisibility(View.VISIBLE);
                                gridReversi.setVisibility(View.INVISIBLE);
                                String nombre = puntuacion1 > puntuacion2 ? nombre1 : nombre2;
                                piezaGandora.setImageResource(piezas(nombre));
                                turno.setText(nombre+" es el ganador");
                                if (puntuacion2 > puntuacion1){
                                    if (puntuacion2 > puntuacionMaxima){
                                        sharedPreferences.edit().putInt("puntos", puntuacion2).apply();
                                        highscore.setText("La puntuacion mas alta es: "+puntuacion2);
                                    }
                                }else if (puntuacion1 > puntuacion2){
                                    if (puntuacion1 > puntuacionMaxima){
                                        sharedPreferences.edit().putInt("puntos", puntuacion1).apply();
                                        highscore.setText("La puntuacion mas alta es: "+puntuacion1);
                                    }
                                }else{
                                    turno.setText("¡Tablas!");
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public int piezas(String nombre){
        int imagen = 0;
        switch (nombre) {
            case "Arquero":
                imagen = R.drawable.arquero;
                break;
            case "Ninja":
                imagen = R.drawable.ninja;
                break;
            case "Robot":
                imagen = R.drawable.robot;
                break;
        }
        return  imagen;
    }

    public boolean checarPieza(int fila, int columna){
        int jugador = player1 ? 1 : 2;
        int piezaPresionada = matrizJuego[fila][columna];
        // Reference the pieces around the piece pressed
        int[] piezas = piezasAdyacentes(fila,columna);
        if (piezaPresionada == 0) {
            for (int i = 0; i < 8; i++) {
                if (piezas[i] != jugador) {
                    return true;
                }
            }
        }
        Toast.makeText(this, "Presiona otra", Toast.LENGTH_SHORT).show();
        return false;
    }

    public int[] piezasAdyacentes(int fila, int columna){
        int[] piezas = new int[8];
        if (fila == 7  && columna > 0 && columna < 7) {
            piezas[0] = matrizJuego[fila - 1][columna];
            piezas[2] = matrizJuego[fila][columna - 1];
            piezas[3] = matrizJuego[fila][columna + 1];
            piezas[4] = matrizJuego[fila - 1][columna - 1];
            piezas[5] = matrizJuego[fila - 1][columna + 1];
        } else if (fila == 0 & columna == 0) {
            piezas[1] = matrizJuego[fila + 1][columna];
            piezas[3] = matrizJuego[fila][columna + 1];
            piezas[7] = matrizJuego[fila + 1][columna + 1];
        } else if (fila == 0 && columna == 7) {
            piezas[1] = matrizJuego[fila + 1][columna];
            piezas[2] = matrizJuego[fila][columna - 1];
            piezas[6] = matrizJuego[fila + 1][columna - 1];
        }else if (fila == 7 && columna == 0){
            piezas[0] = matrizJuego[fila - 1][columna];
            piezas[3] = matrizJuego[fila][columna + 1];
            piezas[5] = matrizJuego[fila - 1][columna + 1];
        } else if (fila == 7 & columna == 7) {
            piezas[0] = matrizJuego[fila - 1][columna];
            piezas[2] = matrizJuego[fila][columna - 1];
            piezas[4] = matrizJuego[fila - 1][columna - 1];
        }else if (fila > 1 && fila < 7 && columna > 1 && columna < 7){
            piezas[0] = matrizJuego[fila - 1][columna];
            piezas[1] = matrizJuego[fila + 1][columna];
            piezas[2] = matrizJuego[fila][columna - 1];
            piezas[3] = matrizJuego[fila][columna + 1];
            piezas[4] = matrizJuego[fila - 1][columna - 1];
            piezas[5] = matrizJuego[fila - 1][columna + 1];
            piezas[6] = matrizJuego[fila + 1][columna - 1];
            piezas[7] = matrizJuego[fila + 1][columna + 1];
        }else if (fila == 0  && columna > 0 && columna < 7){
            piezas[1] = matrizJuego[fila + 1][columna];
            piezas[2] = matrizJuego[fila][columna - 1];
            piezas[3] = matrizJuego[fila][columna + 1];
            piezas[6] = matrizJuego[fila + 1][columna - 1];
            piezas[7] = matrizJuego[fila + 1][columna + 1];
        }
        return piezas;
    }

    public ArrayList<int[]> voltearPiezas(int fila, int columna){
        int jugador = player1 ? 1 : 2;
        ArrayList<int[]> indicesPieza = new ArrayList<>();
        // Check vertically
        for (int i = -1; i <= 1; i+=2) {
            int row = fila + i;
            if (row >= 0 && row < matrizJuego.length && columna >= 0 && columna < matrizJuego[0].length){
                if (jugador != matrizJuego[row][columna] && matrizJuego[row][columna] != 0){
                    indicesPieza.addAll(iterador(fila, columna, i, 0));
                }
            }
        }
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                int row = fila + j;
                int col = columna + i;
                if (row >= 0 && row < matrizJuego.length && col >= 0 && col < matrizJuego[0].length){
                    if (jugador != matrizJuego[row][col] && matrizJuego[row][col] != 0){
                        indicesPieza.addAll(iterador(fila, columna, j, i));
                    }
                }
            }
        }
        for (int i = -1; i <= 1; i+=2) {
            int col = columna + i;
            if (fila >= 0 && fila < matrizJuego.length && col >= 0 && col < matrizJuego[0].length){
                if (jugador != matrizJuego[fila][col] && matrizJuego[fila][col] != 0){
                    indicesPieza.addAll(iterador(fila, columna, 0, i));
                }
            }
        }
        return indicesPieza;
    }

    public void actualizar(int fila, int columna,boolean valida){
        ImageView piezaActual =matrizCasillas[fila][columna];
        String jugador = player1 ? nombre1 : nombre2;
        int value = player1 ? 1: 2;
        if (valida){
            piezaActual.setImageResource(piezas(jugador));
            matrizJuego[fila][columna] = value;
        }else{
            matrizJuego[fila][columna] = 0;
        }
    }

    public ArrayList<int[]> iterador(int fila,int columna,int desplazamientoX,int desplazamientoY){
        ArrayList<int[]> indices = new ArrayList<>();
        int jugador = player1 ? 1 : 2;
        int j = fila + desplazamientoX;
        int i = columna + desplazamientoY;
        while (i >= 0 && j >= 0 && i < 8 && j < 8) {
            if (matrizJuego[j][i] != jugador && matrizJuego[j][i] != 0) {
                // Keep iterating in the same direction until a piece of the same player is reached
                boolean mismoJugador = false;
                int k = j + desplazamientoX;
                int l = i + desplazamientoY;
                while (k >= 0 && l >= 0 && k < 8 && l < 8) {
                    if (matrizJuego[k][l] == jugador && matrizJuego[k][l] != 0) {
                        mismoJugador = true;
                        break;
                    }
                    k += desplazamientoX;
                    l += desplazamientoY;
                }
                if (mismoJugador) {
                    int[] index = new int[]{j, i};
                    indices.add(index);
                }
            }
            i += desplazamientoY;
            j += desplazamientoX;
        }
        return indices;
    }

    public boolean hayMovimientosValidos() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrizJuego[i][j] == 0) {
                    boolean piezaValida = checarPieza(i, j);
                    if (piezaValida) {
                        if (voltearPiezas(i, j).size() > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}