package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    private TreeMap<TimeOfDay, List<TrainingSession>> getOrCreateSessionsForDay(
            TrainingSession trainingSession
    ) {
        var dayOfWeek = trainingSession.getDayOfWeek();

        return timetable.computeIfAbsent(dayOfWeek, i -> new TreeMap<>());
    }

    private List<TrainingSession> getOrCreateSessionsForTime(TrainingSession trainingSession) {
        var timeOfDay = trainingSession.getTimeOfDay();

        var dayOfWeekSessions = getOrCreateSessionsForDay(trainingSession);

        return dayOfWeekSessions.computeIfAbsent(timeOfDay, i -> new ArrayList<>());
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {

        var timeOfDaySessions = getOrCreateSessionsForTime(trainingSession);
        timeOfDaySessions.add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        var daySessions = getTrainingSessionsForDay(dayOfWeek);

        if (daySessions == null) {
            return null;
        }
        return daySessions.get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> coachCountMap = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            var currentDaySessionsMap = getTrainingSessionsForDay(day);

            if (currentDaySessionsMap == null) {
                continue;
            }

            for (var sessions : currentDaySessionsMap.values()) {
                for (TrainingSession session : sessions) {
                    var coach = session.getCoach();
                    var nexCount = coachCountMap.getOrDefault(coach, 0) + 1;
                    coachCountMap.put(coach, nexCount);
                }
            }
        }

        return coachCountMap.entrySet().stream()
                .map((entry) -> new CounterOfTrainings(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(CounterOfTrainings::count).reversed())
                .toList();
    }
}
