package br.com.pereiraeng.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HTMLtests {

	@Test
	void testHTML() {
		String head = HTML.getHead();
		assertEquals("<!DOCTYPE html>\n<html>\n<head>\n<title></title>\n</head>\n<body>\n", head);
	}

}
