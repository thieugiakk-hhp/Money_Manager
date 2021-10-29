package com.unknown.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddAndEditActivity extends AppCompatActivity {

    private static final int MODE_CREATE = 0;
    private static final int MODE_EDIT = 1;

    private EditText editTextTitle;
    private EditText editTextMoney;
    private EditText editTextContent;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonSpend;
    private Button btnSave;
    private Button btnCancel;

    private Note note;
    private boolean needRefresh;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextMoney = findViewById(R.id.editTextMoney);
        editTextContent = findViewById(R.id.editTextContent);

        radioButtonIncome = findViewById(R.id.radioButtonIncome);
        radioButtonSpend = findViewById(R.id.radioButtonSpend);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(saveOnClickListener);
        btnCancel.setOnClickListener(cancelOnClickListener);

        note = (Note) getIntent().getSerializableExtra("note");

        if(note == null)  {
            mode = MODE_CREATE;
        } else  {
            mode = MODE_EDIT;
            editTextTitle.setText(note.getNoteTitle());
            editTextMoney.setText(String.valueOf(note.getMoney()));
            editTextContent.setText(note.getNoteContent());
            if (note.getIncomeOrSpend() == 0) {
                radioButtonIncome.setChecked(true);
                radioButtonSpend.setChecked(false);
            } else {
                radioButtonIncome.setChecked(false);
                radioButtonSpend.setChecked(true);
            }
        }
    }

    private View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyDatabaseHelper db = new MyDatabaseHelper(AddAndEditActivity.this);

            int id = db.getNotesCount() + 1;
            String title = editTextTitle.getText().toString();
            String money = editTextMoney.getText().toString();
            String content = editTextContent.getText().toString();
            int ios;
            if (radioButtonIncome.isChecked() == true) {
                ios = 0;
            } else {
                ios = 1;
            }

            if(title.equals("") || content.equals("") || money.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Please enter title & content & money", Toast.LENGTH_LONG).show();
                return;
            }

            if(mode == MODE_CREATE ) {
                note = new Note(id, title, Integer.parseInt(money), content, ios);
                Log.e("Note", " " + id + " " + title + " " + money + " " + content + " " + ios);
                db.addNote(note);
            } else  {
                note.setNoteTitle(title);
                note.setMoney(Integer.parseInt(money));
                note.setNoteContent(content);
                note.setIncomeOrSpend(ios);
                db.updateNote(note);
            }

            needRefresh = true;

            // Back to MainActivity.
            onBackPressed();
        }
    };

    private View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
}