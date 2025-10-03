package com.github.cnxucheng.xcojfeignclient.service;

import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.entity.UserStatus;
import com.github.cnxucheng.xcojModel.enums.UserProblemStatusEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "xcoj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    @GetMapping("/getLoginUser")
    User getLoginUser(@RequestParam("token") String token);

    @PostMapping("/update/statistics")
    void updateStatistics(@RequestParam("userId") Long userId, @RequestParam("isAc") Integer isAc);

    @GetMapping("/getByUserId")
    User getById(@RequestParam("userId") Long userId);

    @GetMapping("/getByUsername")
    User getByUsername(@RequestParam("username") String username);

    @GetMapping("/status/list")
    List<Long> getUserStatusList(@RequestParam("userId") Long userId, @RequestParam("status") Integer status);

    @GetMapping("/status/get")
    UserProblemStatusEnum getUserProblemStatus(@RequestParam("userId") Long userId,
                                               @RequestParam("problemId") Long problemId);

    @PostMapping("/status/update")
    void updateStatus(@RequestParam("userId") Long userId,
                      @RequestParam("problemId") Long problemId,
                      @RequestParam("status") int status);

    @PostMapping("/status/save")
    void save(@RequestBody UserStatus userStatus);
}
