package org.java.demo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.java.demo.auth.pojo.Role;
import org.java.demo.auth.pojo.User;
import org.java.demo.auth.service.RoleService;
import org.java.demo.auth.service.UserService;
import org.java.demo.pojo.Ingredienti;
import org.java.demo.pojo.OffertaSpeciale;
import org.java.demo.pojo.Pizza;
import org.java.demo.service.IngredientiService;
import org.java.demo.service.OffertaSpecialeService;
import org.java.demo.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringLaMiaPizzeriaCrudApplication implements CommandLineRunner {

	@Autowired
	private PizzaService pizzaService;
	public static void main(String[] args) {
		SpringApplication.run(SpringLaMiaPizzeriaCrudApplication.class, args);
	}
	
	@Autowired 
	private OffertaSpecialeService offertaSpecialeService;

	@Autowired
	private IngredientiService ingredientiService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void run(String... args) throws Exception{
		Pizza p = new Pizza("margherita", "descrizioneeeeee", "https://www.agricolapiano.com/blogpiano/wp-content/uploads/2022/10/Pizza-contemporanea.jpeg", 12.45);
		Pizza p1 = new Pizza("carbonara", "descrizioneeeeee", "https://latteriasorrentina.com/wp-content/uploads/2019/06/come-fare-la-vera-pizza-1764x882.jpg", 12.45);
		Pizza p2 = new Pizza("messinese", "descrizioneeeeee", "https://media-assets.lacucinaitaliana.it/photos/61fae816034a68e68c6375e0/1:1/pass/undefined", 12.45);
		Pizza p3 = new Pizza("capricciosa", "descrizioneeeeee", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMA55QHkG9zUWvw_h9NJhGD2FWlosbJpRHy-Qh8bmZauQt8efr_NAyUnymxkN84O8xWd0&usqp=CAU", 12.45);
		Pizza p4 = new Pizza("napoletana", "descrizioneeeeee", "https://foodohfood.it/wp-content/uploads/pizza-alta-soffice-facile-ricetta.jpg", 12.45);
		System.out.println(p);
		
		pizzaService.save(p);
		pizzaService.save(p1);
		pizzaService.save(p2);
		pizzaService.save(p3);
		pizzaService.save(p4);
		
		List<Pizza> pizze = pizzaService.findAll();
		
		System.out.println(pizze);
		
		OffertaSpeciale o = new OffertaSpeciale("1 day", LocalDate.now(), LocalDate.of(2023, 06, 01), 65, p);
		OffertaSpeciale o1 = new OffertaSpeciale("2 day", LocalDate.now(), LocalDate.of(2023, 06, 02), 45, p1);
		OffertaSpeciale o2 = new OffertaSpeciale("3 day", LocalDate.now(), LocalDate.of(2023, 06, 03), 25, p2);
		OffertaSpeciale o3 = new OffertaSpeciale("4 day", LocalDate.now(), LocalDate.of(2023, 06, 04), 15, p3);
		
		offertaSpecialeService.save(o);
		offertaSpecialeService.save(o1);
		offertaSpecialeService.save(o2);
		offertaSpecialeService.save(o3);
		
		for(Pizza pizza : pizze) {
			
			Optional<Pizza> optPizzaSconto = pizzaService.findByIdWithOffertaSpeciale(pizza.getId());
			Pizza pizzaSconto = optPizzaSconto.get();
			System.out.println(pizzaSconto.getOffertaSpeciali());
		}
		
		Ingredienti i1 = new Ingredienti("pomodoro");
		
		ingredientiService.save(i1);
		
		Ingredienti i2 = new Ingredienti("mozzarella");
		ingredientiService.save(i2);
		
		Ingredienti i3 = new Ingredienti("patatine");
		ingredientiService.save(i3);
		
		Ingredienti i4 = new Ingredienti("olio");
		ingredientiService.save(i4);
		
		Ingredienti i5 = new Ingredienti("farina");
		ingredientiService.save(i5);
	
		Role roleUser = new Role("USER");
		Role roleAdmin = new Role("ADMIN");
		
		roleService.save(roleUser);
		roleService.save(roleAdmin);
	
		final String pws = new BCryptPasswordEncoder().encode("pws");
		
		User userUser = new User("user", pws, roleUser);
		User userAdmin = new User("admin", pws, roleAdmin);
				
		userService.save(userUser);
		userService.save(userAdmin);
	}
	
}
