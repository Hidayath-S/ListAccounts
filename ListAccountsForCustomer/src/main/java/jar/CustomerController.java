package jar;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
	
	@Autowired
	CustDetailsRepo repo;
	
	@GetMapping(path="customer", produces="application/json")
public Object getCustDetailsByCustId(@RequestParam String custId) {
		
		ErrorResponse er = new ErrorResponse();
		boolean status = repo.existsById(custId);
		if (status == false) {
			er.setErrorCode(HttpStatus.BAD_REQUEST.value());
			er.setErrorMessage("No customer found with custId=" + custId);

			return er;

		}
Optional<CustDetailsEntity> custDetails = repo.findById(custId);
		return custDetails;

	}
	

}
