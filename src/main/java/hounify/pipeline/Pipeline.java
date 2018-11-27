package hounify.pipeline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.pipeline.JsonPipeline;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.scheduler.DeriveSchedulerContext;
import hounify.entity.BaseContent;
import hounify.entity.Configuration;
import hounify.repository.ConfigurationRepository;
import hounify.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * Created by gongmingbo on 2018/4/24.
 * 爬取a标签管道
 */
@Service("newPipelines")
public class Pipeline extends JsonPipeline {
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private ContentRepository contentRepository;

    @Override
    public void process(JSONObject jo) {
    	
        HttpRequest currRequest = HttpGetRequest.fromJson(jo.getJSONObject("request"));
        String currURL = currRequest.getUrl();
        String id = currRequest.getParameter("id");
        Configuration config =
                configurationRepository.findAllByIdAndState(Long.parseLong(id), "1");
        List<BaseContent> lists=contentRepository.findByContentOrigin("spider");
        JSONObject jsonObject = new JSONObject(jo);
        // 返回json的数组
        JSONArray jsonArray = jsonObject.getJSONArray("name");
        System.out.println(currURL + " ____爬取记录____：" + jsonArray.size());
        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String title = (String) jsonObject2.get("title");
                String url = (String) jsonObject2.get("url");
                System.out.println(url+title);
                Map<String, String> map=new HashMap<String, String>();
                map.put("id", id);
                map.put("title", title);
                currRequest.setParameters(map);
                if (title.matches(getKeyWord(config.getKeyWord()))) {
                 if (!PiplineUntil.distinct(lists, title)) {
                	//二级详细页面
                	 DeriveSchedulerContext.into(currRequest.subRequest(url));
				     }              	
				}              
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * 解析关键字
	 */
	public static String getKeyWord(String key) {
		String keyWord = ".*" + key + ".*";
		String keys2 = keyWord.replaceAll(";|；", ".*");
		return keys2;

	}
	public static void main(String[] args) {
		String reg=getKeyWord("中国");
		String string="我校举办第八届本科非师范专业学生专业技能展示活动";
		System.out.println(string.matches(reg));
	}
}
