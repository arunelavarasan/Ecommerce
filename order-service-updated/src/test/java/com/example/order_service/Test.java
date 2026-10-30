package com.example.order_service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args) {
		Map<Integer,Integer> mp = Arrays.asList(11,12,2,34,5).stream().map(x -> x*2).collect(Collectors.toMap(x -> x,x -> x/2));
		mp.forEach((a,b) -> System.out.println(a + " - " + b));
		System.out.println();
		mp.keySet().stream().collect(Collectors.toList()).forEach(System.out::println);
	}
}
