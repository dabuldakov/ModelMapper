package model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Human {
    private Integer id;
    private String name;
    private int[] marks;
    private List<String> descriptions;


    @Override
    public String toString() {
        return "Human{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", marks=" + Arrays.toString(marks) +
                ", descriptions=" + descriptions +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getMarks() {
        return marks;
    }

    public void setMarks(int[] marks) {
        this.marks = marks;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return Objects.equals(id, human.id) && Objects.equals(name, human.name) && Arrays.equals(marks, human.marks) && Objects.equals(descriptions, human.descriptions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, descriptions);
        result = 31 * result + Arrays.hashCode(marks);
        return result;
    }
}
