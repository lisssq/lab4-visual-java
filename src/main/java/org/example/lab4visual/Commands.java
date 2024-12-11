package org.example.lab4visual;


public class Commands
{
    private Tasks t;
    int index_memory;
    int figure; //число которое мы передаем
    String register, register_dop;
    String name;
    String [] args;



    public String getName() {
        return t.name(); // Метод name() возвращает строковое представление из enum
    }

    public String[] getArgs()
    {
        return args;
    }

    //CPU c = new CPU();      //экземпляр процессора для команд

    public Tasks getTasks()
    {
        return t;
    }

    public Commands(Tasks t, int index_memory, int figure) {
        this.t = t;
        this.index_memory = index_memory;
        this.figure = figure;
        this.args = new String[]{String.valueOf(index_memory), String.valueOf(figure)};
    }

    public Commands(Tasks t, String register, int index_memory) {
        this.t = t;
        this.register = register;
        this.index_memory = index_memory;
        this.args = new String[]{register, String.valueOf(index_memory)};
    }

    public Commands(Tasks t) {
        this.t = t;
        this.args = new String[]{}; // Пустой массив, если аргументов нет
    }

    public Commands(Tasks t, String register_1, String register_2) {
        this.t = t;
        this.register = register_1;
        this.register_dop = register_2;
        this.args = new String[]{register_1, register_2};
    }


}