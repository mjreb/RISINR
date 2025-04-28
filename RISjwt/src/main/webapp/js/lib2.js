
function activeTab(evt, opcionMenu) {
    var i, tabcontent, tablinks;
    //console.log(evt.currentTarget);
    tabcontent = document.getElementsByClassName("tabcontent"); // Trae a todos los elementos del html actual que tiene  como clase tabcintent(coontenido de la tabla)
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none"; // Esconde todos los contenidos 
    }
    tablinks = document.getElementsByClassName("tablinks"); // Trae todos los elements que tiene por clase tablinks 

    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", ""); // Deja desmarcados todas las opciones 
    }

    document.getElementById(opcionMenu).style.display = "block"; // Deja marcado solo uno 
    strtablinks = evt.currentTarget.className;
    if (strtablinks === "tablinks")
        evt.currentTarget.className += " active";//solo para la clase del menu
}

function agregarPreloader(servicio) {
    $('#loader' + servicio).show();
    $('#fade' + servicio).show();
    //document.getElementById('loader'+servicio).style.display = 'block';
    //document.getElementById('fade'+servicio).style.display = 'block';
}

function removerPreloader(servicio) {
    $('#loader' + servicio).hide();
    $('#fade' + servicio).hide();
    //document.getElementById('loader'+servicio).style.display = 'none';
    //document.getElementById('fade'+servicio).style.display = 'none';
} 

function cleanCheckboxValues(name) {
    var clist = document.querySelectorAll(`input[name="${name}"]`);
    for (var i = 0; i < clist.length; ++i) { 
        clist[i].checked = false; 
    }
}

function html_GetElement(pNd) {
    try {
        var node;
        switch (typeof (pNd)) {
            case 'string':
                node = document.getElementById(pNd);
                break;
            case 'object':
                node = pNd;
                break;
            default:
                node = false;
                break;
        }
        return node;
    } catch (e) {
        return false;
    }
}

function html_disableItem(nd, a,coloractivo,colorinactivo) {
    var lEl = html_GetElement(nd);
    if (lEl && lEl !== false) {
        if (a) {
            lEl.disabled = true;
            //lEl.style.background = '#cccccc';
            lEl.style.background = colorinactivo;
        } else {
            lEl.disabled = false;
            //lEl.style.background = '#ffffff';
            lEl.style.background = coloractivo;
        }
    }
    return true;
}

function html_VisibleElement(pNd) {
    var l_Node = html_GetElement(pNd);
    if (l_Node) {
        l_Node.style.display = "block";
    }
    return l_Node;
}

function html_HideElement(pNd) {
    var l_Node = html_GetElement(pNd);
    if (l_Node) {
        l_Node.style.display = "none";
    }
    return l_Node;
}

function html_ShowElement(pNd) {
    var node = html_GetElement(pNd);
    if (node) {
        node.style.display = "";
    }
    return node;
}
//Cambia estado de despliegue de un elemento
function html_ToggleDisplayElement(pNd) {
    var node = html_GetElement(pNd);
    if (node) {
        if (node.style.display === "none") {
            html_VisibleElement(node);
        } else {
            html_HideElement(node);
        }
    }
    return node;
}

//Cambia estado de despliegue de un elemento
function html_ToggleVisibleElement(pNd) {
    var node = html_GetElement(pNd);
    if (node) {
        if (node.style.visibility === "visible") {
            node.style.visibility = "hidden";
        } else {
            node.style.visibility = "visible";
        }
    }
    return node;
}

//color de renglones % 2
function backGroundColor(tablename, headerback, headercol, rowback, rowcol) {
    var tbl = document.getElementById(tablename);
    if (tbl !== null) {
        if (tbl.rows[0] !== null) {
            tbl.rows[0].style.backgroundColor = headerback;
            tbl.rows[0].style.color = headercol;
        }
        for (var i = 1; i < tbl.rows.length; i++) {
            if (i % 2 === 0) {
                tbl.rows[i].style.backgroundColor = rowback;
                tbl.rows[i].style.color = rowcol;
            } else {
                tbl.rows[i].style.backgroundColor = rowcol;
                tbl.rows[i].style.color = rowback;
            }
        }
    }
}

