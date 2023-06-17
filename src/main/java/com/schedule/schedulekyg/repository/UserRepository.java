package com.schedule.schedulekyg.repository;

import com.schedule.schedulekyg.vo.user.UserVO;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {


  public UserVO getUserInfo() {

    UserVO userVO = new UserVO();
    userVO.setUserId(0);
    userVO.setUserName("USER");
    userVO.setAuth("ADMIN");
    userVO.setPassword("1234");

    return userVO;
  }
}
