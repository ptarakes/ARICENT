<?php
   // connect to mongodb
   $m = new MongoClient();
   echo "Connection to database successfully";
	
	$data = json_decode(file_get_contents("php://input"));
Print "Your information has been successfully added to the database.";

   // select a database
   $db = $m->aricent;
   echo "Database mydb selected";
   $collection = $db->store;
   echo "Collection selected succsessfully";
//'devicename': $scope.devicename, 'devicetype': $scope.devicetype, 'selectedclient': $scope.selectedclient, 'devicenumber': $scope.devicenumber, 'nodename': $scope.nodename,'nodekey': $scope.nodekey,'nodenumber': $scope.nodenumber,'rulename': $scope.rulename,'maxtem': $scope.maxtem,'mintem': $scope.mintem,'alarmname': $scope.alarmname,'alarmtype': $scope.alarmtype
  $document = array( 
      "devicename" => "$data->devicename", 
      "devicetype" => "$data->devicetype", 
      "client" => "$data->selectedclient",
      "devicenumber" => "$data->devicenumber",
      "nodename" => "$data->nodename",
	   "nodekey" => "$data->nodekey", 
      "nodenumber" => "$data->nodenumber", 
      "rulename" => "$data->rulename",
      "maxtem" => "$data->maxtem",
      "mintem" => "$data->mintem", 
	  "alarmname" => "$data->alarmname", 
      "alarmtype" => "$data->alarmtype"
   );
	
   $collection->insert($document);
   echo "Document inserted successfully";
?>