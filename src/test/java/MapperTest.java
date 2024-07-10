import model.*;
import org.example.Mapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

class MapperTest {

    private static Mapper mapper;
    private static ModelMapper modelMapper;

    @BeforeAll
    public static void before() {
        System.out.println("before");
        ArrayList<Class<?>> classes = new ArrayList<>();
        classes.add(DocDto.class);
        classes.add(Doc.class);
        mapper = new Mapper(classes);
        modelMapper = modelMapper();
    }

    @Test
    void shouldReturnNewMappedObject() {
        //Given
        Doc doc = createDocData();
        //When
        DocDto docDto = modelMapper.map(doc, DocDto.class);
        DocDto docDtoResult = mapper.convert(doc, DocDto.class);
        //Then
        assertEquals(docDto, docDtoResult);
    }

    @Test
    void shouldReturnNewMappedObjectWithQueue() {
        //Given
        Doc doc = createDocDataWithQueue();
        //When
        // TODO: 10.07.2024 modelMapper библиотека не отрабатывает мапинг очереди падает с ошибкой
        //DocDto docDto = modelMapper.map(doc, DocDto.class);

        DocDto docDtoResult = mapper.convert(doc, DocDto.class);
        Queue<CarDto> carQueue = docDtoResult.getCarQueue();
        //Then
        assertEquals(doc.getCarQueue().size(), carQueue.size());
        assertEquals(doc.getCarQueue().poll().getId(), carQueue.poll().getId());
        assertEquals(doc.getCarQueue().poll().getId(), carQueue.poll().getId());
        assertEquals(doc.getCarQueue().poll().getId(), carQueue.poll().getId());
        assertEquals(0, carQueue.size());
    }

    @Test
    void shouldReturnNewMappedObjectWithNullParams() {
        //Given
        Doc doc = new Doc();
        doc.setId(1);
        doc.setNumber("sss-zzz-sss");
        doc.setExpiryDate(new Date());
        //When
        DocDto docDto = modelMapper.map(doc, DocDto.class);
        DocDto docDtoResult = mapper.convert(doc, DocDto.class);
        //Then
        assertEquals(docDto, docDtoResult);
    }

    private static ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        return mapper;
    }

    private Human getHuman() {
        Human human = new Human();
        human.setId(1);
        human.setName("Korney");
        human.setMarks(new int[]{1, 2, 3, 4, 5});
        List<String> stringList = new ArrayList<>();
        stringList.add("first");
        stringList.add("second");
        stringList.add("third");
        human.setDescriptions(stringList);

        return human;
    }

    private Car getFirstCar() {
        Car car = new Car();
        car.setId(15);
        car.setName("Lada");
        car.setPrice(1200);
        car.setHuman(getHuman());

        return car;
    }

    private Car getSecondCar() {
        Car car = new Car();
        car.setId(22);
        car.setName("Volvo");
        car.setPrice(1500);

        return car;
    }

    private Car getThirdCar() {
        Car car = new Car();
        car.setId(23);
        car.setName("Audi");
        car.setPrice(2000);

        return car;
    }

    private Doc createDocData() {

        Car[] carsArray = new Car[3];
        carsArray[0] = getFirstCar();
        carsArray[1] = getSecondCar();
        carsArray[2] = getThirdCar();

        Doc doc = new Doc();
        doc.setId(22);
        doc.setNumber("222-sss-555");
        doc.setExpiryDate(new Date());
        doc.setCar(getFirstCar());
        doc.setCars(List.of(getSecondCar(), getThirdCar()));
        doc.setCarsArray(carsArray);
        doc.setFlag(true);
        doc.setFlagSimple(false);

        return doc;
    }

    private Doc createDocDataWithQueue() {
        Car[] carsArray = new Car[3];
        carsArray[0] = getFirstCar();
        carsArray[1] = getSecondCar();
        carsArray[2] = getThirdCar();

        Queue<Car> carQueue = new LinkedList<>();
        carQueue.add(getFirstCar());
        carQueue.add(getSecondCar());
        carQueue.add(getThirdCar());

        Doc doc = new Doc();
        doc.setId(22);
        doc.setNumber("222-sss-555");
        doc.setExpiryDate(new Date());
        doc.setCar(getFirstCar());
        doc.setCars(List.of(getSecondCar(), getThirdCar()));
        doc.setCarsArray(carsArray);
        doc.setCarQueue(carQueue);

        return doc;
    }
}