package com.app.aalap.notes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.aalap.notes.Fragments.ReminderListFragment;
import com.app.aalap.notes.R;
import com.app.aalap.notes.Utils.Reminder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;

import static com.app.aalap.notes.Utils.AppUtils.cancelAlarm;
import static com.app.aalap.notes.Utils.AppUtils.setUpAlarm;

public class NewReminderActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = NewReminderActivity.class.getSimpleName() + ":";
    EditText title, reminderDetails, reminderTime;
    Button dateButton, timeButton;
    Realm realm;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    StringBuilder stringBuilder;
    Spinner spinner;
    Button button;
    String titleS;
    boolean isUpdating;
    long id = 0;
    public static final String SPINNER_MINUTES = "Minutes";
    public static final String SPINNER_DAYS = "Days";
    public static final String SPINNER_HOURS = "Hours";

    long ONEDAY_MILIS = (1440 * 60 * 1000);
    long ONEHOUR_MILIS = (60 * 60 * 1000);
    long TENMINS_MILIS = (60 * 1000);
    InputFilter[] filters = new InputFilter[1];


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();

        if (extras != null) {
            id = extras.getLong("id");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Reminder reminder = realm.where(Reminder.class).equalTo("id", id).findFirst();
                    setReminderData(reminder);
                    isUpdating = true;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        realm = Realm.getDefaultInstance();

        title = findViewById(R.id.reminder_title);
        reminderDetails = findViewById(R.id.reminder_details);
        reminderTime = findViewById(R.id.reminder_time);
        dateButton = findViewById(R.id.end_date);
        timeButton = findViewById(R.id.notif_date);
        button = findViewById(R.id.add_reminder);

        spinner = findViewById(R.id.spinner);
        Calendar calendar = Calendar.getInstance();
        dateButton.setText(calendar.get(Calendar.YEAR) + ":" + getDoubleDigit(calendar.get(Calendar.MONTH) + 1)
                + ":" + getDoubleDigit(calendar.get(Calendar.DAY_OF_MONTH)));
        timeButton.setText(getDoubleDigit(calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + getDoubleDigit(calendar.get(Calendar.MINUTE)));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        setDatePickers();

        id = getIntent().getLongExtra("id", 0);

        if (id != 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Reminder reminder = realm.where(Reminder.class).equalTo("id", id).findFirst();
                    setReminderData(reminder);
                    isUpdating = true;
                    button.setText("UPDATE");
                }
            });
        }
    }

    private void setReminderData(Reminder reminder) {
        title.setText(reminder.getTitle());
        reminderDetails.setText(reminder.getDetails());
        Calendar c = Calendar.getInstance();
        c.setTime(reminder.getEndTime());
        dateButton.setText(c.get(Calendar.YEAR) + ":" + getDoubleDigit(c.get(Calendar.MONTH) + 1) + ":" + getDoubleDigit(c.get(Calendar.DAY_OF_MONTH)));
        timeButton.setText(getDoubleDigit(c.get(Calendar.HOUR_OF_DAY)) + ":" + getDoubleDigit(c.get(Calendar.MINUTE)));

        long difference = (reminder.getEndTime().getTime() - reminder.getReminderTime().getTime());

        if (difference / TENMINS_MILIS < 60) {
            spinner.setSelection(0);
            reminderTime.setText((difference / TENMINS_MILIS) + "");
        } else if (difference / ONEHOUR_MILIS < 24) {
            spinner.setSelection(1);
            reminderTime.setText((difference / ONEHOUR_MILIS) + "");
        } else {
            spinner.setSelection(2);
            reminderTime.setText((difference / ONEDAY_MILIS) + "");
        }


    }

    String getDoubleDigit(int num) {
        return num < 10 ? "0" + num : num + "";
    }

    void setDatePickers() {
        Calendar now = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(
                NewReminderActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setAccentColor(ContextCompat.getColor(NewReminderActivity.this, R.color.update_color));

        datePickerDialog = DatePickerDialog.newInstance(
                NewReminderActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setAccentColor(ContextCompat.getColor(NewReminderActivity.this, R.color.update_color));
        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        stringBuilder = new StringBuilder();
        String hour = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String min = (minute) < 10 ? "0" + (minute) : (minute) + "";
        stringBuilder.append(hour + ":" + min);
        timeButton.setText(stringBuilder.toString());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        stringBuilder = new StringBuilder();
        String yr = year < 10 ? "0" + year : "" + year;
        String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
        String day = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
        stringBuilder.append(yr + ":" + month + ":" + day);
        dateButton.setText(stringBuilder.toString());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.end_date:
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.notif_date:
                timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
                break;
        }
    }

    public void addReminder(View view) {

        if (validateInputs()) {
            realm.beginTransaction();

            Date reminderTimeVal = getReminderTime();
            Date enteredDate = getEnteredDate();

            if (reminderTimeVal != null) {

                if (isUpdating) {
                    Reminder first = realm.where(Reminder.class).equalTo("id", id).findFirst();
                    first.setTitle(title.getText().toString());
                    first.setDetails(reminderDetails.getText().toString());
                    first.setEndTime(enteredDate);
                    first.setReminderTime(reminderTimeVal);


                    cancelAlarm(this, (int) first.getId());
                    setUpAlarm(this, reminderTimeVal.getTime(), (int) first.getId());
                } else {
                    String reminderTimeText = reminderTime.getText().toString() + " " + spinner.getSelectedItem().toString();
                    Log.d(TAG, "addReminder: " + reminderTimeText);

                    Reminder reminder = new Reminder
                            (titleS, reminderDetails.getText().toString(), enteredDate, reminderTimeVal, System.currentTimeMillis());
                    realm.copyToRealm(reminder);
                    setUpAlarm(this, reminderTimeVal.getTime(), (int) enteredDate.getTime());
                }

                realm.commitTransaction();
                ReminderListFragment.itemAdded = true;

                Toasty.success(this, "Saved successfully").show();
                finish();

            } else
                Toasty.error(this, "Please enter valid reminder time").show();

        }
    }

    Date getReminderTime() {

        String reminderText = spinner.getSelectedItem().toString();
        long endTime = 0;

        try {
            int timeDigit = Integer.parseInt(reminderTime.getText().toString());
            switch (reminderText) {

                case "Minutes":
                    endTime = getEnteredDate().getTime() - (timeDigit * TENMINS_MILIS);
                    break;
                case "Hours":
                    endTime = getEnteredDate().getTime() - (timeDigit * ONEHOUR_MILIS);
                    break;
                case "Days":
                    endTime = getEnteredDate().getTime() - (timeDigit * ONEDAY_MILIS);
                    break;

            }
            return new Date(endTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }


    }

    private boolean validateInputs() {
        boolean isValidated = true;
        titleS = title.getText().toString();
        String timeDigit = reminderTime.getText().toString();

        if (titleS == null || titleS.isEmpty()) {
            isValidated = false;
            Toasty.error(this, "Please add title").show();
        }
        if (dateButton.getText().toString().isEmpty() || timeButton.getText().toString().isEmpty()) {
            isValidated = false;
            Toasty.error(this, "Please add date").show();
        }

        if (getReminderTime() != null && (getEnteredDate().getTime() < System.currentTimeMillis() || getReminderTime().getTime() < System.currentTimeMillis())) {
            isValidated = false;
            Toasty.error(this, "Invalid date").show();
        }

        if (timeDigit.isEmpty()) {
            isValidated = false;
            Toasty.error(this, "Enter valid time digit").show();
        }

        if (!timeDigit.isEmpty()) {

            String spinnerValue = spinner.getSelectedItem().toString();
            int digit = Integer.parseInt(timeDigit);

            if (spinnerValue.equalsIgnoreCase(SPINNER_MINUTES) && digit >= 60) {
                isValidated = false;
                Toasty.error(this, "Enter minutes between 1 to 59").show();
            } else if (spinnerValue.equalsIgnoreCase(SPINNER_HOURS) && digit >= 24) {
                isValidated = false;
                Toasty.error(this, "Enter minutes between 1 to 23").show();
            }
        }

        return isValidated;
    }

    Date getEnteredDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd hh:mm");
        try {
            Date date = df.parse(dateButton.getText().toString() + " " + timeButton.getText().toString());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
