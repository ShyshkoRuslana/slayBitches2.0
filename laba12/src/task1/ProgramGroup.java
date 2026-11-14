package laba12.task1;

import java.util.ArrayList;
import java.util.List;

public class ProgramGroup implements FestivalComponent {
    private final String groupType;
    private final String name;
    private final List<FestivalComponent> children = new ArrayList<>();

    public ProgramGroup(String groupType, String name) {
        this.groupType = groupType;
        this.name = name;
        EventLogger.log("ProgramGroup", "Створено вузол [" + groupType + "]: \"" + name + "\"");
    }

    public void add(FestivalComponent component) {
        children.add(component);
        EventLogger.log("ProgramGroup", "До \"" + name + "\" додано \"" + component.getName() + "\"");
    }

    public void remove(FestivalComponent component) {
        children.remove(component);
        EventLogger.log("ProgramGroup", "З \"" + name + "\" видалено \"" + component.getName() + "\"");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDuration() {
        int total = 0;
        for (FestivalComponent child : children) {
            total += child.getDuration();
        }
        return total;
    }

    @Override
    public int getMovieCount() {
        int count = 0;
        for (FestivalComponent child : children) {
            count += child.getMovieCount();
        }
        return count;
    }

    @Override
    public void printTree(String indent) {
        System.out.printf("%s+ [%s] %s — разом: %d хв (фільмів: %d)%n",
                indent, groupType, name, getDuration(), getMovieCount());
        for (FestivalComponent child : children) {
            child.printTree(indent + "    ");
        }
    }
}