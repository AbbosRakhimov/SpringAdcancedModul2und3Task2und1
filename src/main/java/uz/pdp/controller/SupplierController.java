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

import uz.pdp.entity.Supplier;
import uz.pdp.payload.Result;
import uz.pdp.payload.SupplierDto;
import uz.pdp.service.SupplierService;

@RestController
@RequestMapping(value = "/supplier")
public class SupplierController {

	@Autowired
	SupplierService supplierService;
	
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR')")
	@PostMapping
	public HttpEntity<?> addSupplier(@Valid @RequestBody SupplierDto supplierDto) {
		Result result = supplierService.addSupplier(supplierDto);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.BAD_REQUEST).body(result);
	}
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR')")
	@GetMapping
	public HttpEntity<?> getResults(){
		List<Supplier> suppliers = supplierService.getSuppliers();
		return ResponseEntity.status(suppliers.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.OK).body(suppliers);
	}
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR','OPERATOR')")
	@GetMapping(value = "/{id}")
	public HttpEntity<?> getSupplier(@PathVariable Integer id) {
		Result result = supplierService.getSupplier(id);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND).body(result);
	}
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN','MODERATOR')")
	@PutMapping(value = "/{id}")
	public HttpEntity<?> editeSuplier(@PathVariable Integer id, @Valid @RequestBody SupplierDto supplierDto) {
		Result result = supplierService.editSupplier(id, supplierDto);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(result);
	}
	@PreAuthorize(value = "hasAnyRole('SUPER_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public HttpEntity<?> deleteSuppleir(@PathVariable Integer id) {
		Result result = supplierService.deleSupplier(id);
		return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(result);
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
