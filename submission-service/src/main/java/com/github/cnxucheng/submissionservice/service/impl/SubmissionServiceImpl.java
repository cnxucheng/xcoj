package com.github.cnxucheng.submissionservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.submissionservice.mapper.SubmissionMapper;
import com.github.cnxucheng.xcojModel.dto.submision.SubmissionQueryDTO;
import com.github.cnxucheng.xcojModel.entity.Submission;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import com.github.cnxucheng.xcojModel.vo.SubmissionVO;
import com.github.cnxucheng.submissionservice.service.SubmissionService;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * SubmissionServiceImpl
 * @author : xucheng
 * @since : 2025-7-8
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission>
    implements SubmissionService {

    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public LambdaQueryWrapper<Submission> getQueryWrapper(SubmissionQueryDTO submissionQueryDTO) {
        LambdaQueryWrapper<Submission> queryWrapper = new LambdaQueryWrapper<>();
        if (submissionQueryDTO == null) {
            return queryWrapper;
        }
        Long submissionId = submissionQueryDTO.getSubmissionId();
        String username = submissionQueryDTO.getUsername();
        if (username != null) {
            User user = userFeignClient.getByUsername(username);
            Long userId = user.getUserId();
            queryWrapper.eq(Submission::getUserId, userId);
        }
        Long problemId = submissionQueryDTO.getProblemId();
        String lang = submissionQueryDTO.getLang();
        String judgeResult = submissionQueryDTO.getJudgeResult();
        if (submissionId != null) {
            queryWrapper.eq(Submission::getSubmissionId, submissionId);
        }
        if (problemId != null) {
            queryWrapper.eq(Submission::getProblemId, problemId);
        }
        if (StringUtils.isNotBlank(lang)) {
            queryWrapper.eq(Submission::getLang, lang);
        }
        if (StringUtils.isNotBlank(judgeResult)) {
            queryWrapper.eq(Submission::getJudgeResult, judgeResult);
        }
        queryWrapper.orderByDesc(Submission::getSubmissionId);
        return queryWrapper;
    }

    @Override
    public MyPage<SubmissionVO> getMyPage(Page<Submission> page) {
        MyPage<SubmissionVO> myPage = new MyPage<>();
        List<SubmissionVO> data = new ArrayList<>();
        for (Submission submission : page.getRecords()) {
            SubmissionVO submissionVO = new SubmissionVO();
            BeanUtil.copyProperties(submission, submissionVO);
            Long userId = submission.getUserId();
            submissionVO.setUsername(userFeignClient.getById(userId).getUsername());
            submissionVO.setCreateTime(submission.getCreateTime());
            data.add(submissionVO);
        }
        myPage.setData(data);
        myPage.setCurrent(page.getCurrent());
        myPage.setPageSize((int) page.getSize());
        myPage.setTotal(page.getTotal());
        myPage.setTotalPages(page.getPages());
        return myPage;
    }

    @Override
    public void updateSubmissionJudgeInfo(Long submissionId, JudgeResponse response) {
        LambdaUpdateWrapper<Submission> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Submission::getSubmissionId, submissionId);
        wrapper.set(Submission::getJudgeResult, response.getMessage());
        wrapper.set(Submission::getUsedTime, response.getUsedTime());
        wrapper.set(Submission::getUsedMemory, response.getUsedMemory());
        this.update(wrapper);
    }

    @Override
    public void updateToRunning(Long submissionId) {
        LambdaUpdateWrapper<Submission> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Submission::getSubmissionId, submissionId);
        wrapper.set(Submission::getJudgeResult, "Running");
        this.update(wrapper);
    }
}




