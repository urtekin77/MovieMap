package com.example.moviapp.Model;

import com.google.gson.annotations.SerializedName;

public class CrewModel {
    @SerializedName("id")
    private int crew_id;
    @SerializedName("known_for_department")
    private String department_crew;
    @SerializedName("name")
    private String crew_name;
    @SerializedName("profile_path")
    private String crew_profile_path;
    @SerializedName("department")
    private String department;
    @SerializedName("job")
    private String job;

    public int getCrew_id() {
        return crew_id;
    }

    public String getDepartment_crew() {
        return department_crew;
    }

    public String getCrew_name() {
        return crew_name;
    }

    public String getCrew_profile_path() {
        return crew_profile_path;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }
}
