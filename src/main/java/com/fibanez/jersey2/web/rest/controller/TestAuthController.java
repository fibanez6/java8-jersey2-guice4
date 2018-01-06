package com.fibanez.jersey2.web.rest.controller;

import com.fibanez.jersey2.domain.Param;
import com.fibanez.jersey2.security.Secured;
import com.fibanez.jersey2.web.rest.dto.GenericResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/")
@Secured
@Produces(MediaType.APPLICATION_JSON)
public class TestAuthController {

    @GET
    @Path("/{"+ Param.TOKEN_SUBJECT+" : [A-Za-z0-9]*}")
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
