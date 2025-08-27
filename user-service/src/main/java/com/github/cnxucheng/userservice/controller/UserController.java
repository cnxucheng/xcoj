package com.github.cnxucheng.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.common.common.PageRequest;
import com.github.cnxucheng.common.common.Result;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.xcojModel.dto.user.UserLoginDTO;
import com.github.cnxucheng.xcojModel.dto.user.UserRegisterDTO;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.enums.UserRoleEnum;
import com.github.cnxucheng.xcojModel.vo.UserRankVO;
import com.github.cnxucheng.xcojModel.vo.UserVO;
import com.github.cnxucheng.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("/")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        if (username == null || password == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (username.length() < 4 || username.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (password.length() < 6 || password.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return Result.success(userService.login(userLoginDTO, request));
    }

    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        if (username == null || password == null || checkPassword == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (username.length() < 4 || username.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过短或过长");
        }
        if (password.length() < 6 || password.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短或过长");
        }
        if (!checkPassword.equals(password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        return Result.success(userService.register(userRegisterDTO));
    }

    @GetMapping("/getLoginUser")
    public Result<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return Result.success(userService.toVO(user));
    }

    /**
     * 分页获取用户排名
     * @param pageRequest 分页请求
     */
    @PostMapping("/rank")
    public Result<MyPage<UserRankVO>> rank(@RequestBody PageRequest pageRequest) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (pageRequest.getPageSize() > 50 || pageRequest.getPageSize() < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不合法请求");
        }
        if (pageRequest.getCurrent() < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不合法请求");
        }
        Page<User> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getAcceptedNum)
                .orderByAsc(User::getSubmitNum)
                .eq(User::getUserRole, UserRoleEnum.USER.getValue())
                .select(User::getUsername, User::getAcceptedNum, User::getSubmitNum);
        Page<User> result = userService.page(page, wrapper);
        
        MyPage<UserRankVO> res = new MyPage<>();
        res.setCurrent(result.getCurrent());
        res.setTotal(result.getTotal());
        res.setPageSize((int) result.getSize());
        res.setTotalPages(result.getPages());

        ArrayList<UserRankVO> resList = new ArrayList<>();
        for (User user : result.getRecords()) {
            UserRankVO vo = UserRankVO.builder()
                    .username(user.getUsername())
                    .acceptedNum(user.getAcceptedNum())
                    .submitNum(user.getSubmitNum())
                    .build();
            resList.add(vo);
        }
        res.setData(resList);
        
        return Result.success(res);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        userService.logout(request);
        return Result.success("ok");
    }
}
