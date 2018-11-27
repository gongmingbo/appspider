/*package hounify.service;


import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.dynamic.DynamicGecco;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HrefBean;
import com.geccocrawler.gecco.spring.SpringPipelineFactory;
import hounify.entity.Configuration;
import hounify.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


*//**
 * Created by gongmingbo on 2018/5/8.
 * 爬虫初始化类
 *//*
@Service
public class StartPider {
	@Autowired
	SpringPipelineFactory springPipelineFactory;
	@Autowired
	ConfigurationRepository configurationRepository;
    @Value("${timeNumber}")
    private String timeNumber;
    *//**
     * 初始化方法
     *//*
	public void start() {
		List<Configuration> configs = configurationRepository.findByState("1");
		System.out.println(configs);
		List<HttpRequest> urls = new ArrayList<HttpRequest>();
		if (configs.size() > 0) {
			for (Configuration c : configs) {
				HttpRequest request = new HttpGetRequest(c.getUrl());
				request.addParameter("id", c.getId()+"");
				urls.add(request);
				if (c.getContent().endsWith("a")) {
					DynamicGecco.html().gecco(new String[] { c.getUrl() }, "myDownloder", 5000, "newPipelines")
							.requestField("request").request().build()
							.listField("name", HrefBean.class).csspath(c.getContent()).build()
							.register();

				} else {
					String div = c.getContent();
					String endElement=div.substring(div.lastIndexOf(" "));
					DynamicGecco.html()
					        .gecco(new String[] { c.getUrl() }, "myDownloder", 5000, "customPipeline")
							.requestField("request").request().build()
							.listField("list", DynamicGecco.html()
							.stringField("endElement").csspath(endElement).build()
							.register())
							.csspath(c.getContent()).build()
							.register();
				}
			}
		}
		GeccoEngine.create()
		.pipelineFactory(springPipelineFactory)
		.classpath("hounify")
		.start(urls)
		.interval(30000)
		.thread(urls.size() + 1)
		//.loop(true)
		.start();
	}
	*//**
	 * 定时爬取
	 *//*
	public void startSpider() {
		long time;
		System.out.println("timeNumber "+timeNumber);
		if (StringUtils.isEmpty(timeNumber)) {
			time = 1000 * 60 * 5;
		}else {
			time = Long.parseLong(timeNumber);
		}
		Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					start();
				}
			}, 5000, time);
	}
}
*/