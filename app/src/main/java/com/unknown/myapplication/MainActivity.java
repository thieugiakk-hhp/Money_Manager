package com.unknown.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private static final int MENU_ITEM_VIEW = 0;
    private static final int MENU_ITEM_EDIT = 1;
    private static final int MENU_ITEM_DELETE = 2;

    private static final int MY_REQUEST_CODE = 101;

    private final List<Note> noteList = new ArrayList<Note>();
    private ArrayAdapter<Note> listViewAdapter;

    private Button btnAdd;

    private TextView tv;

    private TextView textViewMoneyIncome;
    private TextView textViewMoneySpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);

        textViewMoneyIncome = findViewById(R.id.textViewMoneyIncome);
        textViewMoneySpend = findViewById(R.id.textViewMoneySpend);

        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(AddOnClickListener);

        listView = (ListView) findViewById(R.id.listView);

        MyDatabaseHelper db = new MyDatabaseHelper(this);

        if (db.getNotesCount() == 0) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }

        List<Note> list =  db.getAllNotes();
        noteList.addAll(list);

        listViewAdapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, android.R.id.text1, this.noteList);

        // Assign adapter to ListView
        listView.setAdapter(this.listViewAdapter);

        // Register the ListView for Context menu
        registerForContextMenu(this.listView);

        textViewMoneyIncome.setText(String.valueOf(totalMoneyIncome()));
        textViewMoneySpend.setText(String.valueOf(totalMoneySpend()));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        refreshLayout();
    }

    public void refreshLayout(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "View Note");
        menu.add(0, MENU_ITEM_EDIT , 1, "Edit Note");
        menu.add(0, MENU_ITEM_DELETE, 2, "Delete Note");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Note selectedNote = (Note) listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_VIEW){
            Intent intent = new Intent(this, ViewActivity.class);
            intent.putExtra("note", selectedNote);

            // Start AddEditNoteActivity, (with feedback).
            startActivity(intent);
        }
        else if(item.getItemId() == MENU_ITEM_EDIT ){
            Intent intent = new Intent(this, AddAndEditActivity.class);
            intent.putExtra("note", selectedNote);

            // Start AddEditNoteActivity, (with feedback).
            startActivity(intent);
        }
        else if(item.getItemId() == MENU_ITEM_DELETE){
            // Ask before deleting.
            new AlertDialog.Builder(this)
                    .setMessage(selectedNote.getNoteTitle()+". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteNote(selectedNote);
                            refreshLayout();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            return false;
        }
        return true;
    }

    private View.OnClickListener AddOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);

            // Start AddEditNoteActivity, (with feedback).
            startActivity(intent);
        }
    };

    private View.OnClickListener SumOnCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    // Delete a record
    private void deleteNote(Note note)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.deleteNote(note);
        this.noteList.remove(note);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }

    // When AddEditNoteActivity completed, it sends feedback.
    // (If you start it using startActivityForResult ())
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.noteList.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this);
                List<Note> list = db.getAllNotes();
                this.noteList.addAll(list);


                // Notify the data change (To refresh the ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshLayout();
    }

    private int totalMoneyIncome() {
        int total = 0;

        for (Note item : noteList) {
            if (item.getIncomeOrSpend() == 0) {
                total += item.getMoney();
            }
        }

        return total;
    }

    private int totalMoneySpend() {
        int total = 0;

        for (Note item : noteList) {
            if (item.getIncomeOrSpend() == 1) {
                total -= item.getMoney();
            }
        }

        return total;
    }
}