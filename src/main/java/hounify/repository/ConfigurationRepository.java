package hounify.repository;

import hounify.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gongmingbo on 2018/4/26.
 * 配置文件持久层
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    List<Configuration> findByState(String state);

    Configuration findAllByIdAndState(Long id,String state);
}
