package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Activity;
import wld.accelerate.pipelinec.java.model.ActivityModel;
import wld.accelerate.pipelinec.java.repository.ActivityRepository;
import java.util.List;
@Service
public class ActivityService {
	@Autowired
	public ActivityRepository activityRepository;


	public Activity findById(Integer id) {
		return activityRepository.findById(id).orElseThrow();
	}
	public List<Activity> findAll() {
		return activityRepository.findAll();
	}
	public Activity createActivity(ActivityModel activityModel){
		Activity activity = ActivityModel.toActivity(activityModel);
		return activityRepository.save(activity);
	}
	public Activity updateActivity(Integer id, ActivityModel activityModel){
		Activity activity = findById(id);
		activity.setName(activity.getName());
		return activityRepository.save(activity);
	}
}