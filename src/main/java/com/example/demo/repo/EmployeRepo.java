package com.example.demo.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.model.Employee;

public interface EmployeRepo extends ElasticsearchRepository<Employee, Integer> {

	Employee findByName(String name);

}
