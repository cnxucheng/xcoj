package com.github.cnxucheng.xcojfeignclient.service;

import com.github.cnxucheng.xcojModel.entity.UserStatus;
import com.github.cnxucheng.xcojModel.enums.UserProblemStatusEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "xcoj-backend-userProblemStatus-service", path = "/api/userProblemStatus/inner")
public interface UserProblemStatusFeignClient {

    @GetMapping("/getList")
    List<Long> getUserStatusList(@RequestParam("userId") Long userId, @RequestParam("status") Integer status);

    @GetMapping("/getStatus")
    UserProblemStatusEnum getUserProblemStatus(@RequestParam("userId") Long userId,
                                               @RequestParam("problemId") Long problemId);

    @PostMapping("/update")
    void updateStatus(@RequestParam("userId") Long userId,
                      @RequestParam("problemId") Long problemId,
                      @RequestParam("status") int status);

    @PostMapping("/save")
    void save(@RequestBody UserStatus userStatus);
}
