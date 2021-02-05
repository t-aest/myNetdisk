package com.taest.mynetdisk.bussiness.user.service.impl;

import com.taest.mynetdisk.bussiness.user.entity.MyUser;
import com.taest.mynetdisk.bussiness.user.mapper.MyUserMapper;
import com.taest.mynetdisk.bussiness.user.service.IMyUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-02-04
 */
@Service
public class MyUserServiceImpl extends ServiceImpl<MyUserMapper, MyUser> implements IMyUserService {

}
