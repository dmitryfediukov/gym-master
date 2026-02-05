package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    //Создаю расписание.
    //Мапа с ключом - день, чтобы быстро доставать данные по дню.
    //TreeMap, чтобы быстро доставать данные за время +, чтобы по дню можно было данные возвращать отсортировано.
    //Список тренировок, тк в одно и то же время может быть несколько тренировок.
    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    //Создаю мапу тренеров и количества их тренировок, чтобы быстро можно было у тренеров апдейтить кол-во тренировок
    private Map<Coach, Integer> coachesAndAmountTrainings = new HashMap<>();

    //Метод добавления тренировки в расписание
    public void addNewTrainingSession(TrainingSession trainingSession) {
        //Вынес переменные по тренировке, чтобы код был менее громоздким
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek(); //день недели
        Coach coach = trainingSession.getCoach();             //тренер
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay(); //слот времени

        //В список тренировок по слоту кладу тренировки за временной слот добавляемой тренировки.
        //Добавляю в него новую тренировку.
        List<TrainingSession> trainingsOfTimeSlot = getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay);
        trainingsOfTimeSlot.add(trainingSession);

        //Если день добавляемой тренировки уже есть в мапе расписания, то записываю/перезаписываю в нем мапу
        //тренировок за временной слот списком trainingsOfTimeSlot.
        //А если дня в расписании еще нет, то добавляю день и записываю в нем мапу тренировок за временной слот
        // списком trainingsOfTimeSlot.
        if (timetable.containsKey(dayOfWeek)) {
            timetable.get(dayOfWeek).put(timeOfDay, trainingsOfTimeSlot);
        } else {
            TreeMap<TimeOfDay, List<TrainingSession>> timeSlot = new TreeMap<>();
            timeSlot.put(timeOfDay, trainingsOfTimeSlot);
            timetable.put(dayOfWeek, timeSlot);
        }

        //Проверяю, есть ли у меня уже тренер в расписании из добавляемой тренировки.
        //Если есть, то инкрементирую его счетчик тренировок.
        //Если это первая тренировка тренера за неделю, то добавляю в coachesAndAmountTrainings тренера и ставлю ему
        //1 тренировку.
        if (coachesAndAmountTrainings.containsKey(coach)) {
            coachesAndAmountTrainings.put(coach, coachesAndAmountTrainings.get(coach) + 1);
        } else {
            coachesAndAmountTrainings.put(coach, 1);
        }
    }

    //Метод получения тренировок за день (за счет TreeMap сортируем по времени).
    //Если дня нет, о вернем пустую мапу.
    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    //Метод возвращения тренировок за временной слот конкретного дня.
    //Если день и время есть в расписании, то возвращаем.
    //Если нет - возвращаем пустой список.
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.containsKey(dayOfWeek) && timetable.get(dayOfWeek).containsKey(timeOfDay)) {
            return timetable.get(dayOfWeek).get(timeOfDay);
        }
        return new ArrayList<>();
    }

    //Метод возвращения тренеров и их количества заведенных тренировок за неделю
    public List<CounterOfTrainings> getCoachTrainingCounts() {
        //Создаем список объектов - счетчик количества тренировок тренеров
        List<CounterOfTrainings> listCounterOfTrainings = new ArrayList<>();
        //Проходимся по мапе coachesAndAmountTrainings и ее записи закидываем в список listCounterOfTrainings
        for (Map.Entry<Coach, Integer> entry : coachesAndAmountTrainings.entrySet()) {
            CounterOfTrainings counterOfTrainings = new CounterOfTrainings(entry.getKey(), entry.getValue());
            listCounterOfTrainings.add(counterOfTrainings);
        }
        //сортируем получившийся список по количеству тренировок
        Collections.sort(listCounterOfTrainings);

        return listCounterOfTrainings;
    }

}
