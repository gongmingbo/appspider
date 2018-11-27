package hounify.entity;

import java.sql.Timestamp;

import javax.persistence.*;

/**
 * 附件表
 * @author gongmingbo
 *
 */
@Entity
@Table(name="app_content_attachment_info")
public class Attachment {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;
	 private String resourcesId;
	 private String attachmentName;
	 private String attachmentUrl;
	 private String attachmentType;
	 private Timestamp attachmentTime;
	 private String remark;
	@Override
	public String toString() {
		return "Attachment [id=" + id + ", resourcesId=" + resourcesId + ", attachmentName=" + attachmentName
				+ ", attachmentUrl=" + attachmentUrl + ", attachmentType=" + attachmentType + ", attachmentTime="
				+ attachmentTime + ", remark=" + remark + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getResourcesId() {
		return resourcesId;
	}
	public void setResourcesId(String resourcesId) {
		this.resourcesId = resourcesId;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public Timestamp getAttachmentTime() {
		return attachmentTime;
	}
	public void setAttachmentTime(Timestamp attachmentTime) {
		this.attachmentTime = attachmentTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	 

}
