package com.github.cnxucheng.xcojfeignclient.service;

import com.github.cnxucheng.xcojModel.entity.Problem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "xcoj-backend-problem-service", path = "/api/problem/inner/")
public interface ProblemFeignClient {

    @PostMapping("/update")
    void updateStatistics(@RequestParam("problemId") Long problemId, @RequestParam("status") Integer status);

    @PostMapping("/getById")
    Problem getById(@RequestParam("problemId") Long problemId);
}
