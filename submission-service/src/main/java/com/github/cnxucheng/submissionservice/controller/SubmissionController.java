package com.github.cnxucheng.submissionservice.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.common.common.Result;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.submissionservice.rabbitmq.RabbitMQProducer;
import com.github.cnxucheng.xcojModel.dto.submision.SubmissionQueryDTO;
import com.github.cnxucheng.xcojModel.dto.submision.SubmissionSubmitDTO;
import com.github.cnxucheng.xcojModel.entity.Submission;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.enums.UserRoleEnum;
import com.github.cnxucheng.xcojModel.vo.SubmissionVO;
import com.github.cnxucheng.submissionservice.service.SubmissionService;
import com.github.cnxucheng.xcojfeignclient.service.JudgeFeignClient;
import com.github.cnxucheng.xcojfeignclient.service.UserFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class SubmissionController {

    @Resource
    private SubmissionService submissionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private RabbitMQProducer rabbitMQProducer;

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody SubmissionSubmitDTO submitDTO, HttpServletRequest request) {
        Submission submission = new Submission();
        BeanUtil.copyProperties(submitDTO, submission);
        User user  = userFeignClient.getLoginUser(request.getHeader("token"));
        submission.setUserId(user.getUserId());
        submissionService.save(submission);
        rabbitMQProducer.sendMessage("submission_exchange", "my_routingKey", String.valueOf(submission.getSubmissionId()));
        return Result.success("ok");
    }

    @PostMapping("/list")
    public Result<MyPage<SubmissionVO>> list(@RequestBody SubmissionQueryDTO submissionQueryDTO) {
        if (submissionQueryDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (submissionQueryDTO.getSize() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<Submission> qpage = new Page<>(submissionQueryDTO.getCurrent(), submissionQueryDTO.getSize());
        LambdaQueryWrapper<Submission> wrapper = submissionService.getQueryWrapper(submissionQueryDTO);
        Page<Submission> page = submissionService.page(qpage, wrapper);
        return Result.success(submissionService.getMyPage(page));
    }

    @GetMapping("/detail")
    public Result<Submission> getDetail(@RequestParam("id") Long id, HttpServletRequest request) {
        Submission submission = submissionService.getById(id);
        User user = userFeignClient.getLoginUser(request.getHeader("token"));
        if (!Objects.equals(user.getUserId(), submission.getUserId()) &&
                UserRoleEnum.getWeight(user.getUserRole()) < UserRoleEnum.getWeight(UserRoleEnum.ADMIN.getValue())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return Result.success(submission);
    }
}
