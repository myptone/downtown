package webapp;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


//Tutorial from
//http://www.leveluplunch.com/java/tutorials/014-post-json-to-spring-rest-webservice/
// Also check out http://www.baeldung.com/rest-template

@RestController
public class CarControllerTutorial {
    
	@RequestMapping(value = "/")
	public ResponseEntity<Car> get() {

	    Car car = new Car();
	    car.setColor("Blue");
	    car.setMiles(100);
	    car.setVIN("1234");

	    return new ResponseEntity<Car>(car, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Car> update(@RequestBody Car car) {

	    if (car != null) {
	        car.setMiles(car.getMiles() + 100);
	    }

	    
	    return new ResponseEntity<Car>(car, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cars", method = RequestMethod.POST)
	public ResponseEntity<List<Car>> update(@RequestBody List<Car> cars) {
		
		for(Car c : cars){
			c.setMiles(c.getMiles() + 100);
		}
	    
	    
	    return new ResponseEntity<List<Car>>(cars, HttpStatus.OK);
	}
    
	@RequestMapping(value = "/carsandtrucks", method = RequestMethod.POST)
	public ResponseEntity<RequestWrapper> updateWithMultipleObjects(
	        @RequestBody RequestWrapper requestWrapper) {

		for(Car c : requestWrapper.getCars()){
			c.setMiles(c.getMiles() + 100);
		}
		
		requestWrapper.getTruck().setMiles(requestWrapper.getTruck().getMiles() + 500);
	    

	    return new ResponseEntity<RequestWrapper>(requestWrapper, HttpStatus.OK);
	}
	
}

class Car {

    private String VIN;
    private String color;
    private Integer miles;
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getMiles() {
		return miles;
	}
	public void setMiles(Integer miles) {
		this.miles = miles;
	}

    //...
}

class Truck {

    private String VIN;
    private String color;
    private Integer miles;
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getMiles() {
		return miles;
	}
	public void setMiles(Integer miles) {
		this.miles = miles;
	}

    //...
}

class RequestWrapper {
    
	private List<Car> cars;
	private Truck truck;
	public List<Car> getCars() {
		return cars;
	}
	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
	public Truck getTruck() {
		return truck;
	}
	public void setTruck(Truck truck) {
		this.truck = truck;
	}

    //...
}
