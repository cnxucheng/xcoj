package com.github.cnxucheng.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.cnxucheng.userservice.service.UserService;
import com.github.cnxucheng.userservice.service.UserStatusService;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.entity.UserStatus;
import com.github.cnxucheng.xcojModel.enums.UserProblemStatusEnum;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    @Resource
    private UserStatusService userStatusService;

    @PostMapping("/update/statistics")
    @Override
    public void updateStatistics(@RequestParam Long userId, @RequestParam Integer isAc) {
        userService.updateStatistics(userId, isAc);
    }

    @GetMapping("/getByUserId")
    @Override
    public User getById(@RequestParam Long userId) {
        return userService.getById(userId);
    }

    @GetMapping("/getByUsername")
    @Override
    public User getByUsername(@RequestParam String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userService.getOne(queryWrapper, false);
    }

    @GetMapping("/status/list")
    @Override
    public List<Long> getUserStatusList(@RequestParam Long userId, @RequestParam Integer status) {
        return userStatusService.getUserStatusList(userId, status);
    }

    @GetMapping("/status/get")
    @Override
    public UserProblemStatusEnum getUserProblemStatus(@RequestParam Long userId, @RequestParam Long problemId) {
        return userStatusService.getUserProblemStatus(userId, problemId);
    }

    @PostMapping("/status/update")
    @Override
    public void updateStatus(@RequestParam Long userId, @RequestParam Long problemId, @RequestParam int status) {
        userStatusService.updateStatus(userId, problemId, status);
    }

    @PostMapping("/status/save")
    @Override
    public void save(@RequestBody UserStatus userStatus) {
        userStatusService.save(userStatus);
    }
}
