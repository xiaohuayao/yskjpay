package com.yishang.yspay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yishang.yspay.bean.PayClub;
import com.yishang.yspay.mapper.PayClubMapper;
import com.yishang.yspay.service.PayClubService;
import org.springframework.stereotype.Service;

@Service
public class PayClubServiceImpl extends ServiceImpl<PayClubMapper, PayClub> implements PayClubService {
}
