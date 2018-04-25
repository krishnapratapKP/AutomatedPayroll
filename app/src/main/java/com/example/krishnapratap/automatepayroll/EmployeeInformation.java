package com.example.krishnapratap.automatepayroll;

public class EmployeeInformation {

    private String mEmployeeName;
    private String mEmployeeEmail;
    private String mEmployeeImage;
    private int mEmployeeId;
    private long mEmployeeMobile;

    public EmployeeInformation(int EmployeeId, String EmployeeName, String EmployeeEamil, String EmployeeImage, long EmployeeMobile) {
        mEmployeeName = EmployeeName;
        mEmployeeId = EmployeeId;
        mEmployeeEmail = EmployeeEamil;
        mEmployeeMobile = EmployeeMobile;
        mEmployeeImage = EmployeeImage;
    }

    public int getmEmployeeId() {
        return mEmployeeId;
    }

    public String getmEmployeeName() {
        return mEmployeeName;
    }

    public String getmEmployeeEmail() {
        return mEmployeeEmail;
    }

    public String getmEmployeeImage() {
        return mEmployeeImage;
    }

    public long getmEmployeeMobile() {
        return mEmployeeMobile;
    }
}
