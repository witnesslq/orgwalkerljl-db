package org.walkerljl.db.orm;

import java.io.Serializable;

import org.walkerljl.db.orm.annotation.Column;
import org.walkerljl.db.orm.annotation.Entity;

/**
 *
 * OrmTestBean
 *
 * @author lijunlin
 */
@Entity("test_user")
public class OrmTestBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(value = "id", key = true)
	private Long id;
	
	@Column("user_id")
	private String userId;
	
	public OrmTestBean(){}

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
}