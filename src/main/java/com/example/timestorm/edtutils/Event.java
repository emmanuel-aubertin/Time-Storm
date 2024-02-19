package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;

public class Event {
    /*
                "code": null,
                        "start": "2023-09-14T08:00:00+00:00",
                        "end": "2023-09-14T10:00:00+00:00",
                        "title": "Matière : PREPARATION AU PROJET PROFESS\nPromotion : M1 HYDROGEOLOGIE, SOL ET ENVIRONNEMENT (HSE)\nSalle : A016 V ( GT )\nType : CM/TD\nMémo : Maison de l'eau\n",
                        "type": "CM",
                        "memo": null,
                        "favori": ""*/
    private String code;
    private String start;
    private String end;
    private String type;
    private String memo;
    private String favori;
    private Teacher teacher;
    private Classroom classroom;

    public Event(String code, String start, String title, String end, String type, String memo, String favori, LoginProvider user) {
        this.code = code;
        this.start = start;
        this.end = end;
        this.type = type;
        this.memo = memo;
        this.favori = favori;
        TeacherCollection teachers = new TeacherCollection(user);
        teacher = teachers.getTeacherLike(title.split("Enseignant : ")[1].split("\n")[0]).get(0);
        ClassroomCollection classrooms = new ClassroomCollection(user);
        classroom = classrooms.getClassroomLike(title.split("Salle : ")[1].split("\n")[0]).get(0);

    }
}
