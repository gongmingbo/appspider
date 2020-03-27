package hounify.entity;



import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;


import lombok.Data;


/**
 * 基础类容表（如文章，视频）
 *
 * @author gongmingbo
 */
@Entity
@Table(name = "app_content_info")
@Data
public class BaseContent implements Serializable {
    @Id
    private String id;
    private String resourcesId;
    private String contentTitle;
    private String contentType;

}
