package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description
 *
 * @author 文攀 2019/10/20 12:18
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 根据条件进行分页查询品牌
     * @param page 要显示第几页
     * @param rows 每页显示多少行
     * @param sortBy 根据什么进行排序
     * @param desc 排升序还是降序
     * @param key 查询关键字
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "rows",defaultValue = "5") int rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key",required = false) String key){

        PageResult<Brand> result = brandService.queryBrandByPage(page,rows,sortBy,desc,key);

        if(CollectionUtils.isEmpty(result.getItems())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * 新增品牌信息
     * 在restful风格中，前端若使用1,2,3这种字符串方式传递id值，那么后端可以使用List<Long>来接收
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping()
    public ResponseEntity<Void> saveBrand(Brand brand,@RequestParam("cids") List<Long> cids){
        this.brandService.saveBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping()
    public ResponseEntity<Void> updateBrand(Brand brand,@RequestParam("cids") List<Long> cids){
        this.brandService.updateBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * 通过商品分类id查询品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){
        List<Brand> brands = this.brandService.queryBrandsByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    /**
     * 通过品牌id查询品牌名称
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id")Long id){

        Brand brand = this.brandService.queryBrandById(id);
        return brand;
    }

}
