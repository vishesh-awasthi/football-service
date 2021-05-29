package com.publicis.sapient.service;

import com.publicis.sapient.exception.InvalidParamException;
import com.publicis.sapient.exception.ResourceNotFound;
import com.publicis.sapient.model.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class AbstractTeamService {

    protected void checkArguments(String countryName, String leagueName, String teamName) throws InvalidParamException {
        if (StringUtils.isEmpty(countryName) || StringUtils.isEmpty(leagueName) || StringUtils.isEmpty(teamName)) {
            throw new InvalidParamException("One of the expected param is missing: countryName, leagueName or teamName");
        }
    }

    protected void checkApiResponse(ApiResponse apiResponse) throws ResourceNotFound {
        if (apiResponse.isErrorResponse()) {
            if (apiResponse.getError() != null) {
                throw new ResourceNotFound(apiResponse.getError().getMessage());
            } else {
                throw new ResourceNotFound("Error occurred while parsing the third party api response");
            }
        }
    }

    /**
     * Constructs the base headers required for rest call.
     *
     * @param action represents the type of action required
     * @return {@link HttpHeaders}
     */
    protected HttpHeaders getBaseHeaders(String action, String apiSecretKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("action", action);
        headers.add("APIkey", apiSecretKey);
        return headers;
    }
}