function rowColor(tablename, selbak, selcol, parbak, parcol, imparback, imparcol) {
    //"#00FFFF","#000000","#7F7F7F","#FFFFFF","","#000000"
    var tbl = document.getElementById(tablename);
    if (tbl !== null) {
        /*if (tbl.rows[0] != null) { 
         //tbl.rows[0].style.backgroundColor = "#365890"; 
         tbl.rows[0].style.backgroundColor = "#FF0000"; //color de cabecera 
         tbl.rows[0].style.color = "#FFFFFF";  //color de letra
         }*/
        for (var i = 1; i < tbl.rows.length; i++) {
            tbl.rows[i].style.cursor = "pointer";
            tbl.rows[i].onmousemove = function () {
                this.style.backgroundColor = selbak;
                this.style.color = selcol;
            };

            if (i % 2 === 0) {
                tbl.rows[i].onmouseout = function () {
                    this.style.backgroundColor = parbak;
                    this.style.color = parcol;
                };
            } else {
                tbl.rows[i].onmouseout = function () {
                    this.style.backgroundColor = imparback;
                    this.style.color = imparcol;
                };
            }

        }
    }
}

function cellSelection(tablename, funcion) {
    var tbl = document.getElementById(tablename);
    if (tbl !== null) {
        for (var i = 0; i < tbl.rows.length; i++) {
            for (var j = 0; j < tbl.rows[i].cells.length; j++)
                //tbl.rows[i].cells[j].onclick = function () { getval(this); }; 
                //tbl.rows[i].cells[j].setAttribute('onclick', 'getval(this)');
                tbl.rows[i].cells[j].setAttribute('onclick', funcion);
            //tbl.rows[i].cells[0].style.visibility="hidden"; //ocultar coluna
        }
    }
}

function cellHeaderSelection(tablename, funcion, columna) {
    var tbl = document.getElementById(tablename);
    if (tbl !== null) {
                tbl.rows[0].cells[columna].setAttribute('onclick', funcion);
    }
}

function cellKnSelection(tablename, funcion, columna) {
    var tbl = document.getElementById(tablename);
    if (tbl !== null) {
        for (var i = 0; i < tbl.rows.length; i++) {
            //for (var j = 0; j < tbl.rows[i].cells.length; j++)
                tbl.rows[i].cells[columna].setAttribute('onclick', funcion);
        }
    }
}

function clearRadioSelNumber(idRadio, rowId) {
    var tiposel = document.getElementsByName(idRadio);
    tiposel[rowId].checked = false; //limpiar seleccion del renglon 
}


//Hora del sistema cliente
function timestamp() {
    var d, s = "";
    var c = ":";
    d = new Date();
    s += d.getHours() + c;
    s += d.getMinutes() + c;
    s += d.getSeconds() + c;
    s += d.getMilliseconds();
    return(s);
}

function dateYYYYmmdd() {
    var d, s = "";
    var c = "-";
    d = new Date();
    s += d.getFullYear() + c;
    s += (d.getMonth() + 1) + c;
    // ojo day: 0=Sunday, 1=Monday, etc.
    //s+=d.getDay();
    s += d.getDate();
    return(s);
}

//construye una tabla html utilizando la informacion de un arreglo en json
function CreateTableFromJSON(divnameref, tablename, refhed) {
    console.log("creando la tabla con CreateTableFromJSON");
    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table"); // Crear un elemento llamdo tabla en el html
    // SET THE TABLE ID. 
    // WE WOULD NEED THE ID TO TRAVERSE AND EXTRACT DATA FROM THE TABLE.	        
    table.setAttribute('id', tablename); //Definicion de id para tabla dinamica
    table.setAttribute('width', '100%'); //100% del componente padre
    //table.setAttribute('height', 200);
    //table.setAttribute('border', '1');
    //table.scrollIntoView(true);

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.
    var tr = table.insertRow(-1);                   // Esto significa que se pone una nueva fila al final 
    //for (var i = 0; i < col.length; i++) {
    for (var i = 0; i < refhed.length; i++) { //refhed.length es el tamaño de la tabla 
        var th = document.createElement("th");      // TABLE HEADER.
        //th.innerHTML = col[i]; //cabecera con las claves del objeto json
        th.innerHTML = refhed[i]; //Asigna el texto que aparecerá dentro de la celda de cabecera (<th>).

        tr.appendChild(th); // Añade la celda de cabecera (<th>) como hijo de la fila (<tr>) que se creó previamente.
    }

    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
    var divContainer = html_GetElement(divnameref);
    divContainer.innerHTML = ""; // Elimina cualquier elemento hijo previo del div (para evitar duplicados).
    divContainer.appendChild(table); // Añade la tabla al div.
}


