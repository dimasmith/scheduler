package scheduler.app.converters.entity;

import com.google.inject.Inject;
import org.springframework.stereotype.Service;
import scheduler.app.repositories.UserRepository;
import scheduler.app.entities.SchedulerTaskEntry;
import scheduler.app.entities.UserEntry;
import scheduler.app.models.SchedulerTask;

@Service
public class SchedulerTaskEntityConverterImpl implements SchedulerTaskEntityConverter {

	@Inject
	private UserEntityConverter userEntityConverter;

	@Inject
	private UserRepository userRepository;

	@Override
	public void populateEntity(final SchedulerTaskEntry entity, final SchedulerTask model) {
		entity.setId(model.getId());
		entity.setUser(getUser(model.getId()));
		entity.setTaskName(model.getTaskName());
		entity.setTaskDescription(model.getTaskDescription());
	}

	@Override
	public SchedulerTask toModel(final SchedulerTaskEntry entity) {
		SchedulerTask model = new SchedulerTask();
		model.setId(entity.getId());
		model.setUser(userEntityConverter.toModel(getUser(model.getId())));
		model.setTaskName(entity.getTaskName());
		model.setTaskDescription(entity.getTaskDescription());
		return model;
	}

	private UserEntry getUser(final Long userId) {
		return userRepository.findById(userId);
	}
}