<?php
   // connect to mongodb
   $m = new MongoClient();
   echo "Connection to database successfully";
	
	$data = json_decode(file_get_contents("php://input"));
Print "Your information has been successfully added to the database.";

   // select a database
   $db = $m->aricent;
   echo "Database mydb selected";
   $collection = $db->password;
   echo "Collection selected succsessfully";
   
  $document = array( 
      "frist_name" => "$data->frist", 
      "last_name" => "$data->last", 
      "mobile" => "$data->mobile",
      "user_name" => "$data->user",
      "password" => "$data->password"
   );
	
   $collection->insert($document);
   echo "Document inserted successfully";
?>