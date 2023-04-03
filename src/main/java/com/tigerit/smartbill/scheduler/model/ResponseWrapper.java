package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.common.model.api.response.common.GenericAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseWrapper extends ResponseEntity<GenericAPIResponse> {

	public ResponseWrapper(HttpStatus status) {
		super(status);
		// TODO Auto-generated constructor stub
	}

}
