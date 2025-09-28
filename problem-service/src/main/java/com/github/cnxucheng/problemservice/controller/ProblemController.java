package com.github.cnxucheng.problemservice.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.common.common.Result;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.xcojModel.dto.problem.ProblemQueryDTO;
import com.github.cnxucheng.xcojModel.entity.Problem;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.enums.UserRoleEnum;
import com.github.cnxucheng.xcojModel.vo.ProblemSampleVO;
import com.github.cnxucheng.xcojModel.vo.ProblemVO;
import com.github.cnxucheng.problemservice.service.ProblemService;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class ProblemController {

    private static final Logger log = LoggerFactory.getLogger(ProblemController.class);
    @Resource
    private ProblemService problemService;

    @Resource
    private UserFeignClient userFeignClient;

    @GetMapping("/")
    public Result<ProblemVO> findById(@RequestParam(value = "id") Integer id, HttpServletRequest request) {
        Problem problem  = problemService.getById(id);
        User user = userFeignClient.getLoginUser(request.getHeader("token"));
        UserRoleEnum userRoleEnum = (user != null ? UserRoleEnum.getEnum(user.getUserRole()) : UserRoleEnum.BAN);
        // 如果是隐藏的题目，要求具有管理员权限
        if (problem.getIsHidden() == 1) {
            if (userRoleEnum != null && userRoleEnum.getWeight() <
                    UserRoleEnum.ADMIN.getWeight()) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        ProblemVO problemVO = problemService.getProblemVO(problem);
        // 如果没有管理员权限，不能获取到测试数据
        if (userRoleEnum != null && userRoleEnum.getWeight() < UserRoleEnum.ADMIN.getWeight()) {
            problemVO.setJudgeCase(null);
        }
        return Result.success(problemVO);
    }

    @PostMapping("/list")
    public Result<MyPage<ProblemSampleVO>> find(@RequestBody ProblemQueryDTO queryDTO, HttpServletRequest request) {
        QueryWrapper<Problem> queryWrapper;
        User user = userFeignClient.getLoginUser(request.getHeader("token"));
        Page<Problem> qpage = new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize());
        if (user != null && Objects.requireNonNull(UserRoleEnum.getEnum(user.getUserRole())).getWeight() >= UserRoleEnum.ADMIN.getWeight()) {
            queryWrapper = problemService.getQueryWrapper(queryDTO, 1);
        } else {
            queryWrapper = problemService.getQueryWrapper(queryDTO, 0);
        }
        Page<Problem> page = problemService.page(qpage, queryWrapper);
        log.info(JSONUtil.toJsonStr(page));
        return Result.success(problemService.getProblemSampleVOPage(page));
    }
}
