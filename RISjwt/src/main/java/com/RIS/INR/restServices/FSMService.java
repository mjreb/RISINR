package com.RIS.INR.restServices;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONException;

@Path("/RISFSM")
public class FSMService {
	
     /**
     *
     * @param perfil
     * @return
     * @throws JSONException
     * //http://localhost:8081/RISSERVER/rest/RISFSM/fsm2/Admin //[General | Admin | Recepcionista |...]
     */
    @GET
    @Path("/fsm2/{perfil}")
    @Produces("application/json")
    public Response getFSM2(@PathParam("perfil") String perfil) throws JSONException {
        String path = "";
        switch (perfil) {
            case "General":
                path = FSMService.class.getClassLoader().getResource("FSM/General.json").getFile();
                break;
            case "Admin":
                path = FSMService.class.getClassLoader().getResource("FSM/Admin.json").getFile();
                break;
            case "Recepcionista":
                path = FSMService.class.getClassLoader().getResource("FSM/Admin.json").getFile();
                break;
            default:
                path = FSMService.class.getClassLoader().getResource("FSM/General.json").getFile(); // En caso de perfil erroneo.
                break;
        }
        File file = new File(path);
        ResponseBuilder responseBuilder = Response.ok((Object) file);
        return responseBuilder.status(200).build();
    }
}
