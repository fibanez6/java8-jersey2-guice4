package com.fibanez.jersey2.web.rest.controller;

import com.fibanez.jersey2.domain.Param;
import com.fibanez.jersey2.service.TokenService;
import com.fibanez.jersey2.web.rest.dto.GenericResponse;
import io.swagger.annotations.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Api( value = "Auth",
        description = "Authentication Resource")
public class AuthController {

    @Inject
    private TokenService tokenService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "JWT AUTH GENERATOR",
            notes = "Given a subject, it generates a JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(
                    code = 200,
                    message = "JSON Web Token",
                    response = GenericResponse.class
            )
    })
    public Response getToken(@QueryParam(Param.TOKEN_SUBJECT) String authId) {

        if (authId == null || authId.trim().isEmpty()) {
            throw new BadRequestException("INVALID "+Param.TOKEN_SUBJECT+" PARAM");
        }

        String token = tokenService.createJsonWebToken(authId);

        GenericResponse response = new GenericResponse.Builder()
                .status("OK")
                .type("TOKEN")
                .message(token)
                .build();
        return Response.ok(response).build();
    }

}
