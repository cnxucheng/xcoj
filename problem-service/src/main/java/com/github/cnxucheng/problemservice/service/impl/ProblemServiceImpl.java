package com.github.cnxucheng.problemservice.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.problemservice.mapper.ProblemMapper;
import com.github.cnxucheng.xcojModel.dto.problem.ProblemQueryDTO;
import com.github.cnxucheng.xcojModel.entity.Problem;
import com.github.cnxucheng.xcojModel.entity.TestCase;
import com.github.cnxucheng.xcojModel.vo.ProblemSampleVO;
import com.github.cnxucheng.xcojModel.vo.ProblemVO;
import com.github.cnxucheng.problemservice.service.ProblemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProblemServiceImpl
 * @author : xucheng
 * @since : 2025-7-8
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem>
    implements ProblemService {

    @Override
    public void validProblem(Problem problem) {
        if (problem == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = problem.getTitle();
        String content = problem.getContent();
        String solution = problem.getSolution();
        String judgeCase = problem.getJudgeCase();
        String tags = problem.getTags();
        if (title != null && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (content != null && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目内容过长");
        }
        if (solution != null && solution.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题解过长");
        }
        if (judgeCase != null && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测试用例过大");
        }
        if (tags.length() > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签过多或过长");
        }
    }

    @Override
    public QueryWrapper<Problem> getQueryWrapper(ProblemQueryDTO problemQueryDTO, Integer isAdmin) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        if (problemQueryDTO == null) {
            return queryWrapper;
        }
        Long problemId = problemQueryDTO.getProblemId();
        String title = problemQueryDTO.getTitle();
        String content = problemQueryDTO.getContent();
        List<String> tags = problemQueryDTO.getTags();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.eq(ObjectUtils.isNotEmpty(problemId), "problemId", problemId);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        if (isAdmin == 0) {
            queryWrapper.eq("isHidden", 0);
        }
        queryWrapper.eq("isDelete", 0);
        return queryWrapper;
    }

    @Override
    public void updateStatistics(Long problemId, Integer isAc) {
        Problem problem = this.getById(problemId);
        LambdaUpdateWrapper<Problem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Problem::getProblemId, problemId);
        updateWrapper.set(Problem::getSubmitNum, problem.getSubmitNum() + 1);
        if (isAc == 1) {
            updateWrapper.set(Problem::getAcceptedNum, problem.getAcceptedNum() + 1);
        }
        this.update(updateWrapper);
    }

    @Override
    public ProblemSampleVO getProblemSampleVO(Problem problem) {
        return ProblemSampleVO.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .acceptedNum(problem.getAcceptedNum())
                .submitNum(problem.getSubmitNum())
                .tags(JSONUtil.toList(problem.getTags(), String.class))
                .build();
    }

    @Override
    public ProblemVO getProblemVO(Problem problem) {
        ProblemVO problemVO = new ProblemVO();
        BeanUtils.copyProperties(problem, problemVO);
        List<TestCase> testCases = JSONUtil.toList(problem.getJudgeCase(), TestCase.class);
        problemVO.setJudgeCase(testCases);
        problemVO.setTags(JSONUtil.toList(problem.getTags(), String.class));
        return problemVO;
    }

    @Override
    public MyPage<ProblemSampleVO> getProblemSampleVOPage(Page<Problem> problemPage) {
        List<Problem> records = problemPage.getRecords();
        long total = problemPage.getTotal();
        long size = problemPage.getSize();
        long current = problemPage.getCurrent();

        MyPage<ProblemSampleVO> result = new MyPage<>();
        result.setPageSize((int) size);
        result.setCurrent(current);
        result.setTotal(total);

        List<ProblemSampleVO> data = records.stream().map(this::getProblemSampleVO).collect(Collectors.toList());
        result.setData(data);
        return result;
    }
}




