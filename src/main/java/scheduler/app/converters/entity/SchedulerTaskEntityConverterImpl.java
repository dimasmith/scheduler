package scheduler.app.converters.entity;

import org.springframework.stereotype.Service;
import scheduler.app.entities.RemoteJobEntity;
import scheduler.app.entities.SchedulerTaskEntry;
import scheduler.app.entities.UserEntry;
import scheduler.app.models.SchedulerTask;
import scheduler.app.repositories.UserRepository;

import javax.inject.Inject;

@Service
public class SchedulerTaskEntityConverterImpl implements SchedulerTaskEntityConverter {

	@Inject
	private UserEntityConverter userEntityConverter;

	@Inject
	private UserRepository userRepository;

	@Inject
	private RemoteJobEntityConverter remoteJobEntityConverter;

	@Override
	public void populateEntity(final SchedulerTaskEntry entity, final SchedulerTask model) {
		entity.setId(model.getId());
		entity.setUser(getUser(model.getId()));
		entity.setSchedulerTaskType(model.getSchedulerTaskType());
		entity.setTaskName(model.getTaskName());
		entity.setTaskDescription(model.getTaskDescription());
		entity.setTaskParametersJSON(model.getTaskParametersJSON());

		final RemoteJobEntity remoteJob = entity.getRemoteJob();
		remoteJobEntityConverter.populateEntity(remoteJob, model.getRemoteJob());
		entity.setRemoteJob(remoteJob);
	}

	@Override
	public SchedulerTask toModel(final SchedulerTaskEntry entity) {
		SchedulerTask model = new SchedulerTask();
		model.setId(entity.getId());
		model.setUser(userEntityConverter.toModel(entity.getUser()));
		model.setSchedulerTaskType(entity.getSchedulerTaskType());
		model.setTaskName(entity.getTaskName());
		model.setTaskDescription(entity.getTaskDescription());
		model.setTaskParametersJSON(entity.getTaskParametersJSON());
		model.setRemoteJob(remoteJobEntityConverter.toModel(entity.getRemoteJob()));
		return model;
	}

	private UserEntry getUser(final Long userId) {
		return userRepository.findById(userId);
	}
}
