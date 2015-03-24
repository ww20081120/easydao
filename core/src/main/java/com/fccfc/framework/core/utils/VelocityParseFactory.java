/**
 * 
 */
package com.fccfc.framework.core.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import com.fccfc.framework.core.db.DaoConstants;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年10月28日 <br>
 * @since V1.0<br>
 * @see com.fccfc.framework.core.utils <br>
 */
public class VelocityParseFactory {

	private static final Logger logger = Logger
			.getLogger(VelocityParseFactory.class);

	private static Properties properties;

	static {
		try {
			properties = new Properties();
			properties.setProperty("runtime.log.error.stacktrace", "false");
			properties.setProperty("runtime.log.warn.stacktrace", "false");
			properties.setProperty("runtime.log.info.stacktrace", "false");
			properties.setProperty("runtime.log.logsystem.class",
					"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
			properties.setProperty("runtime.log.logsystem.log4j.category",
					"velocity_log");
			properties.setProperty(VelocityEngine.RESOURCE_LOADER, "string");
			properties
					.setProperty("string.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.StringResourceLoader");
			properties.setProperty("input.encoding",
					DaoConstants.DEFAULT_CHARSET);
			properties.setProperty("output.encoding",
					DaoConstants.DEFAULT_CHARSET);
			Velocity.init(properties);
		} catch (Exception e) {
			logger.error("初始化Velocity模板失败", e);
		}
	}

	/**
	 * templateName
	 * 
	 * @param templateName
	 *            templateName
	 * @param body
	 *            body
	 * @param params
	 *            params
	 * @return String
	 * @throws UtilException
	 *             UtilException
	 */
	public static String parse(String templateName, String body,
			Map<String, Object> params) throws UtilException {
		VelocityContext context = new VelocityContext();
		if (CommonUtil.isNotEmpty(params)) {
			for (Entry<String, Object> entry : params.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
		}

		StringResourceRepository repository = StringResourceLoader
				.getRepository();
		repository.putStringResource(templateName, body);

		ByteArrayOutputStream out = null;
		try {
			Template template = Velocity.getTemplate(templateName,
					DaoConstants.DEFAULT_CHARSET);
			out = new ByteArrayOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out, DaoConstants.DEFAULT_CHARSET));
			template.merge(context, writer);
			writer.flush();
			return new String(out.toByteArray(), DaoConstants.DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new UtilException(e.getMessage(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new UtilException(e);
				}
			}
		}
	}
}
