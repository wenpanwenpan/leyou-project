package com.leyou.search.client;

import com.leyou.ElasticSearchApplication;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description
 *
 * @author 文攀 2019/10/31 13:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticSearchApplication.class)
public class GoodsClientTest {

    @Autowired
    private GoodsClient goodsClient;

    /**
     * 测试使用feign远程调用商品微服务
     */
    @Test
    public void testQuerySpuByPage(){
        PageResult<SpuBo> items = goodsClient.querySpuByPage(1, 5, true, "");
        items.getItems().forEach(System.out::println);
    }

}
