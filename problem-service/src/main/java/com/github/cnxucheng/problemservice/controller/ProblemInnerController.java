package com.github.cnxucheng.problemservice.controller;

import com.github.cnxucheng.problemservice.service.ProblemService;
import com.github.cnxucheng.xcojModel.entity.Problem;
import com.github.cnxucheng.xcojfeignclient.service.ProblemFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class ProblemInnerController implements ProblemFeignClient {

    @Resource
    private ProblemService problemService;

    @PostMapping("/update")
    @Override
    public void updateStatistics(@RequestParam Long problemId, @RequestParam Integer status) {
        problemService.updateStatistics(problemId, status);
    }

    @PostMapping("/getById")
    @Override
    public Problem getById(@RequestParam Long problemId) {
        return problemService.getById(problemId);
    }
}
