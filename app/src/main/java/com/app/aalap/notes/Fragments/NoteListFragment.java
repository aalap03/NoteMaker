package com.app.aalap.notes.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.aalap.notes.Activities.MainActivity;
import com.app.aalap.notes.Activities.NewNoteActivity;
import com.app.aalap.notes.DBUtils.NoteDBHelper;
import com.app.aalap.notes.R;
import com.app.aalap.notes.Utils.NoteItem;

import java.util.ArrayList;

/**
 * Created by Aalap on 2017-04-26.
 */

public class NoteListFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView addNote;
    NoteDBHelper noteDBHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    NoteAdapter noteAdapter;
    NoteItem noteObject;
    ArrayList<NoteItem> noteLists;
    ImageView emptyState;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.notes_recycler);
        emptyState = (ImageView) view.findViewById(R.id.empty_state);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        addNote = (ImageView) view.findViewById(R.id.add_note);
        noteLists = new ArrayList<>();

        updateDisplay();
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if(NewNoteActivity.updateList)
            updateDisplay();

    }

    public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder>{

        ArrayList<NoteItem> noteList;

        public NoteAdapter(ArrayList<NoteItem> noteList){
            this.noteList = noteList;
        }

        @Override
        public NoteAdapter.NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
            return new NoteAdapter.NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteAdapter.NoteHolder holder, final int position) {

            String time = noteList.get(position).getTime();
            String displayTime = (String) DateUtils.getRelativeTimeSpanString(Long.parseLong(time), System.currentTimeMillis()
                    , DateUtils.SECOND_IN_MILLIS);

            final String title = noteList.get(position).getTitle();
            final String details = noteList.get(position).getDetails();
            final int ID = noteList.get(position).getId();

            holder.noteDate.setText(displayTime);
            holder.noteTitle.setText(title);
            holder.notePreview.setText(details);


            holder.itemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("details", details);
                    intent.putExtra("ID", ID);
                    startActivity(intent);
                }
            });

            holder.itemParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    View alertView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_share_dialog, null);
                    View delete =  alertView.findViewById(R.id.delete_note_icon);

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noteDBHelper.delete(sqLiteDatabase, ID, view.getContext());
                            notifyItemRemoved(position);
                            updateDisplay();
                            alertDialog.cancel();
                        }
                    });

                    alertDialog.setView(alertView);
                    alertDialog.show();
                    return false;
                }
            });
        }



        @Override
        public int getItemCount() {
            return noteList!=null ? noteList.size() : 0 ;
        }

        public class NoteHolder extends RecyclerView.ViewHolder{

            TextView noteTitle, noteDate, notePreview;
            ViewGroup itemParent;
            public NoteHolder(View itemView) {
                super(itemView);
                noteTitle = (TextView) itemView.findViewById(R.id.note_title);
                noteDate = (TextView) itemView.findViewById(R.id.note_date);
                notePreview = (TextView) itemView.findViewById(R.id.note_detail_preview);
                itemParent = (ViewGroup) itemView.findViewById(R.id.item_parent);
            }
        }
    }

    public void updateDisplay(){

        noteDBHelper = new NoteDBHelper(getActivity().getApplicationContext());
        sqLiteDatabase = noteDBHelper.getReadableDatabase();
        cursor = noteDBHelper.getInformation(sqLiteDatabase);

        if(cursor.moveToFirst()){

            if(noteLists.size()>0){
                noteLists.clear();
            }
            do{
                noteObject = new NoteItem() ;
                noteObject.setId(cursor.getInt(0));
                noteObject.setTitle(cursor.getString(1));
                noteObject.setDetails(cursor.getString(2));
                noteObject.setTime(cursor.getString(3));
                noteLists.add(noteObject);
            }while(cursor.moveToNext());
        }
        else {
            noteLists.clear();
            noteAdapter = new NoteAdapter(noteLists);
            recyclerView.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
            //getActivity().getSupportActionBar().setTitle("Notes");
        }
        if(noteLists.size()>0) {
            noteAdapter = new NoteAdapter(noteLists);
            recyclerView.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
            //getSupportActionBar().setTitle("Notes(" + noteLists.size() + ")");
        }


        emptyState.setVisibility(noteLists.size()>0? View.GONE:View.VISIBLE);
    }
}
