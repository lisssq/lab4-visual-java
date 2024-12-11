package org.example.lab4visual;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CommandsController
{
    @FXML
    private Label nameCom;
    @FXML
    private Label args;

    private Commands command;
    private Runnable onDelete;
    private Runnable onMoveUp;
    private Runnable onMoveDown;

    // Инициализация контроллера для команды
    public void setCommand(Commands command, Runnable onDelete, Runnable onMoveUp, Runnable onMoveDown) {
        this.command = command;
        this.onDelete = onDelete;
        this.onMoveUp = onMoveUp;
        this.onMoveDown = onMoveDown;

        // Устанавливаем текст для названия команды
        nameCom.setText(command.getName()); // Отображаем имя инструкции

        // Устанавливаем текст для деталей команды (регистр и число)
        if (command.getArgs() != null && command.getArgs().length > 0) {
            StringBuilder details = new StringBuilder();
            for (String arg : command.getArgs()) {
                details.append(arg).append(" ");
            }
            args.setText(details.toString().trim()); // Отображаем аргументы команды
        } else {
            args.setText("  "); // Если аргументов нет
        }


    }

    @FXML
    private void deleteCommand() {
        if (onDelete != null) {
            onDelete.run();
        }
    }

    @FXML
    private void moveUpCommand() {
        if (onMoveUp != null) {
            onMoveUp.run();
        }
    }

    @FXML
    private void moveDownCommand() {
        if (onMoveDown != null) {
            onMoveDown.run();
        }
    }



    // красный стиль
    public void redStyle() {
        nameCom.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        args.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
    }

    // дефолт черный
    public void defaultStyle() {
        nameCom.setStyle("-fx-text-fill: black;");
        args.setStyle("-fx-text-fill: black;");
    }
}
