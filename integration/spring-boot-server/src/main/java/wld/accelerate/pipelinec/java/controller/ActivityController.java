package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Activity;
import wld.accelerate.pipelinec.java.model.ActivityModel;
import wld.accelerate.pipelinec.java.service.ActivityService;
import java.util.List;

@RestController
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@GetMapping("/activity/{id}")
	public ResponseEntity<ActivityModel> getActivity(@PathVariable Integer id) {
		return ResponseEntity.ok(ActivityModel.fromActivity(activityService.findById(id)));
	}
	@GetMapping("/activity/")
	public ResponseEntity<List<ActivityModel>> getAllActivity() {
		return ResponseEntity.ok(activityService.findAll().stream().map(ActivityModel::fromActivity).toList());
	}
	@PostMapping("/activity/")
	public ResponseEntity<ActivityModel> saveActivity(ActivityModel activityModel) {
		Activity activity = activityService.createActivity(activityModel);
		return ResponseEntity.ok(ActivityModel.fromActivity(activity));
	}
	@PostMapping("/activity/{id}")
	public ResponseEntity<ActivityModel> updateActivity(@PathVariable Integer id, ActivityModel activityModel) {
		Activity activity = activityService.updateActivity(id, activityModel);
		return ResponseEntity.ok(ActivityModel.fromActivity(activity));
	}
}