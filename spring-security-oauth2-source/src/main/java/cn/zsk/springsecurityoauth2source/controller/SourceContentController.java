package cn.zsk.springsecurityoauth2source.controller;

import cn.zsk.springsecurityoauth2source.dto.ResponseResult;
import cn.zsk.springsecurityoauth2source.entity.SourceContentEntity;
import cn.zsk.springsecurityoauth2source.service.SourceContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SourceContentController {

    @Autowired
    private SourceContentService sourceContentService;

    /**
     * 获取全部资源
     *
     * @return
     */
    @GetMapping("/")
    public ResponseResult<List<SourceContentEntity>> selectAll() {
        return new ResponseResult<>(Integer.valueOf(HttpStatus.OK.value()), HttpStatus.OK.toString(), sourceContentService.selectAll());
    }

    /**
     * 获取资源详情
     *
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    public ResponseResult<SourceContentEntity> getById(@PathVariable Long id) {
        return new ResponseResult<>(Integer.valueOf(HttpStatus.OK.value()), HttpStatus.OK.toString(), sourceContentService.getById(id));
    }

    /**
     * 新增资源
     */
    @PostMapping("/insert")
    public ResponseResult<Integer> insert(@RequestBody SourceContentEntity tbContentEntity) {
        sourceContentService.insert(tbContentEntity);

        return new ResponseResult<>(Integer.valueOf(HttpStatus.OK.value()), HttpStatus.OK.toString());
    }

    /**
     * 更新资源
     */
    @PutMapping("/update")
    public ResponseResult<Integer> update(@RequestBody SourceContentEntity tbContentEntity) {
        sourceContentService.update(tbContentEntity);

        return new ResponseResult<>(Integer.valueOf(HttpStatus.OK.value()), HttpStatus.OK.toString());

    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Integer> delete(@PathVariable Long id) {
        sourceContentService.delete(id);

        return new ResponseResult<>(Integer.valueOf(HttpStatus.OK.value()), HttpStatus.OK.toString());

    }
}
