
<?php
   // connect to mongodb
   $m = new MongoClient();
	$data = json_decode(file_get_contents("php://input"));

   $db = $m->aricent;
  // $user=$data->userlogin;
   //$password=$data->passlogin;
   
   $collection = $db->password;
   
 //$document = array( 
   // "user_name" => $user, 
     //"password" => $password,
  //);
	$document = array( 
    "user_name" => $data->usernames, 
     "password" => $data->passwords 
  );
   $cursor=$collection->find($document);
    foreach ($cursor as $document) {
  if ( $data->usernames === $document["user_name"] && 
	$data->passwords === $document["password"] ) {
		echo 'correct';
} else {
		echo 'wrong'; 
}
	}
 
   
?>