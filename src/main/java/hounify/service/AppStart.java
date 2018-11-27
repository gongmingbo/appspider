package hounify.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.dynamic.DynamicGecco;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HrefBean;
import com.geccocrawler.gecco.spring.SpringPipelineFactory;

import hounify.entity.Configuration;
import hounify.repository.ConfigurationRepository;
@Service
public class AppStart {
	@Autowired
	SpringPipelineFactory springPipelineFactory;
	@Autowired
	private ConfigurationRepository configurationRepository;
	@Value("${timeNumber}")
	private String timeNumber;
	@Scheduled(cron ="0/60 * * * * *")
	public void start() {
		List<Configuration> configs = configurationRepository.findByState("1");
		System.out.println(configs);
		List<HttpRequest> urls = new ArrayList<HttpRequest>();
		if (configs.size() > 0) {
			for (Configuration c : configs) {
				HttpRequest request = new HttpGetRequest(c.getUrl());
				request.addParameter("id", c.getId()+"");
				urls.add(request);
				if (c.getCssPath().endsWith("a")) {
					DynamicGecco.html().gecco(new String[] { c.getUrl() },  "myDownloder", 50000, "newPipelines")
						.requestField("request").request().build()
						.listField("name", HrefBean.class).csspath(c.getCssPath()).build()
						.register();

				} else  {
					String cssPath = c.getCssPath();
					String endElement=cssPath.substring(cssPath.lastIndexOf(" "));
					DynamicGecco.html()
					        .gecco(new String[] { c.getUrl() }, "customPipeline")
							.requestField("request").request().build()
							.listField("list", DynamicGecco.html()
							.stringField("endElement").csspath(endElement).build()			
							.register())
							.csspath(c.getCssPath()).build()
							.register();
				}
				//处理二级详细页面
				DynamicGecco.html().gecco(new String[] {c.getSecondaryUrl() },  "myDownloder", 50000,"detailPipelines")
				.requestField("request").request().build()
				.stringField("detailContent").csspath(c.getDetailCssPath()).build()
				.register();
			}
			
		}
		GeccoEngine.create()
		.pipelineFactory(springPipelineFactory)
		.classpath("hounify")
		.start(urls)
		.interval(3000)
		.thread(urls.size()*2+1)
		//.loop(true)
		.start();
	}
	/**
	 * 定时器
	 */
	public void scheduled() {
		long time;
		if (StringUtils.isEmpty(timeNumber)) {
			time = 1000 * 60 ;
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
