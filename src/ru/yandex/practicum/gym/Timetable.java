package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    private TreeMap<TimeOfDay, List<TrainingSession>> getDayOfWeekSessions(TrainingSession trainingSession) {
        var dayOfWeek = trainingSession.getDayOfWeek();

        var dayOfWeekSessions = timetable.getOrDefault(dayOfWeek, null);

        if (dayOfWeekSessions == null) {
            timetable.put(dayOfWeek, new TreeMap<>());
        }

        return timetable.get((dayOfWeek));
    }

    private List<TrainingSession> getTimeOfDaySessions(TrainingSession trainingSession) {
        var timeOfDay = trainingSession.getTimeOfDay();

        var dayOfWeekSessions = getDayOfWeekSessions(trainingSession);

        var timeOfDaySessions = dayOfWeekSessions.getOrDefault(timeOfDay, null);

        if (timeOfDaySessions == null) {
            dayOfWeekSessions.put(timeOfDay, new ArrayList<>());
        }

        return dayOfWeekSessions.get(timeOfDay);
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {

        var timeOfDaySessions = getTimeOfDaySessions(trainingSession);
        timeOfDaySessions.add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek,
            TimeOfDay timeOfDay) {
        var daySessions = getTrainingSessionsForDay(dayOfWeek);
        return daySessions.getOrDefault(timeOfDay, null);
    }
}
