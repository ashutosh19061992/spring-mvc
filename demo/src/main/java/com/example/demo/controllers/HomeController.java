package com.example.demo.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Employe;
import com.example.demo.response.Response;
import com.example.demo.service.EmployeService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
public class HomeController {

	@Autowired
	private EmployeService employeService;

	@RequestMapping(method = RequestMethod.GET, value = "/employees")
	public List<Employe> list() {
		return employeService.getAllEmploye();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/save")
	public ModelAndView empoloye() {
		ModelAndView andView = new ModelAndView("employe");
		return andView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public ModelAndView getAllEmployee() {
		ModelAndView modelAndView = new ModelAndView("employeList");
		modelAndView.addObject("tittle", "Employe List");
		List<Employe> employeList = employeService.getAllEmploye();
		modelAndView.addObject("empList", employeList);
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveEmployeDetails")
	public ModelAndView addEmployee(@Valid @ModelAttribute Employe employe, BindingResult bindingResult) {

		Employe addedEmploye = null;
		ModelAndView modelAndView = new ModelAndView("employe");

		try {
			addedEmploye = employeService.saveEmploye(employe);
			if (addedEmploye != null) {
				modelAndView.addObject("hasError", false);
				modelAndView.addObject("messageType", "success");
				modelAndView.addObject("message", addedEmploye.getName() + " deatils added successfully. !!");
			} else {
				modelAndView.addObject("hasError", true);
				modelAndView.addObject("messageType", "danger");
				modelAndView.addObject("message", "Deatils already exists !!");
			}
		} catch (ConstraintViolationException e) {

			Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
			List<String> errorMessages = new ArrayList<String>(constraintViolations.size());
			errorMessages.addAll(constraintViolations.stream()
					.map(constraintViolation -> constraintViolation.getMessage()).collect(Collectors.toList()));

			modelAndView.addObject("messageType", "danger");
			modelAndView.addObject("validMessages", errorMessages);
			modelAndView.addObject("hasError", true);
		}
		modelAndView.addObject("tittle", "Add Employe");
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/edit/employe/{employeId}")
	@ResponseBody
	public Employe editEmployeDetails(@PathVariable("employeId") Integer employeId) {
		Employe empDetails = this.employeService.getEmployeById(employeId);
		return empDetails;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/employe/{employeId}")
	@ResponseBody
	public Response deleteEmployeDetails(@PathVariable("employeId") Integer employeId) {
		this.employeService.deleteEmploye(employeId);
		return new Response("200", "Deleted Successfully..!!");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update/employe/")
	@ResponseBody
	public Response updateEmployeDetails(@RequestBody Employe employe) {
		Employe updatedEmploye = this.employeService.updateEmploye(employe.getId(), employe);
		if (updatedEmploye != null) {
			return new Response("Ok", updatedEmploye);
		}
		return new Response("404", "unable to update");
	}
}
