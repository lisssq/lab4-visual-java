package org.example.lab4visual;

import java.util.HashMap;
import java.util.*;
import java.util.function.*;





public class CPU implements ICpu
{
    int r1;
    int r2;
    int r3;
    int r4;

    private final Map<String, Integer> registers = new HashMap<>();


    private List<ICpuObserver> observers = new ArrayList<>();


    Memory mem = new Memory();      //экземпляр памяти


    // Методы для добавления/удаления наблюдателей
    public void addObserver(ICpuObserver observer)
    {
        observers.add(observer);
    }

//    public void removeObserver(ICpuObserver observer)
//    {
//        observers.remove(observer);
//    }

    // Метод для уведомления наблюдателей об изменении
    private void notifyObservers()
    {
        System.out.println("Notify observers called"); // Для отладки
        for (ICpuObserver observer : observers)
        {
            observer.onCpuStateChanged();
        }
    }







    // методы для получения и установки значений регистров
//    public int GetR1() {return r1;}
//    public void SetR1(int r1) {this.r1=r1;}
//
//    public int GetR2() {return r2;}
//    public void SetR2(int r2) {this.r2=r2;}
//
//    public int GetR3() {return r3;}
//    public void SetR3(int r3) {this.r3=r3;}
//
//    public int GetR4() {return r4;}
//    public void SetR4(int r4) {this.r4=r4;}


    // Ваши методы работы с регистрами
     void SetR1(int value) { r1 = value; }
     void SetR2(int value) { r2 = value; }
     void SetR3(int value) { r3 = value; }
     void SetR4(int value) { r4 = value; }

     int GetR1() { return r1; }
     int GetR2() { return r2; }
     int GetR3() { return r3; }
     int GetR4() { return r4; }




   @Override
    // получаем значение регистра или же возвращаем 0
    public int getRegisterValue(String register)
    {
        return registers.getOrDefault(register, 0);
    }


    @Override
    public void doCommand(Commands c)
    {
        Map<String, Consumer<Integer>> registerSetMap = new HashMap<>();
        registerSetMap.put("r1", this::SetR1);       // хранит функции для записи значений в регистры
        registerSetMap.put("r2", this::SetR2);
        registerSetMap.put("r3", this::SetR3);
        registerSetMap.put("r4", this::SetR4);

        Map<String, Supplier<Integer>> registerGetMap = new HashMap<>();
        registerGetMap.put("r1", this::GetR1);       // хранит функции для получения значений из регистров
        registerGetMap.put("r2", this::GetR2);
        registerGetMap.put("r3", this::GetR3);
        registerGetMap.put("r4", this::GetR4);

// Перед выполнением команды можно добавить логи:
        System.out.println("Before Command Execution:");
        System.out.println("R1: " + GetR1());
        System.out.println("R2: " + GetR2());
        System.out.println("R3: " + GetR3());
        System.out.println("R4: " + GetR4());



        switch (c.getTasks())
        {
            case init:
                mem.memory[c.index_memory] = c.figure;    //в ячейку памяти №** загрузится какое-то число/значение
                break;

            case ld:
                Consumer<Integer> registerSetter = registerSetMap.get(c.register);      //выбираем регистр
                // c.register - это строка, представляющая регистр (например, "r1").
                // Если c.register равно "r1", то registerSetter будет указывать на метод SetterR1.
                if (registerSetter != null)         // если нашли функцию для установки значения в регистр
                {
                    registerSetter.accept(mem.memory[c.index_memory]);  //загружаем значение из памяти в регистр
                }
                break;

            case print:
                System.out.println("Reg1: " + GetR1());
                System.out.println("Reg2: " + GetR2());
                System.out.println("Reg3: " + GetR3());
                System.out.println("Reg4: " + GetR4());
                break;

            case add:
                SetR4(GetR1() + GetR2());
                break;

            case sub:
                SetR4(GetR1() - GetR2());
                break;

            case mul:
                SetR4(GetR1() * GetR2());
                break;

            case div:
                SetR4(GetR1() / GetR2());
                break;

            case st:            // запись значения из регистра в память
                Supplier<Integer> registerGetter = registerGetMap.get(c.register);  //если c.register равно "r1",
                // то registerGetter будет указывать на метод GetterR1
                if (registerGetter != null)
                {
                    (mem.memory[c.index_memory]) = registerGetter.get();    // получаем значение из регистра и сохраняем его в памяти по адресу c.index_memory.
                }
                break;

            case mv:        // перемещение значения из одного регистра в другой
                Supplier<Integer> registerGetter_1 = registerGetMap.get(c.register);    //получаем значение из источника (у нас это р1)
                Consumer<Integer> registerSetter_2 = registerSetMap.get(c.register_dop);    //функция записи в регистр (?)

                if (registerGetter_1 != null && registerSetter_2 != null)     //если оба существуют
                {
                    registerSetter_2.accept(registerGetter_1.get());    //перемещаем значение из р1 в р2
                }
                break;
        }


        // Логи после выполнения команды
        System.out.println("After Command Execution:");
        System.out.println("R1: " + GetR1());
        System.out.println("R2: " + GetR2());
        System.out.println("R3: " + GetR3());
        System.out.println("R4: " + GetR4());




        // Уведомление наблюдателей после выполнения команды
        notifyObservers();
    }




}
