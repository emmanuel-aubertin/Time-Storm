package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;

/**
 * Represents an event in the schedule at Avignon University.
 * Provides methods to retrieve information about the event, such as its code, start time, end time, type, memo, and associated teacher and classroom.
 * This class interacts with Avignon University's API to fetch data.
 * An event can have an associated teacher and classroom.
 * The teacher and classroom information is fetched from the API using the provided LoginProvider.
 * The event may also have additional attributes such as type, memo, and favorite status.
 * Note: The class assumes that the title contains specific information about the event, such as teacher and classroom details.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class Event {
    private String code;
    private String start;
    private String end;
    private String type;
    private String memo;
    private String favori;
    private Teacher teacher;
    private Classroom classroom;

    /**
     * Constructs an Event object with the specified parameters.
     *
     * @param code    The code associated with the event.
     * @param start   The start time of the event.
     * @param title   The title of the event, which may contain additional information such as teacher and classroom details.
     * @param end     The end time of the event.
     * @param favori  The favorite status of the event.
     * @param user    The LoginProvider used to authenticate the API request.
     */
    public Event(String code, String start, String end, String title, String favori, LoginProvider user) {
        if(code != null){
            this.code = code;
        } else {
            this.code = "";
        }

        this.start = start;
        this.end = end;

        if (title.contains("Type : ")) {
            String[] typeSplit = title.split("Type : ");
            this.type = typeSplit.length > 1 ? typeSplit[1].split("\n")[0] : null;
        } else {
            this.type = null;
        }
        if (title.contains("Mémo : ")) {
            String[] memoSplit = title.split("Mémo : ");
            this.memo = memoSplit.length > 1 ? memoSplit[1] : null;
        } else {
            this.memo = null;
        }

        if (title.contains("Enseignant : ")) {
            String[] teacherSplit = title.split("Enseignant : ");
            if (teacherSplit.length > 1 && TeacherCollection.isIsInitialized()) {
                teacher = TeacherCollection.getInstance().getTeacherLike(teacherSplit[1].split("\n")[0]).stream().findFirst().orElse(null);
            } else {
                teacher = null;
            }
        } else {
            teacher = null;
        }

        if (title.contains("Salle : ")) {
            String[] classroomSplit = title.split("Salle : ");
            if (classroomSplit.length > 1 && ClassroomCollection.isIsInitialized()) {
                classroom = ClassroomCollection.getInstance().getClassroomLike(classroomSplit[1].split("\n")[0]).stream().findFirst().orElse(null);
            } else {
                classroom = null;
            }
        } else {
            classroom = null;
        }

        this.favori = favori;
    }

    /**
     * Gets the code associated with the event.
     *
     * @return The code of the event.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the start time of the event.
     *
     * @return The start time of the event.
     */
    public String getStart() {
        return start;
    }

    /**
     * Gets the end time of the event.
     *
     * @return The end time of the event.
     */
    public String getEnd() {
        return end;
    }

    /**
     * Gets the type of the event.
     *
     * @return The type of the event.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the memo associated with the event.
     *
     * @return The memo of the event.
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Gets the favorite status of the event.
     *
     * @return The favorite status of the event.
     */
    public String getFavori() {
        return favori;
    }

    /**
     * Gets the teacher associated with the event.
     *
     * @return The teacher of the event.
     */
    public Teacher getTeacher() {
        if(teacher == null){
            return new Teacher("Inconnu", "Inconnu", "Inconnu", "Inconnu");
        }
        return teacher;
    }

    /**
     * Gets the classroom associated with the event.
     *
     * @return The classroom of the event.
     */
    public Classroom getClassroom() {
        if(classroom == null){
            return new Classroom("Inconnu", "Inconnu", "Inconnu");
        }
        return classroom;
    }

    /**
     * Returns a string representation of the Event object.
     *
     * @return A string representation of the Event object.
     */
    @Override
    public String toString() {
        return "Event{" +
                "code='" + code + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", type='" + type + '\'' +
                ", memo='" + memo + '\'' +
                ", favori='" + favori + '\'' +
                ", teacher=" + teacher +
                ", classroom=" + classroom +
                '}';
    }
}
