package com.app.aalap.notes.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.app.aalap.notes.DBUtils.NoteDBHelper;
import com.app.aalap.notes.R;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class NewNoteActivity extends AppCompatActivity {

    private static final String TAG = NewNoteActivity.class.getSimpleName();
    EditText noteTitle;
    EditText noteDetail;
    String title, note, time;
    Calendar calendar;

    NoteDBHelper noteDBHelper;
    SQLiteDatabase sqLiteDatabase;
    int ID;
    public static boolean updateList;
    Typeface font;
    public static long addedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        getSupportActionBar().setTitle(getIntent().hasExtra("title")? getIntent().getStringExtra("title"):"New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteDBHelper = new NoteDBHelper(NewNoteActivity.this);
        sqLiteDatabase = noteDBHelper.getWritableDatabase();

        noteTitle = (EditText) findViewById(R.id.txtname);
        noteDetail = (EditText) findViewById(R.id.note_content);
        noteTitle.setTypeface(Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf"));
        noteDetail.setTypeface(Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf"));
        calendar = Calendar.getInstance();

        addedTime = calendar.getTimeInMillis();

        time = calendar.getTime().toString();
        ID = getIntent().getIntExtra("ID", 0);

        font = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");

        if (getIntent().hasExtra("title")){
            noteTitle.setText(getIntent().getStringExtra("title"));
            noteDetail.setText(getIntent().getStringExtra("details"));
        }
    }

    public void saveNote(){

        if(noteDetail.getText()!=null)
            note = noteDetail.getText().toString();

        else
            note = "";

        if(ID==0) {
            noteDBHelper.addNote(title, note, calendar.getTimeInMillis()+"", sqLiteDatabase);
            noteDBHelper.close();
            Toasty.success(this, "Note " + title + " saved").show();
            finish();
        }
        else{
            noteDBHelper.updateNote(ID, title, note, calendar.getTimeInMillis()+"", sqLiteDatabase);
            Toasty.success(this, "Note "+title + " Updated").show();
            finish();
        }

        updateList = true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.save_note:
                saveORupdate();
                hideKeyboard();
                return true;
            case R.id.delete_from_menu:
                deleteNote();
                hideKeyboard();
                return true;
            case R.id.share:
                shareUsingIntent();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveORupdate() {
        title = noteTitle.getText().toString();
        if (title.isEmpty())
            Toasty.error(this, "Please add note title").show();
        else {
            saveNote();
        }
    }

    private void shareUsingIntent() {
        String body = noteDetail.getText().toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(body);
        stringBuffer.append("\n\n\n");
        stringBuffer.append("Sent from Notemaker app");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, noteTitle.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, stringBuffer.toString());
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void deleteNote() {
        final AlertDialog confirmDelete = new AlertDialog.Builder(NewNoteActivity.this).create();
        View confirmDeleteView = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);
        View yesDelete =  confirmDeleteView.findViewById(R.id.yes_delete);
        View noDelete =  confirmDeleteView.findViewById(R.id.no_delete);

        yesDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDBHelper.delete(sqLiteDatabase, ID, NewNoteActivity.this);
                confirmDelete.cancel();
                startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
                finish();
            }
        });
        noDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete.cancel();
            }
        });
        confirmDelete.setView(confirmDeleteView);
        confirmDelete.show();
    }

    void hideKeyboard(){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    void showSnackBar(String message, int color){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.scrollView1), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(color));
        TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
        tv.setTypeface(font);
        tv.setTextSize(18);
        tv.setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
    }
}
