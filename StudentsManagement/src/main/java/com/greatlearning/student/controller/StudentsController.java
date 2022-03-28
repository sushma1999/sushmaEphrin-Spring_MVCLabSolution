package com.greatlearning.student.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greatlearning.student.model.Student;
import com.greatlearning.student.service.StudentService;
@Controller
@RequestMapping("/students")

public class StudentsController {

	@Autowired
	private StudentService studentService;

	// add mapping for list of students using "/list"
	@RequestMapping("/list")
	public String listStudents(Model theModel) {

		// get list of students from database
		List<Student> theStudents = studentService.findAll();

		// add to the spring model
		theModel.addAttribute("Students", theStudents);
		return "list-students";
	}

	@RequestMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Student thestudent = new Student();

		// create model attribute to bind form data
		theModel.addAttribute("Student", thestudent);
		return "student-form";
	}

	@RequestMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("studentId") int id, Model theModel) {

		// getting the student from the service by id
		Student thestudent = studentService.findById(id);

		// set students as an attribute to pre-populate the form
		theModel.addAttribute("Student", thestudent);

		// send over to our form
		return "student-form";
	}

	@PostMapping("/save")
	public String saveStudent(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("department") String department, @RequestParam("country") String country) {

		System.out.println(id);
		Student thestudent;
		if (id != 0) {
			thestudent = studentService.findById(id);
			thestudent.setName(name);
			thestudent.setDepartment(department);
			thestudent.setCountry(country);
		} else
			thestudent = new Student(name, department, country);

		// save the student details
		studentService.save(thestudent);

		// using a redirect to prevent duplicate submissions
		return "redirect:/students/list";

	}

	@RequestMapping("/delete")
	public String delete(@RequestParam("studentId") int id) {

		// delete the student along with it's details
		studentService.deleteById(id);

		// redirect to /students/list --> main page
		return "redirect:/students/list";
	}
}