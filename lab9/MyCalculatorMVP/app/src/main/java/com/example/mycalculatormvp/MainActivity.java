package com.example.mycalculatormvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity implements CalculatorView{

    EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Presenter presenter = new Presenter(this);
        txt = findViewById(R.id.txtNumber);

        TableLayout table = findViewById(R.id.numPad);
        for (int i=0; i<table.getChildCount();i++){
            TableRow row =(TableRow) table.getChildAt(i);
            for (int j=0;j<row.getChildCount();j++){
                Button btn = (Button) row.getChildAt(j);
                btn.setOnClickListener(presenter);
            }
        }
    }

    @Override
    public void setNumber(String result){
        txt.setText(result);
    }

    @Override
    public String getNumber(){
        return txt.getText().toString();
    }
}
