package hu.unisopron.inf.todo_try;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private static final String TAG = "TodoAdapter";
    List<Todo> list;
    Context context;
    SharedPreferences sp;
    public TodoAdapter(Context context,SharedPreferences sp){
        this.sp=sp;
        this.context=context;
        list=getListFromPreferences();
        if (list==null){
            list=new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        Log.d(TAG, "onCreateViewHolder: "+i);
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);
        ViewHolder v=new ViewHolder(view);

        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: "+i);
        Todo todo=list.get(i);
        viewHolder.itemView.setBackgroundResource(todo.priority==priority.Alacsony?R.drawable.row_shape_green:todo.priority==priority.Közepes?R.drawable.row_shape_orange:R.drawable.row_shape_red);
        viewHolder.typeView.setImageResource(R.drawable.default_icon);
        viewHolder.nameView.setText(todo.name);
        viewHolder.dateView.setText(new SimpleDateFormat("yyyy.MM.dd hh:mm").format(todo.date));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu=new PopupMenu(v.getContext(),v);
                menu.getMenuInflater().inflate(R.menu.dropdown,menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        deleteTodo(i);
                        return false;
                    }
                });
                menu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView,dateView;
        ImageView typeView;
        LinearLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView=itemView.findViewById(R.id.name);
            typeView=itemView.findViewById(R.id.type);
            dateView=itemView.findViewById(R.id.date);
            this.parentLayout=itemView.findViewById(R.id.parent);
        }
    }
    private void deleteTodo(int i){
        list.remove(i);
        notifyDataSetChanged();
    }
    public void addTodo(Todo todo){
        list.add(todo);
        Collections.sort(list);
        notifyDataSetChanged();
    }

    private List<Todo> getListFromPreferences() {
        Gson gson=new Gson();
        String listString=sp.getString("todo_list","");
        Type type=new TypeToken<ArrayList<Todo>>(){}.getType();
        return gson.fromJson(listString,type);

    }
    public void saveList(){
        Gson gson=new Gson();
        SharedPreferences.Editor editor=sp.edit();
        String listString=gson.toJson(list);
        editor.putString("todo_list",listString);
        editor.apply();
    }

}
