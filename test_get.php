<?php
   // connect to mongodb
   $m = new MongoClient();
   // select a database
   $db = $m->aricent;
  $cursor = $db->store->find();
echo json_encode(iterator_to_array($cursor));
?>