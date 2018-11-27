package hounify.pipeline;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.pipeline.JsonPipeline;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;

import hounify.entity.BaseContent;
import hounify.entity.Configuration;
import hounify.repository.ConfigurationRepository;
import hounify.repository.ContentRepository;

@Service("detailPipelines")
public class DetailPipelines extends JsonPipeline {
	@Autowired
	private ConfigurationRepository configurationRepository;
	@Autowired
	private ContentRepository contentRepository;
	int n = 0;

	@Override
	public void process(JSONObject jo) {
		List<BaseContent> lists = contentRepository.findByContentOrigin("spider");
		++n;
		System.out.println("详细页面====================" + n);
		// System.out.println(jo);
		HttpRequest currRequest = HttpGetRequest.fromJson(jo.getJSONObject("request"));
		//String currURL = currRequest.getUrl();
		String id = currRequest.getParameter("id");
		String title = currRequest.getParameter("title");
		Configuration config = configurationRepository.findAllByIdAndState(Long.parseLong(id), "1");
		BaseContent content = new BaseContent();
		content.setContenBody(jo.getString("detailContent"));
		content.setContentTitle(title);
		content.setContentOrigin("spider");
		content.setState("t");
		content.setResourcesId(UUID.randomUUID().toString());
		content.setPublishTime(new Timestamp(new Date().getTime()));
		content.setContentType("article");
		content.setContentTag(JSON.toJSONString(config.getKeyWord().split(";")));
		if (!StringUtils.isEmpty(title)&&!PiplineUntil.distinct(lists, title)) {
			contentRepository.save(content);
			System.out.println(JSON.toJSONString(config.getKeyWord()) + "保存成功====================" + n);

		}		
	}

}
