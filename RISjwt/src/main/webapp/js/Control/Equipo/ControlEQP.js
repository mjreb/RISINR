var uriserv = "/RISSERVER/rest/USRSesionRST";

function activaBotonesEQP(table, bandera) {
    //bandera: [true,false]
    document.getElementById("btnAdd" + table).disabled = !bandera;
    document.getElementById("btnEdt" + table).disabled = bandera;
    document.getElementById("btnDel" + table).disabled = bandera;
}


function evaluaRadioButtonTablaPK0(tabla, colPK) {
    html_VisibleElement("btnAddEqptbl");
    html_VisibleElement("btnEdtEqptbl");
    html_VisibleElement("btnDelEqptbl");
}

function barraBotonesEQP(e) {
    var opc = e.target.id;
    console.log("selección: " + opc);
    switch (opc) {
        case 'btnCatEqptbl':
            readTblsEQP();
            break;
        case 'btnAddEqptbl':
            var tablaref = "Eqptbl";
            //activaBotonesEQP(tablaref, true);
            html_HideElement("updateEQPRIS"); //boton del modal de edición.
            html_ShowElement("agregarEQPRIS");
            html_ShowElement("secSerie"); //seccion de numero de serie
            //document.getElementById("nserEQP").disabled = false; //número de serie (activo al geenrar uno nuevo)

            document.getElementById("nomEQP").value = "";
            document.getElementById("marcaEQP").value = "";

            cambiaEstadoModal(".modalEquipoRIS", true); //true =activaer
            actualizaDialogoModal(".modalEquipoRIS-content", "12%", "1%", "60%", "50%"); //top 12%

            break;
        case 'btnEdtEqptbl':
            html_ShowElement("updateEQPRIS"); //boton del modal de edición.
            html_HideElement("agregarEQPRIS");
            html_HideElement("secSerie");    //seccion de numero de serie        
            //document.getElementById("nserEQP").disabled = true; //número de serie (inhabilitado por ser PK)
            var tabla = "Eqptbl";
            var valorRadioPK = getRadioValIndice("radio" + tabla) + 1;//valor de 0 a k-1, sumarle 1
            console.log(valorRadioPK);
            if ((valorRadioPK) > 0) {
                //console.log("llave primaria renglon: "+valorRadioPK);
                //var tablerowref = findDatainTable(tabla, colPK, valorRadioPK);
                var columnasrow = getRowCells(valorRadioPK, tabla);
                console.log("Radio sel: " + valorRadioPK);
                console.log(columnasrow);
                clearRadioSelNumber("radio" + tabla, valorRadioPK - 1); //limpiar renglon seleccionado
                //nserEQP,nomEQP,marcaEQP,modeloEQP, modalEqp->idmod,areEqp->idarea,edoEqp->idedo
                //var cabecerapac = ["Serie", "Nombre", "Marca", "Modelo", "Modalida", "Id_area", "Área","Estado"];   
                document.getElementById("nserEQP").value = columnasrow[0].innerText;
                document.getElementById("nomEQP").value = columnasrow[1].innerText;
                document.getElementById("marcaEQP").value = columnasrow[2].innerText;
                document.getElementById("modeloEQP").value = columnasrow[3].innerText;

                document.getElementById("modalEqp").value = columnasrow[4].innerText;
                //listbox modalidad
                document.getElementById("idmod").value = columnasrow[4].innerText;
                //listbox modalidad                

                document.getElementById("areEqp").value = columnasrow[5].innerText;
                //listbox area
                document.getElementById("idarea").value = columnasrow[6].innerText; //campo de area actual

                document.getElementById("edoEqp").value = columnasrow[7].innerText; //listbox estado
                document.getElementById("idedo").value = columnasrow[7].innerText; //campo de estado actual


                cambiaEstadoModal(".modalEquipoRIS", true); //true =activaer 
                actualizaDialogoModal(".modalEquipoRIS-content", "12%", "1%", "60%", "50%"); //top 12%                
            } else {
                alert("Seleccione un registro de la tabla");
            }
            break;
        case 'btnDelEqptbl':
            //borrar registro seleccioando
            break;

    }

}

//funcion para listboxes al cambiar actualizar campos
function updatePKSEqp(e) {
    var sellist = e.target.id;
    console.log(sellist);
    switch (sellist) {
        case 'modalEqp':
            var modalidad = document.getElementById("modalEqp").value;
            document.getElementById("idmod").value = modalidad;
            break;
        case 'areEqp':
            var area = document.getElementById("areEqp").value;
            document.getElementById("idarea").value = area;
            break;
        case 'edoEqp':
            var estado = document.getElementById("edoEqp").value;
            document.getElementById("idedo").value = estado;
            break;
    }
}

