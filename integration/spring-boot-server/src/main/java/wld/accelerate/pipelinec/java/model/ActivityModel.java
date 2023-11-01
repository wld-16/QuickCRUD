package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Activity;

public class ActivityModel {
	private Long id;
	private String name;
	public static ActivityModel fromActivity(Activity activity) {
		ActivityModel activityModel = new ActivityModel();
		activityModel.name = activity.getName();
		return activityModel;
	}
	public static Activity toActivity(ActivityModel activityModel){
		Activity activity = new Activity();
		activityModel.setName(activity.getName());
		return activity;
	}

	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}