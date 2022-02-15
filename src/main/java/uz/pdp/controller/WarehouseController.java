package uz.pdp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uz.pdp.entity.Warehouse;
import uz.pdp.payload.Result;
import uz.pdp.service.WarehouseService;

@RestController
@RequestMapping(value = "/warehouse")
public class WarehouseController {

	@Autowired
	WarehouseService warehouseService;
	
	@PostMapping
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR')")
	public HttpEntity<?> addWarehouse(@Valid @RequestBody Warehouse warehouse) {
		Result result = warehouseService.addWarehous(warehouse);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.BAD_REQUEST).body(result);
	}
	@GetMapping
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR')")
	public HttpEntity<?> getWarehouses(){
		List<Warehouse> warehouses = warehouseService.getWarehouses();
		return ResponseEntity.status(warehouses.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.OK).body(warehouses);
	}
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR','OPERATOR')")
	@GetMapping(value = "/{id}")
	public HttpEntity<?> getWarehous(@PathVariable Integer id) {
		Result result = warehouseService.getWarehi(id);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND).body(result);
	}
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR')")
	@PutMapping(value = "/{id}")
	public HttpEntity<?> editWarehouse(@PathVariable Integer id, @Valid @RequestBody Warehouse warehouse) {
		Result result = warehouseService.editWarehouse(id, warehouse);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND).body(result);
	}
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public HttpEntity<?> deleteWarehiuse(@PathVariable Integer id) {
		Result result = warehouseService.deleteWarehouse(id);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND).body(result);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
}
