package hounify.repository;

import java.util.List;

import javax.print.DocFlavor.STRING;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hounify.entity.BaseContent;

@Repository
public interface ContentRepository extends JpaRepository<BaseContent, String>{
	List<BaseContent> findByContentOrigin(String origin);
}