function UpdateTableRows(tablename, jsonarray) {
    // ADD JSON DATA TO THE TABLE AS ROWS.
    var tableref = document.getElementById(tablename);
    //borrar contenido si exite. la cabecera se mantiene
    var rowCount = tableref.rows.length;
    for (var i = 1; i < rowCount; i++) {
        tableref.deleteRow(1);
    }

    var col =getKeysJsonArray(jsonarray);
    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < jsonarray.length; i++) {
        tr = tableref.insertRow(-1);
        for (var j = 0; j < col.length; j++) {
            var tabCell = document.createElement('td');          // TABLE DEFINITION.
            tabCell = tr.insertCell(-1);
            tabCell.innerHTML = jsonarray[i][col[j]];// ADD VALUES TO EACH CELL.
        }
    }
}

function UpdateTableRowsSort(tablename, jsonarray, col) {
    // ADD JSON DATA TO THE TABLE AS ROWS.
    var tableref = document.getElementById(tablename);
    //borrar contenido si exite. la cabecera se mantiene
    var rowCount = tableref.rows.length;
    for (var i = 1; i < rowCount; i++) {
        tableref.deleteRow(1);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < jsonarray.length; i++) {
        tr = tableref.insertRow(-1);
        for (var j = 0; j < col.length; j++) {
            var tabCell = document.createElement('td');          // TABLE DEFINITION.
            tabCell = tr.insertCell(-1);
            tabCell.innerHTML = jsonarray[i][col[j]];// ADD VALUES TO EACH CELL.
        }
    }
}


function UpdateListBox(ref,jsonarray,colvalue,coltext){
    var listbox = document.getElementById(ref);
    //var listbox = document.createElement(ref);
    listbox.options.length=1; //borra contenido del listbox
    var columnas =getKeysJsonArray(jsonarray);
    //console.log(jsonarray);
    for (var i = 0; i < jsonarray.length; i++) {
          //elem='<option value="'+jsonarray[i][columnas[3]]+'">' + jsonarray[i][columnas[2]] + "</option>";
          var optionelem = document.createElement('option');
          optionelem.setAttribute('value', jsonarray[i][columnas[colvalue]]); //Definicion de valor para tabla dinamica
          optionelem.innerHTML=jsonarray[i][columnas[coltext]];
          listbox.appendChild(optionelem);
    } 
}

function getSelectedCheckboxValues(name) {
    //const checkboxes = document.querySelectorAll(`input[name="${name}"]:checked`);
    che2 = document.querySelectorAll(`input[name="${name}"]`);
    //console.log(che2);
    let values = [];//arreglo de seleccion
    //let values = "";
    for (var i = 0; i < che2.length; i++) {
        if (che2[i].checked===true) {
            //values.push(i+":"+che2[i].checked);
            values.push(i);
            //values += (i+1) + ",";
        }
    }
    return values;
}

//setSelectedCheckboxValues("perfilapp",[1,5]); //PRUEBAS: activar los elementos del arrray [1,5]
function setSelectedCheckboxValues(name,arrayIndexs) {
    //const checkboxes = document.querySelectorAll(`input[name="${name}"]:checked`);
    
    var che2 = document.querySelectorAll(`input[name="${name}"]`);
    //console.log(che2);
    for (var j = 0; j < arrayIndexs.length; j++) {
        var colIndex = arrayIndexs[j];
        //console.log("che2: "+colIndex);
        if(colIndex<=che2.length)
           che2[colIndex].checked = true;
         //che2[colIndex].setAttribute('checked', true);
    }
    
}

function getRadioVal(idRadio) {
    var val;
    var tiposel = document.getElementsByName(idRadio);
    for (let x = 0; x < tiposel.length; x++) {
        if (tiposel[x].checked) {
            val = tiposel[x].value;
            break; // and break out of for loop
        }
    }
    return val; // return value of checked radio or undefined if none checked
}

