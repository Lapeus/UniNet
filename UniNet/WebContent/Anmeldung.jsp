<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="bootstrap.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Willkommen bei UniNet</title>
</head>
<body>
<p>
<form class="form-inline">
  <div class="col-md-6"></div>
  <div class="form-group">
    <label class="sr-only" for="exampleInputEmail3">Email Adresse</label>
    <input type="email" class="form-control" id="exampleInputEmail3" placeholder="E-Mail">
  </div>
  <div class="form-group">
    <label class="sr-only" for="exampleInputPassword3">Passwort</label>
    <input type="password" class="form-control" id="exampleInputPassword3" placeholder="Passwort">
  </div>
  <div class="checkbox">
    <label>
      <input type="checkbox"> Angemeldet bleiben
    </label>
  </div>
  <button type="submit" class="btn btn-default">Anmelden</button>
</form>


<form role="form" class="form-horizontal">
<div class="row"><br><br><br></div>

	<div class="form-group" >
	    <label for="email" class="col-sm-2 control-label">
	        Name</label>
	    <div class="col-sm-4">
	        <div class="row">
	            <div class="col-md-3">
	                <select class="form-control">
	                    <option>Herr</option>
	                    <option>Frau</option>
	                </select>
	            </div>
	            <div class="col-md-9">
	                <input type="text" class="form-control" placeholder="Name" />
	            </div>
	        </div>
	    </div>
	</div>
	<div class="form-group">
	    <label for="email" class="col-sm-2 control-label">
	        Email</label>
	    <div class="col-sm-4">
	        <input type="email" class="form-control" id="email" placeholder="Email" />
	    </div>
	</div>
	<div class="form-group">
	    <label for="mobile" class="col-sm-2 control-label">
	        Mobile</label>
	    <div class="col-sm-4">
	        <input type="email" class="form-control" id="mobile" placeholder="Mobile" />
	    </div>
	</div>
	<div class="form-group">
	    <label for="password" class="col-sm-2 control-label">
	        Password</label>
	    <div class="col-sm-4">
	        <input type="password" class="form-control" id="password" placeholder="Password" />
	    </div>
	</div>
	<div class="row">
	    <div class="col-sm-2">
	    </div>
	    <div class="col-sm-4">
	        <button type="button" class="btn btn-primary btn-sm">
	            Save & Continue</button>
	        <button type="button" class="btn btn-default btn-sm">
	            Cancel</button>
	    </div>
	</div>
</form>




</body>
</html>