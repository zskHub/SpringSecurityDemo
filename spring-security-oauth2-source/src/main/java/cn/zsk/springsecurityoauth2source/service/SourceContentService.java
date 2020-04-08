package cn.zsk.springsecurityoauth2source.service;

import cn.zsk.springsecurityoauth2source.entity.SourceContentEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SourceContentService extends IService<SourceContentEntity> {

    /**
     * 根据 ID 获取
     * @param id
     * @return: cn.zsk.springsecurityoauth2source.entity.TbContentEntity
     * @throws
     * @time: 2020-04-03 17:33
     * @author: zsk
     */
    SourceContentEntity getById(Long id);

   /**
    * 获取全部数据
    * @param
    * @return: java.util.List<cn.zsk.springsecurityoauth2source.entity.TbContentEntity>
    * @throws
    * @time: 2020-04-03 17:33
    * @author: zsk
    */
    List<SourceContentEntity> selectAll();

    /**
     * 新增
     * @param tbContent
     * @return: int
     * @throws
     * @time: 2020-04-03 17:33
     * @author: zsk
     */
     boolean insert(SourceContentEntity tbContent);


    /**
     *  编辑
     * @param tbContent
     * @return: int
     * @throws
     * @time: 2020-04-03 17:32
     * @author: zsk
     */
    void update(SourceContentEntity tbContent);

    /**
     * 删除
     * @param id
     * @return: int
     * @throws
     * @time: 2020-04-03 17:33
     * @author: zsk
     */
    void delete(Long id);

}