function getRadioValIndice(idRadio) {
    var val;
    var tiposel = document.getElementsByName(idRadio);
    for (let x = 0; x < tiposel.length; x++) {
        if (tiposel[x].checked) {
            //val = tiposel[x].value;
            val = x;
            break; // and break out of for loop
        }
    }
    return val; // return value of checked radio or undefined if none checked
}

function setSelectedIndex(refselid, texto) {
    for (var i = 0; i < refselid.options.length; i++) {
        if (refselid.options[i].text === texto) {
            refselid.options[i].selected = true;
            return;
        }
    }
}

function getKeysJsonArray(jsonDataArray){
    columnas = [];
    for (var i = 0; i < jsonDataArray.length; i++) {
        for (var key in jsonDataArray[i]) {
            if (columnas.indexOf(key) === -1) {
                columnas.push(key);
            }
        }
    } 
    /*
    //pruebas para ver el contenido
    for (var i = 0; i < jsonDataArray.length; i++) {
        for (var j = 0; j < columnas.length; j++) {
          console.log(columnas[j]+": "+jsonDataArray[i][columnas[j]]);// ADD VALUES TO EACH CELL.
       } 
       console.log("********");// ADD VALUES TO EACH CELL.
    } */ 
    return columnas;
}

function updateTableColumns(tablename, column, newdata) {
    //segunda forma via html
    const tableReg = document.getElementById(tablename);
    for (var j = 1; j < tableReg.rows.length; j++) {
        tmp = tableReg.rows[j].cells[column].innerHTML;
        tableReg.rows[j].cells[column].innerHTML = tmp + " " + newdata;
        //tableReg.rows[j].cells[column].appendChild(newdata);

    }
}

//COMPARA EL CONTIDO DE UNA COLUMNA CON EL TEXTO SELECCIONADO, REGRESA EL NUMERO DE RENGLON
function compareTableColumns(tablename, column, newdata) {
    //segunda forma via html
    var ren=0;
    const tableReg = document.getElementById(tablename);
    //console.log(tableReg);
    for (var j = 1; j < tableReg.rows.length; j++) {
        tmp = tableReg.rows[j].cells[column].innerHTML;
        //console.log("Columna: "+tmp+":   "+newdata);
        if(tmp===newdata){
            //console.log("Columna: "+tmp+":   "+newdata);
           ren=j; 
        }
    }
    return ren;
}

function hideTableColumns(tablename, columnsIndexs) {
    //ojo: columnsIndexs es un arreglo [1,2,..n]
    //primera forma via jquery
    /*$("#" + tablename + " tr").each(function () {
     for (var i = 0; i < columnsIndexs.length; i++) {
     var colIndex = columnsIndexs[i];
     //console.log($(this));
     //$($(this).find("th")[colIndex]).hide(); //cabecera                       
     $($(this).find("td")[colIndex]).hide(); //renglones
     }
     });*/
    //segunda forma via html
    const tableReg = document.getElementById(tablename);
    for (var j = 0; j < tableReg.rows.length; j++) {
        for (var i = 0; i < columnsIndexs.length; i++) {
            var colIndex = columnsIndexs[i];
            tableReg.rows[j].cells[colIndex].style.display = 'none';
        }
    }
}

function insertColumnK(tablename, columnsIndexs, tituloCabecera, refcolumnas) {
    //ojo columnsIndexs no debe ser mayor que las columnas existentes
    const tableReg = document.getElementById(tablename);
    //console.log(tableReg);
    for (var j = 0; j < tableReg.rows.length; j++) {
        //const cellsOfRow = getRowCells(j, "tblusuarios");
        //console.log(cellsOfRow);
        var firstRow = document.getElementById(tablename).rows[j];
        var x = firstRow.insertCell(columnsIndexs);
        if (j === 0) {
            x.innerHTML = tituloCabecera;
        } else {
            x.innerHTML = refcolumnas;
        }
    }
}

function deleteTableRow(tablename,rowindex) {
  document.getElementById(tablename).deleteRow(rowindex);
}

function getRowCells(rowindex, tablename) {
    var table = document.getElementById(tablename);
    var rowSelected = table.getElementsByTagName('tr')[rowindex];
    celldas = rowSelected.getElementsByTagName('td');
    return celldas;
}

//segunda forma de obtener un renglon de una tabla
function getRowCells2(rowindex, tablename) {
    var table = document.getElementById(tablename);
    var cellsOfRow = table.rows[rowindex].getElementsByTagName('td');
    return cellsOfRow;
}

