package hounify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import hounify.service.AppStart;

/**
 * Created by gongmingbo on 2018/5/7.
 * springBoot初始化类
 */
@Component
@Order(1)
public class Scheduled implements CommandLineRunner {
    @Autowired
    private AppStart appStart;
    @Override
    public void run(String... args) throws Exception {
        //startPider.startSpider();
    	//appStart.start();
        //appStart.scheduled();
    	
    }
}
