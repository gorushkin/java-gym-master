package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimetableTest {
    private static TrainingSession getTrainingSession(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        return new TrainingSession(groupAdult, coach, dayOfWeek, timeOfDay);
    }

    private static TrainingSession getTrainingSession(Coach coach) {
        TimeOfDay timeOfDay = new TimeOfDay(13, 0);
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        return new TrainingSession(groupAdult, coach, dayOfWeek, timeOfDay);
    }


    private static Timetable createTimetable() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach, DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0));

        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        return timetable;
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);


        var mondayResults = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        var tuesdayResults = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertEquals(1, mondayResults.size());
        Assertions.assertNull(tuesdayResults);
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = createTimetable();

        var mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        var thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        var tuesdayResults = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertNull(tuesdayResults);

        var actualTimes = new ArrayList<>(thursdaySessions.keySet());

        List<TimeOfDay> expectedThursdayOrder = new ArrayList<>(List.of(new TimeOfDay(13, 0), new TimeOfDay(20, 0)));

        for (int i = 0; i < expectedThursdayOrder.size(); i++) {
            var expectedTime = expectedThursdayOrder.get(i);
            var actualTime = actualTimes.get(i);
            Assertions.assertEquals(actualTime, expectedTime);
        }
    }

    @Test
    void testGetTrainingSessionsForDayAndTimePropperAdding() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        var mondayOneResults = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, mondayOneResults.size());

        var mondayTwoResults = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertNull(mondayTwoResults);
    }

    @Test
    void testAddNewTrainingSession() {
        Timetable timetable = new Timetable();

        DayOfWeek day = DayOfWeek.MONDAY;

        var mondaySessionsBeforeAdding = timetable.getTrainingSessionsForDay(day);

        Assertions.assertNull(mondaySessionsBeforeAdding);

        TrainingSession singleTrainingSession = getTrainingSession(day, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);
        var mondaySessionsAfterAdding = timetable.getTrainingSessionsForDay(day);

        Assertions.assertEquals(1, mondaySessionsAfterAdding.size());
    }

    @Test
    void testSortingLessonsWithOnlyMinutesDiff() {
        Timetable timetable = new Timetable();

        var timeOdDayOne = new TimeOfDay(13, 30);
        var timeOdDayTwo = new TimeOfDay(13, 10);

        var weekDay = DayOfWeek.MONDAY;

        List<TimeOfDay> testData = new ArrayList<>(List.of(timeOdDayOne, timeOdDayTwo));

        testData.forEach(i -> {
            TrainingSession session = getTrainingSession(weekDay, i);
            timetable.addNewTrainingSession(session);
        });

        var sessionsForDay = timetable.getTrainingSessionsForDay(weekDay);

        var actualTimes = new ArrayList<>(sessionsForDay.keySet());

        List<TimeOfDay> sortedTestData = new ArrayList<>(testData);

        sortedTestData.sort(null);

        for (int i = 0; i < sortedTestData.size(); i++) {
            var expectedTime = sortedTestData.get(i);
            var actualTime = actualTimes.get(i);
            Assertions.assertEquals(actualTime, expectedTime);
        }
    }

    @Test
    void testGetCountByCoachesSortedDescending() {
        Timetable timetable = new Timetable();

        var couchesCount = 3;

        for (int i = 1; i <= couchesCount; i++) {
            Coach coach = new Coach("s" + i, "n" + i, "m" + i);

            for (int j = 0; j < i; j++) {
                TrainingSession session = getTrainingSession(coach);
                timetable.addNewTrainingSession(session);
            }
        }


        var result = timetable.getCountByCoaches();

        for (int i = 0; i < couchesCount - 1; i++) {
            var currCoachCount = result.get(i).count();
            var nextCoachCount = result.get(i + 1).count();
            Assertions.assertTrue(currCoachCount >= nextCoachCount);
        }
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();

        var couchesCount = 3;

        var expectedSessionsInfo = new HashMap<Coach, Integer>();

        for (int i = 1; i <= couchesCount; i++) {
            Coach coach = new Coach("s" + i, "n" + i, "m" + i);
            expectedSessionsInfo.put(coach, i);

            for (int j = 0; j < i; j++) {
                TrainingSession session = getTrainingSession(coach);
                timetable.addNewTrainingSession(session);
            }
        }


        var result = timetable.getCountByCoaches();

        for (int i = couchesCount - 1; i >= 0; i--) {
            var current = result.get(i);
            var currCoachCount = current.count();
            var currCoach = current.coach();
            var expectedResult = expectedSessionsInfo.get(currCoach);
            Assertions.assertEquals(expectedResult, currCoachCount);
        }
    }

    @Test
    void testMultipleTrainingSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        var sessionsCount = 3;

        var timeOfDay = new TimeOfDay(14, 15);
        var dayOfWeek = DayOfWeek.SATURDAY;

        for (var i = 0; i < sessionsCount; i++) {
            TrainingSession session = getTrainingSession(dayOfWeek, timeOfDay);
            timetable.addNewTrainingSession(session);
        }

        var selectedTimeSessions = timetable.getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay);

        Assertions.assertEquals(sessionsCount, selectedTimeSessions.size());
    }


    @Test
    void testGetCountByCoachesSameCoachDifferentInstances() {
        Timetable timetable = new Timetable();

        var name = "name";
        var surname = "surname";
        var middleName = "middleName";

        Coach currentCoach = new Coach(surname, name, middleName);


        var count = 3;

        for (var i = 0; i < count; i++) {
            Coach coach = new Coach(surname, name, middleName);
            TrainingSession session = getTrainingSession(coach);
            timetable.addNewTrainingSession(session);
        }
        var allCoachesSessionsInfo = timetable.getCountByCoaches();
        Assertions.assertEquals(1, allCoachesSessionsInfo.size());

        var currentCoachSessionsInfo = timetable.getCountByCoaches().getFirst();
        Assertions.assertEquals(currentCoachSessionsInfo.coach(), currentCoach);
        Assertions.assertEquals(count, currentCoachSessionsInfo.count());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        var allCoachesSessionsInfo = timetable.getCountByCoaches();
        Assertions.assertEquals(0, allCoachesSessionsInfo.size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeEmptyDay() {
        Timetable timetable = new Timetable();

        var dayOfWeek = DayOfWeek.SATURDAY;
        var timeOfDay = new TimeOfDay(12, 15);

        var currentDayAndTimeTrainingSessions = timetable.getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay);

        Assertions.assertNull(currentDayAndTimeTrainingSessions);
    }
}
