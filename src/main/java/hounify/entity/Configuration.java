package hounify.entity;

import javax.persistence.*;

import lombok.Data;

import java.awt.print.PrinterAbortException;
import java.sql.Timestamp;

/**
 * Created by gongmingbo on 2018/4/26.
 * 爬虫配置表
 */
@Entity
@Table(name = "app_spider_config")
@Data
public class Configuration {
	/**
	 * 主键id自增长
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id;
    /**
     * 爬取地址一级地址
     */
    private String url;
    /**
     * 二级地址
     */
    private String secondaryUrl;
    /**
     * 详细页面规则
     */
    private String detailCssPath;
    /**
     * 自定义爬取规则 (自定义爬取一级地址)
     */
    private String urlReg;
    /**
     * 编辑配置人员id
     */
    private String userid;
    /**
     * 爬取的内容 jQuery 选择风格
     */
    private String cssPath;
    /**
     * 爬取关键字
     */
    private String keyWord;
    /**
     * 状态
     */
    private String state;
	private String titileReg;
}
