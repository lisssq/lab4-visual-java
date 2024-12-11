package org.example.lab4visual;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;

import javafx.util.Pair;

import java.util.Map;


public class AppController implements ICpuObserver
{
    @FXML
    private ScrollPane scrollPane;


    @FXML
    private GridPane gridPane;
    @FXML
    private Label R1, R2, R3, R4;
//    @FXML
//    private ScrollPane instructionScrollPane;
    @FXML
    private VBox instructionBox;      // контейнеры для отображения инструкций

//    @FXML
//    private ScrollPane memoryScrollPane;

    @FXML
    private VBox memoryBox;           // и памяти

    private Map<String, Integer> instructionCounter;     //хранит сколько каждая инструкций встречалась
    private Map<Integer, Integer> memory;                // хранит адрес памяти и ее содержимое
    private int commandIndex = 0;                         // индекс текущей команды

    private ICpu cpu;
    //private BCpu
    private ObservableList<Commands> commands;


    @Override
    public void onCpuStateChanged()
    {
        Platform.runLater(() -> {
            // Обновление меток с новыми значениями регистров
            R1.setText(String.valueOf(cpu.getRegisterValue("r1")));
            R2.setText(String.valueOf(cpu.getRegisterValue("r2")));
            R3.setText(String.valueOf(cpu.getRegisterValue("r3")));
            R4.setText(String.valueOf(cpu.getRegisterValue("r4")));
        });
        updateRegisters();       // Обновляем регистры
        updateMemory();          // Обновляем память
        updateInstructions();    // Обновляем инструкции

    }


    @FXML
    public void initialize()
    {
        if (scrollPane == null)
        {
            throw new RuntimeException("ScrollPane не инициализирован!");
        }
        System.out.println("ScrollPane успешно инициализирован!");
        // Инициализация процессора и коллекции команд
        cpu = new CPU();  // Инициализируем процессор

        // экзекьютер (?)
        cpu.addObserver(this);


        updateRegisters();


        commands = FXCollections.observableArrayList(

                new Commands(Tasks.init, 10, 25),
                new Commands(Tasks.init, 1, 5),
                new Commands(Tasks.ld, "R1", 10),
                new Commands(Tasks.ld, "R2", 1),
                new Commands(Tasks.add),
                new Commands(Tasks.div),
                new Commands(Tasks.mul),
                new Commands(Tasks.st));


        instructionCounter = new HashMap<>();       // инициализация счетчика инструкций для кажой = 0
        for (Commands command : commands)
        {
            instructionCounter.put(command.getName(), 0);
        }

        memory = new HashMap<>();       // создаем 25 ячеек памяти
        for (int i = 1; i <= 25; i++)
        {
            memory.put(i, 0);
        }


        updateGrid(); // отображение списка команд.
        updateMemory(); // отображение содержимого памяти.
    }


    private void updateGrid()
    {
        scrollPane.setContent(new Label("Updated Content"));
        gridPane.getChildren().clear();     // удаляем все старые элементы таблицы
        int row = 0;

        for (Commands command : commands)   // загружаем для каждой команды визуальное представление, настраиваем контроллер для команды (чтоб удалять перемещать команды)
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Command.fxml"));
                Pane commandPane = loader.load();
                CommandsController controller = loader.getController();

                controller.setCommand(command,
                        () -> {
                            commands.remove(command); // Удаляем команду
                            updateGrid();             // Сразу обновляем интерфейс
                            updateInstructions();     // Обновляем частоту инструкций
                        },
                        () -> moveCommandUp(command),
                        () -> moveCommandDown(command)
                );
                // подсветка команд
                if (row == commandIndex) {
                    controller.redStyle();
                } else {
                    controller.defaultStyle();
                }
                gridPane.add(commandPane, 0, row++);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        scrollPane.setContent(gridPane);        // связываем таблицу с интерфейсом
    }



    @FXML
    private void addInstruction()
    {
        // Создаём новое диалоговое окно
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Добавить инструкцию");

        // Добавляем кнопки действий: "Добавить" и "Отмена"
        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Создаём панель для ввода данных (GridPane)
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10); // Устанавливаем горизонтальный промежуток между элементами
        inputGrid.setVgap(10); // Устанавливаем вертикальный промежуток между элементами

        // Поле для ввода названия инструкции
        TextField nameField = new TextField();
        nameField.setPromptText("Название инструкции");
        inputGrid.addRow(0, new Label("Название:"), nameField);

        // Поле для ввода аргументов инструкции
        TextField argsField = new TextField();
        argsField.setPromptText("Аргументы инструкции");
        inputGrid.addRow(1, new Label("Аргументы:"), argsField);

        // Устанавливаем панель ввода в диалоговое окно
        dialog.getDialogPane().setContent(inputGrid);

