<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:skeleton headerImports="${PageHeader}" notes="${Notifications}" user="${User}" title="${Title}Comparison - " contextPath="${contextPath}">
    <h1 id="heading">Comparison</h1>
    
    <div id="entitiesToCompare">
    loading...
    </div>
    
	<div id="filedetails">
		<div class="closebtn"><small>
		    <a id="exportPlot" style="display: none;">export plot data</a> | 
		    <a id="fileclose">&otimes; close</a>
		</small></div>
    	<h3 id="filename"></h3>
	    <div id="filedisplay"></div>
	</div>
    
    
    <div id="files">
	    <h3 id="outputFileHeadline"></h3>
	    <table id="filestable">
		</table>
	</div>
</t:skeleton>

