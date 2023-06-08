package org.java.demo.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.java.demo.api.dto.PizzaResponseDto;
import org.java.demo.pojo.OffertaSpeciale;
import org.java.demo.pojo.Pizza;
import org.java.demo.service.OffertaSpecialeService;
import org.java.demo.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;



@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class PizzaApiController {

	@Autowired
	private PizzaService pizzaService;
	
	@Autowired
	private OffertaSpecialeService offertaSpecialeService;

	@GetMapping("/pizze")
	public ResponseEntity<List<Pizza>> indexPizza() {
		
		List<Pizza> pizza = pizzaService.findAll();
		return new ResponseEntity<>(pizza, HttpStatus.OK);
	}
	
	@GetMapping("/pizze/{id}")
	public ResponseEntity<Pizza> showPizza(@PathVariable int id) {
		
		Pizza pizza = pizzaService.findById(id).get();
		return new ResponseEntity<>(pizza, HttpStatus.OK);
	}
	
	@PostMapping("/pizze/filtro")
	public ResponseEntity<List<Pizza>> filtroPizza(@RequestParam(required = false) String nome){
		
		List<Pizza> pizze = pizzaService.findByNome(nome);
		return new ResponseEntity<>(pizze,HttpStatus.OK);
	}
	
	@PostMapping("/pizze")
	public ResponseEntity<PizzaResponseDto> storePizza(
			@RequestBody Pizza pizza, 
			@Valid BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(
					new PizzaResponseDto(bindingResult), 
					HttpStatus.BAD_REQUEST
				);
		}
		
		pizza = pizzaService.save(pizza);
		return new ResponseEntity<>(
				new PizzaResponseDto(pizza), 
				HttpStatus.OK);	
	}
	
	@PutMapping("/pizze")
	public ResponseEntity<PizzaResponseDto> updatePizza(
			@RequestBody Pizza pizza,
			@Valid BindingResult bindingResult
		) {
		
		if (bindingResult.hasErrors()) {	
			return new ResponseEntity<>(
					new PizzaResponseDto(bindingResult), 
					HttpStatus.BAD_REQUEST
				);
		}
		
		pizza = pizzaService.save(pizza);
		return new ResponseEntity<>(
				new PizzaResponseDto(pizza), 
				HttpStatus.OK);	
	}
	
	@DeleteMapping("/pizze/{id}")
	public ResponseEntity<PizzaResponseDto> deletePizza(
			@PathVariable int id
		) {
		Optional<Pizza> optPizza = pizzaService.findById(id);
		if (optPizza.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);	
		}
		
		Pizza pizza = optPizza.get();
		List<OffertaSpeciale> offerteSpeciali = pizza.getOffertaSpeciali();
		for(OffertaSpeciale o : offerteSpeciali) {	
			offertaSpecialeService.deleteOfferte(o);
		}
		
		pizzaService.deletePizza(pizza);		
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<PizzaResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
	    
	    List<String> errors = ex.getConstraintViolations()
	            .stream()
	            .map(ConstraintViolation::getMessage)
	            .collect(Collectors.toList());

	   
	    return new ResponseEntity<>(
	            new PizzaResponseDto(errors),
	            HttpStatus.BAD_REQUEST
	    );
	}
}
