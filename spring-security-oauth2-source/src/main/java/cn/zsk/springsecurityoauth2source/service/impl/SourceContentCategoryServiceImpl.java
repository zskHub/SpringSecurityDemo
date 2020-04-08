package cn.zsk.springsecurityoauth2source.service.impl;

import cn.zsk.springsecurityoauth2source.entity.SourceContentCategoryEntity;
import cn.zsk.springsecurityoauth2source.mapper.SourceContentCategoryMapper;
import cn.zsk.springsecurityoauth2source.service.SourceContentCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceContentCategoryServiceImpl extends ServiceImpl<SourceContentCategoryMapper, SourceContentCategoryEntity> implements SourceContentCategoryService {

    @Autowired
    private SourceContentCategoryMapper sourceContentCategoryMapper;

}
