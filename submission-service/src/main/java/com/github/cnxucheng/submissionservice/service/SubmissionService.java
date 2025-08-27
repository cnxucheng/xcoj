package com.github.cnxucheng.submissionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.xcojModel.dto.submision.SubmissionQueryDTO;
import com.github.cnxucheng.xcojModel.entity.Submission;
import com.github.cnxucheng.xcojModel.vo.JudgeResponse;
import com.github.cnxucheng.xcojModel.vo.SubmissionVO;

/**
 * SubmissionService
 * @author : xucheng
 * @since : 2025-7-8
 */
public interface SubmissionService extends IService<Submission> {

    LambdaQueryWrapper<Submission> getQueryWrapper(SubmissionQueryDTO submissionQueryDTO);

    MyPage<SubmissionVO> getMyPage(Page<Submission> page);

    void updateSubmissionJudgeInfo(Long submissionId, JudgeResponse response);

    void updateToRunning(Long submissionId);
}
