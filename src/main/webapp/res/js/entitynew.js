
var uploadedFiles = new Array ();
//var knownTypes = ["unknown", "CellML", "CSV", "HDF5", "EPS", "PNG", "XMLPROTOCOL", "TXTPROTOCOL"];
var knownTypes = ["unknown", "CellML", "CSV", "HDF5", "EPS", "PNG", "XMLPROTOCOL", "TXTPROTOCOL"];

function verifyNewEntity (jsonObject, elem, entityNameAction, versionNameAction, storeAction)
{
    elem.innerHTML = "<img src='"+contextPath+"/res/img/loading2-new.gif' alt='loading' />";
    
	var xmlhttp = null;
    // !IE
    if (window.XMLHttpRequest)
    {
        xmlhttp = new XMLHttpRequest();
    }
    // IE -- microsoft, we really hate you. every single day.
    else if (window.ActiveXObject)
    {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    xmlhttp.open("POST", '', true);
    xmlhttp.setRequestHeader("Content-type", "application/json");

    xmlhttp.onreadystatechange = function()
    {
        if(xmlhttp.readyState != 4)
        	return;
        
    	var json = JSON.parse(xmlhttp.responseText);
    	console.log (json);
    	displayNotifications (json);
    	
        if(xmlhttp.status == 200)
        {
        	
        	if (json.entityName && entityNameAction)
        	{
	        	var msg = json.entityName.responseText;
	        	if (json.entityName.response)
	        		entityNameAction.innerHTML = "<img src='"+contextPath+"/res/img/check.png' alt='valid' /> " + msg;
	        	else
	        		entityNameAction.innerHTML = "<img src='"+contextPath+"/res/img/failed.png' alt='invalid' /> " + msg;
        	}
        	if (json.versionName)
        	{
	        	var msg = json.versionName.responseText;
	        	if (json.versionName.response)
	        		versionNameAction.innerHTML = "<img src='"+contextPath+"/res/img/check.png' alt='valid' /> " + msg;
	        	else
	        		versionNameAction.innerHTML = "<img src='"+contextPath+"/res/img/failed.png' alt='invalid' /> " + msg;
        	}
        	if (json.createNewEntity)
        	{
	        	var msg = json.createNewEntity.responseText;
	        	if (json.createNewEntity.response)
	        	{
	        		var form = document.getElementById ("newentityform");
	        		removeChildren (form);
	        		var h1 = document.createElement("h1");
	        		var img = document.createElement("img");
	        		img.src = contextPath + "/res/img/check.png";
	        		img.alt = "created entity successfully";
	        		h1.appendChild(img);
	        		h1.appendChild(document.createTextNode (" Congratulations"));
	        		
	        		var p = document.createElement("p");
	        		p.appendChild(document.createTextNode ("You've just created a new entity! Have a look at "));
	        		var a = document.createElement("a");
	        		a.href = contextPath + "/myfiles.html";
	        		a.appendChild(document.createTextNode ("your files"));
	        		p.appendChild(a);
	        		p.appendChild(document.createTextNode ("."));
	        		form.appendChild(p);
	        		
	        		p = document.createElement("p");
	        		p.appendChild(document.createTextNode ("Schedule  "));
	        		a = document.createElement("a");
	        		a.href = contextPath + "/batch/" + json.createNewEntity.versionType + "/newVersion/" + json.createNewEntity.versionId;
	        		a.appendChild(document.createTextNode ("Batch Jobs"));
	        		p.appendChild(a);
	        		p.appendChild(document.createTextNode (" using this entity."));
	        		form.appendChild(p);

	        		form.appendChild(h1);
	        	}
	        	else
	        		storeAction.innerHTML = "<img src='"+contextPath+"/res/img/failed.png' alt='invalid' /> " + msg;
        	}
        }
        else
        {
        	elem.innerHTML = "<img src='"+contextPath+"/res/img/failed.png' alt='error' /> sorry, serverside error occurred.";
        }
    };
    xmlhttp.send(JSON.stringify(jsonObject));
}


function initNewEntity ()
{
	var entityName = document.getElementById("entityname");
	var versionName = document.getElementById("versionname");
	var entityNameAction = document.getElementById("entityaction");
	var versionNameAction = document.getElementById("versionaction");
	var storeAction = document.getElementById("saveaction");
	var svbtn = document.getElementById('savebutton');
	
	entityName.addEventListener("blur", function( event )
	{
		verifyNewEntity ({
	    	task: "verifyNewEntity",
	    	entityName: entityName.value
	    }, entityNameAction, entityNameAction, versionNameAction, storeAction);
	  }, true);
	
	versionName.addEventListener("blur", function( event ) {
		verifyNewEntity ({
	    	task: "verifyNewEntity",
	    	entityName: entityName.value,
	    	versionName: versionName.value
	    }, versionNameAction, entityNameAction, versionNameAction, storeAction);
	  }, true);
	
	
	var insertDate = document.getElementById('dateinserter');
	insertDate.addEventListener("click", function (ev) {
		if (versionName)
		{
			versionName.focus ();
			versionName.value = getYMDHMS (new Date());
			versionName.blur ();
		}
	}, true);
	

	
	
	
	initUpload (uploadedFiles, knownTypes);
	

	svbtn.addEventListener("click", function (ev) {
		verifyNewEntity(
		{
	    	task: "createNewEntity",
	    	entityName: entityName.value,
	    	versionName: versionName.value,
	    	files: uploadedFiles,
	    	mainFile: $('input[name="mainEntry"]:checked').val ()
	    }, storeAction, entityNameAction, versionNameAction, storeAction);
	}, true);
	
}


document.addEventListener("DOMContentLoaded", initNewEntity, false);