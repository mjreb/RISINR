package com.RIS.INR.restServices;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.DAOSpecialization.AppointmentManager;
import com.RIS.MVC.model.DAOSpecialization.AreaManager;
import com.RIS.MVC.model.DAOSpecialization.DeviceManager;
import com.RIS.MVC.model.DAOSpecialization.EquipoImagenologiaManager;
import com.RIS.MVC.model.DAOSpecialization.RolManager;
import com.RIS.MVC.model.DAOSpecialization.SesionManager;
import com.RIS.MVC.model.DAOSpecialization.StudiesManager;
import com.RIS.MVC.model.DAOSpecialization.StudyRequestManager;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.RIS.MVC.model.DAOSpecialization.UsersManager;
import com.RIS.MVC.model.JPA.entities.AreaDeServicio;
import com.RIS.MVC.model.JPA.entities.Estudio;
import com.RIS.MVC.model.JPA.entities.Rol;
import com.RIS.MVC.model.JPA.entities.Usuario;
import com.RIS.MVC.model.JPA.entities.UsuarioPK;
import com.RIS.MVC.model.daoInterface.ServicesManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.RIS.security.Cybersecurity;
import io.jsonwebtoken.Claims;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import javax.ws.rs.core.MultivaluedHashMap;

@Path("/USRSesionRST")
public class RISRESTFulOrchestrator {

    private final ServicesManager sm;
    private static String authString;

    public RISRESTFulOrchestrator() {
        System.out.println("Ejecutando spring inyeccion de dependencias");
        sm = new ServicesManager("AplicationContext.xml");
        /*if (sm.getServiciosRegistrados() != null) {
            for (String serviciosRegistrado : sm.getServiciosRegistrados()) {
                    System.out.println("######Registrando Servicio: " + serviciosRegistrado);
            }
        }*/
    }
    
    
    
