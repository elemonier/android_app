<?php 
/**
 * File to handle all API requests.
 * 
 * Each request will be identified by a TAG.
 * Response will be JSON data.
 * 
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') 
{
    $tag = $_POST['tag']; // get tag

    $response = array("tag" => $tag, "success" => 0, "error" => 0);
    
    // include db handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();
    	
    // possible tags: 'create_group', 'get_groups', 'login', 'register', 
    // and 'get_network_info'
    switch ($tag)
    {
    	case "create_group":
    		$owner_email = $_POST['owner_email'];
    		$network = $_POST['network'];
    		$start_location = $_POST['start_location'];
    		$end_location = $_POST['end_location'];
	    	$datetime = $_POST['departure_date_time'];
	    	$direction = $_POST['direction'];
	
	    	$group = $db->storeGroup($owner_email, $network, $start_location, 
	    			$end_location, $datetime, $direction); 	
	    	
	    	if ($group != false)
	    	{
	    		$response["success"] = 1;
	    		$response["group"]["group_id"] = $group["group_id"];
	    		$response["group"]["owner_email"] = $group["owner_email"];
	    		$response["group"]["network"] = $group["network"];
	    		$response["group"]["start_location"] = $group["start_location"];
	    		$response["group"]["end_location"] = $group["end_location"];
	    		$response["group"]["departure_date_time"] = $group["departure_date_time"];
	    		$response["group"]["direction"] = $group["direction"];
	    		$response["group"]["created_at"] = $group["created_at"];
	    		$response["group"]["updated_at"] = $group["updated_at"];
	    		
	    		echo json_encode($response);
				
	    	}
	    	else
	    	{
	    		// echo json with error = 1
				$response["error"] = 1;
		        $response["error_msg"] = "Error storing group";
		        echo json_encode($response);
	    	}
	    	break;
    /* Getting groups */
    	case "get_groups":
    		$direction = $_POST['direction'];
    		
    		$all_groups_array = $db->getAllGroups($direction);
			$groups["groups"] = $all_groups_array;
			
	    	echo json_encode($groups);

    		break;
    	/* Getting network info */
    	case 'get_network_info':
    
	    	$network_name = $_POST['network'];
	    	$network_info = $db->getNetwork($network_name);
	    	$network["success"] = 1;
	    	$network["network"] = $network_info;
	
	    	echo json_encode($network);
			break;
    /* User logging in */
    	case "login":
	        // Request type is check Login
	        $email = $_POST['email'];
	        $password = $_POST['password'];
	
	        // check for user
	        $user = $db->getUserByEmailAndPassword($email, $password);
	        if ($user != false)
	        {
	            // user found
	            // echo json with success = 1
	            $response["success"] = 1;
	            $response["user"]["uid"] = $user["unique_id"];
	            $response["user"]["unique_id"] = $user["unique_id"];
	            $response["user"]["name"] = $user["name"];
	            $response["user"]["email"] = $user["email"];
				$response["user"]["network"] = $user["network"];
	            $response["user"]["created_at"] = $user["created_at"];
	            $response["user"]["updated_at"] = $user["updated_at"];
	            
	            echo json_encode($response);
	        } 
	        else
	        {
	            // user not found
	            // echo json with error = 1
	            $response["error"] = 1;
	            $response["error_msg"] = "Incorrect email or password!";
	            echo json_encode($response);
	        }
	        break;
    	/* user registration */
    	case "register":
        // Request type is Register new user
			$name = $_POST['name'];
		    $email = $_POST['email'];
			$network = $_POST['network'];
			$password = $_POST['password'];
			// check if user is already existed
		    if ($db->isUserExisted($email))
		    {
		    	// user is already existed - error response
		        $response["error"] = 2;
				$response["error_msg"] = "User already existed";
		        echo json_encode($response);
		    }
		    else
		    {
		    	// store user
		        $user = $db->storeUser($name, $email, $network, $password);
		        if ($user)
		        {
		        	// user stored successfully
		            $response["success"] = 1;
		            $response["uid"] = $user["unique_id"];
		            $response["user"]["name"] = $user["name"];
		            $response["user"]["email"] = $user["email"];
					$response["user"]["network"] = $user["network"];
		            $response["user"]["created_at"] = $user["created_at"];
		            $response["user"]["updated_at"] = $user["updated_at"];
		            echo json_encode($response);
		        }
		        else
		        {
		        	// user failed to store
		            $response["error"] = 1;
		            $response["error_msg"] = "Error occured in Registartion";
		            echo json_encode($response);
		        }
		    }
		    break;
	}
}
else
{
    echo "Invalid tag. Access denied.";
}
?>
