package com.panda.project.dubboService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.panda.common.support.Convert;
import com.panda.project.monitor.logininfor.domain.Logininfor;
import com.panda.project.monitor.logininfor.mapper.LogininforMapper;
import com.panda.project.dubboService.ILogininforDubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author panda
 */
@Service(version = "0.0.0",timeout = 30000)
public class LogininforDubboServiceImpl implements ILogininforDubboService
{
    @Autowired
    private LogininforMapper logininforMapper;

    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(Logininfor logininfor)
    {
        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<Logininfor> selectLogininforList(Logininfor logininfor)
    {
        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int deleteLogininforByIds(String ids)
    {
        return logininforMapper.deleteLogininforByIds(Convert.toStrArray(ids));
    }
    
    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
