<?php

/**
 * Handles functions having to do with storing and getting group and user data
 * from the online database.
 * @author Emily Pakulski
 */

class DB_Functions
{
    private $db;

    //put your code here
    // constructor
    function __construct()
	{
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() { }
    
    public function getNetwork($network)
    {
    	$network_info = mysql_query("SELECT * FROM networks WHERE domain_string = '$network'");
    	$network_array = mysql_fetch_assoc($network_info);
    	return $network_array;
    }
    
//     public function getAllGroups()
//     {
//     	$group_ids = mysql_query('SELECT group_id FROM groups');
//     	$all_groups_array = array();

//     	while ($group = mysql_fetch_assoc($group_ids))
//     	{
//     		$val = array_pop($group);
//     		$all_groups_array[] = $this->getGroup($val);
//     	}
//     	return $all_groups_array;
//     }
    
    public function getAllGroups($direction)
    {
    	$group_ids = mysql_query("SELECT group_id FROM groups where direction = '$direction'");
    	$all_groups_array = array();
    
    	while ($group = mysql_fetch_assoc($group_ids))
    	{
    		$val = array_pop($group);
    		$all_groups_array[] = $this->getGroup($val);
    	}
    	return $all_groups_array;
    }
    
    /** Get a group associated with the unique group_id passed
     * in the form of a JSONObject.
     * @param unknown $group_id
     * @return string. */
     public function getGroup($group_id)
     {
     $result = mysql_query("SELECT * FROM groups WHERE group_id = '$group_id'");
     $group_array = mysql_fetch_assoc($result);
     return $group_array;
     }
    
    /**
     * Storing new group. Returns group details.
     */
    public function storeGroup($owner_email, $network, $start_location, $end_location, 
    		$departure_date_time, $direction)
    {
    	
    	$result = mysql_query("INSERT INTO groups(owner_email, network, 
    			start_location, end_location, direction, departure_date_time, 
    			created_at) 
				VALUES('$owner_email', '$network', '$start_location', 
    			'$end_location', '$direction', '$departure_date_time', NOW())");
    	// check for successful store
    	if ($result)
    	{
    		// get group details
    		$id = mysql_insert_id(); // last inserted id
    		$result = mysql_query("SELECT * FROM groups WHERE group_id = $id");
    		// return group details
    		return mysql_fetch_array($result);
    	}
    	else
    	{
    		return false;
    	}
    }    
    
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $network, $password)
    {
    	$uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users(unique_id, name, email, network, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$network', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        if ($result)
        {
            // get user details 
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM users WHERE uid = $uid");
            // return user details
            return mysql_fetch_array($result);
        } 
        else 
        {
        	return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password)
    {
    	$result = mysql_query("SELECT * FROM users WHERE email = '$email'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /**
     * Check whether user exists or not
     */
    public function isUserExisted($email)
    {
    	$result = mysql_query("SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0)
        { 
            return true; // user existed
        } 
        else 
		{
			return false; // user not existed
		}
	}

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password)
    {
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password)
    {
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
		return $hash;
	}
}

?>