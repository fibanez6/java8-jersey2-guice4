package com.fibanez.jersey2.web.rest.controller;

import com.fibanez.jersey2.domain.Param;
import com.fibanez.jersey2.service.TokenService;
import com.fibanez.jersey2.web.rest.dto.GenericResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private TokenService tokenService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(@QueryParam(Param.TOKEN_SUBJECT) String authId) {

        String token = tokenService.createJsonWebToken(authId);

        GenericResponse response = new GenericResponse.Builder()
                .status("OK")
                .type("TOKEN")
                .message(token)
                .build();
        return Response.ok(response).build();
    }

}
