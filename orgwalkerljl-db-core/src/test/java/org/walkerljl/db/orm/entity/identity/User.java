package org.walkerljl.db.orm.entity.identity;

import java.io.Serializable;
import java.util.Date;

import org.walkerljl.db.api.annotation.Column;
import org.walkerljl.db.api.annotation.Entity;

/**
 *
 * User
 *
 * @author lijunlin
 */
@Entity("id_user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(value = "id", key = true)
	private Long id;
	@Column(value = "user_id")
	private String userId;
	@Column(value = "user_name")
	private String userName;
	@Column(value = "sex")
	private String sex;
	@Column(value = "email")
	private String email;
	@Column(value = "mobile")
	private String mobile;
	@Column(value = "telephone")
	private String telephone;
	@Column(value = "birthday")
	private Date birthday;
	@Column(value = "id_card_number")
	private String idCardNumber;
	@Column(value = "last_login_date")
	private Date lastLoginDate;
	@Column(value = "remark")
	private String remark;
	@Column(value = "status")
	private Integer status;
	@Column(value = "create_date")
	private Date createDate;
	@Column(value = "create_user_id")
	private String createUserId;
	@Column(value = "create_user_name")
	private String createUserName;
	@Column(value = "last_modify_date")
	private Date lastModifyDate;
	@Column(value = "last_modify_user_id")
	private String lastModifyUserId;
	@Column(value = "last_modify_user_name")
	private String lastModifyUserName;
	
	/**
	 * 默认构造函数
	 */
	public User() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getLastModifyUserId() {
		return lastModifyUserId;
	}

	public void setLastModifyUserId(String lastModifyUserId) {
		this.lastModifyUserId = lastModifyUserId;
	}

	public String getLastModifyUserName() {
		return lastModifyUserName;
	}

	public void setLastModifyUserName(String lastModifyUserName) {
		this.lastModifyUserName = lastModifyUserName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", sex=" + sex + ", email=" + email + ", mobile="
				+ mobile + ", telephone=" + telephone + ", birthday="
				+ birthday + ", idCardNumber=" + idCardNumber
				+ ", lastLoginDate=" + lastLoginDate + ", remark=" + remark
				+ ", status=" + status + ", createDate=" + createDate
				+ ", createUserId=" + createUserId + ", createUserName="
				+ createUserName + ", lastModifyDate=" + lastModifyDate
				+ ", lastModifyUserId=" + lastModifyUserId
				+ ", lastModifyUserName=" + lastModifyUserName + "]";
	}
}