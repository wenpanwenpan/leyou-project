package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    /**
     * 处理insert和update的消息
     *
     * @param id
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            //若这个队列的不存在则创建该队列，否则直接使用该队列
            value = @Queue(value = "leyou.create.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "ture",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert","item.update"}
    ))
    public void listenCreate(Long id) throws Exception{
        if(id == null){
            return;
        }
        //创建或更新索引
        this.searchService.createIndex(id);
    }

    /**
     * 当商品删除的时候，删除消息队列中会被放入一条数据，
     * 在搜索微服务中监听到该队列，取出该队列中的数据，
     * 然后通过id去索引库里面删除一条数据     *
     * @param id
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "leyou.delete.index.queue",durable = "true"),
                    exchange = @Exchange(
                            value = "leyou.item.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = "item.delete"
            )
    )
    public void listenDelete(Long id){
        if(id == null){
            return;
        }
        //删除索引
        this.searchService.deleteIndex(id);
    }

}
