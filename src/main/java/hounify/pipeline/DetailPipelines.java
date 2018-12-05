package hounify.pipeline;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String currURL = currRequest.getUrl();
		String configId = currRequest.getParameter("configId");
		String title = currRequest.getParameter("title");
		Configuration config = configurationRepository.findAllByIdAndState(Long.parseLong(configId), "1");
		BaseContent content = new BaseContent();
		String body = jo.getString("detailContent");
		body = getImgStr(body, currURL);
		Set<String> imagesUrls = new HashSet<String>();
		imagesUrls = getImgStrs(body, currURL);
		content.setContentTitleImage(JSON.toJSONString(imagesUrls));
		content.setContenBody(body);
		content.setContentTitle(title);
		content.setContentOrigin("spider");
		content.setState("t");
		content.setResourcesId(UUID.randomUUID().toString());
		content.setPublishTime(new Timestamp(new Date().getTime()));
		content.setContentType("article");
		content.setContentTag(JSON.toJSONString(config.getKeyWord().split(";")));
		if (!StringUtils.isEmpty(title) && !PiplineUntil.distinct(lists, title)) {
			contentRepository.save(content);
			System.out.println(JSON.toJSONString(config.getKeyWord()) + "保存成功====================" + n);

		}
	}

	public String getImgStr(String htmlStr, String currURL) {

		String urlAddress = currURL.substring(0, currURL.indexOf("/", 10));
		Set<String> pics = new HashSet<String>();
		String img = "";
		Pattern p_image;
		Matcher m_image;
		// String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
		String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			// 得到<img />数据
			img = m_image.group();
			// 匹配<img>中的src数据
			Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
			while (m.find()) {
				pics.add(m.group(1));
			}
		}
		System.out.println(pics);
		for (String str : pics) {
			if (!str.startsWith("http")) {
				if (str.contains("?")) {
					str = str.substring(0, str.indexOf("?"));
					String url = urlAddress + str;
					htmlStr = htmlStr.replaceAll(str, url).replaceAll("amp;", "");
					break;
				}
				String url = urlAddress + str;
				htmlStr = htmlStr.replaceAll(str, url).replaceAll("amp;", "");
			}
		}
		System.out.println("------------" + htmlStr);
		return htmlStr;

	}
	/*
	 * public static void main(String[] args) { String
	 * html=" <img alt=\"\" src=\"/seeyon/fileUpload.domethod=showRTE&amp;fileId=4060192640867255954&amp;createDate=2018-11-29&amp;type=image\" style=\"height: 450px"
	 * ; String dtr=
	 * "/seeyon/fileUpload.do?method=showRTE&amp;fileId=4060192640867255954&amp;createDate=2018-11-29&amp;type=image";
	 * // String dtr="/seeyon/fileUpload.domethod=showRTE&amp;fileId=";
	 * System.out.println(html.replaceAll(dtr, "http"+dtr));
	 * System.out.println(dtr.contains("?")); }
	 */

	public Set<String> getImgStrs(String htmlStr, String currURL) {
		String urlAddress = currURL.substring(0, currURL.indexOf("/", 10));
		Set<String> pics = new HashSet<String>();
		String img = "";
		Pattern p_image;
		Matcher m_image;
		// String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
		String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			// 得到<img />数据
			img = m_image.group();
			// 匹配<img>中的src数据
			Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
			while (m.find()) {
				pics.add(m.group(1));
			}
		}
		return pics;
	}

}
