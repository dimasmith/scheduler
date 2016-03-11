package scheduler.app.controllers.rest.scheduler.tasks.list;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scheduler.app.converters.dto.SchedulerTaskDtoConverter;
import scheduler.app.dto.IdDto;
import scheduler.app.dto.SchedulerTaskDto;
import scheduler.app.services.tasks.SchedulerTaskService;
import scheduler.app.services.users.UserService;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/tasks")
public class TaskListRestController {

	@Inject
	private UserService userService;

	@Inject
	private SchedulerTaskService schedulerTaskService;

	@Inject
	private SchedulerTaskDtoConverter schedulerTaskDtoConverter;

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public List<SchedulerTaskDto> tasks(final Principal principal) {
		return schedulerTaskDtoConverter.toDtos(schedulerTaskService.loadAll(currentUserId(principal)));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ids/")
	public List<IdDto> taskIds(final Principal principal) {
		return schedulerTaskService.loadAll(currentUserId(principal))
				.stream()
				.map(schedulerTaskEntry -> new IdDto(schedulerTaskEntry.getId()))
				.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{taskId}/")
	public SchedulerTaskDto taskEntry(final @PathVariable Long taskId, final Principal principal) {
		return schedulerTaskDtoConverter.toDto(schedulerTaskService.load(currentUserId(principal), taskId));
	}

	private Long currentUserId(final Principal principal) {
		return userService.findByLogin(principal.getName()).getId();
	}
}
