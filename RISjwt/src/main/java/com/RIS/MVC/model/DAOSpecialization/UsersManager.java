package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.INR.restServices.RisApplicationException;
import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.AreaDeServicio;
import com.RIS.MVC.model.JPA.entities.Perfil;
import com.RIS.MVC.model.daoInterface.ServicesManager;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.RIS.MVC.model.JPA.entities.Usuario;
import com.RIS.MVC.model.JPA.entities.UsuarioPK;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.ws.rs.core.MultivaluedMap;

 //@NamedQuery(name = "Usuario.findByNamePasswd", query = "SELECT u FROM Usuario u WHERE u.passwd = :passwd and u.usuarioID = :usuarioID"), //++DVG 21/01/2021
 //@NamedQuery(name = "Usuario.findByIDusrCurp", query = "SELECT u FROM Usuario u WHERE u.usuarioPK.numEmpleado = :numEmpleado and u.usuarioPK.curp = :curp"), //++DVG 05/03/2021  

public class UsersManager extends GenericDAOFacade<Usuario> {

    public UsersManager() {
        super(Usuario.class);
    }

    public Collection<Usuario> getAllDataUser() {
        TypedQuery<Usuario> query;
        query = entityManager.createNamedQuery("Usuario.findAll", Usuario.class);//JPQL
        return query.getResultList();
    }
    
    
    public Usuario getUserByNamePassword2(String userName, String userPwd) {
        //aqui es necesario hacer joins para obtener los perfiles y roles
        String consultasms = "SELECT * FROM Usuario WHERE nombre ='"+userName +"' and passwd ='"+userPwd+"';";
        System.out.println("consulta: "+consultasms);
        Query query = entityManager.createNativeQuery(consultasms);
        /*List<Usuario> columnas = (List<Usuario>)query.getResultList();
        Iterator i = columnas.iterator();
        Usuario usrrow=null;
        while (i.hasNext()) {
           usrrow = (Usuario) i.next();
           System.out.println(usrrow.getNombre()+"");
           System.out.println(usrrow.getApellidoMaterno()+"");
           System.out.println(usrrow.getApellidoMaterno()+"");
           System.out.println(usrrow.getAreaidArea().getNombre()+"");

        }  
        return usrrow;*/
        Usuario usr = new Usuario();
        Collection<Object[]> columnas = query.getResultList();        
        
        if (!columnas.isEmpty()) {
            for (Object[] col : columnas) {
                System.out.println("registro BD: "+col[0]+", "+col[1]+", "+col[2]+", "+col[3]+", "+col[4]+", "+col[5]+", "+col[6]);
                usr.setNombre(""+col[2]);
                usr.setApellidoPaterno(""+col[3]);
                usr.setApellidoMaterno(""+col[4]);
            }
        }
        return usr;
    } 
    
    //Método para el login y regresar un Usuario, con sus perifiles, rol y area
    public Usuario getUserByNamePassword(String nombreusr, String password) {
        Query q = entityManager.createNamedQuery("Usuario.findByNamePasswd", Usuario.class); //ojo agregar a USER la consulta
        q.setParameter("passwd", password);
        //q.setParameter("nombre", nombreusr);
        q.setParameter("usuarioID", nombreusr);

        Usuario usr = null;
        try {
            usr = (Usuario) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Usuario no encontrado");            
            ex.printStackTrace(System.out);
        }
        //System.out.println(usr.toString());
        return usr;
    }

    //Método para el login y regresar un Usuario
    /*public Usuario findUser(String email, String password) {
        Query q = entityManager.createNamedQuery("Usuario.findByEmailPassword", Usuario.class); 
        q.setParameter("userEmail", email);
        q.setParameter("userPwd", password);
        System.out.println("Llamando metodo: getUserByEmailPassword");
        Usuario usr = null;
        try {
            usr = (Usuario) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Usuario no encontrado");
            ex.printStackTrace(System.out);
        }
        return usr;
    }*/
    
    //Método para el login y regresar un Usuario, con sus perifiles, rol y area
    public Usuario getUserByIdCurp(int numEmpleado, String curp) {
        //System.out.println("num empleado: "+numEmpleado+" CURP: "+curp);
        Query q = entityManager.createNamedQuery("Usuario.findByNumEmpleadoCurp", Usuario.class); //ojo agregar a USER la consulta
        q.setParameter("numEmpleado", numEmpleado);
        q.setParameter("curp", curp);
        Usuario usr = null;
        try {
            usr = (Usuario) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Usuario no encontrado");            
            ex.printStackTrace(System.out);
        }
        //System.out.println(usr.toString());
        return usr;
    }    

