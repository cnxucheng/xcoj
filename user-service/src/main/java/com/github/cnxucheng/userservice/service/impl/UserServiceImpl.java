package com.github.cnxucheng.userservice.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cnxucheng.common.common.ErrorCode;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.common.constant.UserLoginState;
import com.github.cnxucheng.common.exception.BusinessException;
import com.github.cnxucheng.userservice.constant.RedisConstant;
import com.github.cnxucheng.userservice.mapper.UserMapper;
import com.github.cnxucheng.userservice.service.UserStatusService;
import com.github.cnxucheng.xcojModel.dto.user.UserLoginDTO;
import com.github.cnxucheng.xcojModel.dto.user.UserRegisterDTO;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.vo.UserVO;
import com.github.cnxucheng.userservice.service.UserService;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UserServiceImpl
 * @author : xucheng
 * @since : 2025-7-8
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private RedissonClient redissonClient;

    private final String SALT = "xc_oj_xucheng";

    private final String JWT_KEY = "xcoj_jwt_key";

    @Resource
    private UserStatusService userStatusService;

    @Override
    public String login(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String passwordByMd5 = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", passwordByMd5);
        User user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在或密码错误");
        }
        // request.getSession().setAttribute(UserLoginState.USER_LOGIN_STATE, user);
        // 使用jwt实现登录
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("userId", user.getUserId());
        payload.put("userRole", user.getUserRole());
        return JWTUtil.createToken(payload, JWT_KEY.getBytes());
    }

    @Override
    public Long register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        synchronized (username.intern()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            String passwordByMd5 = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordByMd5);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getUserId();
        }
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        return getLoginUser(request.getHeader("token"));
    }

    @Override
    public User getLoginUser(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        JWTPayload payload = jwt.getPayload();
        Long userId = Convert.toLong(payload.getClaim("userId"));
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        User currentUser = this.getById(userId);
        if (currentUser.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        currentUser = this.getById(userId);
        return currentUser;
    }

    @Override
    public void updateStatistics(Long userId, Integer isAc) {
        User user = this.getById(userId);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserId, userId);
        updateWrapper.set(User::getSubmitNum, user.getSubmitNum() + 1);
        if (isAc == 1) {
            updateWrapper.set(User::getAcceptedNum, user.getAcceptedNum() + 1);
        }
        this.update(updateWrapper);
    }

    @Override
    public UserVO toVO(User user) {
        return UserVO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .acceptedNum(user.getAcceptedNum())
                .userRole(user.getUserRole())
                .submitNum(user.getSubmitNum())
                .createTime(user.getCreateTime())
                .solved(userStatusService.getUserStatusList(user.getUserId(), 1))
                .notSolved(userStatusService.getUserStatusList(user.getUserId(), 0))
                .build();
    }

    @Override
    public MyPage<UserVO> toVOPage(Page<User> userPage) {
        List<User> userList = userPage.getRecords();
        MyPage<UserVO> userVOPage = new MyPage<>();
        if (CollectionUtils.isEmpty(userList)) {
            return userVOPage;
        }
        List<UserVO> userVOList = userList.stream().map(this::toVO).collect(Collectors.toList());
        userVOPage.setData(userVOList);
        userVOPage.setTotal(userPage.getTotal());
        userVOPage.setCurrent(userPage.getCurrent());
        userVOPage.setPageSize((int) userPage.getSize());
        userVOPage.setTotalPages(userPage.getPages());
        return userVOPage;
    }

    @Override
    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserLoginState.USER_LOGIN_STATE);
    }

    @Override
    public void signIn(long userId) {
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int offset = date.getDayOfYear();
        String key = RedisConstant.getUserSignKey(year, userId);
        RBitSet bitSet = redissonClient.getBitSet(key);
        if (!bitSet.get(offset)) {
            bitSet.set(offset, true);
        }
    }

    @Override
    public List<Integer> getSignInData(int year, long userId) {
        int allDays = getDayByYear(year);
        String key = RedisConstant.getUserSignKey(year, userId);
        RBitSet bitSet = redissonClient.getBitSet(key);
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= allDays; i ++ ) {
            if (bitSet.get(i)) {
                result.add(i);
            }
        }
        return result;
    }

    private int getDayByYear(int year) {
        if (year % 400 == 0) {
            return 366;
        }
        if (year % 4 == 0 && year % 100 != 0) {
            return 366;
        }
        return 365;
    }
}




