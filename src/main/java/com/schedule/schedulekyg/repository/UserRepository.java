package com.schedule.schedulekyg.repository;

import com.schedule.schedulekyg.vo.user.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {
  UserVO getUserInfo();

}
