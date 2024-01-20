package kreadcn.homework.service;

import kreadcn.homework.model.AllocSchedule;

import java.util.Map;

public interface CourseOverviewService {
    //Key:labId, Value: AllocSchedule(Details)
    Map<Integer, AllocSchedule> getDetails();
}
