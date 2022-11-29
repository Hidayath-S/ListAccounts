package jar;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListAccountsController {
	@Autowired
	ListAccountsRepo repo;

	@PostMapping(path = "Accounts", consumes = "application/json", produces = "application/json")
	public ResponseEntity addAccount(@Valid @RequestBody ListAccountsEntity accountsEntity, Errors errors) {
		ErrorResponse er = new ErrorResponse();
		String response = "";
		if (errors.hasErrors()) {
			response = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(","));
			er.setErrorCode(HttpStatus.NOT_ACCEPTABLE.value());
			er.setErrorMessage(response);
			return ResponseEntity.ok(er);
		} else if(repo.existsById(accountsEntity.getAccountNumber()) == false) {
			repo.save(accountsEntity);
			response = "Account details added successfully";
			return ResponseEntity.ok(response);
		} else {
			er.setErrorCode(HttpStatus.NOT_ACCEPTABLE.value());
			er.setErrorMessage("Account already exists!");
			return ResponseEntity.ok(er);
		}
			
			

		}

//	@GetMapping(path = "Account/{accountNum}", produces = "application/json")
//	public Object getAcctDetails(@PathVariable long accountNum) {
//		
//		ErrorResponse er = new ErrorResponse();
//		boolean status = repo.existsById(accountNum);
//		if (status == false) {
//			er.setErrorCode(HttpStatus.BAD_REQUEST.value());
//			er.setErrorMessage("No account found with accountNumber=" + accountNum);
//
//			return er;
//
//		}
//Optional<ListAccountsEntity> acctDetails = repo.findById(accountNum);
//		return acctDetails;
//
//	}

	@GetMapping(path = "Account", produces = "application/json")
	public Object getAcctDetailsByAcctNum(@RequestParam long accountNum) {

		ErrorResponse er = new ErrorResponse();
		boolean status = repo.existsById(accountNum);
		if (status == false) {
			er.setErrorCode(HttpStatus.BAD_REQUEST.value());
			er.setErrorMessage("No account found with accountNumber=" + accountNum);

			return er;

		}
		Optional<ListAccountsEntity> acctDetails = repo.findById(accountNum);
		return acctDetails;

	}

	@PutMapping(path = "Accounts", consumes = "application/json", produces = "application/json")
	public ResponseEntity updateAccount(@RequestBody ListAccountsEntity accountsEntity) {

		UpdateResponse updateResponse = new UpdateResponse();
		repo.save(accountsEntity);
		updateResponse.setCode(HttpStatus.ACCEPTED.value());
		updateResponse.setMessage("Account details updated successfully");

		return ResponseEntity.ok(updateResponse);

	}

}
