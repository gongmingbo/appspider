package hounify.config;
import com.geccocrawler.gecco.spring.SpringPipelineFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gongmingbo on 2018/4/24.
 * 把SpringPipelineFactory实例交给springBoot框架来管理
 */
@Configuration
public class SpringPipelineFactoryBean {
	@Bean
	public SpringPipelineFactory springPipelineFactory() {

		return new SpringPipelineFactory();
	}
}