        // Устанавливаем логику обработки нажатий на кнопки
        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == addButtonType)
            {
                // Возвращаем введённые данные в виде пары (название, аргументы)
                return new Pair<>(nameField.getText().trim(), argsField.getText().trim());
            }
            return null;
        });

        // Отображаем диалоговое окно и обрабатываем результат
        dialog.showAndWait().ifPresent(result ->
        {
            // Извлекаем введённые данные
            String name = result.getKey();
            String args = result.getValue();

            // Проверяем, что название команды не пустое
            if (!name.isEmpty())
            {
                // Пример создания команды с использованием существующего конструктора
                if (name.equals("init"))
                {
                    // Пример для команды "init"
                    Commands newCommand = new Commands(Tasks.init, Integer.parseInt(args), 100); // Пример с числовыми аргументами
                    commands.add(newCommand);
                    instructionCounter.put(newCommand.getName(), 0);  // Обновляем счетчик инструкций
                } else if (name.equals("ld"))
                {
                    // Пример для команды "ld"
                    Commands newCommand = new Commands(Tasks.ld, args, 10); // Пример для "ld" с аргументами
                    commands.add(newCommand);
                    instructionCounter.put(newCommand.getName(), 0);  // Обновляем счетчик инструкций
                }

                // Обновляем интерфейс, чтобы новая команда отобразилась
                // После добавления команды обновите отображение:
                updateGrid(); // Обновление таблицы команд
                updateInstructions(); // Обновление отображения частоты инструкций
                updateMemory(); // Обновление отображения памяти

            }
        });
    }


    @FXML
    private void executeInstruction()
    {

        // Проверяем, все ли команды выполнены
        if (commandIndex >= commands.size())
        {
            System.out.println("Выполнение завершено"); // Сообщаем о завершении
            return; // Прекращаем выполнение
        }

        // Получаем текущую команду для выполнения
        Commands currentCommand = commands.get(commandIndex);

        // Выполняем команду
        cpu.doCommand(currentCommand);  // выполняем текущую команду
//        for (Commands command : commands)
//        {  // collect - это объект класса Collect, который хранит команды
//            cpu.doCommand(command);  // выполняем команду с помощью CPU
//        }
        updateRegisters();

        // Увеличиваем счётчик выполнения для этой инструкции
        String commandName = currentCommand.getName();
        instructionCounter.put(commandName, instructionCounter.getOrDefault(commandName, 0) + 1);

        // Обрабатываем изменения в памяти, вызванные выполнением команды
        memoryChanges(currentCommand);

        // Переходим к следующей команде
        commandIndex++;

        // Обновляем интерфейс:
        updateInstructions(); // Обновляем информацию о выполненных командах
        updateMemory();       // Обновляем отображение памяти
        updateGrid();         // Обновляем таблицу с командами
        updateRegisters();

    }

    @FXML
    private void resetToDefault()
    {
        // сброс регистров
        //updateRegisters(0, 0, 0, 0);
        updateRegisters();
        // сброс памяти
        for (int i = 1; i <= 25; i++)
        {
            memory.put(i, 0);
        }
        updateMemory();
        // сброс частоты инструкций
        for (Commands command : commands)
        {
            instructionCounter.put(command.getName(), 0);
        }
        updateInstructions();
        commandIndex = 0;
        updateGrid();

    }

    private void updateRegisters()
    {
        // Получаем значения всех регистров через CPU и обновляем интерфейс
        R1.setText(String.valueOf(cpu.getRegisterValue("R1")));
        R2.setText(String.valueOf(cpu.getRegisterValue("R2")));
        R3.setText(String.valueOf(cpu.getRegisterValue("R3")));
        R4.setText(String.valueOf(cpu.getRegisterValue("R4")));
    }

    private void updateMemory()
    {
        memoryBox.getChildren().clear();
        for (Map.Entry<Integer, Integer> entry : memory.entrySet())
        {
            String memoryText = entry.getKey() + " : " + entry.getValue();
            Label label = new Label(memoryText);
            memoryBox.getChildren().add(label);
        }
    }

    private void updateInstructions()
    {
        // очищаем пространство
        instructionBox.getChildren().clear();
        // добавляем поэлементно инструкции
        for (Map.Entry<String, Integer> entry : instructionCounter.entrySet())
        {
            String instructionText = entry.getKey() + " : " + entry.getValue();
            Label label = new Label(instructionText);
            instructionBox.getChildren().add(label);
        }
    }


    // отслеживаем изменения памяти
    private void memoryChanges(Commands currentCommand)
    {
        String[] args = currentCommand.getArgs();
        if (currentCommand.getName().equals("init"))
        {
            int address = Integer.parseInt(args[0]);
            int value = Integer.parseInt(args[1]);
            // получили значения - записали в память по адресу
            memory.put(address, value);
        }

    }



    private void moveCommandUp(Commands command) {
        int index = commands.indexOf(command);
        if (index > 0) { // Проверяем, что команда не на первом месте
            commands.remove(index);
            commands.add(index - 1, command); // Перемещаем команду вверх
            updateGrid(); // Обновляем отображение
        }
    }

    private void moveCommandDown(Commands command) {
        int index = commands.indexOf(command);
        if (index < commands.size() - 1) { // Проверяем, что команда не на последнем месте
            commands.remove(index);
            commands.add(index + 1, command); // Перемещаем команду вниз
            updateGrid(); // Обновляем отображение
        }
    }

}