package cn.zsk.springsecurityoauth2source.service.impl;

import cn.zsk.springsecurityoauth2source.entity.SourceContentEntity;
import cn.zsk.springsecurityoauth2source.mapper.SourceContentMapper;
import cn.zsk.springsecurityoauth2source.service.SourceContentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SourceContentServiceImpl extends ServiceImpl<SourceContentMapper, SourceContentEntity> implements SourceContentService {

    @Autowired
    private SourceContentMapper sourceContentMapper;


    @Override
    public SourceContentEntity getById(Long id) {
        SourceContentEntity sourceContentEntity = sourceContentMapper.selectById(id);
        return sourceContentEntity;
    }

    @Override
    public List<SourceContentEntity> selectAll() {
        List<SourceContentEntity> sourceContentEntityList = sourceContentMapper.selectList(new QueryWrapper<SourceContentEntity>());
        return sourceContentEntityList;
    }

    @Override
    public boolean insert(SourceContentEntity sourceContentEntity) {
        sourceContentEntity.setCreated(new Date());
        sourceContentMapper.insert(sourceContentEntity);
        return true;
    }

    @Override
    public void update(SourceContentEntity sourceContentEntity) {
        if(null == sourceContentEntity || null == sourceContentEntity.getId()){
            return;
        }
        sourceContentEntity.setUpdated(new Date());
        sourceContentMapper.update(null,new UpdateWrapper<SourceContentEntity>()
                .set(null != sourceContentEntity.getContent(),"content",sourceContentEntity.getContent())
                .eq("id",sourceContentEntity.getId()));
    }

    @Override
    public void delete(Long id) {
        sourceContentMapper.deleteById(id);
    }
}
