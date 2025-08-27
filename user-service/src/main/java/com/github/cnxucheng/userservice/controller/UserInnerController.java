package com.github.cnxucheng.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.cnxucheng.userservice.service.UserService;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

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
}
