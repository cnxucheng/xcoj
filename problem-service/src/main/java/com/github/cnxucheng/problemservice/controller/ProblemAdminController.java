package com.github.cnxucheng.problemservice.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.cnxucheng.common.common.Result;
import com.github.cnxucheng.problemservice.service.ProblemService;
import com.github.cnxucheng.xcojModel.dto.problem.ProblemAddDTO;
import com.github.cnxucheng.xcojModel.dto.problem.ProblemUpdateDTO;
import com.github.cnxucheng.xcojModel.entity.Problem;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
@Slf4j
public class ProblemAdminController {

    @Resource
    private ProblemService problemService;

    @Resource
    private UserFeignClient userFeignClient;

    @PostMapping("/add")
    public Result<?> add(@RequestBody ProblemAddDTO problemAddDTO, HttpServletRequest request) {
        Problem problem = new Problem();
        BeanUtil.copyProperties(problemAddDTO, problem);
//        Long userId = userFeignClient.getLoginUser(request).getUserId();
        problem.setUserId(1L);
        problem.setTags(JSONUtil.toJsonStr(problemAddDTO.getTags()));
        String testCaseString = JSONUtil.toJsonStr(problemAddDTO.getJudgeCase());
        problem.setJudgeCase(testCaseString);
        problemService.save(problem);
        return Result.success("ok");
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody ProblemUpdateDTO problemUpdateDTO) {
        Problem problem = new Problem();
        BeanUtil.copyProperties(problemUpdateDTO, problem);
        String testCaseString = JSONUtil.toJsonStr(problemUpdateDTO.getJudgeCase());
        problem.setJudgeCase(testCaseString);
        problem.setTags(JSONUtil.toJsonStr(problemUpdateDTO.getTags()));
        LambdaUpdateWrapper<Problem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Problem::getProblemId, problem.getProblemId())
                .set(Problem::getTitle, problem.getTitle())
                .set(Problem::getTags, problem.getTags())
                .set(Problem::getContent, problem.getContent())
                .set(Problem::getSolution, problem.getSolution())
                .set(Problem::getIsHidden, problem.getIsHidden())
                .set(Problem::getTimeLimit, problem.getTimeLimit())
                .set(Problem::getMemoryLimit, problem.getMemoryLimit())
                .set(Problem::getJudgeCase, problem.getJudgeCase());
        problemService.update(wrapper);
        return Result.success("ok");
    }
}
