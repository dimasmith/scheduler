package scheduler.app.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

@Getter
@Setter
public class RemoteJobDto implements Dto {
	private Long id;
	private String requestUrl;
	private RequestMethod requestMethod;
	private String authString;
	private String postJson;

	@Override
	public String toString() {
		return String.format("#%d: %s (%s)", id, requestUrl, requestMethod);
	}
}