package hu.unisopron.inf.todo_try;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link todoWidgetConfigureActivity todoWidgetConfigureActivity}
 */
public class todoWidget extends AppWidgetProvider {
static List<Todo> todos;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = todoWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        //duplikátum
        Gson gson=new Gson();
        String listString=context.getSharedPreferences("todos",MODE_PRIVATE).getString("todo_list","");
        Type type=new TypeToken<ArrayList<Todo>>(){}.getType();
        todos=gson.fromJson(listString,type);
        String first = todos.get(0).toString();
        views.setTextViewText(R.id.widget_1_row, first);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            // code starts here
            Gson gson=new Gson();
            String listString=context.getSharedPreferences("todos",MODE_PRIVATE).getString("todo_list","");
            Type type=new TypeToken<ArrayList<Todo>>(){}.getType();
            todos=gson.fromJson(listString,type);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.todo_widget);
            if(todos.size()!=0) {
                String first = todos.get(0).toString();
                remoteViews.setTextViewText(R.id.widget_1_row, first);
            }
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            todoWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    private void config(){

    }
}

