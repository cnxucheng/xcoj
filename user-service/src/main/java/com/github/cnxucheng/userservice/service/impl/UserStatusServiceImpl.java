package com.github.cnxucheng.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cnxucheng.userservice.mapper.UserStatusMapper;
import com.github.cnxucheng.userservice.service.UserStatusService;
import com.github.cnxucheng.xcojModel.entity.UserStatus;
import com.github.cnxucheng.xcojModel.enums.UserProblemStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserAcceptServiceImpl
 * @author : xucheng
 * @since : 2025-7-8
 */
@Service
public class UserStatusServiceImpl extends ServiceImpl<UserStatusMapper, UserStatus>
    implements UserStatusService {

    @Override
    public List<Long> getUserStatusList(Long userId, Integer status) {
        LambdaQueryWrapper<UserStatus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserStatus::getUserId, userId)
                .eq(UserStatus::getIsAc, status)
                .select(UserStatus::getProblemId);
        return this.list(wrapper).stream()
                .map(UserStatus::getProblemId)
                .collect(Collectors.toList());
    }

    @Override
    public UserProblemStatusEnum getUserProblemStatus(Long userId, Long problemId) {
        LambdaQueryWrapper<UserStatus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserStatus::getUserId, userId)
                .eq(UserStatus::getProblemId, problemId);
        UserStatus userStatus = this.getOne(wrapper);
        if (userStatus == null) {
            return UserProblemStatusEnum.NO_SUBMIT;
        }
        if (userStatus.getIsAc() == 1) {
            return UserProblemStatusEnum.AC;
        }
        return UserProblemStatusEnum.NOT_AC;
    }

    @Override
    public void updateStatus(Long userId, Long problemId, int status) {
        LambdaUpdateWrapper<UserStatus> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserStatus::getUserId, userId)
                .eq(UserStatus::getProblemId, problemId)
                .set(UserStatus::getIsAc, status);
        this.update(updateWrapper);
    }
}




