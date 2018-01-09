package com.fibanez.jersey2.web.rest.controller;

import com.fibanez.jersey2.domain.Param;
import com.fibanez.jersey2.security.Secured;
import com.fibanez.jersey2.web.rest.dto.GenericResponse;
import io.swagger.annotations.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
@Secured
@Produces(MediaType.APPLICATION_JSON)
@Api( value = "Subject",
      description = "JWT authentication test")
public class TestAuthController {

    @GET
    @Path("/{"+ Param.TOKEN_SUBJECT+": [A-Za-z0-9]*}")
    @ApiOperation(
            value = "JWT AUTH VALIDATOR",
            notes = "Validate if the subject is correctly authenticated"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",
                    value = "token",
                    required = true,
                    dataType = "string",
                    defaultValue = "Bearer {{JWToken}}",
                    paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 409, message = "Conflict - subject query param and subject JWT do not match"),
            @ApiResponse(
                    code = 200,
                    message = "Authenticated Subject",
                    response = GenericResponse.class
            )
    })
    public Response validateToken(
            @Context SecurityContext securityContext,
            @PathParam(Param.TOKEN_SUBJECT) String subject) {

        if (!securityContext.getUserPrincipal().getName().equals(subject)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        GenericResponse response = new GenericResponse.Builder()
                .status("OK")
                .type("TOKEN")
                .message("Authenticated")
                .build();

        return Response.ok(response).build();
    }

}