    //http://localhost:8081/RISINR/rest/USRSesionRST/login
    //http://localhost:8080/RISSERVER/rest/USRSesionRST/login
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request) throws JSONException, NoSuchAlgorithmException {
        String json = "";
        Usuario tmpusr = null;
        JSONArray jsondatos = new JSONArray();
        JSONObject mensajedelogin = new JSONObject();
        //System.out.println("--Buscando usuarios---");
        UsersManager userRef = (UsersManager) sm.getServicio("usersManager");
        if (userRef != null) {
            if (userRef instanceof UsersManager) {
                //System.out.println("---Accediendo via Spring----");
                ((GenericDAOFacade<?>) userRef).setEntityManager(sm.getEntityManager()); //registrar el entitymanager para el bean correpsondiente                  
                tmpusr = userRef.authenticate(authString, userRef);
                
                
                if (tmpusr == null) {
                    String result = "Error Usuario no autorizado ******\n\n";
                    return Response.status(Response.Status.UNAUTHORIZED).entity(result).build();
                } else {
                    System.out.println("Nombre: " + tmpusr.getApellidoMaterno() + " " + tmpusr.getApellidoPaterno() + " " + tmpusr.getNombre());

                    ArrayNode perfilNodeArray=userRef.nodoPerfiles(tmpusr.getPerfilCollection());
                    //System.out.println(perfilNodeArray.toString());
                    JsonNode jsonTree = new ObjectMapper().createObjectNode()
                            .put("Nombre",tmpusr.getNombre())
                            .put("Apaterno",tmpusr.getApellidoPaterno())
                            .put("Amaterno",tmpusr.getApellidoMaterno())
                            .put("Area",tmpusr.getAreaidArea().getNombre())
                            .put("perfil",perfilNodeArray.toString());
                    
                    jsondatos.put(jsonTree);
                    String payload = jsonTree.toString();
                    Cybersecurity loginsecurity = new Cybersecurity();
                    loginsecurity.generarLlaves();
                    String llavelogin = loginsecurity.getLlavePublica();
                    String firmalogin = loginsecurity.encriptarDatos(payload);
                    System.out.println(firmalogin);
                    mensajedelogin.put("llavepublica", llavelogin);
                    mensajedelogin.put("JWT", firmalogin);
                    
                    

                    //Analizar cifrado de datos
                    //System.out.println(jsonTree.toString());
                    
                    //OJO CON LOS PERFILES MULTIPLES
                    HttpSession session = request.getSession();
                    long horainicio=session.getCreationTime();
                    String idusr=tmpusr.getUsuarioID();
                    UsuarioPK llaveprimaria = tmpusr.getUsuarioPK();
                    int numempl = llaveprimaria.getNumEmpleado();
                    String usercurp = llaveprimaria.getCurp();
                    System.out.println("El numero de empleado es:"+ numempl);
                    System.out.println("El curp de empleado es:"+ usercurp);
                    session.setAttribute("idusr",idusr);
                    session.setAttribute("nombre", tmpusr.getNombre()+" "+tmpusr.getApellidoPaterno()+" "+tmpusr.getApellidoMaterno());
                    session.setAttribute("area",tmpusr.getAreaidArea().getNombre());
                    session.setAttribute("perfil",perfilNodeArray.toString());
                    session.setAttribute("horaInicio",horainicio);
                    String ipremota=request.getRemoteAddr();
                    session.setMaxInactiveInterval(10*60); //10 MINUTOS 
                    
                    SesionManager sesionm=new SesionManager();
                    sesionm.setEntityManager(sm.getEntityManager());
                    sesionm.createSesion(horainicio, idusr, ipremota, numempl, usercurp);
                   
/*
                    ArrayList<Sesion> sesionCollection= new ArrayList<>();
                    Iterator<Perfil> iterator = tmpusr.getPerfilCollection().iterator();
                    // while loop
                    while (iterator.hasNext()) {
                     System.out.println("value= " + iterator.next());
                     iterator.next().getRol().getIdRol();
                     SesionPK sesionDBpk= new SesionPK(iterator.next().getRol().getIdRol(),tmpusr.getUsuarioPK().getNumEmpleado(), new Date());
                     Sesion sesionUsr=new Sesion(sesionDBpk);
                     sesionCollection.add(sesionUsr);
                    }                            
       
                    tmpusr.setSesionCollection(sesionCollection);
                */                 
                    return Response.status(Response.Status.OK).entity(mensajedelogin.toString()).build();
                }
            }
        }
        return Response.status(Response.Status.OK).entity(json).build();
    }
    //http://localhost:8080/RISSERVER/rest/USRSesionRST/logout
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        //LOGGER.info(String.format("Logout of user: %s with email: %s", session.getAttribute("username"),session.getAttribute("email")));
        System.out.println("INVALIDANDO SESION");
        if (session != null) {
            long horainicio=session.getCreationTime();
            String idusr=(String)session.getAttribute("idusr");
            session.removeAttribute("nombre");
            session.removeAttribute("area");
            session.removeAttribute("perfil");
            session.removeAttribute("horaInicio");
            session.removeAttribute("idusr");
            session.removeAttribute("rolActivo"); //se actualiza cuando se activa la vista correpsondiente al rol
            session.invalidate();
            //actualizar bd, sesion generada en login por cada usuario.
            SesionManager sesionm=new SesionManager();
            sesionm.setEntityManager(sm.getEntityManager());
            long horaFin=System.currentTimeMillis();
            sesionm.updateSesion(horainicio, idusr,horaFin);
        }
        //return Response.status(Response.Status.OK).entity("INVALIDANDO").build();
        return Response.status(Response.Status.OK).build();
    }
    
    //http://localhost:8080/RISSERVER/rest/USRSesionRST/session
    @GET
    @Path("/session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("nombre") != null) {
            JSONObject json = new JSONObject();
            //json.accumulate("user", session.getAttribute("username"));
            //json.accumulate("perfil", session.getAttribute("perfil"));
            json.put("nombre", session.getAttribute("nombre"));
            json.put("area", session.getAttribute("area"));
            json.put("perfil", session.getAttribute("perfil"));  
            json.put("rolActivo", session.getAttribute("rolActivo"));
            return Response.status(Response.Status.OK).entity(json.toString()).build();
        } else {
            //LOGGER.warn("User not logged in");
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
    }
    
    //actualización de roles cuando se selecciono una pagina con perfiles multiples
    @GET
    @Path("/rol/{nuevo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSessionRol(@PathParam("nuevo") String nuevorolSesionActiva,@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("nombre") != null) {
            long horainicio=session.getCreationTime();
            String idusr=(String)session.getAttribute("idusr"); 
            //actualizar tabla de sesion
            SesionManager sesionm=new SesionManager();
            sesionm.setEntityManager(sm.getEntityManager());
            sesionm.updateRol(horainicio, idusr, nuevorolSesionActiva);
            
            //actulizar el rolactivo en la sesion valida
            session.setAttribute("rolActivo",nuevorolSesionActiva); //actulizar rol al llegar a la vista correpsondiente
            
            JSONObject json = new JSONObject();
            json.put("nombre", session.getAttribute("nombre"));
            json.put("area", session.getAttribute("area"));
            //json.put("perfil", session.getAttribute("perfil"));  
            json.put("rolActivo", nuevorolSesionActiva);
            
            return Response.status(Response.Status.OK).entity(json.toString()).build();
        } else {
            //LOGGER.warn("User not logged in");
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
    }


    //http://localhost:8080/RISSERVER/rest/USRSesionRST/getAllUsuariosDependecias
    @GET
    @Path("/getAll/{entidad}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDependencias(@PathParam("entidad") String entidad) throws NoSuchAlgorithmException {
        JSONArray jsonArray = new JSONArray();
        JSONObject mensaje = new JSONObject();
        int identificador = 0;
        Object variable = null;
        if (sm.getServiciosRegistrados() != null) {
            /*for (String serviciosRegistrado : sm.getServiciosRegistrados()) {
                System.out.println("######Registrando DAO: " + serviciosRegistrado);
            }*/
            //Object userRef = sm.getServicio("usersManager"); //al regresar el servicio se le agrega su entityManager
            Object userRef = sm.getServicio(entidad); //al regresar el servicio se le agrega su entityManager
            if (userRef != null) {
                ((GenericDAOFacade<?>) userRef).setEntityManager(sm.getEntityManager()); //registrar el entitymanager para el bean correpsondiente de datos                                    
                if (userRef instanceof UsersManager) {
                    Collection<Usuario> listausrdao = ((UsersManager) userRef).getAllDataUser();
                    System.out.println("Numero de usuarios: "+listausrdao.size());
                    for (Usuario tmpusr : listausrdao) {
                    //se mantiene el orden, pero se regresa como arreglo de cadenas
                    ArrayNode perfilNodeArray=((UsersManager) userRef).nodoPerfiles(tmpusr.getPerfilCollection());
                    //System.out.println("Perfiles: "+perfilNodeArray.toString());
                    JsonNode resultSet = new ObjectMapper().createObjectNode()
                            .put("numEmp",tmpusr.getUsuarioPK().getNumEmpleado())
                            .put("nombre",tmpusr.getNombre())
                            .put("aPaterno",tmpusr.getApellidoPaterno())
                            .put("aMaterno",tmpusr.getApellidoMaterno())
                            .put("areaHosp",tmpusr.getAreaidArea().getNombre())
                            .put("CURP",tmpusr.getUsuarioPK().getCurp())
                            .put("Perfil",perfilNodeArray.toString());                   
                    
                        jsonArray.put(resultSet); 
                    }
                    identificador = 1;
                    //Bloque de seuridad informática utilizando JWT
                    
                    JSONArray jsonArray2 = jsonArray;
                    String payload = jsonArray2.toString();// Se convierte el tipo de la variable JSONArray a cadena de texto
                    Cybersecurity seguridad = new Cybersecurity();// Se instancia la clase Cibersecurity
                    seguridad.generarLlaves();//Se declara el método generarLLaves de la clase Cibersecurity
                    String llavepublica = seguridad.getLlavePublica();//Se obtiene la llavepública
                    String datoscifrados = seguridad.encriptarDatos(payload);// Se encripta firma los datos que se enviaran al Frontend
                    System.out.println("El Token es: "+datoscifrados);
                    System.out.println("Perfiles: "+jsonArray.toString());
                    //Se guarda la llave pública y el Token creado 
                    mensaje.put("llavepublica", llavepublica);
                    mensaje.put("JWT",datoscifrados);
                    
                    
                }else if(userRef instanceof RolManager){
                    Collection<Rol> listaroldao = ((RolManager) userRef).getAllDataRol();                    
                    for (Rol rol : listaroldao) {
                        //se mantiene el orden, pero se regresa como arreglo de cadenas
                        JsonNode resultSet = new ObjectMapper().createObjectNode()
                                .put("idRol",rol.getIdRol())
                                .put("Nombre",rol.getNombre())
                                .put("Descripcion",rol.getDescripcion());                   
                        jsonArray.put(resultSet); 
                    }
                    identificador = 2;
                }else if(userRef instanceof AreaManager){
                    Collection<AreaDeServicio> listaareadao = ((AreaManager) userRef).getAllDataArea();
                    for (AreaDeServicio area : listaareadao) {
                        JsonNode resultSet = new ObjectMapper().createObjectNode()
                                .put("idRol",area.getIdArea())
                                .put("Nombre",area.getNombre())
                                .put("Descripcion",area.getDescripcion());                   
                        jsonArray.put(resultSet);      
                        //jsonArray.put(area.toString());
                    }
                    identificador = 3;
                }
            }
            
            if (identificador == 1){
                variable = mensaje.toString();
            }else if (identificador ==2){
                variable = jsonArray.toString();
            }else if (identificador ==3){
                variable = jsonArray.toString();
            }
                
        }
        //System.out.println(jsonArray);
        return Response.status(Response.Status.OK).entity(variable).build();
    }
    
    @POST
    @Path("/RolEntity/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRole(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> formParams) {
        JSONObject datos = new JSONObject();
        RolManager rolentity=new RolManager();
        rolentity.setEntityManager(sm.getEntityManager());
        String rolid= formParams.getFirst("roleId");
        String nombre=formParams.getFirst("nombre");
        String descripcion=formParams.getFirst("descripcion"); 
        boolean respcrud=rolentity.resolveCRUD(operacion, rolid, nombre, descripcion);
        //System.out.println("Operacion CRUD: "+operacion);
        //respuesta objeto json{operacion:[true,false]} para toda operacion en: Create, Read, Update, Delete 
        datos.put(operacion, respcrud);
       return Response.status(Response.Status.OK).entity(datos.toString()).build();
    }
    
    @POST
    @Path("/AreaEntity/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addArea(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> formParams) {
        JSONObject datos = new JSONObject();
        AreaManager areaentity=new AreaManager();
        areaentity.setEntityManager(sm.getEntityManager());
        String areaid= formParams.getFirst("areaId");
        String nombre=formParams.getFirst("nombre");
        String descripcion=formParams.getFirst("descripcion"); 
        boolean respcrud=areaentity.resolveCRUD(operacion, areaid, nombre, descripcion);
        //System.out.println("Operacion CRUD: "+operacion);
        //respuesta objeto json{operacion:[true,false]} para toda operacion en: Create, Read, Update, Delete 
        datos.put(operacion, respcrud);
       return Response.status(Response.Status.OK).entity(datos.toString()).build();
    }  
    
    @POST
    @Path("/EquipoIMGEntity/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response manageEquipoIMG(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> formParams) {
        System.out.println("Equipos img");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();  
        EquipoImagenologiaManager EqIMGmanager = new EquipoImagenologiaManager();
        EqIMGmanager.setEntityManager(sm.getEntityManager());
        JSONArray datoseqp=null;
        switch (operacion) {
            case "ReadAll": 
                String nombre=formParams.getFirst("nombre");
                datoseqp=EqIMGmanager.getAllEquipoImagenologia();
                System.out.println(datoseqp);
                jsonArray.addPOJO(datoseqp);                        
            break;
            /*case "DeleteREgPK": 
                String nombre=formParams.getFirst("nombre");
                datoseqp=EqIMGmanager.getAllEquipoImagenologia();
                System.out.println(datoseqp);
                jsonArray.addPOJO(datoseqp);                        
            break;*/
        }        
       return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    } 

    @POST
    //@Path("/FormularioEqpImg/{CRUD}")
    @Path("/FormularioEqpImg")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //public Response resolveServicesPost(@PathParam("CRUD") String operation,FormDataMultiPart formParams) throws IOException {
    public Response resolveServicesPost(FormDataMultiPart formParams) throws IOException {            
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();
        
        /*System.out.println("Datos: " + formParams.getHeaders().toString());
        System.out.println("URL: " + formParams.getHeaders().getFirst("referer"));
        Iterator<String> it = formParams.getFields().keySet().iterator();
         while(it.hasNext()){
           String theKey = (String)it.next();
           if(formParams.getField(theKey).isSimple()){
            System.out.println("Clave: ["+theKey+"]-> "+formParams.getField(theKey).getValue());
           }else{
              System.out.println("Clave: ["+theKey+"]-> "+formParams.getField(theKey)); 
           } 
       } */
        EquipoImagenologiaManager EqIMGmanager = new EquipoImagenologiaManager();
        EqIMGmanager.setEntityManager(sm.getEntityManager());         
        JSONArray datoseqp=null; /*new JSONArray();*/
        //leer datos de la forma
        String oper=formParams.getField("Operation").getValue(); //definido en la forma
        System.out.println("**Operacion: "+oper);
        String nserie=formParams.getField("nserEQP").getValue();
        String nombreeqp=formParams.getField("nomEQP").getValue();
        String marcaeqp=formParams.getField("marcaEQP").getValue();
        String modeloeqp=formParams.getField("modeloEQP").getValue();
        String modalidaeqp=formParams.getField("modalEqp").getValue();
        String idarea=formParams.getField("areEqp").getValue();
        String estadoeqp=formParams.getField("edoEqp").getValue();  
        datoseqp=EqIMGmanager.createUpdateEquipoImagenologia(nserie,nombreeqp,marcaeqp,modeloeqp,modalidaeqp,Integer.parseInt(idarea),estadoeqp,oper);  
        if(datoseqp!=null){
         jsonArray.add("1");
        }else{
         jsonArray.add("0");
        }         
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
     }   
    /*
    @POST
    @Path("/pruebaseguridad/actualizarusuario")
    @Produces(MediaType.APPLICATION_JSON)
    public void pruebaactusuario(MultivaluedMap<String, String> informacion) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        String llavepublica = informacion.getFirst("llavepublica");
        String Token = informacion.getFirst("token");
        Cibersecurity seguridad1 = new Cibersecurity();
        Claims informa = seguridad1.desencriptarDatos(llavepublica, Token);
        //System.out.println(informa);
        
        String nombre= (String) informa.get("nombreUsr");
        String aPaterno= (String)informa.get("aPaterno");
        String aMaterno= (String)informa.get("aMaterno");
        String curpusr= (String)informa.get("curp");
        String areaasignada=(String)informa.get("areaAsignada"); //area de prueba
        List<String> roles=(List<String>) informa.get("usrPerf");// roles seleccionados
        
        System.out.println(nombre);
        System.out.println(aPaterno);
        System.out.println(aMaterno);
        System.out.println(curpusr);
        System.out.println(areaasignada);
        
        System.out.println("ROLES: "+roles);
        
        
       
    }
    
    */
    @POST
    @Path("/USREntity/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addusr(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> informacionJson) throws NoSuchAlgorithmException, InvalidKeySpecException{
        JSONObject datos = new JSONObject();
        UsersManager userentity=new UsersManager();
        userentity.setEntityManager(sm.getEntityManager());
        
        String llavepublica = informacionJson.getFirst("llavepublica");
        String Token = informacionJson.getFirst("token");
        //System.out.println("El Token recibido es: "+Token);
        Cybersecurity seguridad1 = new Cybersecurity();
        Claims infor = seguridad1.desencriptarDatos(llavepublica, Token);
        
        boolean respcrud=userentity.resolveCRUD(operacion,infor);
        //System.out.println("Operacion CRUD: "+operacion);
        //respuesta objeto json{operacion:[true,false]} para toda operacion en: Create, Read, Update, Delete 
        datos.put(operacion, respcrud);
       return Response.status(Response.Status.OK).entity(datos.toString()).build();
    }    
    
    //http://localhost:8080/RISSERVER/rest/USRSesionRST/resolveServioce/usersManager
    //http://localhost:8080/RISSERVER/rest/USRSesionRST/resolveServioce/studyManager
