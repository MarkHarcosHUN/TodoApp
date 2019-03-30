package hu.unisopron.inf.todo_try;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import hu.unisopron.inf.todo_try.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {
TodoAdapter adapter;
SharedPreferences sp;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("todos",MODE_PRIVATE);
        button=findViewById(R.id.newTodoButton);
        button.setTransformationMethod(null); // all caps off
        RecyclerView r=findViewById(R.id.recyclerView);
        r.setLayoutManager(new LinearLayoutManager(this));
        adapter=new TodoAdapter(this,sp);
        r.setAdapter(adapter);


    }
    public void makeNewTodo(View view){
        Intent intent=new Intent(this,newTodo.class);
       startActivityForResult(intent,42342);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==42342 && data!=null){
            Todo todo=new Gson().fromJson(data.getStringExtra("todoJson"),new TypeToken<Todo>(){}.getType());
            adapter.addTodo(todo);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.saveList();
        updateWidget();
    }
    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, todoWidget.class));
        if (appWidgetIds.length > 0) {
            new todoWidget().onUpdate(this, appWidgetManager, appWidgetIds);
        }
    }
    public void showAboutUs(View view) {
        Intent i=new Intent(this,AboutUs.class);
        startActivity(i);
    }
}