function readTblsEQP() {
    var divtable = "showDataEQP";
    var tabladatos = "Eqptbl";
    var columnaedicion = 9;
    var columnaPK = 0; //se toma como llave primaria para busquedas la columna 0
    var coleditar = "Ref";
    var roweditar = "Sel ";
    var actionListener = "evaluaRadioButtonTablaPK0('" + tabladatos + "'," + columnaPK + ")";
    //var actionListener = null;
    //var colocultas=[0,5];//se oculta la columna 0 y 5
    //AreaDeServicio: idArea,Nombre,Descripcion
    //EQUIPO: NSerie, AreaDeServicio_idArea ,Ubicacion 
    //EquipoImagenologia: NSerie,AreaDeServicio_idArea,Nombre,Marca,Modelo,Modalidad,FechaInstalacion,Estado

    var colocultas = [5]; //se oculta id area
    var cabecerapac = ["Serie", "Nombre", "Marca", "Modelo", "Modalida", "Id_area", "Área", "Estado","Fecha Instalación"];
    CreateTableFromJSON(divtable, tabladatos, cabecerapac); //parametros referencia div, nombre tabla , cabecera
    var jsonData = {"nombre": "*"};
    var getEquipoimg = postRestService(uriserv + "/EquipoIMGEntity/ReadAll", jsonData);
    $.when(getEquipoimg.done(function (data) {
        var array = convertTojsonArray(data[0]);
        UpdateTableRows(tabladatos, array);
        tableRowColorCellSelectionKlib(tabladatos);
        hideTableColumns(tabladatos, colocultas); //ocultar columnas (medico, idpac) 
        addRadioButtonColumnPKTBL(tabladatos, columnaedicion, coleditar, roweditar, actionListener, columnaPK); //columna k con radiobutton y acctionlistne                     
    }));
}

function convertTojsonArray(arreglocadena) {
    var resultSet = []; //arreglo de objetos json
    for (var i in arreglocadena) {
        //var rowi = eval('(' + arreglocadena[i] + ')'); //conversión de cada renglo (cadena )a objeto json
        var rowi = JSON.parse(arreglocadena[i]);
        resultSet.push(rowi);
    }
    return resultSet;
}

function listenermodalEQPRIS(e) {
    var opc = e.target.id;
    switch (opc) {
        case 'cancelarEQPRIS':
            html_HideElement("btnEdtEqptbl"); //boton del modal de edición.
            html_HideElement("btnDelEqptbl"); //boton del modal de edición.            
            cambiaEstadoModal(".modalEquipoRIS", false); //true =activaer            
            break;
        case 'updateEQPRIS':
            var formData = getFormData("UpdateEqp", "formEquipoRIS");
            //var getEquipoimg =POSTForDataFiles(formData, uriserv + "/FormularioEqpImg/UpdateEqp");
            var getEquipoimg = POSTForDataFiles(formData, uriserv + "/FormularioEqpImg");
            $.when(getEquipoimg.done(function (data) {
                console.log(data);
                var resp = JSON.parse(data);
                console.log(resp);
                if (resp[0] === '1') {
                    alert("Se actualizo el registro con exito");
                    //actualizar la lista nuevamente                        
                   readTblsEQP();
                   html_HideElement("btnEdtEqptbl"); //boton del modal de edición.
                   html_HideElement("btnDelEqptbl"); //boton del modal de edición.

                } else {
                    alert("Error al actualizar el registro");
                }
            }));
            cambiaEstadoModal(".modalEquipoRIS", false); //true =activaer                                 
            break;
        case 'agregarEQPRIS':
            var formData = getFormData("CreateEqp", "formEquipoRIS");
            //var getEquipoimg =POSTForDataFiles(formData, uriserv + "/FormularioEqpImg/CreateEqp");
            var getEquipoimg = POSTForDataFiles(formData, uriserv + "/FormularioEqpImg");
            $.when(getEquipoimg.done(function (data) {
                console.log(data);
                var resp = JSON.parse(data);
            }));
            cambiaEstadoModal(".modalEquipoRIS", false); //true =activaer                 
            break;
    }
}

function getFormData(crud, formname) {
    //var form = document.forms.namedItem("formEquipoRIS");
    var form = document.forms.namedItem(formname);
    var formData = new FormData(form); //campos con referencia de name
    //formData.append('Operation', 'Create'); //agregar campo para CRUD, webservce contra parte
    formData.append('Operation', crud); //agregar campo para CRUD, webservce contra parte
    return formData;
}

window.onload = function () {
    //$().ready(function () {   
    console.log("Cosntruyendo pagina EQP 3");
    //dragElement(document.getElementById("mymodalEquipoRIS")); //modal que se puede arrastrar cabecera
    //html_disableItem('btnDelEqptbl', false,'#cccccc','#ffffff');
};
//});