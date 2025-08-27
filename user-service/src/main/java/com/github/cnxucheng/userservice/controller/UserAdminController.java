package com.github.cnxucheng.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.common.Result;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.xcojModel.dto.user.UserQueryDTO;
import com.github.cnxucheng.xcojModel.dto.user.UserUpdateDTO;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.enums.UserRoleEnum;
import com.github.cnxucheng.userservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class UserAdminController {

    @Resource
    private UserService userService;

    @PostMapping("/list")
    public Result<?> getUserList(@RequestBody UserQueryDTO pageRequest, HttpServletRequest request) {
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        if  (pageSize < 0 || current < 0 || pageSize > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不合法请求");
        }
        String userRole = userService.getLoginUser(request).getUserRole();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(User::getUserRole, "root");
        if (UserRoleEnum.getEnum(userRole) == UserRoleEnum.ADMIN) {
            queryWrapper.ne(User::getUserRole, "admin");
        }
        queryWrapper.eq(pageRequest.getUserId() != null, User::getUserId, pageRequest.getUserId());
        queryWrapper.eq(pageRequest.getUsername() != null && !pageRequest.getUsername().isEmpty(), User::getUsername, pageRequest.getUsername());
        Page<User> page = new Page<>(current, pageSize);
        Page<User> result = userService.page(page, queryWrapper);
        if (result.getRecords().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有此数据");
        }
        return Result.success(userService.toVOPage(result));
    }

    @PostMapping("/update")
    public Result<?> updateUser(@RequestBody UserUpdateDTO updateDTO, HttpServletRequest request) {
        if (Objects.requireNonNull(UserRoleEnum.getEnum(updateDTO.getUserRole())).getWeight()
                > UserRoleEnum.ADMIN.getWeight()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        UserRoleEnum userRoleEnum = UserRoleEnum.getEnum(updateDTO.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不存在此权限");
        }

        UserRoleEnum adminUserRoleEnum = UserRoleEnum.getEnum(userService.getLoginUser(request).getUserRole());
        if (userRoleEnum.getWeight() > UserRoleEnum.USER.getWeight() && adminUserRoleEnum != UserRoleEnum.ROOT) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getUserRole, updateDTO.getUserRole())
                .eq(User::getUserId, updateDTO.getUserId());
        userService.update(updateWrapper);
        return Result.success("ok");
    }
}
