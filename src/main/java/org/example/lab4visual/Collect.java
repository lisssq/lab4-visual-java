package org.example.lab4visual;

//package org.example;
import java.util.*;

public class Collect implements Iterable<Commands>
{
    private final List<Commands> commands = new ArrayList<>();   // создаем список команд, в который можно добавлять и удалять команды

    public void add(Commands command)
    {
        commands.add(command);
    }

    public Tasks mostPopularInstruction()
    {
        Map<Tasks, Integer> countMap = new HashMap<>();     // карта, хранящая название команды и кол-во ее использования

        for (Commands command : commands)
        {
            Tasks task = command.getTasks();                                   // проверка на наличие команды в карте countMap
            countMap.put(task, countMap.getOrDefault(task, 0) + 1); // если есть - возвращаем кол-во ее использований,..
            // ..если нет - то 0. затем увеличиваем это число на 1,..
            // ..если команда снова встретилась и пересохраняем кол-во вхождений команды в карту

        }

        // непосредственно ищем команду с максимальной частотой
        Tasks mostPopularTask = null;
        int maxCount = 0;
        for (Map.Entry<Tasks, Integer> entry : countMap.entrySet())         // перебираем все пары в countMap
        {
            if (entry.getValue() > maxCount)                    // проверка больше ли кол-во текущей команды чем макс.значение
            {
                maxCount = entry.getValue();                    // обновляем макс.значение частоты
                mostPopularTask = entry.getKey();               // назначаем команду как наиболее часто встречающуюся
            }
        }
        return mostPopularTask;
    }


    public String getMemoryAddressRange()    // получение диапазона используемых адресов памяти
    {
        int minAddress = Integer.MAX_VALUE;
        int maxAddress = Integer.MIN_VALUE;
        boolean isUsed = false;            // флаг, чтобы отслеживать, использовались ли адреса

        for (Commands command : commands)
        {
            if (command.index_memory != 0)          // проверяем, что команда использует индекс памяти
            {
                minAddress = Math.min(minAddress, command.index_memory);
                maxAddress = Math.max(maxAddress, command.index_memory);
                isUsed = true;                      // установим флаг, если адрес использовался
            }
        }
        if (!isUsed)
        {
            return "Адреса памяти не использовались"; // Сообщение, если адреса не использовались
        }
        return "от " + minAddress + " до " + maxAddress; // Возвращаем диапазон
    }


    public List<String> getInstructionsByCount()
    {
        Map<String, Integer> countMap = new HashMap<>();           // карта, хранящая название команды и кол-во ее использования
        for (Commands command : commands)
        {                                                          // имя команды, кол-во появлений команды + 1
            countMap.put(command.getTasks().name(), countMap.getOrDefault(command.getTasks().name(), 0) + 1);
        }

        List<Map.Entry<String, Integer>> commandsList = new ArrayList<>(countMap.entrySet());  // преобразуем множество пар в список

//        Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>()      // компаратор для сортировки по убыванию
//        {
//            @Override
//            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2)
//            {
//                return entry2.getValue().compareTo(entry1.getValue());  // получаем значение кол-ва 1ой и 2ой команды и сравниваем
//            }
//        };

        Comparator<Map.Entry<String, Integer>> comparator = (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue());
        commandsList.sort(comparator);




        List<String> sortedInstructions = new ArrayList<>();       // список для отсортированного по кол-ву появления команд
        for (Map.Entry<String, Integer> entry : commandsList)
        {
            sortedInstructions.add(entry.getKey());     // по каждой паре из commandsList берем имя команды и кладем его в итоговый список
        }
        return sortedInstructions;
    }

    @Override
    public Iterator<Commands> iterator()
    {
        return commands.iterator();
    }
}
