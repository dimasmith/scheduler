package scheduler.app.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import scheduler.app.models.RemoteJob;
import scheduler.app.models.SchedulerTaskType;

@Getter
@Setter
@EqualsAndHashCode
public class SchedulerTaskDTO implements DTO {
	private Long taskId;
	private UserDto user;
	private SchedulerTaskType taskType;
	private String taskName;
	private String taskDescription;
	private String taskParametersJSON;
	private RemoteJob remoteJob;

	@Override
	public String toString() {
		return String.format("#%d: %s (%s)", taskId, taskName, taskType);
	}
}
