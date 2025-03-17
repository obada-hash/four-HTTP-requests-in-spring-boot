package com.book.bootbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class BootbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootbookApplication.class, args);
	}

}



@RestController
class Response {


	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(){
		return "Hello World";
	}
}

class Coffee{
	private final String id;
	private String name;

	public Coffee() {
		this.id = UUID.randomUUID().toString(); // Generate a random ID
	}

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Coffee(String name){
		this(UUID.randomUUID().toString(),name);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

@RestController
@RequestMapping("/coffees/")
class RestApiDemoController {
	private List<Coffee> coffees = new ArrayList<>();
	public RestApiDemoController() {
		coffees.addAll(List.of(
				new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")
		));
	}
	@GetMapping()
	Iterable<Coffee> getCoffees() {
		return coffees;
	}


	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id){

		for(Coffee c: coffees){
			if(c.getId().equals(id)){
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}

	@PostMapping()
	Coffee postCoffee(@RequestBody Coffee coffee) {
		coffees.add(coffee);
		return coffee;
	}


	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee){

		int index = -1;

		for(Coffee c : coffees){
			if(c.getId().equals(id)){
				index = coffees.indexOf(c);
				coffees.set(index, coffee);
				break;
			}
		}

		return (index == -1) ?
				new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED): //201
				new ResponseEntity<>(coffee, HttpStatus.OK); // 200
	}


	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id){
		coffees.removeIf(c -> c.getId().equals(id));
//		for(Coffee c : coffees){
//			if(c.getId().equals(id)){
//				coffees.remove(c);
//				return true;
//			}
//		}
//		return false;
	}
}
