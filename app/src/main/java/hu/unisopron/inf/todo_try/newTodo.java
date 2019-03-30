package hu.unisopron.inf.todo_try;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class newTodo extends AppCompatActivity {
    int year=0,month=0,day=0,hour=0,minute=0;
    EditText name,date,time;
    Spinner priority;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    Calendar c=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_todo);
        this.setFinishOnTouchOutside(true);
        name=findViewById(R.id.name);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        setTimeVariables();
        date.setText(new SimpleDateFormat("yyyy.MM.dd").format(c.getTime()));
        time.setText(new SimpleDateFormat("hh:mm").format(c.getTime()));
        priority=findViewById(R.id.priority);
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int iyear, int imonth, int idayOfMonth) {
                year=iyear;
                month=imonth;
                day=idayOfMonth;
                c.set(year,month,day,hour,minute);
                date.setText(new SimpleDateFormat("yyyy.MM.dd").format(c.getTime()));
            }
        };
        timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int ihourOfDay, int iminute) {
                hour=ihourOfDay;
                minute=iminute;
                c.set(year,month,day,hour,minute);
                time.setText(new SimpleDateFormat("hh:mm").format(c.getTime()));
            }
        };

        priority.setAdapter(new ArrayAdapter<priority>(this,R.layout.support_simple_spinner_dropdown_item, hu.unisopron.inf.todo_try.priority.values()));
    }
    public void sendIt(View view){

        Intent returnIntent=new Intent();
        Todo todoToSend=new Todo(name.getText().toString(),c.getTime(),(hu.unisopron.inf.todo_try.priority)priority.getSelectedItem(),type.DEFAULT);
        returnIntent.putExtra("todoJson",new Gson().toJson(todoToSend));
        setResult(RESULT_OK,returnIntent);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Szeretné létrehozni az eseményt a naptárban?")
                .setMessage("Az 'igen' gombra kattintva lehetősége van menteni a Todo-t a naptárba")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        putInCalendar();
                        finish();
                    }
                })
                .setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }
    private void setTimeVariables(){
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);
        hour=c.get(Calendar.HOUR);
        minute=c.get(Calendar.MINUTE);
    }
    public void showDatePicker(View view){
        DatePickerDialog dialog=new DatePickerDialog(this,R.style.Theme_AppCompat_Light_Dialog_MinWidth,dateSetListener,year,month,day);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
    private void putInCalendar(){
    Intent calIntent = new Intent(Intent.ACTION_INSERT);
    calIntent.setType("vnd.android.cursor.item/event");
    calIntent.putExtra(CalendarContract.Events.TITLE, name.getText().toString()+" - "+priority.getSelectedItem()+" prioritású.");
    calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
    calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Egy Todo a todoAPP-ból.");
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            c.getTimeInMillis());
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
            c.getTimeInMillis());
    startActivity(calIntent);
}

    public void showTimePicker(View view) {
        TimePickerDialog dialog=new TimePickerDialog(this,R.style.Theme_AppCompat_Light_Dialog_MinWidth,timeSetListener,hour,minute,true);
        dialog.show();
    }
}
