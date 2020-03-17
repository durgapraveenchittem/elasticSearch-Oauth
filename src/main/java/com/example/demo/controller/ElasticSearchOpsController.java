package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;
import com.example.demo.service.ElasticSearchopsService;
import com.example.demo.view.AggregationsView;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class ElasticSearchOpsController {

	@Autowired
	ElasticSearchopsService service;

	@GetMapping("/age_range/{from}/{to}")
	public List<Employee> ageRange(@PathVariable int from, @PathVariable int to) {
		return service.ageRange(from, to);

	}

	@GetMapping("/sal_range/{from}/{to}")
	public List<Employee> salRange(@PathVariable int from, @PathVariable int to) {
		return service.salRange(from, to);

	}

	@GetMapping("/serachMultiField/{name}/{age}")
	public List<Employee> serachMultiField(@PathVariable String name, @PathVariable int age) {

		return service.serachMultiField(name, age);
	}

	@GetMapping("/customSearch/{input}")
	public List<Employee> customSearch(@PathVariable String input) {

		
		return service.customSearch(input);
	}

	@GetMapping("/search/{text}")
	public List<Employee> search(@PathVariable String text) {

		return service.search(text);
	}
	 
	@GetMapping("/minAge")
	public String minAge(){
	
		return service.minAge();
	}
	
	@GetMapping("/maxSal")
	public String maxsal(){
		
		return service.maxsal();
	}
	
	@GetMapping("/aggregations")
	public AggregationsView allAggregations(){
		
		return service.allAggregations();
	}
	
}
