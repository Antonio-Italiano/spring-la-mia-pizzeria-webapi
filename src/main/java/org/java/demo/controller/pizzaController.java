package org.java.demo.controller;


import java.util.List;
import java.util.Optional;

import org.java.demo.pojo.Ingredienti;
import org.java.demo.pojo.Pizza;
import org.java.demo.service.IngredientiService;
import org.java.demo.service.PizzaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class pizzaController {

	@Autowired
	private PizzaService pizzaService;
	
	@Autowired
	private IngredientiService ingredientiService;
	
	@GetMapping("/user/pizze")
	public String index(Model model) {
		
		List<Pizza> pizze = pizzaService.findAll();
		
		model.addAttribute("pizze", pizze);
		
		return "index-pizza";
	}
	
	
	@GetMapping("/user/pizze/{id}")
	public String show(
			Model model,
			@PathVariable("id") int id
	) {
		
		Optional<Pizza> optPizza = pizzaService.findByIdWithOffertaSpeciale(id);
		Pizza pizza = optPizza.get();
		
		Optional<Pizza> firstPizzaOpt = pizzaService.findByIdWithOffertaSpeciale(id);
		Pizza offertePizza = firstPizzaOpt.get();
		
		List<Ingredienti> ingredienti = pizza.getIngredienti();
		
		model.addAttribute("ingredienti",ingredienti);
		model.addAttribute("offerte", offertePizza.getOffertaSpeciali());
		model.addAttribute("pizza", pizza);
		
		return "show";
	}	

	@GetMapping("/admin/pizze/create")
	public String getCreate(Model model) {
		
		List<Ingredienti> ingredienti = ingredientiService.findAll();
		
		model.addAttribute("ingredienti",ingredienti);
		model.addAttribute("pizza",new Pizza());
		return "create";
	}
	
	@PostMapping("/admin/pizze/create")
	public String storeBook(@ModelAttribute Pizza pizza) {
		
		pizzaService.save(pizza);
		
		return "redirect:/user/pizze/" + pizza.getId();
	}
	
	@PostMapping("/user/pizze/filter")
	public String indexFiltro(Model model,@RequestParam(required = false) String nome) {
		
		List<Pizza> pizze = pizzaService.findByNome(nome);
		
		model.addAttribute("pizze", pizze);
		model.addAttribute("nome",nome);
		
		return "index-pizza";
	}
	
	@GetMapping("/admin/pizze/update/{id}")
	public String editPizza(
			Model model,
			@PathVariable int id
		) {
		
		Optional<Pizza> pizzaOpt = pizzaService.findById(id);
		Pizza pizza = pizzaOpt.get();
		model.addAttribute("pizza", pizza);
		List<Ingredienti> ingredienti = ingredientiService.findAll();		
		model.addAttribute("ingredienti",ingredienti);
		
		return "update";
	}
	@PostMapping("/admin/pizze/update/{id}")
	public String updatePizza(
			@PathVariable int id,
			@ModelAttribute Pizza pizza
		) {
		
		pizzaService.save(pizza);
		
		return "redirect:/user/pizze/" + pizza.getId();
	}
	
	@GetMapping("/admin/pizze/delete/{id}")
	public String deletePizza(@PathVariable int id) {
		Optional<Pizza> pizzaOpt = pizzaService.findById(id);
		Pizza pizza = pizzaOpt.get();
		pizzaService.deletePizza(pizza);
		return "redirect:/user/pizze";
	}
}

