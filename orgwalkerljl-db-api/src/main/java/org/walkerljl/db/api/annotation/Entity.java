package org.walkerljl.db.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 实体
 *
 * @author lijunlin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Entity {

	/**
	 * 表名
	 * @return
	 */
	String value() default "";
	
	/**
	 * 备注
	 * @return
	 */
	String comment() default "";
}