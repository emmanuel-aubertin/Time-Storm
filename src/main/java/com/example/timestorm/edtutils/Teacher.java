package com.example.timestorm.edtutils;

public class Teacher {
    private String FirstName;
    private String LastName;
    private String Code;
    private String UapvHR;
    private String SearchString;

    public Teacher(String name, String code, String uapvHR, String searchString) {
        String[] nameParts = name.split(" ");
        FirstName = nameParts[0];
        LastName = nameParts.length > 1 ? nameParts[1] : "";
        Code = code;
        UapvHR = uapvHR;
        SearchString = searchString.toUpperCase();
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getCode() {
        return Code;
    }

    public String getUapvHR() {
        return UapvHR;
    }

    public String getSearchString() {
        return SearchString;
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Code='" + Code + '\'' +
                ", UapvHR='" + UapvHR + '\'' +
                ", SearchString='" + SearchString + '\'' +
                '}';
    }
}


