package com.example.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TableLayout table;
    static final String Player_1_symbol = "X";
    static final String Player_2_symbol = "O";
    static final String empty_symbol = "";
    static int[][] board = new int[3][3];
    static final int empty_value = 0;
    static final int Player_1_value = 1;
    static final int Player_2_value = 2;
    boolean Player_1_Turn = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        table = findViewById(R.id.table);
        for (int i=0;i<table.getChildCount();i++){
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j=0;j<row.getChildCount();j++){
                Button btn = (Button) row.getChildAt(j);
                btn.setOnClickListener(new CellListener(i,j));
            }
        }

        if (savedInstanceState != null){
            Player_1_Turn = savedInstanceState.getBoolean("turn");
            int[] arr = savedInstanceState.getIntArray("board");
            for (int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    board[i][j] = arr[i * 3 + j];
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++){
                list.add(board[i][j]);
            }
        }

        int[] board2 = new int[list.size()];
        for (int i=0;i<board2.length;i++){
            board2[i] = list.get(i);
        }

        outState.putIntArray("board",board2);
        outState.putBoolean("turn", Player_1_Turn);
    }

    class CellListener implements View.OnClickListener{

        int row,col;

        public CellListener(int row,int col){
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (board[row][col] != empty_value){
                Toast.makeText(MainActivity.this, "Cell is alredy occupied!", Toast.LENGTH_LONG).show();
                return;
            }

            int playerValue = empty_value;
            if (Player_1_Turn){
                ((Button)v).setText(Player_1_symbol);
                board[row][col] = Player_1_value;
                Player_1_Turn = false;
                playerValue = Player_1_value;
            }else{
                ((Button)v).setText(Player_2_symbol);
                board[row][col] = Player_2_value;
                Player_1_Turn = true;
                playerValue = Player_2_value;
            }
            int gameState = gameEnded(row,col,playerValue);
            if (gameState >0){
                Toast.makeText(MainActivity.this, "Player " + gameState + " has won!", Toast.LENGTH_LONG).show();
                setBoardEnable(false);
            }
        }
    }

    public int gameEnded(int row,int col,int playerValue){
        boolean win = true;

        //vertical
        for (int r=0;r< board.length;r++){
            if (board[r][col] != playerValue){
                win = false;
                break;
            }
        }

        //horizontal
        if (!win){
            for (int c=0;c<board.length;c++){
                if (board[row][c] != playerValue){
                    win = false;
                    break;
                }
                win = true;
            }
        }

        //diagonal
        if (!win){
            for (int r=0,c=0;r<board.length;r++,c++){
                if (board[r][c] != playerValue){
                    win = false;
                    break;
                }
                win = true;
            }
        }
        if (!win){
            for (int r=0,c=2;r<board.length;r++,c--){
                if (board[r][c] != playerValue){
                    win = false;
                    break;
                }
                win = true;
            }
        }

        if (win){
            return playerValue;
        }
        return -1;
    }

    public void setBoardEnable(boolean enable){

        TableLayout table = findViewById(R.id.table);
        for (int i=0;i<table.getChildCount();i++){
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j=0;j<row.getChildCount();j++){
                Button btn = (Button) row.getChildAt(j);
                btn.setEnabled(enable);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Player_1_Turn = true;
        if (item.getItemId() == R.id.new_game){
            setBoardEnable(true);
            for (int i=0;i<board.length;i++){
                for (int j=0;j<board.length;j++){
                    board[i][j] = empty_value;
                }
            }
            TableLayout table = findViewById(R.id.table);
            for (int i=0;i<table.getChildCount();i++){
                TableRow row = (TableRow) table.getChildAt(i);
                for (int j=0;j<row.getChildCount();j++){
                    Button btn = (Button) row.getChildAt(j);
                    btn.setText(empty_symbol);
                }
            }
        }else if(item.getItemId() == R.id.save_game){
            SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            long b = 0;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    b += (long) (board[i][j] * Math.pow(10, i * 3 + j));
                }
            }

            editor.putLong("board", b);
            System.out.println("deneme"+Player_1_Turn);
            editor.putBoolean("turn", Player_1_Turn);
            editor.commit();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

        }else if (item.getItemId() == R.id.load_game){
            SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
            Player_1_Turn = preferences.getBoolean("turn",true);
            System.out.println("deneme"+Player_1_Turn);
            long b = preferences.getLong("board", 0);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = (byte) ((b / Math.pow(10, i * 3 + j)) % 10);
                }
            }

            //update the button labels
            TableLayout table = findViewById(R.id.table);
            for (int i = 0; i < 3; i++) {
                TableRow row = (TableRow) table.getChildAt(i);
                for (int j = 0; j < 3; j++) {
                    Button btn = (Button) row.getChildAt(j);
                    switch (board[i][j]) {
                        case 0: btn.setText("");
                            break;
                        case 1: btn.setText(Player_1_symbol);
                            break;
                        case 2: btn.setText(Player_2_symbol);
                            break;
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
