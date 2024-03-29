package com.panda.project.dubboService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.panda.common.support.Convert;
import com.panda.common.utils.security.ShiroUtils;
import com.panda.project.dubboService.INoticeDubboService;
import com.panda.project.system.notice.domain.Notice;
import com.panda.project.system.notice.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 公告 服务层实现
 * 
 * @author panda
 * @date 2018-06-25
 */
@Service(version = "0.0.0",timeout = 30000)
public class NoticeDubboServiceImpl implements INoticeDubboService
{
    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public Notice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<Notice> selectNoticeList(Notice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(Notice notice)
    {
        notice.setCreateBy(ShiroUtils.getLoginName());
        return noticeMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(Notice notice)
    {
        notice.setUpdateBy(ShiroUtils.getLoginName());
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String ids)
    {
        return noticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
    }
}
