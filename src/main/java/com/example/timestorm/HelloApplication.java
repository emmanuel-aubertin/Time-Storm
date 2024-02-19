package com.example.timestorm;


import com.example.timestorm.edtutils.Classroom;
import com.example.timestorm.edtutils.ClassroomCollection;
import com.example.timestorm.edtutils.Teacher;
import com.example.timestorm.edtutils.TeacherCollection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.exit;

public class HelloApplication extends Application {

    private static LoginProvider user;



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Time Storm");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        user = new LoginProvider();
        boolean isLogged = user.tryLogin("uapv2001785", "tozxAn-tekxut-4rerju");
        if(isLogged){
            System.out.println("User connected");
        } else {
            System.out.println("User not connected");
            exit(0);
        }

        System.out.println(user.getToken());

        TeacherCollection teacherCollection = new TeacherCollection(user);

        System.out.println("Looking for teacher :)");
        ArrayList<Teacher> findTeachers =  teacherCollection.getTeacherLike("juanchi");

        System.out.println("Here is all the teacher found: ");

        for (Teacher t: findTeachers
             ) {
            System.out.println(t.getSearchString());
            System.out.println(t.getName());
        }

        System.out.println(findTeachers.get(0).getTeacherEdt(user));

        ClassroomCollection classroomCollection = new ClassroomCollection(user);

        Classroom stat = classroomCollection.getClassroomLike("Stat 3").get(0);

        System.out.println(stat.getClassroomEdt(user));
        launch();
    }

}