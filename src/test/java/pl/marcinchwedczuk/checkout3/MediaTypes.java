package pl.marcinchwedczuk.checkout3;

import com.google.common.base.Charsets;
import org.springframework.http.MediaType;

public final class MediaTypes {
	private MediaTypes() { }

	public static final MediaType APPLICATION_JSON_UTF8 =
			new MediaType(
					MediaType.APPLICATION_JSON.getType(),
					MediaType.APPLICATION_JSON.getSubtype(),
					Charsets.UTF_8);

	public static final MediaType PLAIN_TEXT_UTF8 =
			new MediaType(
					MediaType.TEXT_PLAIN.getType(),
					MediaType.TEXT_PLAIN.getSubtype(),
					Charsets.UTF_8);
}
