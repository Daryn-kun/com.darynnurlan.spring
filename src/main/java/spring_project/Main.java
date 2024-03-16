package spring_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appContext2.xml");

//        Movie movie = context.getBean("horrorMovie", Movie.class);
//        movie.watch();

        DVD dvd = context.getBean("dvd", DVD.class);
        /* Это буквально равно к этому если constructor-arg в appContext.xml:
           Movie movie = new Horror();
           DVD dvd     = new DVD(movie);

           Если property то создается пустой объект и вызывается сеттер
           DVD dvd     = new DVD();
           dvd.setHorror(movie);
           Вместо нас спринг сам создает объекты */
        dvd.play();

        // singleton создает только 1 объект, prototype несколько
//        Person person = context.getBean("myPerson", Person.class);
//        person.setName("Daryn");
//        Person person2 = context.getBean("myPerson", Person.class);
//        person2.setName("Assel");
//
//        System.out.println(person);
//        System.out.println(person2);

        /* init
           destroy
           factoryMethod
           Запуск приложения - начало работы Spring контейнера -> Создание Бина -> DI ->
                            -> init method -> использование бина -> конец работы Spring контейнера -> destroy method
         */

        context.close();
    }
}

class Person{
    private String name;

    private Person(){
        System.out.println("Person is created");
    }

    public static Person getPerson(){
        return new Person();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void myInitMethod(){
        System.out.println("Person initialization");
    }

    // сработает только для singleton, для prototype нужно вызвать вручную
    public void myDestroyMethod(){
        System.out.println("Person is destroyed");
    }
}

interface Movie{
    void watch();
}

@Component("horror")
class Horror implements Movie{

    public Horror() {
        System.out.println("Horror is created");
    }

    @Override
    public void watch() {
        System.out.println("Horror");
    }

    @Override
    public String toString() {
        return "Horror{}";
    }
}

@Component("drama")
class Drama implements Movie{

    public Drama(){
        System.out.println("Drama is created");
    }

    @Override
    public void watch() {
        System.out.println("Drama");
    }

    @Override
    public String toString() {
        return "Drama{}";
    }
}

@Component("dvd")
class DVD {

    @Autowired
    @Qualifier("drama")
    private Movie horror;
    private String name;
    private int cost;

    public DVD() {
    }

    public DVD(Movie horror){
        this.horror = horror;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setHorror(Movie horror) { // в спринге будет просто horror без set
        this.horror = horror;
    }

    void play(){
//        System.out.println(horror);
        System.out.println(name + " " + cost);
    }
}