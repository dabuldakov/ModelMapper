package model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HumanDto {
    private Integer id;
    private String name;
    private int[] marks;
    private List<String> descriptions;

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
    public String toString() {
        return "HumanDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", marks=" + Arrays.toString(marks) +
                ", descriptions=" + descriptions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanDto humanDto = (HumanDto) o;
        return Objects.equals(id, humanDto.id) && Objects.equals(name, humanDto.name) && Arrays.equals(marks, humanDto.marks) && Objects.equals(descriptions, humanDto.descriptions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, descriptions);
        result = 31 * result + Arrays.hashCode(marks);
        return result;
    }
}