var asc = 0;
function sort_tableORI(table, col) {
    $('.sortorder').remove();
    if (asc === 2) {
        asc = -1;
    } else {
        asc = 2;
    }
    var rows = table.tBodies[0].rows;
    var rlen = rows.length;
    var arr = new Array();
    var i, j, cells, clen;
    // fill the array with values from the table
    //se quita la cabecera
    for (i = 1; i < rlen; i++) {
        cells = rows[i].cells;
        clen = cells.length;
        arr[i] = new Array();
        for (j = 0; j < clen; j++) {
            arr[i][j] = cells[j].innerHTML;
        }
    }

    // sort the array by the specified column number (col) and order (asc)
    arr.sort(function (a, b) {
        var retval = 0;
        //var col1 = a[col].toLowerCase().replace(',', '').replace('$', '').replace(' usd', '');
        //var col2 = b[col].toLowerCase().replace(',', '').replace('$', '').replace(' usd', '');
        var col1 = a[col].toLowerCase().replace(',', '').replace('$', '');
        var col2 = b[col].toLowerCase().replace(',', '').replace('$', '');
        var fA = parseFloat(col1);
        var fB = parseFloat(col2);
        if (col1 !== col2) {
            if ((fA === col1) && (fB === col2)) {
                retval = (fA > fB) ? asc : -1 * asc;
            } else {
                //numerical
                retval = (col1 > col2) ? asc : -1 * asc;
            }
        }
        return retval;
    });

    for (var rowidx = 0; rowidx < arr.length - 1; rowidx++) {
        for (var colidx = 0; colidx < arr[rowidx].length; colidx++) {
            //table.tBodies[0].rows[rowidx+1].cells[colidx].innerHTML=arr[rowidx][colidx]; 
            table.tBodies[0].rows[rowidx + 1].cells[colidx].innerHTML = arr[rowidx][colidx];
        }
    }
    hdr = table.rows[0].cells[col];
    if (asc === -1) {
        $(hdr).html($(hdr).html() + '<span class="sortorder">▲</span>');
    } else {
        $(hdr).html($(hdr).html() + '<span class="sortorder">▼</span>');
    }



}

function sortTable(n, tablename) {
    sort_tableORI(document.getElementById(tablename), n);
}
//----------------
//----------------------------

//Make the DIV element draggagle:
//dragElement(document.getElementById("mydiv"));

function dragElement(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  console.log(elmnt.id);
  if (document.getElementById(elmnt.id + "header")) {
    /* if present, the header is where you move the DIV from:*/
    document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
  } else {
    /* otherwise, move the DIV from anywhere inside the DIV:*/
    elmnt.onmousedown = dragMouseDown;
  }

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
    //elmnt.style.width=300+"px";
    //console.log(elmnt.style.top+" "+elmnt.style.left);
    //console.log(e.clientX+" "+e.clientY);
  }

  function closeDragElement() {
    /* stop moving when mouse button is released:*/
    document.onmouseup = null;
    document.onmousemove = null;
  }
}

function cambiaEstadoModal(idmodal,bandera){
    //var dialogoUsuarios = document.querySelector(".modalPERFILES");
    var dialogoUsuarios = document.querySelector(idmodal);
    if(bandera){
     dialogoUsuarios.style.display = "block";//activar dialogo modal, cristal transparente                
    }else{
     dialogoUsuarios.style.display = "none";//desactivar dialogo modal, cristal transparente  
    }
}

//OJO el parametro es el modal-content
//actualizaDialogoModal(".modalDialogVal-content","20%","50%","40%","30%");
function actualizaDialogoModal(idmodal,xi,yi,xf,yf){
    //var dialogoUsuarios = document.querySelector(".modalPERFILES");
    var dialogoUsuarios = document.querySelector(idmodal);
     dialogoUsuarios.style.top=xi;
     dialogoUsuarios.style.left=yi;
     dialogoUsuarios.style.width=xf;
     dialogoUsuarios.style.height=yf;
}

