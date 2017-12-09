package com.app.aalap.notes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.aalap.notes.Activities.NewReminderActivity;
import com.app.aalap.notes.R;
import com.app.aalap.notes.Utils.Reminder;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.app.aalap.notes.Fragments.ReminderListFragment.itemAdded;

/**
 * Created by Aalap on 2017-05-13.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    Context context;
    RealmResults<Reminder> reminderItem;
    Realm realm = Realm.getDefaultInstance();
    ImageView emptyView;

    public ReminderAdapter(Context context, RealmResults<Reminder> reminderItem, ImageView emptyView) {
        this.context = context;
        this.reminderItem = reminderItem;
        this.emptyView = emptyView;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReminderViewHolder holder, final int position) {
        holder.title.setText(reminderItem.get(position).getTitle());
        holder.details.setText(reminderItem.get(position).getDetails());
        holder.endDate.setText(reminderItem.get(position).getEndTime().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewReminderActivity.class);
                intent.putExtra("id", reminderItem.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                View alertView = LayoutInflater.from(context).inflate(R.layout.delete_share_dialog, null);
                View delete = alertView.findViewById(R.id.delete_note_icon);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.where(Reminder.class)
                                        .equalTo("id", reminderItem.get(position).getId())
                                        .findFirst()
                                        .deleteFromRealm();
                            }
                        });
                        alertDialog.cancel();

                        if(realm.where(Reminder.class).findAll().size()==0)
                            emptyView.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();
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
        return reminderItem.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView title, details, endDate;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.note_title);
            details = (TextView) itemView.findViewById(R.id.note_detail_preview);
            endDate = (TextView) itemView.findViewById(R.id.note_date);
        }
    }
}

