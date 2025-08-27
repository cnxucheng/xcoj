package com.github.cnxucheng.problemservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.xcojModel.dto.problem.ProblemQueryDTO;
import com.github.cnxucheng.xcojModel.entity.Problem;
import com.github.cnxucheng.xcojModel.vo.ProblemSampleVO;
import com.github.cnxucheng.xcojModel.vo.ProblemVO;

/**
 * ProblemService
 * @author : xucheng
 * @since : 2025-7-8
 */
public interface ProblemService extends IService<Problem> {

    void validProblem(Problem problem);

    QueryWrapper<Problem> getQueryWrapper(ProblemQueryDTO problemQueryDTO, Integer isAdmin);

    void updateStatistics(Long problemId, Integer status);

    ProblemSampleVO getProblemSampleVO(Problem problem);

    ProblemVO getProblemVO(Problem problem);

    MyPage<ProblemSampleVO> getProblemSampleVOPage(Page<Problem> problemPage);
}
