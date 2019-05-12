package com.panda.project.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.panda.common.support.Convert;
import com.panda.project.monitor.operlog.domain.OperLog;
import com.panda.project.monitor.operlog.mapper.OperLogMapper;
import com.panda.project.monitor.operlog.service.IOperLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 操作日志 服务层处理
 * 
 * @author panda
 */
@Service(version = "0.0.0",timeout = 30000)
public class OperLogDubboServiceImpl implements IOperLogService
{
    @Autowired
    private OperLogMapper operLogMapper;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(OperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<OperLog> selectOperLogList(OperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int deleteOperLogByIds(String ids)
    {
        return operLogMapper.deleteOperLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public OperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }
    
    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}