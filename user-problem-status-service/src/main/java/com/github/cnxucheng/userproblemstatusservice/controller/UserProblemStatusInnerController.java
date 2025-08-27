package com.github.cnxucheng.userproblemstatusservice.controller;

import com.github.cnxucheng.userproblemstatusservice.service.UserStatusService;
import com.github.cnxucheng.xcojModel.entity.UserStatus;
import com.github.cnxucheng.xcojModel.enums.UserProblemStatusEnum;
import com.github.cnxucheng.xcojfeignclient.service.UserProblemStatusFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/inner")
public class UserProblemStatusInnerController implements UserProblemStatusFeignClient {

    @Resource
    UserStatusService userStatusService;

    @GetMapping("/getList")
    @Override
    public List<Long> getUserStatusList(@RequestParam Long userId, @RequestParam Integer status) {
        return userStatusService.getUserStatusList(userId, status);
    }

    @GetMapping("/getStatus")
    @Override
    public UserProblemStatusEnum getUserProblemStatus(@RequestParam Long userId, @RequestParam Long problemId) {
        return userStatusService.getUserProblemStatus(userId, problemId);
    }

    @PostMapping("/update")
    @Override
    public void updateStatus(@RequestParam Long userId, @RequestParam Long problemId, @RequestParam int status) {
        userStatusService.updateStatus(userId, problemId, status);
    }

    @PostMapping("/save")
    @Override
    public void save(@RequestBody UserStatus userStatus) {
        userStatusService.save(userStatus);
    }
}
