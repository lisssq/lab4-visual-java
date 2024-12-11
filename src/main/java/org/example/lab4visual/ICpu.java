package org.example.lab4visual;

// интерфейс - способ задать набор обязательных методов, которые должны..
// ..реализовываться в классе, если класс использует данный интерфейс


// любой класс, который implements (реализует) этот интерфейс, будет обязан реализовать метод execute(Commands c)
public interface ICpu
{   //выполняет определенную переданную ему команду
    void doCommand(Commands c);

    void addObserver(ICpuObserver observer); // Добавляем метод в интерфейс

    int getRegisterValue(String registerName); // Возвращает значение регистра по имени
}
