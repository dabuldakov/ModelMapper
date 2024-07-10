package model;

import java.util.Objects;

public class CarDto {
    private int id;
    private String name;
    private Integer price;
    private HumanDto human;

    public HumanDto getHuman() {
        return human;
    }

    public void setHuman(HumanDto human) {
        this.human = human;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CarDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", human=" + human +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDto carDto = (CarDto) o;
        return id == carDto.id && Objects.equals(name, carDto.name) && Objects.equals(price, carDto.price) && Objects.equals(human, carDto.human);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, human);
    }
}
