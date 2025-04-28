class FSM {

    constructor(arregloFSM) {
        this.arregloFSM = arregloFSM;
        //console.log(this.arregloFS);
    }

    // Se le 
    set setArregloFSM(arregloFSM) {
        this.arregloFSM = arregloFSM;
        //	this.jsonArray=JSON.stringify()
        //console.log(arregloFSM);
    }

    imprimirArreglo() {
        //console.log("Desde la clase");
        var pu = JSON.stringify(this.arregloFSM);
        console.log(pu);
        console.log(typeof pu);
        console.log(pu.length);
    }

    //regresa arreglo k del objeto json
    elementArray(jsonArray, num) {
        for (var z = 0; z < jsonArray.length; z++) {
            if (z === num) {
                return jsonArray[z];
            }
        }
    }
    // Funcion que regresa el objeto del estado; {estado: nombre}, {mensajes: 0,1,...}
    getFSMStateById(cadbusquedaedo) {
        for (var z = 0; z < this.arregloFSM.length; z++) {
            var estadok = this.elementArray(this.arregloFSM, z);
            //Estado k siempre compara con el elemento 0
            if (cadbusquedaedo === Object.values(estadok["estado"])[0].id.toString()) {
                //console.log("estado encontrado en la FSM: " + cadbusquedaedo);
                return estadok;
            }
        }
        return null;
    }

    getFSMStateMessageById(estadok, cadbusquedaedo) {
        //console.log("getFSMStateMessageById: "+estadok+" , "+cadbusquedaedo);
        for (var z = 0; z < estadok["mensaje"].length; z++) {
            //console.log(Object.values(estadok["Mensaje"])[z]);
            if (cadbusquedaedo === Object.values(estadok["mensaje"])[z].id.toString()) {
                //console.log("estado encontrado en la FSM: "+cadbusquedaedo);
                return  Object.values(estadok["mensaje"])[z];
            }
        }
        return null;
    }

    //lectura de la interface REST, accion del estado
    getRestInterface(estado, mensaje) {
        var estadok = this.getFSMStateById(estado);
        //console.log("estadok: "+estadok);
        //falta cachar errores..
        var mensajek = this.getFSMStateMessageById(estadok, mensaje);
        return mensajek.accion;
    }

    // Funcion que regresa el estado a partir de la vista
    getFSMStateByView(view) {
        for (var z = 0; z < this.arregloFSM.length; z++) {
            var estadok = this.elementArray(this.arregloFSM, z); //Estado k siempre compara con el elemento 0
            if (view === Object.values(estadok["estado"])[0].vista.toString()) {
                return Object.values(estadok["estado"])[0].id.toString();
            }
        }
        return null;
    }

    // Funcion que regresa el estado anterior al estado actual, pasamos estado actual 
    getFSMStateBeforeByActualState(cadbusquedaedo) {
        for (var z = 0; z < this.arregloFSM.length; z++) {
            var estadoAnterior = this.elementArray(this.arregloFSM, z - 2); // Almacenamos el estado anterior, caso de 'login'
            var estadok = this.elementArray(this.arregloFSM, z); //Estado k siempre compara con el elemento 0
            if (cadbusquedaedo === Object.values(estadok["estado"])[0].id.toString()) {
                return estadoAnterior; // Regresamos el estado anterior
            }
        }
        return null;
    }

    // Funcion que regresa el estado siguiente al estado actual, pasamos estado actual 
    getFSMStateAfterByActualState(cadbusquedaedo, i) {
        for (var z = 0; z < this.arregloFSM.length; z++) {
            var estadoSiguiente = this.elementArray(this.arregloFSM, z + i); // Almacenamos el estado anterior
            var estadok = this.elementArray(this.arregloFSM, z); //Estado k siempre compara con el elemento 0
            if (cadbusquedaedo === Object.values(estadok["estado"])[0].id.toString()) {
                return Object.values(estadoSiguiente["estado"])[0].id.toString();
                //return estadoSiguiente; // Regresamos el estado anterior
            }
        }
        return null;
    }

    // Funcion para regresar la vista del estado
    getFSMViewByState(state) {
        for (var z = 0; z < this.arregloFSM.length; z++) {
            var estadok = this.elementArray(this.arregloFSM, z); //Estado k siempre compara con el elemento 0
            if (state === Object.values(estadok["estado"])[0].id.toString()) {
                return Object.values(estadok["estado"])[0].vista.toString();
            }
        }
        return null;
    }

}