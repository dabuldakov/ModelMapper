package model;

import java.util.*;

public class DocDto {
    private int id;
    private String number;
    private Date expiryDate;
    private CarDto car;
    private List<CarDto> cars;
    private CarDto[] carsArray;
    private Queue<CarDto> carQueue;

    @Override
    public String toString() {
        return "DocDto{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", expiryDate=" + expiryDate +
                ", car=" + car +
                ", cars=" + cars +
                ", carsArray=" + Arrays.toString(carsArray) +
                ", carQueue=" + carQueue +
                '}';
    }

    public Queue<CarDto> getCarQueue() {
        return carQueue;
    }

    public void setCarQueue(Queue<CarDto> carQueue) {
        this.carQueue = carQueue;
    }

    public CarDto[] getCarsArray() {
        return carsArray;
    }

    public void setCarsArray(CarDto[] carsArray) {
        this.carsArray = carsArray;
    }

    public List<CarDto> getCars() {
        return cars;
    }

    public void setCars(List<CarDto> cars) {
        this.cars = cars;
    }

    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocDto docDto = (DocDto) o;
        return id == docDto.id && Objects.equals(number, docDto.number) && Objects.equals(expiryDate, docDto.expiryDate) && Objects.equals(car, docDto.car) && Objects.equals(cars, docDto.cars) && Arrays.equals(carsArray, docDto.carsArray) && Objects.equals(carQueue, docDto.carQueue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, number, expiryDate, car, cars, carQueue);
        result = 31 * result + Arrays.hashCode(carsArray);
        return result;
    }
}
