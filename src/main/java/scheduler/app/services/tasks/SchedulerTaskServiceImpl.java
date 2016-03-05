package scheduler.app.services.tasks;

import org.springframework.stereotype.Service;
import scheduler.app.converters.entity.SchedulerTaskEntityConverter;
import scheduler.app.repositories.SchedulerTaskRepository;
import scheduler.app.entities.SchedulerTaskEntity;
import scheduler.app.models.SchedulerTask;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchedulerTaskServiceImpl implements SchedulerTaskService {

	@Inject
	private SchedulerTaskRepository schedulerTaskRepository;

	@Inject
	private SchedulerTaskEntityConverter schedulerTaskEntityConverter;

	@Override
	public List<SchedulerTask> loadAll() {
		return schedulerTaskRepository.findAll().stream()
				.map(schedulerTaskEntityConverter::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public SchedulerTask load(final long taskId) {
		return schedulerTaskEntityConverter.toModel(schedulerTaskRepository.findOne(taskId));
	}

	@Override
	public SchedulerTask add(final SchedulerTask schedulerTask) {
		return populateAndSave(new SchedulerTaskEntity(), schedulerTask);
	}

	@Override
	public SchedulerTask save(final SchedulerTask schedulerTask) {
		return populateAndSave(schedulerTaskRepository.findById(schedulerTask.getId()), schedulerTask);
	}

	@Override
	public void delete(final long taskId) {
		schedulerTaskRepository.delete(taskId);
	}

	private SchedulerTask populateAndSave(final SchedulerTaskEntity taskEntity, final SchedulerTask schedulerTask) {
		schedulerTaskEntityConverter.populateEntity(taskEntity, schedulerTask);
		SchedulerTaskEntity savedEntity = schedulerTaskRepository.saveAndFlush(taskEntity);
		return schedulerTaskEntityConverter.toModel(savedEntity);
	}
}
