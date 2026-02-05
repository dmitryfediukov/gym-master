package ru.yandex.practicum.gym;

//имплементил Comparable и переопределял compareTo, чтобы отсортировать тренеров по количеству тренировок
public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private Coach coach;
    private int counter;

    public int getCounter() {
        return counter;
    }

    public CounterOfTrainings(Coach coach, int counter) {
        this.coach = coach;
        this.counter = counter;
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        return Integer.compare(o.counter, this.counter);
    }

    @Override
    public String toString() {
        return coach + " | " + counter;
    }
}
