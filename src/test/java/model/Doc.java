package model;

import java.util.*;

public class Doc {
    private int id;
    private String number;
    private Date expiryDate;
    private Boolean flag;
    private boolean flagSimple;
    private Car car;
    private List<Car> cars;
    private Car[] carsArray;
    private Queue<Car> carQueue;

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", expiryDate=" + expiryDate +
                ", car=" + car +
                ", cars=" + cars +
                ", carsArray=" + Arrays.toString(carsArray) +
                ", carQueue=" + carQueue +
                '}';
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public boolean isFlagSimple() {
        return flagSimple;
    }

    public void setFlagSimple(boolean flagSimple) {
        this.flagSimple = flagSimple;
    }

    public Queue<Car> getCarQueue() {
        return carQueue;
    }

    public void setCarQueue(Queue<Car> carQueue) {
        this.carQueue = carQueue;
    }

    public Car[] getCarsArray() {
        return carsArray;
    }

    public void setCarsArray(Car[] carsArray) {
        this.carsArray = carsArray;
    }


    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doc doc = (Doc) o;
        return id == doc.id && Objects.equals(number, doc.number) && Objects.equals(expiryDate, doc.expiryDate) && Objects.equals(car, doc.car) && Objects.equals(cars, doc.cars) && Arrays.equals(carsArray, doc.carsArray) && Objects.equals(carQueue, doc.carQueue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, number, expiryDate, car, cars, carQueue);
        result = 31 * result + Arrays.hashCode(carsArray);
        return result;
    }
}