    public ArrayNode nodoPerfiles(Collection<Perfil> perfilesusr){
      ObjectMapper mapper = new ObjectMapper();
      ArrayNode perfilNodeArray = mapper.createArrayNode();
      Iterator<Perfil> iterator = perfilesusr.iterator();
      while (iterator.hasNext()) {
         Perfil perfilk = iterator.next();
         if(perfilk.getRol()!=null){
          System.out.println("Perfil = " + perfilk + ", ROL: " + perfilk.getRol().getNombre());
           perfilNodeArray.add(perfilk.getRol().getNombre());
         }else{
           perfilNodeArray.add("Null"); 
         }
      } 
      return perfilNodeArray;
    }
    
    
    public boolean updateUsr(Integer idUsr,String curpusr, String nombre,String apaterno,String amaterno,String areaasignada, List<String> roles) {

        Usuario updateusr=getUserByIdCurp(idUsr, curpusr);
        //Usuario updateusr=this.find(idUsr);
        
        System.out.println("Usuario:"+updateusr.getNombre());
        System.out.println("Area Asignada: "+updateusr.getAreaidArea().getNombre());
        System.out.println("Nueva Area: "+areaasignada);
        
        if(roles.size()>0){
            Collection<Perfil> perfilesusr = new ArrayList<>();
            for(String xrol:roles){
              perfilesusr.add(new Perfil(idUsr, curpusr, Integer.parseInt(xrol)));    
            }
            updateusr.setPerfilCollection(perfilesusr); //analizar que pasa cuando se actualiza un registro, al parecer no borrar los anteriores y se generan datos nulos
        }
        
        AreaDeServicio area= new AreaDeServicio(Integer.parseInt(areaasignada));// Rayos X                
        updateusr.setAreaidArea(area);
        
        updateusr.setNombre(nombre);
        updateusr.setApellidoPaterno(apaterno);
        updateusr.setApellidoMaterno(amaterno);
        
        Usuario regalm = this.edit(updateusr); //actualizacion de reg
        return regalm != null;
    }
    
    //MultivaluedMap<String, String>
    public boolean resolveCRUD(String operacion,Claims formParams){
        boolean respcrud=false;
        /*
        System.out.println(formParams);
        
        String nombre = formParams.getFirst("nombreUsr");
        String aPaterno=formParams.getFirst("aPaterno");
        String aMaterno=formParams.getFirst("aMaterno");
        String curpusr= formParams.getFirst("curp");
        String areaasignada=formParams.getFirst("areaAsignada"); //area de prueba        
        String estado= "Bloqueado";//formParams.getFirst("curp");
        System.out.println("Nombre: "+nombre+" "+aPaterno+" "+aMaterno+ " Curp: "+curpusr);
        
        

        List<String> roles=formParams.get("usrPerf[]");// roles seleccionados
        System.out.println("ROLES: "+roles);
        */
        
        String nombre= (String) formParams.get("nombreUsr");
        String aPaterno= (String)formParams.get("aPaterno");
        String aMaterno= (String)formParams.get("aMaterno");
        String curpusr= (String)formParams.get("curp");
        String areaasignada=(String)formParams.get("areaAsignada"); //area de prueba
        List<String> roles=(List<String>) formParams.get("usrPerf");// roles seleccionados
        String estado= "Bloqueado";
        System.out.println("Nombre: "+nombre+" "+aPaterno+" "+aMaterno+ " Curp: "+curpusr);
        System.out.println("ROLES: "+roles);

        String loginusr="RAM";
        String pwdusr="123";
               
        switch(operacion){
            case "CREATE":
                Query q = entityManager.createNativeQuery("SELECT MAX(Numempleado) FROM usuario"); //numero de usuarios en la BD
                //System.out.println("Respuesta: "+q);
                Number result = (Number) q.getSingleResult();
                int newidusr = result.intValue() + 1;//el sigueinte regristro en la BD, es maximo + 1                
                //iterar el arreglo de perfiles
                Usuario newusr= new Usuario(new UsuarioPK(newidusr,curpusr) , nombre, aPaterno, aMaterno, loginusr, pwdusr, estado);
                Collection<Perfil> perfilesusr = new ArrayList<>();
                for(String xrol:roles){
                  perfilesusr.add(new Perfil(newidusr, curpusr, Integer.parseInt(xrol)));  //perfil 2=recepcionista  
                }
                AreaDeServicio area= new AreaDeServicio(Integer.parseInt(areaasignada));// Rayos X  
                newusr.setPerfilCollection(perfilesusr);
                newusr.setAreaidArea(area);
                Usuario regalm = this.save(newusr);
                respcrud= (regalm != null);
               //respcrud=true; //solo pruebas
            break;
            case "UPDATE":
                //String idUsr = formParams.getFirst("usrId");
                String idUsr= (String) formParams.get("usrId");
                respcrud=updateUsr(Integer.parseInt(idUsr),curpusr,nombre,aPaterno,aMaterno,areaasignada, roles);
                System.out.println("respcrud: "+respcrud);
            break;
            case "DELETE":
                //respcrud=deleteRol(Integer.parseInt(rolid));
            break;            
        }
        return respcrud;
    }
    
