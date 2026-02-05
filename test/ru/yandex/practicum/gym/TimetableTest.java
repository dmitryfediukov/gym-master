package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    // ===========================
    // ТЕСТЫ ИЗ ТЗ
    // ===========================

    // [ТЗ]
    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(monday);
        assertEquals(1, monday.size());

        List<TrainingSession> mondayAt13 = monday.get(new TimeOfDay(13, 0));
        assertNotNull(mondayAt13);
        assertEquals(1, mondayAt13.size());
        assertSame(singleTrainingSession, mondayAt13.get(0));

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertNotNull(tuesday);
        assertTrue(tuesday.isEmpty());
    }

    // [ТЗ]
    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());
        assertEquals(1, monday.get(new TimeOfDay(13, 0)).size());
        assertSame(mondayChildTrainingSession, monday.get(new TimeOfDay(13, 0)).get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, List<TrainingSession>> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursday.size());

        NavigableSet<TimeOfDay> keys = thursday.navigableKeySet();
        assertEquals(List.of(new TimeOfDay(13, 0), new TimeOfDay(20, 0)), List.copyOf(keys));

        assertEquals(1, thursday.get(new TimeOfDay(13, 0)).size());
        assertSame(thursdayChildTrainingSession, thursday.get(new TimeOfDay(13, 0)).get(0));

        assertEquals(1, thursday.get(new TimeOfDay(20, 0)).size());
        assertSame(thursdayAdultTrainingSession, thursday.get(new TimeOfDay(20, 0)).get(0));

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesday.isEmpty());
    }

    // [ТЗ]
    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> at13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertNotNull(at13);
        assertEquals(1, at13.size());
        assertSame(singleTrainingSession, at13.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> at14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertNotNull(at14);
        assertTrue(at14.isEmpty());
    }

    // ===========================
    // НОВЫЕ ТЕСТЫ
    // ===========================

    // [НОВЫЙ] В одно и то же время в один день может быть несколько тренировок
    @Test
    void testMultipleSessionsSameDaySameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");
        Group group1 = new Group("Гимнастика", Age.CHILD, 55);
        Group group2 = new Group("Акробатика", Age.CHILD, 60);

        TimeOfDay time = new TimeOfDay(13, 0);

        TrainingSession s1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY, time);
        TrainingSession s2 = new TrainingSession(group2, coach2, DayOfWeek.MONDAY, time);

        timetable.addNewTrainingSession(s1);
        timetable.addNewTrainingSession(s2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );
        assertEquals(2, sessions.size());
        assertSame(s1, sessions.get(0));
        assertSame(s2, sessions.get(1));
    }

    // [НОВЫЙ] Для дня без тренировок должен возвращаться пустой TreeMap (не null)
    @Test
    void testGetTrainingSessionsForDayEmptyDayReturnsEmptyMapNotNull() {
        Timetable timetable = new Timetable();

        TreeMap<TimeOfDay, List<TrainingSession>> sunday = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        assertNotNull(sunday);
        assertTrue(sunday.isEmpty());
    }

    // --- НОВЫЕ ТЕСТЫ ДЛЯ МЕТОДА ИЗ ТЗ getCoachTrainingCounts (подсчёт тренировок тренеров) ---

    // [НОВЫЙ, для getCoachTrainingCounts] Пустой результат, если тренировок нет
    @Test
    void testCoachTrainingCountsEmptyWhenNoSessions() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counts = timetable.getCoachTrainingCounts();
        assertNotNull(counts);
        assertTrue(counts.isEmpty());
    }

    // [НОВЫЙ, для getCoachTrainingCounts] Сортировка по убыванию количества тренировок
    @Test
    void testCoachTrainingCountsSortedDesc() {
        Timetable timetable = new Timetable();

        Coach coachA = new Coach("A", "A", "A");
        Coach coachB = new Coach("B", "B", "B");
        Group group = new Group("Группа", Age.ADULT, 60);

        // coachA — 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        // coachB — 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(group, coachB, DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> counts = timetable.getCoachTrainingCounts();
        assertEquals(2, counts.size());

        // Проверяем убывание по количеству
        assertTrue(counts.get(0).getCounter() >= counts.get(1).getCounter());
        assertEquals(3, counts.get(0).getCounter());
        assertEquals(1, counts.get(1).getCounter());
    }

    // [НОВЫЙ, для getCoachTrainingCounts] Один тренер — несколько тренировок суммируются корректно
    @Test
    void testCoachTrainingCountsSingleCoachMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Сидор", "Сидорович");
        Group group = new Group("Группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> counts = timetable.getCoachTrainingCounts();
        assertEquals(1, counts.size());
        assertEquals(3, counts.get(0).getCounter());
    }
}

