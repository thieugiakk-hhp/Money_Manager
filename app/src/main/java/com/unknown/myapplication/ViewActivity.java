package com.unknown.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RadioButton;

public class ViewActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewMoney;
    private TextView textViewContent;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonSpend;

    private Button btnBack;

    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        note = (Note) getIntent().getSerializableExtra("note");

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewMoney = findViewById(R.id.textViewMoney);
        textViewContent = findViewById(R.id.textViewContent);

        radioButtonIncome = findViewById(R.id.radioButtonViewIncome);
        radioButtonSpend = findViewById(R.id.radioButtonViewSpend);

        textViewTitle.setText(note.getNoteTitle());
        textViewMoney.setText(String.valueOf(note.getMoney()));
        textViewContent.setText(note.getNoteContent());
        if (note.getIncomeOrSpend() == 0) {
            radioButtonIncome.setChecked(true);
            radioButtonSpend.setChecked(false);
        } else {
            radioButtonIncome.setChecked(false);
            radioButtonSpend.setChecked(true);
        }

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}