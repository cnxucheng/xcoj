package com.github.cnxucheng.userservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.cnxucheng.common.common.MyPage;
import com.github.cnxucheng.xcojModel.dto.user.UserLoginDTO;
import com.github.cnxucheng.xcojModel.dto.user.UserRegisterDTO;
import com.github.cnxucheng.xcojModel.entity.User;
import com.github.cnxucheng.xcojModel.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * UserService
 * @author : xucheng
 * @since : 2025-7-8
 */
public interface UserService extends IService<User> {

    String login(UserLoginDTO userLoginDTO, HttpServletRequest request);

    Long register(UserRegisterDTO userRegisterDTO);

    User getLoginUser(HttpServletRequest request);

    User getLoginUser(String token);

    void updateStatistics(Long userId, Integer isAc);

    UserVO toVO(User user);

    MyPage<UserVO> toVOPage(Page<User> userPage);

    void logout(HttpServletRequest request);

    void signIn(long userId);

    List<Integer> getSignInData(int year, long userId);
}