/*    
    @GET
    @Path("/resolveService/{servico}/{tiposerv}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resolveServices(@PathParam("servico") String servicio) {
        JSONArray jsonArray = new JSONArray();
        if (sm.getServiciosRegistrados() != null) {
            for (String serviciosRegistrado : sm.getServiciosRegistrados()) {
                System.out.println("######Registrando DAO: " + serviciosRegistrado);
            }
            Object objRef = sm.getServicio(servicio); //al regresar el servicio se le agrega su entityManager
            
            if (objRef != null) {
                if (objRef instanceof UsersManager) {
                    System.out.println("---Accediendo via AdministradorDaos----");
                    ((GenericDAOFacade<?>) objRef).setEntityManager(sm.getEntityManager()); //registrar el entitymanager para el bean correpsondiente de datos                    
                    Collection<Usuario> listausrdao = ((UsersManager) objRef).getAllDataTable();
                    for (Usuario o : listausrdao) {
                        System.out.println("Nombre: " + o.getNombre() + " Apellidos: " + o.getApellidoPaterno() + " " + o.getApellidoMaterno());
                        JSONObject json = new JSONObject();
                        json.put("nombre", o.getNombre());
                        json.put("ApellidosP", o.getApellidoPaterno());
                        json.put("ApellidosM", o.getApellidoMaterno());
                        jsonArray.put(json);
                    }
                }else if (objRef instanceof EstudyManager){

                }else if (objRef instanceof WKLsmanager){
                    
                }
            }
        }
        System.out.println(jsonArray);
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    }
  */
    /**
     * Servicio API REST para obtener los datos de una cita de estudios. URI:
     * /getStudyOrders Type: POST
     */
    /**
     * Servicio API REST para obtener los datos de los estudios médicos. URI:
     * /getAllStudies Type: GET
     */
    @GET
    @Path("/getAllStudies")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Metodo para resolver las peticiones del servicio que regresa los datos de
     * los estudios médicos.
     *
     * @return Response Respuesta del servicio API REST con los datos de los
     * estudios médicos.
     */
    public Response getAllStudies() {
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();
        if (sm.getServiciosRegistrados() != null) {
            //Se obtiene el servicio y se agrega al Entity Manager el bean
            Object estudioRef = sm.getServicio("StudiesManager");
            //Validación si el objeto de la referencia del servicio es diferente de null.
            if (estudioRef != null) {
                //Validación de la referencia del servicio corresponde al manejador StudiesManager.
                if (estudioRef instanceof StudiesManager) {
                    //Registro del EntityManager para el bean correspondiente de datos.
                    ((GenericDAOFacade<?>) estudioRef).setEntityManager(sm.getEntityManager());
                    //Se invoca el metodo para obtener los datos de los estudios médicos, se guarda el valor en una colección de Objetos.
                    Collection<Estudio> listaestudiodao = ((StudiesManager) estudioRef).getAllDataTable();
                    //Ciclo for para obtener los datos del objeto listaestudiodao.
                    for (Estudio o : listaestudiodao) {
                        //Creación de objeto de tipo JSONObject, para crear un json.
                        JSONObject json = new JSONObject();
                        //Se agregan datos a los elemento en el json.
                        json.put("estudio", o.getNombre());
                        json.put("idestudio", o.getIdEstudio());
                        json.put("idarea", o.getAreaDeServicioidArea());
                        //Se agrega el json al arreglo de tipo json.
                        jsonArray.put(json);

                    }
                }
            }
        }
        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);
        //Retorno los elementos json agregados en el arreglo.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    }

    /**
     * Servicio API REST definido para realizar las operaciones CRUD de una cita
     * de estudios. URI: /AppointmentEntity/{CRUD} Type: POST
     */
    @POST
    @Path("/AppointmentEntity/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones para realizar las operaciones CRUD de
     * una cita de estudios.
     *
     * @return Response Respuesta del servicio API REST con valor verdadero o
     * falso de las operaciones CRUD de una cita de estudios.
     */
    public Response AppointmentEntity(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> formParams) throws ParseException {
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONObject datos = new JSONObject();
        //Creación de objeto referenciado al manejador AppointmentManager.
        AppointmentManager appointmententity = new AppointmentManager();
        //Se obtiene el servicio y se agrega al Entity Manager.
        appointmententity.setEntityManager(sm.getEntityManager());
        //Se buscan los elementos y se setean en la variables.
        String noempleado = formParams.getFirst("noempleado");
        String curp = formParams.getFirst("curp");
        String idpaciente = formParams.getFirst("idpaciente");
        String idestudio = formParams.getFirst("idestudio");
        String fechacontrolpk = formParams.getFirst("fechacontrolpk");
        String fechacontrol = formParams.getFirst("fechacontrol");
        String estado = formParams.getFirst("estado");
        String cerrado = formParams.getFirst("cerrado");
        String observaciones = formParams.getFirst("observaciones");
        //Creación de objeto para definir el formato de Fecha y Hora.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Se invoca el metodo para realizar la operación CRUD desde el manejador, regresando verdadero o falso de acuerdo a la operación.
        boolean respcrud = appointmententity.ResolveCRUDStudyControl(operacion, Integer.parseInt(noempleado), curp, idpaciente, Integer.parseInt(idestudio), Long.parseLong(fechacontrolpk), dateFormat.parse(fechacontrol), estado, Boolean.parseBoolean(cerrado), observaciones);
        //Se agrega verdadero o falso al elemento json.
        datos.put(operacion, respcrud);
        //Se agrega el json al arreglo de tipo json.
        jsonArray.put(datos);
        //Impresión de los elementos json agregados.
        System.out.println(jsonArray);
        //Retorno los elementos json agregados en el arreglo.
        return Response.status(Response.Status.OK).entity(datos.toString()).build();
    }

    /**
     * Servicio API REST para obtener los equipos de un área. URI: /getDevice
     * Type: POST
     */
    @POST
    @Path("/getDevice")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los equipos
     * de un área.
     *
     * @return Response Respuesta del servicio API REST con los datos de los
     * equipos del un área.
     */
    public Response getDevice(MultivaluedMap<String, String> formParams) {
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();
        //Creación de objeto referenciado al manejador DeviceManager.
        DeviceManager deviceentity = new DeviceManager();
        //Se obtiene el servicio y se agrega al Entity Manager.
        deviceentity.setEntityManager(sm.getEntityManager());
        //Se busca el elemento idpaciente y se setea en la variable idarea.
        String idarea = formParams.getFirst("idestudio");
        //Se invoca el metodo para obtener los datos de los equipos de un área, se guarda el valor en una colección de Objetos.
        Collection<Object[]> listadevice = deviceentity.getDevice(idarea);
        //Validación si el objeto tiene elementos.
        if (!listadevice.isEmpty()) {
            //Ciclo for para obtener los datos del objeto listacitas.
            for (Object[] columna : listadevice) {
                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();
                //Se agregan datos a los elemento en el json.
                json.put("noserie", columna[0]);
                json.put("salaequipo", columna[2] + " / " + columna[1]);
                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);
            }
        } else { //Validación si el objeto no tiene elementos.
            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();
            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");
            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);
        }
        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);
        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    }

    /**
     * Servicio API REST para obtener los equipos de un área. URI: /getDevice
     * Type: POST
     */
    @POST
    @Path("/getSala")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los equipos
     * de un área.
     *
     * @return Response Respuesta del servicio API REST con los datos de los
     * equipos del un área.
     */
    public Response getSala(MultivaluedMap<String, String> formParams) {

        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();

        //Creación de objeto referenciado al manejador DeviceManager.
        DeviceManager deviceentity = new DeviceManager();

        //Se obtiene el servicio y se agrega al Entity Manager.
        deviceentity.setEntityManager(sm.getEntityManager());

        //Se busca el elemento idpaciente y se setea en la variable idarea.
        String idarea = formParams.getFirst("idarea");

        //Se invoca el metodo para obtener los datos de los equipos de un área, se guarda el valor en una colección de Objetos.
        Collection<Object[]> listadevice = deviceentity.getSala(idarea);

        //Validación si el objeto tiene elementos.
        if (!listadevice.isEmpty()) {

            //Ciclo for para obtener los datos del objeto listacitas.
            for (Object[] columna : listadevice) {

                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();

                //Se agregan datos a los elemento en el json.
                json.put("sala", columna[0]);
                json.put("ubicacion", columna[1]);

                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);

            }
        } else { //Validación si el objeto no tiene elementos.

            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();

            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");

            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);

        }

        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);

        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();

    }

    /**
     * Servicio API REST definido para realizar las operaciones CRUD para la
     * asignación de citas de estudios médicos. URI: /StudyAssignment/{CRUD}
     * Type: POST
     */
    @POST
    @Path("/StudyAssignment/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones para realizar las operaciones CRUD
     * para la asignación de citas de estudios médicos.
     *
     * @return Response Respuesta del servicio API REST con valor verdadero o
     * falso de las operaciones CRUD para la asignación de citas de estudios
     * médicos.
     */
    public Response StudyAssignment(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> formParams) throws ParseException {

        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();

        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONObject datos = new JSONObject();

        //Creación de objeto referenciado al manejador AppointmentManager.
        AppointmentManager appoinmententity = new AppointmentManager();

        //Se obtiene el servicio y se agrega al Entity Manager.
        appoinmententity.setEntityManager(sm.getEntityManager());

        //Se buscan los elementos y se setean en la variables.
        String noserie = formParams.getFirst("noserie");
        String idestudio = formParams.getFirst("idestudio");
        String fechapk = formParams.getFirst("fechapk");
        String noserie_new = formParams.getFirst("noserie_new");
        String idestudio_new = formParams.getFirst("idestudio_new");
        String fechapk_new = formParams.getFirst("fechapk_new");
        String fecha = formParams.getFirst("fecha");

        //Se invoca el metodo para realizar la operación CRUD desde el manejador, regresando verdadero o falso de acuerdo a la operación.
        boolean respcrud = appoinmententity.ResolveCRUDStudyAssignment(operacion, noserie, idestudio, fecha, fechapk, noserie_new, idestudio_new, fechapk_new);

        //Se agrega verdadero o falso al elemento json.
        datos.put(operacion, respcrud);

        //Se agrega el json al arreglo de tipo json.
        jsonArray.put(datos);

        //Impresión de los elementos json agregados.
        System.out.println(jsonArray);

        //Retorno los elementos json agregados en el arreglo.
        return Response.status(Response.Status.OK).entity(datos.toString()).build();

    }

    /**
     * Servicio API REST definido para realizar las operaciones CRUD de una
     * solicitud de estudios. URI: /StudyEntity/{CRUD} Type: POST
     */
    @POST
    @Path("/StudyEntity/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones para realizar las operaciones CRUD de
     * una solicitud de estudios.
     *
     * @return Response Respuesta del servicio API REST con valor verdadero o
     * falso de las operaciones CRUD de una solicitud de estudios.
     */
    public Response StudyEntity(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> formParams) throws ParseException {

        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();

        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONObject datos = new JSONObject();

        //Creación de objeto referenciado al manejador StudyRequestManager.
        StudyRequestManager studyentity = new StudyRequestManager();

        //Se obtiene el servicio y se agrega al Entity Manager.
        studyentity.setEntityManager(sm.getEntityManager());

        //Se buscan los elementos y se setean en la variables.
        String idpaciente = formParams.getFirst("idpaciente");
        String noempleado = formParams.getFirst("noempleado");
        String curp = formParams.getFirst("curp");
        String fechasolicitudpk = formParams.getFirst("fechasolicitudpk");
        String fechasolicitud = formParams.getFirst("fechasolicitud");
        String areaprocedencia = formParams.getFirst("areaprocedencia");
        String fechaproxima = formParams.getFirst("fechaproxima");
        String diagnostico = formParams.getFirst("diagnostico");
        String observaciones = formParams.getFirst("observaciones");
        String estado = formParams.getFirst("estado");

        //Creación de objeto para definir el formato de Fecha y Hora.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //Se invoca el metodo para realizar la operación CRUD desde el manejador, regresando verdadero o falso de acuerdo a la operación.
        boolean respcrud = studyentity.ResolveCRUDStudyRequest(operacion, idpaciente, Integer.parseInt(noempleado), curp, Long.parseLong(fechasolicitudpk), dateFormat.parse(fechasolicitud), areaprocedencia, dateFormat.parse(fechaproxima), diagnostico, observaciones, estado);

        //Se agrega verdadero o falso al elemento json.
        datos.put(operacion, respcrud);

        //Se agrega el json al arreglo de tipo json.
        jsonArray.put(datos);

        //Impresión de los elementos json agregados.
        System.out.println(jsonArray);

        //Retorno los elementos json agregados en el arreglo.
        return Response.status(Response.Status.OK).entity(datos.toString()).build();

    }

    /**
     * Servicio API REST para obtener las áreas de servicio. URI: /getAllAreas
     * Type: GET
     */
    @GET
    @Path("/getAllAreas")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los equipos
     * de un área.
     *
     * @return Response Respuesta del servicio API REST con los datos de los
     * equipos del un área.
     */
    public Response getAllAreas() {

        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();

        //Creación de objeto referenciado al manejador AppointmentManager.
        AppointmentManager appointmententity = new AppointmentManager();

        //Se obtiene el servicio y se agrega al Entity Manager.
        appointmententity.setEntityManager(sm.getEntityManager());

        //Se invoca el metodo para obtener los datos de las áreas de servicio, se guarda el valor en una colección de Objetos.
        Collection<Object[]> listaareas = appointmententity.getAllAreas();

        //Validación si el objeto tiene elementos.
        if (!listaareas.isEmpty()) {

            //Ciclo for para obtener los datos del objeto listacitas.
            for (Object[] columna : listaareas) {

                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();

                //Se agregan datos a los elemento en el json.
                json.put("idarea", columna[0]);
                json.put("nombrearea", columna[1]);

                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);

            }
        } else { //Validación si el objeto no tiene elementos.

            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();

            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");

            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);

        }

        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);

        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();

    }

    /**
     * Servicio API REST para obtener los estudios médicos de un área. URI:
     * /getStudies Type: POST
     */
    @POST
    @Path("/getStudies")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los estudios
     * médicos de un área.
     *
     * @return Response Respuesta del servicio API REST con los datos de los
     * estudios de un área.
     */
    public Response getStudies(MultivaluedMap<String, String> formParams) {
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();
        //Creación de objeto referenciado al manejador DeviceManager.
        StudiesManager studiesentity = new StudiesManager();
        //Se obtiene el servicio y se agrega al Entity Manager.
        studiesentity.setEntityManager(sm.getEntityManager());
        //Se busca el elemento area y se setea en la variable area.
        String idarea = formParams.getFirst("idarea");
        //Se invoca el metodo para obtener los datos de los estudios médicos de un área, se guarda el valor en una colección de Objetos.
        Collection<Object[]> listastudies = studiesentity.getStudies(idarea);
        //Validación si el objeto tiene elementos.
        if (!listastudies.isEmpty()) {
            //Ciclo for para obtener los datos del objeto listacitas.
            for (Object[] columna : listastudies) {
                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();
                //Se agregan datos a los elemento en el json.
                json.put("idestudio", columna[0]);
                json.put("estudio", columna[2] + " / " + columna[3]);
                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);
            }
        } else { //Validación si el objeto no tiene elementos.
            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();
            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");
            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);
        }

        AreaManager areamanager = new AreaManager();
        areamanager.setEntityManager(sm.getEntityManager()); 
        JSONArray area=areamanager.getAllDataAreabyId(Integer.parseInt(idarea));
        /*ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray2 = mapper.createArrayNode();
        jsonArray2.addPOJO(area);*/
        jsonArray.put(area.toString());
        
        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);
        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    }

    @POST
    @Path("/getStudiesDVG")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudiesdvg(MultivaluedMap<String, String> formParams) {
        JSONArray jsonArray = new JSONArray();
        String idarea = formParams.getFirst("idarea");
        AreaManager areamanager = new AreaManager();
        areamanager.setEntityManager(sm.getEntityManager()); 
        JSONArray area=areamanager.getAllDataAreabyId(Integer.parseInt(idarea));
        /*ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray2 = mapper.createArrayNode();
        jsonArray2.addPOJO(area);*/
        jsonArray.put(area.toString());
        System.out.println(jsonArray);
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    }    

    public static boolean contieneNoImprimibles(String cadena){
            for (int i = 0; i < cadena.length(); i++) {
                char c = cadena.charAt(i);
                // Verifica si el carácter es de control (ASCII 0-31 o 127)
                if (Character.isISOControl(c)) {
                    return true; // Si encontramos un carácter no imprimible
                }
            }
            return false; // Si no se encuentran caracteres no imprimibles
        }
    
    
    @POST
    @Path("/getAppointment")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los datos de
     * una cita de estudios de un paciente.
     *
     * @return Response Respuesta del servicio API REST con los datos de una
     * cita de estudios de un paciente.
     */
    public Response getAppointment(MultivaluedMap<String, String> formParams) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();
        JSONObject mensajeseguro = new JSONObject();
        Cybersecurity seguridadconsultaCit = new Cybersecurity();
        /*
        Cibersecurity seguridad2 = new Cibersecurity();
        seguridad2.generarLlaves();
        String llavepublica2 = seguridad2.getLlavePublica();
        */
        //Creación de objeto referenciado al manejador AppointmentManager.
        AppointmentManager appointmententity = new AppointmentManager();
        //Se obtiene el servicio y se agrega al Entity Manager.
        appointmententity.setEntityManager(sm.getEntityManager());
        
        
        String llavepublicaida = formParams.getFirst("llavepublica");
        String Token = formParams.getFirst("token");
        Claims infodescifrada = seguridadconsultaCit.desencriptarDatos(llavepublicaida, Token);
        System.out.println("La llave publica es:"+ llavepublicaida);
        
        String idpaciente= (String) infodescifrada.get("idpaciente");
        String nombrepaciente = (String) infodescifrada.get("nombrepaciente");
        String apellidopaternopaciente = (String) infodescifrada.get("apellidopaternopaciente");
        String apellidomaternopaciente =  (String)infodescifrada.get("apellidomaternopaciente");
        String idestudio = (String) infodescifrada.get("idestudio");
        String fechamin = (String) infodescifrada.get("fechamin");
        String fechamax = (String)infodescifrada.get("fechamax");
        String idarea = (String) infodescifrada.get("idarea");
        String sala = (String) infodescifrada.get("sala");
        String estado = (String) infodescifrada.get("estado");
        
    
        
        /*
        
        //Se busca el elemento idpaciente y se setea en la variable idpaciente.
        String idpaciente = formParams.getFirst("idpaciente");
        
        
        for (int i = 0; i < idpaciente.length(); i++) {
            char c = idpaciente.charAt(i);
            if (Character.isISOControl(c)) {  // Si es un carácter no imprimible
                System.out.println("Carácter no imprimible encontrado en la posición " + i + ": '" + c + "' (Código Unicode: " + (int)c + ")");
                
                break; // Salir del bucle al encontrar el primer carácter no imprimible
            }
        }
        
        System.out.println("El bucle ha terminado, y se ha revisado toda la cadena.");
        //System.out.println("El id del paciente es:"+idpaciente);
        String nombrepaciente = formParams.getFirst("nombrepaciente");
        String apellidopaternopaciente = formParams.getFirst("apellidopaternopaciente");
        String apellidomaternopaciente = formParams.getFirst("apellidomaternopaciente");
        String idestudio = formParams.getFirst("idestudio");
        String fechamin = formParams.getFirst("fechamin");
        String fechamax = formParams.getFirst("fechamax");
        String idarea = formParams.getFirst("idarea");
        String sala = formParams.getFirst("sala");
        String estado = formParams.getFirst("estado");
        System.out.println("El estado es:"+estado);
        */
        //Se invoca el metodo para obtener los datos de una cita de estudios, se guarda el valor en una colección de Objetos.
        Collection<Object[]> listacitas = appointmententity.getAppointment(idpaciente, nombrepaciente, apellidopaternopaciente, apellidomaternopaciente, idestudio, fechamin, fechamax, idarea, sala, estado);
        //Validación si el objeto tiene elementos.
        if (!listacitas.isEmpty()) {
            //Ciclo for para obtener los datos del objeto listacitas.
            for (Object[] columna : listacitas) {
                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();
                //Se agregan datos a los elemento en el json.
                json.put("ubicacion", columna[1]);
                json.put("medico", columna[2] + " " + columna[3] + " " + columna[4]);
                json.put("estadocita", columna[6]);
                json.put("paciente", columna[7] + " " + columna[8] + " " + columna[9]);
                json.put("idpaciente", columna[10]);
                json.put("noempleado", columna[11]);
                json.put("curpempleado", columna[5]);
                json.put("curp", columna[12]);
                json.put("fechasolicitudpk", columna[13]);
                json.put("fechasolicitud", columna[14]);
                json.put("areaprocedencia", columna[15]);
                json.put("fechaproxima", columna[16]);
                json.put("diagnostico", columna[17]);
                json.put("observaciones", columna[18]);
                json.put("estado", columna[19]);
                json.put("noserie", columna[20]);
                json.put("idestudio", columna[21]);
                json.put("fechaasignpk", columna[22]);
                json.put("tiempotraslado", columna[24]);
                //json.put("fechacita",columna[0] == null ? JSONObject.NULL : String.valueOf(columna[0]).split(" ")[0]);
                json.put("fechacita", String.valueOf(columna[0]).split(" ")[0]);
                json.put("horacita", String.valueOf(columna[0]).split(" ")[1]);
                json.put("turno", ("13:45:00".compareTo(String.valueOf(columna[23]).split(" ")[1]) >= 0 ? "Matutino" : "Vespertino"));
                json.put("estudio", columna[25]);
                json.put("noempleadousuario", columna[26]);
                json.put("idarea", columna[27]);
                json.put("domicilio", columna[28] + ", " + columna[29] + ", " + columna[30] + ", " + columna[31] + ", " + columna[32] + ", " + columna[33]);
                json.put("nombrearea", columna[36]);
                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);
            }
        } else { //Validación si el objeto no tiene elementos.
            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();
            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");
            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);
        }
        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);
        String payload = jsonArray.toString();
        //Cibersecurity seguridadconsultaCit2 = new Cibersecurity();
        seguridadconsultaCit.generarLlaves();
        String llavepublica = seguridadconsultaCit.getLlavePublica();
        String datoscifrados = seguridadconsultaCit.encriptarDatos(payload);
        mensajeseguro.put("llavepublica", llavepublica);
        mensajeseguro.put("JWT", datoscifrados);
        
        
        
        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(mensajeseguro.toString()).build();
        //return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    }

    /**
     * Servicio API REST para obtener los datos de una cita de estudios. URI:
     * /getStudyOrders Type: POST
     */
    @POST
    @Path("/getStudyOrders")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los datos de
     * una cita de estudios de un paciente.
     *
     * @return Response Respuesta del servicio API REST con los datos de una
     * cita de estudios de un paciente.
     */
    public Response getStudyOrders(MultivaluedMap<String, String> formParams) {
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();
        //Creación de objeto referenciado al manejador AppointmentManager.
        AppointmentManager appointmententity = new AppointmentManager();
        //Se obtiene el servicio y se agrega al Entity Manager.
        appointmententity.setEntityManager(sm.getEntityManager());
        //Se busca el elemento idpaciente y se setea en la variable idpaciente.
        String idpaciente = formParams.getFirst("idpaciente");
        String nombrepaciente = formParams.getFirst("nombrepaciente");
        String apellidopaternopaciente = formParams.getFirst("apellidopaternopaciente");
        String apellidomaternopaciente = formParams.getFirst("apellidomaternopaciente");
        String idestudio = formParams.getFirst("idestudio");
        String fechamin = formParams.getFirst("fechamin");
        String fechamax = formParams.getFirst("fechamax");
        String idarea = formParams.getFirst("idarea");
        //Se invoca el metodo para obtener los datos de una cita de estudios, se guarda el valor en una colección de Objetos.
        Collection<Object[]> listaestudios = appointmententity.getStudyOrders(idpaciente, nombrepaciente, apellidopaternopaciente, apellidomaternopaciente, idestudio, fechamin, fechamax, idarea);
        //Validación si el objeto tiene elementos.
        if (!listaestudios.isEmpty()) {
            //Ciclo for para obtener los datos del objeto listacitas.
            for (Object[] columna : listaestudios) {
                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();
                //Se agregan datos a los elemento en el json.
                json.put("medico", columna[0] + " " + columna[1] + " " + columna[2]);
                json.put("paciente", columna[3] + " " + columna[4] + " " + columna[5]);
                json.put("servicio", columna[6]);
                json.put("estudio", columna[7]);
                json.put("estudiodescripcion", columna[8]);
                json.put("idarea", columna[9]);
                json.put("domicilio", columna[10] + ", " + columna[11] + ", " + columna[12] + ", " + columna[13] + ", " + columna[14] + ", " + columna[15]);
                json.put("tiempotraslado", columna[16]);
                json.put("noempleado", columna[17]);
                json.put("curp", columna[18]);
                json.put("idpaciente", columna[19]);
                json.put("idestudio", columna[20]);
                json.put("fechasolicitudpk", columna[21]);
                json.put("fechasolicitud", String.valueOf(columna[22]).split(" ")[0]);
                json.put("horasolicitud", String.valueOf(columna[22]).split(" ")[1]);
                json.put("estado", columna[23]);
                json.put("observaciones", columna[25]);
                json.put("areaprocedencia", columna[26]);
                json.put("fechaproxima", columna[27]);
                json.put("diagnostico", columna[28]);
                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);
            }
        } else { //Validación si el objeto no tiene elementos.
            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();
            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");
            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);
        }
        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);
        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
    }

    /**
     * Servicio API REST para obtener los horarios de citas de estudios de
     * manera manual. URI: /getCalendarManual Type: POST
     */
    @POST
    @Path("/getCalendarManual")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los horarios
     * de citas de estudios de manera manual.
     *
     * @return Response Respuesta del servicio API REST con horarios de citas
     * medicas.
     */
    public Response getCalendarManual(MultivaluedMap<String, String> formParams) throws ParseException {

        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();

        //Creación de objeto referenciado al manejador AppointmentManager.
        AppointmentManager appointmententity = new AppointmentManager();

        //Se obtiene el servicio y se agrega al Entity Manager.
        appointmententity.setEntityManager(sm.getEntityManager());

        //Se busca el elemento idpaciente y se setea en la variable idpaciente.
        String fechaproxima = formParams.getFirst("fechaproxima");
        String fechacita = formParams.getFirst("fechacita");
        String idestudio = formParams.getFirst("idestudio");
        String noserie = formParams.getFirst("noserie");

        //Se invoca el metodo para obtener los datos de una cita de estudios, se guarda el valor en una colección de Objetos.
        ArrayList<String> listacalendario = appointmententity.getCalendarManual(fechaproxima, fechacita, idestudio, noserie);

        if (listacalendario.isEmpty() == false && listacalendario.get(0).split(";")[0].contains("salida") == false) {
            for (String columna : listacalendario) {

                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();

                json.put("Hora", columna);

                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);

            }

        } else if (listacalendario.get(0).split(";")[0].contains("salida")) {

            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();

            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("salida", listacalendario.get(0).split(";")[1]);

            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);

        } else if (listacalendario.isEmpty()) { //Validación si el objeto no tiene elementos.

            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();

            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");

            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);

        }

        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);

        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();

    }

    /**
     * Servicio API REST para obtener los horarios de citas de estudios de
     * manera automatica. URI: /getCalendarManual Type: POST
     */
    @POST
    @Path("/getCalendarAutomatic")
    @Produces(MediaType.APPLICATION_JSON)

    /**
     * Metodo para resolver las peticiones del servicio que regresa los horarios
     * de citas de estudios de manera automatica.
     *
     * @return Response Respuesta del servicio API REST con los datos de 3
     * opciones de fecha, hora, sala, serie y turno de citas medicas.
     */
    public Response getCalendarAutomatic(MultivaluedMap<String, String> formParams) throws ParseException {
        //Creación de objeto de tipo JSONArray para guardar los datos de respuesta.
        JSONArray jsonArray = new JSONArray();
        //Creación de objeto referenciado al manejador AppointmentManager.
        AppointmentManager appointmententity = new AppointmentManager();
        //Se obtiene el servicio y se agrega al Entity Manager.
        appointmententity.setEntityManager(sm.getEntityManager());
        //Se busca el elemento idpaciente y se setea en la variable idpaciente.
        String fechaproxima = formParams.getFirst("fechaproxima");
        String tipo_paciente = formParams.getFirst("tipo_paciente");
        String idestudio = formParams.getFirst("idestudio");
        //Se invoca el metodo para obtener los datos de una cita de estudios, se guarda el valor en una colección de Objetos.
        ArrayList<String> listacalendario = appointmententity.getCalendarAutomatic(fechaproxima, tipo_paciente, idestudio);

        if (listacalendario.isEmpty() == false && listacalendario.get(0).split(";")[0].contains("salida") == false) {
            for (String columna : listacalendario) {
                //Creación de objeto de tipo JSONObject, para crear un json.
                JSONObject json = new JSONObject();
                json.put("fecha", columna.split(";")[0]);
                json.put("hora", columna.split(";")[1]);
                json.put("sala", columna.split(";")[2]);
                json.put("equipo", columna.split(";")[3]);
                json.put("turno", columna.split(";")[4]);
                //Se agrega el json al arreglo de tipo json.
                jsonArray.put(json);
            }

        } else if (listacalendario.get(0).split(";")[0].contains("salida")) {
            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();
            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("salida", listacalendario.get(0).split(";")[1]);
            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);

        } else if (listacalendario.isEmpty()) { //Validación si el objeto no tiene elementos.
            //Creación de objeto de tipo JSONObject, para crear un json.
            JSONObject json = new JSONObject();
            //En caso de que no haya resultados se agrega el mensaje al elemento json.
            json.put("snresultados", "true");
            //Se agrega el json al arreglo de tipo json.
            jsonArray.put(json);
        }

        //Impresión de los elementos json agregados en el arreglo.
        System.out.println(jsonArray);

        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();

    }
    
    @POST
    @Path("/servicioseguridad")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
    public Response servicioSeguridad(MultivaluedMap<String, String> datosjson) throws ParseException, NoSuchAlgorithmException, InvalidKeySpecException {
        String jwt = datosjson.getFirst("token");
        String keyprimaria = datosjson.getFirst("llavepublica");
        System.out.println(jwt);
        System.out.println(keyprimaria);
        Cybersecurity seguridadlogin = new Cybersecurity();
        Claims inforlogin = seguridadlogin.desencriptarDatos(keyprimaria, jwt);
        authString = (String) inforlogin.get("base64");
        System.out.println(authString);

        //Retorno el elemento json con el resultado de la operación CRUD.
        return Response.ok("Se envió correctamente el Token").build();

    }   
}
