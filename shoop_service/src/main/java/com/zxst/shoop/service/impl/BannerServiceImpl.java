package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.Banner;
import com.zxst.shoop.mapper.BannerMapper;
import com.zxst.shoop.service.BannerService;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    @Override
    public List<Banner> showBanner() {
        return bannerMapper.showBanner();
    }
}
