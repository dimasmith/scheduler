package scheduler.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import scheduler.app.models.TaskEntry;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntry, Long> {

}
