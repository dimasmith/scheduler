package scheduler.app.services.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.stereotype.Service;
import scheduler.app.models.SchedulerTask;
import scheduler.app.services.tasks.SchedulerTaskService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchedulerTaskInitializationServiceImpl implements SchedulerTaskInitializationService {

    private static final String CRON = "0 0/1 * * * ?"; // each minute

    @Inject
    private SchedulerTaskService schedulerTaskService;
    ;

    @Override
    public List<Trigger> buildSchedulerJobTriggers() throws SchedulerException {

        List<SchedulerTask> schedulerTasks = schedulerTaskService.loadAll();

        return schedulerTasks.stream()
                .map(schedulerTask -> {
                    String jobName = getJobName(schedulerTask);
                    String jobGroup = getJobGroup(schedulerTask);
                    String triggerName = getTriggerName(schedulerTask);
                    JobKey jobKey = new JobKey(jobName, jobGroup);
                    TriggerKey triggerKey = new TriggerKey(triggerName, jobGroup);

                    JobDetail job = JobBuilder.newJob(SchedulerJob.class)
                            .withIdentity(jobKey)
                            .build();

                    /*return TriggerBuilder
                            .newTrigger()
                            .withIdentity(triggerKey)
                            .withSchedule(CronScheduleBuilder.cronSchedule(CRON))
                            .build();*/
                    return cronTriggerFactoryBean(job, triggerName, jobGroup, CRON).getObject();

                }).collect(Collectors.toList());
    }

    private CronTriggerFactoryBean cronTriggerFactoryBean(final JobDetail job, final String triggerName, final String group, final String crone) {
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(job);
        stFactory.setName(triggerName);
        stFactory.setGroup(group);
        stFactory.setCronExpression(crone);
        return stFactory;
    }

    private String getJobName(final SchedulerTask schedulerTask) {
        return String.format("QUARTZ_JOB_%d_%d", schedulerTask.getUser().getId(), schedulerTask.getId());
    }

    private String getJobGroup(final SchedulerTask schedulerTask) {
        return String.format("QUARTZ_JOB_GROUP_%d", schedulerTask.getUser().getId());
    }

    private String getTriggerName(final SchedulerTask schedulerTask) {
        return String.format("QUARTZ_TRIGGER_%d_%d", schedulerTask.getUser().getId(), schedulerTask.getId());
    }
}
