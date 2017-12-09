package com.app.aalap.notes.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.aalap.notes.Activities.NewReminderActivity;
import com.app.aalap.notes.R;
import com.app.aalap.notes.Adapter.ReminderAdapter;
import com.app.aalap.notes.Utils.Reminder;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Aalap on 2017-04-26.
 */

public class ReminderListFragment extends Fragment {


    private static final String TAG = "ReminderListFragment:";

    ImageView addNote;
    Realm realm = Realm.getDefaultInstance();
    static public boolean itemAdded = false;
    RecyclerView recyclerView;
    ImageView emptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_audio_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNote = (ImageView) view.findViewById(R.id.add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewReminderActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.audio_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        emptyView = (ImageView) view.findViewById(R.id.empty_state);

        setDisplay();
    }

    public void setDisplay() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Reminder> reminders = realm.where(Reminder.class).findAll();

                if (reminders.size() > 0) {
                    ReminderAdapter reminderAdapter = new ReminderAdapter(getActivity(), reminders, emptyView);
                    recyclerView.setAdapter(reminderAdapter);
                }
                emptyView.setVisibility(reminders.size()==0?View.VISIBLE:View.GONE);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (itemAdded) {
            setDisplay();
            itemAdded = false;
        }
    }
}