function tableRowColorCellSelectionKlib(tablName) {
    //backGroundColor(tablName, "#5AB4CA", "#FFFFFF", "#97D1DE", "#666666", "#FFFFFF", "#666666");
    //rowColor(tablName, "#2179FC", "#FFFF0F", "#97D1DE", "#666666", "#FFFFFF", "#666666"); //puntero raton (b,c); pares (b,c); impares (b,c) 
    backGroundColor(tablName, "rgba(88,142,255,.5)", "#000000", "#7F7F7F", "#FFFFFF");
    rowColor(tablName, "#00FFFF", "#000000", "#7F7F7F", "#FFFFFF", "#ffffff", "#000000"); //puntero raton (b,c); pares (b,c); impares (b,c)     
}

//crea un elemento de tipo [text, password,radio, etc.]
function html_CreateFormElement(pType, pName, pValue) {
    //var lEl = document.createElement('<input type=' + pType + ' name=' + pName + ' value=' + pValue + ' />');
    var lEl = document.createElement('input');
    // SET INPUT ATTRIBUTE.
    lEl.setAttribute('type', pType);
    lEl.setAttribute('name', pName);   
    lEl.setAttribute('value', pValue);
    return lEl;
}


//se toma como llave primaria para busquedas la columna: PrimaryKey (value del radiobutton)
function addRadioButtonColumnPKTBL(tablename, columnsIndexs, tituloCabecera, refcolumnas, radiolistener, PrimaryKey) {
    //ojo columnsIndexs no debe ser mayor que las columnas existentes
    const tableReg = document.getElementById(tablename);
    for (var j = 0; j < tableReg.rows.length; j++) {
        var firstRow = tableReg.rows[j];
        if (j === 0) {
            var th = document.createElement("th"); // TABLE HEADER.
            th.innerHTML = tituloCabecera; //cabecera externa
            firstRow.appendChild(th);
        } else {
            var cellsOfRow = firstRow.getElementsByTagName('td'); //columnas del renglon kesimo
            //var cellsOfRow = tableReg.rows[j].getElementsByTagName('td');//columnas del renglon kesimo
            //console.log(cellsOfRow);
            var x = firstRow.insertCell(columnsIndexs);
            x.innerHTML = refcolumnas;
            /*
            // ADD A BUTTON.
            var radiobutton = document.createElement('input');
            // SET INPUT ATTRIBUTE.
            radiobutton.setAttribute('type', 'radio');
            radiobutton.setAttribute('name', "radio" + tablename);
            var pk = cellsOfRow[PrimaryKey].innerText; //columna[PrimaryKey] utilizada comoo llave primaria
            radiobutton.setAttribute('value', pk);
            //radiobutton.setAttribute('id', j); //id radio butoon [1..j]
            // ADD THE BUTTON's 'onclick' EVENT.
            //radiobutton.setAttribute('onclick', 'alert("OJO")');
            radiobutton.setAttribute('onclick', radiolistener);
            */
            
            var pk = cellsOfRow[PrimaryKey].innerText; //columna[PrimaryKey] utilizada comoo llave primaria
            var radiobutton=html_CreateFormElement('radio', 'radio' + tablename, pk);    //elemeto:(tipo, nombre, valor) 
            radiobutton.setAttribute('onclick', radiolistener);
            
            x.appendChild(radiobutton);
        }
    }
}

//busqueda de la cadena "searchtext" en la columna k
function findDatainTable(tablename, columna, searchText) {
    var tbl = document.getElementById(tablename);
    if (tbl !== null) {
        //let found=false;
        let row = -1;
        for (var i = 1; i < tbl.rows.length; i++) {// excluye cabecera
            //const compareWith =tbl.rows[i].cells[columna].innerHTML.toLowerCase();
            const compareWith = tbl.rows[i].cells[columna].innerHTML;
            //console.log("Dato: "+compareWith);
            if (searchText.length === 0 || compareWith.indexOf(searchText) > -1) {
                row = i;
            }
        }
        return row;  //número del renglon donde se encuentra la ocurrencia           
    }
}

function POSTForDataFiles(formData, servicio) {
    return $.ajax({
        url: servicio,
        type: 'post',
        data: formData,
        processData: false, // tell jQuery not to process the data
        enctype: 'multipart/form-data',
        contentType: false  // tell jQuery not to set contentType        
    }).done(function (data) {
        //console.log(data);
    }).fail(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
        console.log(textStatus);
        console.log(jqXHROrData);
        console.log(jqXHROrErrorThrown);
    }).always(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
    });
}