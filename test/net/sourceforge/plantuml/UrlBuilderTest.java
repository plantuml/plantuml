package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.UrlBuilder.ModeUrl;

class UrlBuilderTest {

	@Test
	public void testGetUrl10() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo", "http://foo", "http://foo", b.getUrl("[[http://foo]]"));
	}

	@Test
	public void testGetUrl11() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo", "tooltip", "text", b.getUrl("[[http://foo{tooltip} text]]"));
	}

	@Test
	public void testGetUrl12() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo", "tooltip", "text", b.getUrl("[[http://foo {tooltip} text]]"));
	}

	@Test
	public void testGetUrl13() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo", "tooltip", "http://foo", b.getUrl("[[http://foo {tooltip}]]"));
	}

	@Test
	public void testGetUrl15() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo", "http://foo", "text", b.getUrl("[[http://foo text]]"));
	}

	@Test
	public void testGetUrl18() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo", "http://foo", "http://foo", b.getUrl("[[  http://foo  ]]"));
	}

	@Test
	public void testGetUrl20() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo test", "http://foo test", "http://foo test", b.getUrl("[[\"http://foo test\"]]"));
	}

	@Test
	public void testGetUrl30() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.ANYWHERE);
		assertUrl("http://foo test", "http://foo test", "http://foo test",
				b.getUrl("start [[\"http://foo test\"]] end"));
	}

	@Test
	public void testGetUrl40() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://google.com", "a nice toolip", "<img:HelloWorld.png{scale=2}>",
				b.getUrl("[[http://google.com{a nice toolip} <img:HelloWorld.png{scale=2}>]]"));
	}

	@Test
	public void testGetUrl41() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://google.com", "a nice toolip", "<img:HelloWorld.png{scale=2}>",
				b.getUrl("[[http://google.com {a nice toolip} <img:HelloWorld.png{scale=2}>]]"));
	}

	@Test
	public void testGetUrl42() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://google.com", "http://google.com", "<img:HelloWorld.png{scale=2}>",
				b.getUrl("[[http://google.com <img:HelloWorld.png{scale=2}>]]"));
	}

	@Test
	public void testGetUrl50() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://google.com", "http://google.com", "<img:HelloWorld.png{scale=2}>",
				b.getUrl("[[http://google.com <img:HelloWorld.png{scale=2}>]]"));
	}

	@Test
	public void testGetUrl60() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo?dummy={123}&action=edit", "http://foo?dummy={123}&action=edit",
				"http://foo?dummy={123}&action=edit", b.getUrl("[[http://foo?dummy={123}&action=edit]]"));
	}

	@Test
	public void testGetUrl70() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://www.plantuml.com", "Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}",
				"http://www.plantuml.com",
				b.getUrl("[[http://www.plantuml.com{Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}}]]"));
	}

	@Test
	public void testGetUrl80() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo?dummy=", "123}z{", "http://foo?dummy=", b.getUrl("[[http://foo?dummy={123}z{}]]"));
	}

	@Test
	public void testGetUrl90() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("", "tooltip", "some text", b.getUrl("[[{tooltip} some text]]"));
	}

	@Test
	public void testGetUrl100() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.ANYWHERE);
		assertUrl("www.google.com", "www.google.com", "POST /session/csrStart",
				b.getUrl("fromor [[www.google.com POST /session/csrStart]] end"));
	}

	@Test
	public void testGetUrl110() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("blaha(xyz,12)", " tooltip ", "blaha(xyz,12)", b.getUrl("[[ blaha(xyz,12){ tooltip } ]]"));
	}

	@Test
	public void testGetUrl120() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("", "Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}", "",
				b.getUrl("[[{Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}}]]"));
	}

	@Test
	public void testGetUrl130() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo?dummy=", "123}{", "http://foo?dummy=", b.getUrl("[[http://foo?dummy={123}{}]]"));
	}

	@Test
	public void testGetUrl140() {
		final UrlBuilder b = new UrlBuilder(null, ModeUrl.STRICT);
		assertUrl("http://foo?dummy={123}", "", "http://foo?dummy={123}", b.getUrl("[[\"http://foo?dummy={123}\"{}]]"));
	}

	public void assertUrl(String urlLink, String tooltip, String label, Url url) {
		assertEquals(urlLink, url.getUrl(), "url");
		assertEquals(tooltip, url.getTooltip(), "tooltip");
		assertEquals(label, url.getLabel(), "label");
	}

}
