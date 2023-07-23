package com.example.navigatorappandroid.retrofit.response;
import java.util.List;
import lombok.Data;

@Data
public class JobListResponse {

    private long jobCount;
    private List<Job> jobs;
}
