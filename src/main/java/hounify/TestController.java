package hounify;

import hounify.entity.BaseContent;
/*import hounify.repository.ContentRepository;*/
import hounify.repository.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @描述
 * @Author GONGMINGBO
 * @Date 2019/7/16 17:00
 * @Version V1.0
 **/
@RestController
public class TestController {
  /*  @Autowired
    ContentRepository contentRepository;*/
    @Autowired
    Test test;
    @RequestMapping("/insert")
    public String test(){
        long start = System.currentTimeMillis();
        List<BaseContent> list =new ArrayList<BaseContent>();
        for (int i=0;i<100000;i++){
            BaseContent baseContent=new BaseContent();
            baseContent.setId(UUID.randomUUID().toString());
            baseContent.setContentTitle("测试标题"+i);
            list.add(baseContent);

        }
        System.out.println("------------");
        test.insert(list);
        long end = System.currentTimeMillis();


        System.out.println(end-start);
        return (end-start)+"";
    }
}
