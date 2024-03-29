package com.panda.project.dubboService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.panda.common.constant.ScheduleConstants;
import com.panda.common.support.Convert;
import com.panda.common.utils.security.ShiroUtils;
import com.panda.project.monitor.job.domain.Job;
import com.panda.project.monitor.job.mapper.JobMapper;
import com.panda.project.monitor.job.util.CronUtils;
import com.panda.project.monitor.job.util.ScheduleUtils;
import com.panda.project.dubboService.IJobDubboService;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时任务调度信息 服务层
 * 
 * @author panda
 */
@Service(version = "0.0.0",timeout = 30000)
public class JobDubboServiceImpl implements IJobDubboService
{
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init()
    {
        List<Job> jobList = jobMapper.selectJobAll();
        for (Job job : jobList)
        {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, job.getJobId());
            // 如果不存在，则创建
            if (cronTrigger == null)
            {
                ScheduleUtils.createScheduleJob(scheduler, job);
            }
            else
            {
                ScheduleUtils.updateScheduleJob(scheduler, job);
            }
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     * 
     * @param job 调度信息
     * @return
     */
    @Override
    public List<Job> selectJobList(Job job)
    {
        return jobMapper.selectJobList(job);
    }

    /**
     * 通过调度任务ID查询调度信息
     * 
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public Job selectJobById(Long jobId)
    {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * 暂停任务
     * 
     * @param job 调度信息
     */
    @Override
    public int pauseJob(Job job)
    {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        job.setUpdateBy(ShiroUtils.getLoginName());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            ScheduleUtils.pauseJob(scheduler, job.getJobId());
        }
        return rows;
    }

    /**
     * 恢复任务
     * 
     * @param job 调度信息
     */
    @Override
    public int resumeJob(Job job)
    {
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        job.setUpdateBy(ShiroUtils.getLoginName());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            ScheduleUtils.resumeJob(scheduler, job.getJobId());
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param job 调度信息
     */
    @Override
    public int deleteJob(Job job)
    {
        int rows = jobMapper.deleteJobById(job);
        if (rows > 0)
        {
            ScheduleUtils.deleteScheduleJob(scheduler, job.getJobId());
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public void deleteJobByIds(String ids)
    {
        Long[] jobIds = Convert.toLongArray(ids);
        for (Long jobId : jobIds)
        {
            Job job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    /**
     * 任务调度状态修改
     * 
     * @param job 调度信息
     */
    @Override
    public int changeStatus(Job job)
    {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status))
        {
            rows = resumeJob(job);
        }
        else if (ScheduleConstants.Status.PAUSE.getValue().equals(status))
        {
            rows = pauseJob(job);
        }
        return rows;
    }

    /**
     * 立即运行任务
     * 
     * @param job 调度信息
     */
    @Override
    public int run(Job job)
    {
        return ScheduleUtils.run(scheduler, selectJobById(job.getJobId()));
    }

    /**
     * 新增任务
     * 
     * @param job 调度信息 调度信息
     */
    @Override
    public int insertJobCron(Job job)
    {
        job.setCreateBy(ShiroUtils.getLoginName());
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0)
        {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }

    /**
     * 更新任务的时间表达式
     * 
     * @param job 调度信息
     */
    @Override
    public int updateJobCron(Job job)
    {
        job.setUpdateBy(ShiroUtils.getLoginName());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            ScheduleUtils.updateScheduleJob(scheduler, job);
        }
        return rows;
    }
    
    /**
     * 校验cron表达式是否有效
     * 
     * @param cronExpression 表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression)
    {
        return CronUtils.isValid(cronExpression);
    }
}
