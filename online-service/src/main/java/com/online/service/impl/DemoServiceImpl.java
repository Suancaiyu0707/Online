package com.online.service.impl;


import com.online.service.DemoService;

public class DemoServiceImpl implements DemoService {

	public void sayHello(String word) {
		System.out.println("==========sayHello======"+word);
		
	}

	public String getHi() {
		return "xuzf";
	}

}
