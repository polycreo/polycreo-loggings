/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.polycreo.loggings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.slf4j.MDC;

import org.junit.Test;

public class ExtendedMDCInsertingServletFilterTest {
	
	private static final String REMOTE_HOST = "192.0.2.0"; // NOPMD AvoidUsingHardCodedIP
	
	private static final String X_FORWARDED_FOR = "198.51.100.0"; // NOPMD AvoidUsingHardCodedIP
	
	ExtendedMDCInsertingServletFilter sut = new ExtendedMDCInsertingServletFilter();
	
	
	@Test
	public void testDoFilter() throws Exception {
		// setup
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foobar");
		request.setQueryString("baz=qux");
		request.setRemoteHost(REMOTE_HOST);
		request.addHeader("User-Agent", "sample-ua");
		request.addHeader("X-Forwarded-For", X_FORWARDED_FOR);
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = spy(new FilterChain() {
			@Override
			public void doFilter(ServletRequest req, ServletResponse res) {
				assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_REMOTE_HOST_MDC_KEY))
					.isEqualTo(REMOTE_HOST);
				assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_USER_AGENT_MDC_KEY))
					.isEqualTo("sample-ua");
				assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_METHOD))
					.isEqualTo("GET");
				assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_REQUEST_URI))
					.isEqualTo("/foobar");
				assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_REQUEST_URL))
					.isEqualTo("http://localhost/foobar");
				assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_QUERY_STRING))
					.isEqualTo("baz=qux");
				assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_X_FORWARDED_FOR))
					.isEqualTo(X_FORWARDED_FOR);
			}
		});
		// exercise
		sut.doFilter(request, response, chain);
		// verify
		verify(chain).doFilter(request, response);
		
		assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_REMOTE_HOST_MDC_KEY)).isNull();
		assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_USER_AGENT_MDC_KEY)).isNull();
		assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_METHOD)).isNull();
		assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_REQUEST_URI)).isNull();
		assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_REQUEST_URL)).isNull();
		assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_QUERY_STRING)).isNull();
		assertThat(MDC.get("req_" + ExtendedMDCInsertingServletFilter.REQUEST_X_FORWARDED_FOR)).isNull();
	}
}
