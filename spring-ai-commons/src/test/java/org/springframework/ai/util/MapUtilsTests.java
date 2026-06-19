/*
 * Copyright 2023-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ai.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MapUtils}.
 *
 * @author Evan Yao
 */
class MapUtilsTests {

	@Test
	void unwrapOptionalsWithEmptyOptional() {
		Map<String, Object> map = new HashMap<>();
		map.put("refusal", Optional.empty());
		map.put("key", "value");

		Map<String, Object> result = MapUtils.unwrapOptionals(map);

		assertThat(result).containsEntry("key", "value");
		assertThat(result).containsKey("refusal");
		assertThat(result.get("refusal")).isNull();
	}

	@Test
	void unwrapOptionalsWithPresentOptional() {
		Map<String, Object> map = new HashMap<>();
		map.put("toolCalls", Optional.of("some-tool-call"));
		map.put("key", "value");

		Map<String, Object> result = MapUtils.unwrapOptionals(map);

		assertThat(result).containsEntry("key", "value");
		assertThat(result).containsEntry("toolCalls", "some-tool-call");
	}

	@Test
	void unwrapOptionalsWithMixedValues() {
		Map<String, Object> map = new HashMap<>();
		map.put("refusal", Optional.empty());
		map.put("toolCalls", Optional.of("tc-data"));
		map.put("regularKey", "regularValue");
		map.put("numericKey", 42);

		Map<String, Object> result = MapUtils.unwrapOptionals(map);

		assertThat(result).hasSize(4);
		assertThat(result.get("refusal")).isNull();
		assertThat(result.get("toolCalls")).isEqualTo("tc-data");
		assertThat(result.get("regularKey")).isEqualTo("regularValue");
		assertThat(result.get("numericKey")).isEqualTo(42);
	}

	@Test
	void unwrapOptionalsWithNoOptionals() {
		Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);

		Map<String, Object> result = MapUtils.unwrapOptionals(map);

		assertThat(result).isEqualTo(map);
	}

	@Test
	void unwrapOptionalsWithEmptyMap() {
		Map<String, Object> result = MapUtils.unwrapOptionals(new HashMap<>());
		assertThat(result).isEmpty();
	}

	@Test
	void unwrapOptionalsDoesNotModifyOriginal() {
		Map<String, Object> map = new HashMap<>();
		map.put("key", Optional.of("value"));

		MapUtils.unwrapOptionals(map);

		assertThat(map.get("key")).isInstanceOf(Optional.class);
	}

}
