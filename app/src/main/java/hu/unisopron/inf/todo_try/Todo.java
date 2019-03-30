package hu.unisopron.inf.todo_try;

import java.text.SimpleDateFormat;
import java.util.Date;

enum priority {Alacsony,Közepes,Magas}
enum type {DEFAULT,PRESENTATION,EXAM} // future releases
public class Todo implements Comparable<Todo> {
String name;
priority priority;
Date date;
type type;

    public Todo(String name, Date date, priority priority, type type){
        this.name=name;
        this.date=date;
        this.priority=priority;
        this.type=type;
    }

    public hu.unisopron.inf.todo_try.type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public hu.unisopron.inf.todo_try.priority getPriority() {
        return priority;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public int compareTo(Todo o) {
        return (this.date).compareTo(o.date);
    }
    @Override
    public String toString() {
        return (new SimpleDateFormat("yyyy.MM.dd hh:mm").format(date))+" - "+name+" - "+priority;
    }
}
