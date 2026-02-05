package ru.yandex.practicum.gym;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class GymMaster {

    private static final Scanner scanner = new Scanner(System.in);

    static Timetable timetable = new Timetable();

    //Добавляю тренеров
    static Coach coach1 = new Coach("Федюков", "Дмитрий", "Юрьевич");
    static Coach coach2 = new Coach("Иванов", "Иван", "Иванович");
    static Coach coach3 = new Coach("Третьяков", "Глеб", "Викторович");
    static List<Coach> coaches = new ArrayList<>(List.of(coach1, coach2, coach3));

    //Добавляю группы
    static Group group1 = new Group("Гимнастика", Age.CHILD, 55);
    static Group group2 = new Group("Акробатика", Age.ADULT, 120);
    static Group group3 = new Group("Легкая атлетика", Age.ADULT, 70);
    static List<Group> groups = new ArrayList<>(List.of(group1, group2, group3));

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addtrainingSession();
                case 2 -> showTrainingsPerDay();
                case 3 -> getTrainingSessionsForDayAndTime();
                case 4 -> showCoachesWithTrainings();
//                case 5 -> addCoach;
                case 0 -> running = false;
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 — Добавить новую тренировку в расписание");
        System.out.println("2 — Посмотреть все тренировки за определенный день недели");
        System.out.println("3 — Посмотреть все тренировки за день недели в указанный слот времени");
        System.out.println("4 — Посмотреть тренеров и их количество тренировок за неделю");
        System.out.println("0 — Завершить");
    }


    //Метод создание тренировки
    private static void addtrainingSession() {
        TrainingSession trainingSession = null;
        int coachChoice = -1;
        int groupChoice = -1;
        int dayChoice = -1;
        Coach coach = null;
        Group group = null;
        DayOfWeek day = null;
        int hours = -1;
        int minutes = -1;
        TimeOfDay time = null;

        //Выбор тренера
        while (true) {
            System.out.println("Список тренеров:");
            for (int i = 0; i < coaches.size(); i++) {
                System.out.println((i) + " - " + coaches.get(i));
            }
            System.out.println("Укажите номер тренера: ");
            coachChoice = Integer.parseInt(scanner.nextLine());
            if (coachChoice >= 0 && coachChoice < coaches.size()) {
                coach = coaches.get(coachChoice);
                break;
            } else {
                System.out.println("Тренера с данным номером нет, попробуйте еще раз.");
            }
        }

        //Выбор группы
        while (true) {
            System.out.println("Список групп:");
            for (int i = 0; i < groups.size(); i++) {
                System.out.println((i) + " - " + groups.get(i));
            }
            System.out.println("Укажите номер группы: ");
            groupChoice = Integer.parseInt(scanner.nextLine());
            if (groupChoice >= 0 && groupChoice < groups.size()) {
                group = groups.get(groupChoice);
                break;
            } else {
                System.out.println("Группы с данным номером нет, попробуйте еще раз.");
            }
        }

        //Выбор дня недели
        while (true) {
            System.out.print("1 - Понедельник\n" +
                    "2 - Вторник\n" +
                    "3 - Среда\n" +
                    "4 - Четверг\n" +
                    "5 - Пятница\n" +
                    "6 - Суббота\n" +
                    "7 - Воскресенье\n" +
                    "Введите день недели (1-7): ");
            dayChoice = Integer.parseInt(scanner.nextLine());

            if (dayChoice >= 1 && dayChoice <= 7) {
                switch (dayChoice) {
                    case 1 -> day = DayOfWeek.MONDAY;
                    case 2 -> day = DayOfWeek.TUESDAY;
                    case 3 -> day = DayOfWeek.WEDNESDAY;
                    case 4 -> day = DayOfWeek.THURSDAY;
                    case 5 -> day = DayOfWeek.FRIDAY;
                    case 6 -> day = DayOfWeek.SATURDAY;
                    case 7 -> day = DayOfWeek.SUNDAY;
                };
                break;
            }
            System.out.println("Ошибка: нужно число от 1 до 7. Повторите ввод.");
        }

        //Ввод часов
        while (true) {
            System.out.print("Введите часы (от 0 до 23): ");
            hours = Integer.parseInt(scanner.nextLine());

            if (hours >= 0 && hours <= 23) break;

            System.out.println("Ошибка: часы должны быть от 0 до 23. Повторите ввод.");
        }

        //Ввод минут
        while (true) {
            System.out.print("Введите минуты (от 0 до 59): ");
            minutes = Integer.parseInt(scanner.nextLine());

            if (minutes >= 0 && minutes <= 59) break;

            System.out.println("Ошибка: минуты должны быть от 0 до 59. Повторите ввод.");
        }
        time = new TimeOfDay(hours, minutes);

        //Создаем тренировку
        trainingSession = new TrainingSession(group, coach, day, time);

        //Добавляем тренировку в расписание
        timetable.addNewTrainingSession(trainingSession);

        System.out.println();
        System.out.println("Тренировка создана:\n" + trainingSession);
        System.out.println();
    }

    //Метод получения всех тренировок за день (сортировка по времени)
    private static void showTrainingsPerDay() {
        DayOfWeek day = null;
        int dayChoice = -1;

        //Выбор дня недели
        while (true) {
            System.out.println("За какой день Вы хотите увидеть тренировки?");
            System.out.print("1 - Понедельник\n" +
                    "2 - Вторник\n" +
                    "3 - Среда\n" +
                    "4 - Четверг\n" +
                    "5 - Пятница\n" +
                    "6 - Суббота\n" +
                    "7 - Воскресенье\n" +
                    "Введите день недели (1-7): ");
            dayChoice = Integer.parseInt(scanner.nextLine());

            if (dayChoice >= 1 && dayChoice <= 7) {
                switch (dayChoice) {
                    case 1 -> day = DayOfWeek.MONDAY;
                    case 2 -> day = DayOfWeek.TUESDAY;
                    case 3 -> day = DayOfWeek.WEDNESDAY;
                    case 4 -> day = DayOfWeek.THURSDAY;
                    case 5 -> day = DayOfWeek.FRIDAY;
                    case 6 -> day = DayOfWeek.SATURDAY;
                    case 7 -> day = DayOfWeek.SUNDAY;
                };
                break;
            }
            System.out.println("Ошибка: нужно число от 1 до 7. Повторите ввод.");
            System.out.println();
        }

        //Вызов метода на получение мапы тренировок за день
        TreeMap<TimeOfDay, List<TrainingSession>> trainingSessionsForDay = timetable.getTrainingSessionsForDay(day);
        //Проверяем мапу на пустоту
        if (trainingSessionsForDay == null || trainingSessionsForDay.isEmpty()) {
            System.out.println("На " + day + " тренировок нет.");
            System.out.println();
        } else {
            //Если мапа не пустая, то выводим тренировки за день
            for (TimeOfDay time : trainingSessionsForDay.navigableKeySet()) {
                for (TrainingSession s : trainingSessionsForDay.get(time)) {
                    System.out.println(s);
                    System.out.println();
                }
            }
        }
    }

    //Метод получения тренировок за определенный временной слот определенного дня
    private static void getTrainingSessionsForDayAndTime() {
        int dayChoice = -1;
        DayOfWeek day = null;
        int hours = -1;
        int minutes = -1;
        TimeOfDay time = null;

        //Выбор дня недели
        while (true) {
            System.out.println("За какой день Вы хотите увидеть тренировки?");
            System.out.print("1 - Понедельник\n" +
                    "2 - Вторник\n" +
                    "3 - Среда\n" +
                    "4 - Четверг\n" +
                    "5 - Пятница\n" +
                    "6 - Суббота\n" +
                    "7 - Воскресенье\n" +
                    "Введите день недели (1-7): ");
            dayChoice = Integer.parseInt(scanner.nextLine());

            if (dayChoice >= 1 && dayChoice <= 7) {
                switch (dayChoice) {
                    case 1 -> day = DayOfWeek.MONDAY;
                    case 2 -> day = DayOfWeek.TUESDAY;
                    case 3 -> day = DayOfWeek.WEDNESDAY;
                    case 4 -> day = DayOfWeek.THURSDAY;
                    case 5 -> day = DayOfWeek.FRIDAY;
                    case 6 -> day = DayOfWeek.SATURDAY;
                    case 7 -> day = DayOfWeek.SUNDAY;
                };
                break;
            }
            System.out.println("Ошибка: нужно число от 1 до 7. Повторите ввод.");
        }

        //Ввод часов
        while (true) {
            System.out.print("Введите часы (от 0 до 23): ");
            hours = Integer.parseInt(scanner.nextLine());

            if (hours >= 0 && hours <= 23) break;

            System.out.println("Ошибка: часы должны быть от 0 до 23. Повторите ввод.");
        }

        //Ввод минут
        while (true) {
            System.out.print("Введите минуты (от 0 до 59): ");
            minutes = Integer.parseInt(scanner.nextLine());

            if (minutes >= 0 && minutes <= 59) break;

            System.out.println("Ошибка: минуты должны быть от 0 до 59. Повторите ввод.");
        }
        time = new TimeOfDay(hours, minutes);

        //Вызов метода на получение списка тренировок за определенный временной слот
        List<TrainingSession> trainingSessionsOnTimeSlot = timetable.getTrainingSessionsForDayAndTime(day, time);

        //Проверяем список тренировок на пустоту
        if (trainingSessionsOnTimeSlot == null || trainingSessionsOnTimeSlot.isEmpty()) {
            System.out.println("В " + day + " в " + time + " тренировок нет.");
        } else {
            //Если список не пустой, то выводим тренировки из списка
            System.out.println("Тренировки в " + day + " в " + time + ":");
            System.out.println();
            for (TrainingSession trainingSession : trainingSessionsOnTimeSlot) {
                System.out.println(trainingSession);
                System.out.println();
            }
        }
    }

    //Метод получения тренеров и количества их тренировок (отсортировано по количеству тренировок)
    private static void showCoachesWithTrainings() {
        //Вызов метода на получение списка объектов "Тренер и его количество тренировок в неделю"
        List<CounterOfTrainings> coachesAndAmountTrainings = timetable.getCoachTrainingCounts();

        //Проверяем список на пустоту
        if (coachesAndAmountTrainings == null || coachesAndAmountTrainings.isEmpty()) {
            System.out.println("В расписание недели еще не добавлены тренировки.");
        } else {
            //Если список не пустой, то выводим тренеров и их количество тренировок за неделю
            System.out.println();
            System.out.println("Тренеры:");

            for (CounterOfTrainings counterOfTrainings : coachesAndAmountTrainings) {
                System.out.println(counterOfTrainings);
            }
        }
    }
}

