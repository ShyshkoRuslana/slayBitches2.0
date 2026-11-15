package laba13.task1;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}
