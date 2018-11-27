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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.bcel.generic.NEW;
import org.hibernate.annotations.GenerationTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
/**
 * Created by gongmingbo on 2018/5/2.
 * 自定义管道
 */
@Service("customPipeline")
public class CustomPipeline extends JsonPipeline {
	@Autowired
	private ConfigurationRepository configurationRepository;
	@Autowired
	private ContentRepository contentRepository;

	@Override
	public void process(JSONObject jo) {
		System.out.println(jo);
		HttpRequest currRequest = HttpGetRequest.fromJson(jo.getJSONObject("request"));
		String id = currRequest.getParameter("id");
		String currURL = currRequest.getUrl();
		Configuration c = configurationRepository.findAllByIdAndState(Long.parseLong(id), "1");
		List<BaseContent> lists=contentRepository.findByContentOrigin("spider");
		JSONObject jsonObject = new JSONObject(jo);
		// 返回json的数组
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		System.out.println(currURL + " ____爬取记录____：" + jsonArray.size());
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				String html = (String) jsonObject2.get("endElement");
			    Map<String, String> map = getUrlAadName(html, c.getTitileReg(), c.getUrlReg());
			    String title=map.get("title");
			    String url=map.get("url");	
				String key = getKeyWord(c.getKeyWord());
					if (!StringUtils.isEmpty(title)&& title.matches(key)) {	
						if (!url.startsWith("http")) {
						url=currURL.substring(0, currURL.indexOf("/", 10))+url;
						}
						System.out.println("url---------"+url+title);
						if (!PiplineUntil.distinct(lists, title)) {
							map.put("id", id);
							currRequest.setParameters(map);
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
	public String getKeyWord(String key) {
		String keyWord = ".*" + key + ".*";
		String keys2 = keyWord.replaceAll(";|；", ".*");
		return keys2;

	}
	public static Map<String, String> getUrlAadName(String html, String titleReg,String urlReg) {
		Pattern patternTitle= Pattern.compile(titleReg);
		Matcher matcherTitle = patternTitle.matcher(html);
		String url=null;
		String title=null;
		Map<String, String> map = new HashMap<String, String>();
		 while(matcherTitle.find()){
				title=matcherTitle.group();
				}
			Pattern patternUrl= Pattern.compile(urlReg);
			Matcher matcherUrl = patternUrl.matcher(html);
			 while(matcherUrl.find()){
					url=matcherUrl.group();		
					}
			 title=title.replaceAll(">|<", "");
			 url=url.replaceAll("amp;|\'", "");
			 map.put("title", title);
			 map.put("url", url);
			 
		return map;
	}
	public static void main(String[] args) {
		//String html="<img src=\"../images/list_25.jpg\"> <p><a href=\"../info/1003/9260.htm\">我校学习贯彻习近平新时代中国特色</a></p> <span><a href=\"../info/1003/9260.htm\">6月8日下午，由学校党委举办的</a></span>";
		//String titleReg="";
		//Pattern titleReg= Pattern.compile("htm\">.*</a></p>");
		//String html="<a href=\"../info/1003/9260.htm\">我校学习贯彻习近平新时代中国特色社会主义思想和党的十九大精神专题培训圆满结束</a>";
		String html="<span class=\"rt\">2018-11-20</span><a onclick=\"openWin('/seeyon/xndxNewsData.do?method=userView&amp;id=SHo4cUVuU0x3K3dUaWlGYS8rNWx4Mlo4VkJ0WE84ZGw=')\" href=\"#\">学校41个项目获2018年重庆市留学人员回国创业创新支持计划创新类项目资助 </a>";
		Pattern titleReg= Pattern.compile("#\">.*<");
		Matcher matcherTitle = titleReg.matcher(html);
		System.out.println("222222");
		http://222.198.125.159/seeyon/xndxNewsData.do?method=userView&id={er}=%27
		 while(matcherTitle.find()){
				System.out.println(matcherTitle.group(0));
			//	System.out.println(matcherTitle.group().replaceAll("htm\">|</a></p>", ""));
				
				}
	
	}
}