package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;
import com.example.demo.repo.EmployeRepo;

@RestController
public class ElasticSearchCRUDop {

	@Autowired
	EmployeRepo repo;

	@PostMapping("/employee")
	public Employee saveEmploye(@RequestBody Employee employee) {
		repo.save(employee);
		return employee;
	}
	
	@PostMapping("/employees")
	public List<Employee> saveAll(@RequestBody List<Employee> employees){
		repo.save(employees);
		return employees;
	}

	@GetMapping("/employees")
	public List<Employee> getAll() {
		List<Employee> employees = new ArrayList<Employee>();
		Iterable<Employee> allEmployees = repo.findAll();
		for (Employee employe : allEmployees) {
			employees.add(employe);
		}
		return employees;
	}

	@GetMapping("/employee/{id}")
	public Employee getEmployee(@PathVariable int id) {

		return repo.findOne(id);
	}

	@GetMapping("/employee_name/{name}")
	public Employee getByName(@PathVariable String name) {

		return repo.findByName(name);
	}

	@DeleteMapping("/employee/{id}")
	public String deleteEmployee(@PathVariable int id) {
		repo.delete(id);
		return id + "::DeletedSuccessfully";
	}

	@PutMapping("/employee")
	public Employee saveOrUpdateEmploye(@RequestBody Employee employe) {
		return repo.save(employe);
	}

	@DeleteMapping("/employees")
	public String deleteAll() {
		repo.deleteAll();
		return "ALL Employees DeletedSuccessfully";
	}

}