    /*Authenticate manejando excepciones*/
    public Usuario authenticate(String authCredentials, UsersManager userRef) throws RisApplicationException {
        if (authCredentials == null) {
            return null;
        }
        //System.out.println("Validando a usuario ");
        // header value format will be "Basic encodedstring" for Basic
        final String encodedUserPassword = authCredentials;
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        Usuario usuario = userRef.getUserByNamePassword(username, password);

        if (usuario.getNombre() == null) {//usario inválido
            throw new RisApplicationException("Error, usuario y/o clave incorrectos. Intente de nuevo.");
        } else if (usuario.getEstado().equals("Inactivo")) {//si el usuario está inactivo
            System.out.println("El estado del usuario es: " + usuario.getEstado());
            throw new RisApplicationException("Error, usuario inactivo. Contacte al administrador del RIS.");
        }
        return usuario;
    }  
    
    
    public static void accesoJPA() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PesitenceREST"); // se encuentra en el archivo persistence.xml
        EntityManager entityManager = emf.createEntityManager();
        UsersManager user = new UsersManager();
        user.setEntityManager(entityManager);

        Collection<Usuario> listausuarios = user.getAll();
        for (Usuario o : listausuarios) {
            //System.out.println("ID: " + o.getUsuarioID() + " Nombre: " + o.getNombre() + " pwd: " + o.getPasswd() + " Area: " + o.getAreaidArea().getNombre());
           
            System.out.println("id: "+o.getUsuarioID()+" # emprelado: "+o.getUsuarioPK().getNumEmpleado()+ " Nombre: " + o.getApellidoMaterno() + " " + o.getApellidoPaterno() + " " + o.getNombre()+" curp: "+o.getUsuarioPK().getCurp()+" pwd: "+o.getPasswd());            
            //o.getAreaDeServicio()
            //System.out.println("Cantidad perfiles: "+o.getPerfilCollection().size());
            Iterator<Perfil> iterator = o.getPerfilCollection().iterator();
            while (iterator.hasNext()) {
                Perfil perfilk=iterator.next();
                System.out.println("Perfil = " + perfilk+ ", ROL: "+perfilk.getRol().getNombre());
            }
        }
        /*System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        //Usuario usr=user.getUserByNamePassword2("Carlos", "abc123"); //esta solo regresa usuario
        Usuario usr = user.getUserByNamePassword("Carlos", "abc123"); //se regregresa usuario, perfile, rol y aea
        System.out.println("Nombre: " + usr.getApellidoMaterno() + " " + usr.getApellidoPaterno() + " " + usr.getNombre());
        Iterator<Perfil> iterator = usr.getPerfilCollection().iterator();
        while (iterator.hasNext()) {
            Perfil perfilk = iterator.next();
            System.out.println("Perfil = " + perfilk + ", ROL: " + perfilk.getRol().getNombre());
        }*/
        
        /*
        String deleteperfiles = "delete from Perfil where Usuario_CURP='aop000' AND Usuario_NumEmpleado=11;";
        Query query = entityManager.createNativeQuery(deleteperfiles);
        int result =query.executeUpdate();
        System.out.println("Perfil Delete Status="+result);
        */
        
        /*Query tmp= entityManager.createQuery("DELETE FROM Perfil WHERE Usuario_NumEmpleado=:empleado and Usuario_CURP=:curp");
        tmp.setParameter("empleado", 11);
        tmp.setParameter("curp", "aop000");
        int result =tmp.executeUpdate();
        System.out.println("Perfil Delete Status="+result);
        */
        
        
    }

    public static void accesoInyeccionDependencias() {
        //cualquier servicio definido en serviciosDAOspring.xml
        System.out.println("Ejecutando spring inyección de dependencias");
        ServicesManager admondaos = new ServicesManager("AplicationContext.xml");
        if (admondaos.getServiciosRegistrados() != null) {
            for (String serviciosRegistrado : admondaos.getServiciosRegistrados()) {
                System.out.println("######Registrando DAO: " + serviciosRegistrado);
            }
            Object userRef = admondaos.getServicio("usersManager"); //al regresar el servicio se le agrega su entityManager
            if (userRef != null) {
                if (userRef instanceof UsersManager) {
                    System.out.println("---Accediendo via Spring----");
                    ((GenericDAOFacade<?>) userRef).setEntityManager(admondaos.getEntityManager()); //registrar el entitymanager para el bean correpsondiente                    
                    Collection<Usuario> listausrdao = ((UsersManager) userRef).getAllDataUser();
                    for (Usuario o : listausrdao) {
                        //System.out.println("Nombre: " + o.getNombre());
                        System.out.println("id: "+o.getUsuarioID()+ "Nombre: " + o.getApellidoMaterno() + " " + o.getApellidoPaterno() + " " + o.getNombre()+" curp: "+o.getUsuarioPK().getCurp());
                        
                        Iterator<Perfil> iterator = o.getPerfilCollection().iterator();
                        while (iterator.hasNext()) {
                            Perfil perfilk=iterator.next();
                            System.out.println("Perfil = " + perfilk+ ", ROL: "+perfilk.getRol().getNombre());
                        }
                    }
                }
            }
        }
    }
    //probando usuarios
    public static void main(String args[]) {
        boolean accesojpa = true;
        //boolean accesojpa=false;
        if (accesojpa) {
            //Metodo 1 acceso directo a entidades 
            accesoJPA();
        } else {
            //Metodo 1 acceso por medio de Spring 
            accesoInyeccionDependencias();
        }
    }
}




