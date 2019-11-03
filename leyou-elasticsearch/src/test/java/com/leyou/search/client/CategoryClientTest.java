package com.leyou.search.client;

import com.leyou.ElasticSearchApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticSearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    /**
     * 测试使用feign远程调用商品微服务
     */
    @Test
    public void testQueryCategories() {
        /*ResponseEntity<List<String>> names = this.categoryClient.queryNameByIds(Arrays.asList(1L, 2L, 3L));
        List<String> body = names.getBody();
        body.forEach(System.out::println);*/
    }
}
