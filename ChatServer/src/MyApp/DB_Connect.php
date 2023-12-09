<?php
namespace MyApp;


/**
 * A class file to connect to database
 */
class DB {
	protected $connection ;
	private $enabled;
	private $un = "root";
	private $passWord = "";
	private $dbName = "thirtyone";
	private $hostName = "localhost";

    // constructor
    function __construct() {
		require_once __DIR__ . '/db_config.php';
		$this->connection = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD , DB_DATABASE);
		$this->enabled = true;
		if (!$this->connection) {
			$this->enabled = false;
			die("Connection failed: " . mysqli_connect_error());
		}	
    }

	public function __destruct(){
		mysqli_close($this->connection);
	}
	
	
	public function searchRandomPlayer($userNameForKeepLogin){
		if (!$this->enabled)
			return false;
		$done = false;
		while (!$done)
		{
		
			$sql_num_rows = "SELECT * FROM users";
			if ($result_num_rows = mysqli_query($this->connection,$sql_num_rows))
				$random = mysqli_num_rows($result_num_rows);
			$random_res = rand(1,$random);
			$sql = "SELECT * FROM users WHERE online=1 AND busy=0 AND userName <> '$userNameForKeepLogin' ORDER BY RAND() LIMIT $random_res";
			//echo $userName;
			if ($result = mysqli_query($this->connection,$sql)){
				$row = mysqli_fetch_row($result);
				$userNameFromDB = $row['1'];
				echo $row['1'];
				if (!($userNameForKeepLogin == $userNameFromDB))
					return $userNameFromDB;//userName
		}

		
	}
		
		
		return " ";
		//return $sql; //userName with random id
	}
	
	
	public function updateUserName($newUserName,$userName){
		if (!$this->enabled)
			return false;
		//maybe we need to use 'id' or we need to use a temp variable for userName in database
		$sql = "UPDATE users SET userName = '$newUserName' WHERE userName='$userName'";
		if (mysqli_query($this->connection, $sql)){
			echo "YEEEEEEEEEEEEEES";
			return true;
		}
		echo "NOOOOOOOOO";
		return false;
	}
	
	public function updatePassword($password,$userName){
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET password = '$password' WHERE userName='$userName'";
		if (mysqli_query($this->connection, $sql)){
			echo "YEEEEEEEEEEEEEES";
			return true;
		}
		echo "NOOOOOOOOO";
		return false;
	}
	
	public function updateEmail($email,$userName){
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET email = '$email' WHERE userName='$userName'";
		if (mysqli_query($this->connection, $sql)){
			echo "YEEEEEEEEEEEEEES";
			return true;
		}
		echo "NOOOOOOOOO";
		return false;
	}
	
	public function updateScore($score,$userName){
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET score = '$score'+score WHERE userName='$userName'";
		if (mysqli_query($this->connection, $sql)){
			echo "\n" . $userName . "score updated to :  " . $score . "\n";
			return true;
		}
		echo "NOOOOOOOOO";
		return false;
	}
	
	public function getPWForProfile($userName){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		if ($result = mysqli_query($this->connection,$sql)){
			$row = mysqli_fetch_row($result);
			$PasswordFromDB = $row['2'];
			return $PasswordFromDB;//pw
		}
	}
	
	public function getUserNameForProfile($userName){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		if ($result = mysqli_query($this->connection,$sql)){
			$row = mysqli_fetch_row($result);
			$userNameFromDB = $row['1'];
			return $userNameFromDB;//userName
		}
	}
	
	public function getEmailForProfile($userName){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		if ($result = mysqli_query($this->connection,$sql)){
			$row = mysqli_fetch_row($result);
			$emailFromDB = $row['3'];
			return $emailFromDB;//email
		}
	}
	
	public function getScoreForProfile($userName){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		if ($result = mysqli_query($this->connection,$sql)){
			$row = mysqli_fetch_row($result);
			$scoreFromDB = $row['4'];
			return $scoreFromDB;//score
		}
	}
	
	public function searchForLogin($userName , $password){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName ='$userName' AND password='$password' AND online = 0";
		$result = mysqli_query($this->connection , $sql);
		if (mysqli_num_rows($result) > 0)
			return true;
		return false;
	}
	
	public function search($userName){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		$result = mysqli_query($this->connection, $sql);
		if (mysqli_num_rows($result) > 0)
			return true;
		return false;
	}
	
	public function create($userName,$email,$password)
	{
		if (!$this->enabled)
			return false;
		$sql = "INSERT INTO users (userName , email , password) VALUES ('$userName' ,'$email' ,'$password' )";
		if (mysqli_query($this->connection, $sql)) 
			return true;
		return false;		
	}
	
	public function turnToOnline($userName, $id)
	{
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET id='$id' , online = 1 , busy = 0 WHERE userName='$userName'";
		if (mysqli_query($this->connection, $sql)) 
			return true;
		return false;
	}
	
	public function endGame($userName)
	{
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET busy= 0 WHERE userName='$userName'";
		if (mysqli_query($this->connection, $sql))
		{
				echo "\n" . $userName . " ended his Game" . "\n";
				return true;
		}			
			
		
		return false;
	}
	
	public function getId($userName)
	{
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		$result = mysqli_query($this->connection, $sql);
		if ($result)
		{
			while($row = mysqli_fetch_assoc($result))
			{
				if (($row['online'] == 1) && ($row['busy'] == 0))
					return $row['id'] ;
				return false;
			}
		}
		else 
			return false;
		/*
		if (mysqli_num_rows($result) > 0)
		{
			$result = mysql_fetch_array($result);
			return $result["id"];
		}
		return false;
		*/
	}
	
	public function getIdOnline($userName)
	{
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		$result = mysqli_query($this->connection, $sql);
		if ($result)
		{
			while($row = mysqli_fetch_assoc($result))
			{
				if (($row['online'] == 1))
					return $row['id'] ;
				return false;
			}
		}
		else 
			return false;
		/*
		if (mysqli_num_rows($result) > 0)
		{
			$result = mysql_fetch_array($result);
			return $result["id"];
		}
		return false;
		*/
	}
	
	public function turnToOfflineByUserName($userName){
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET online = 0 WHERE userName = '$userName'";
		if (mysqli_query($this->connection, $sql))
			return true;
		return false;
	}
	
	public function turnToOffline($userId)
	{
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET online = 0 WHERE id='$userId'";
		if (mysqli_query($this->connection, $sql)) 
			return true;
		return false;
	}
	
	public function turnToBusy($userId)
	{
		if (!$this->enabled)
			return false;
		$sql = "UPDATE users SET busy = 1 WHERE id='$userId'";
		if (mysqli_query($this->connection, $sql)) 
			return true;
		return false;
	}
	
	public function checkOnline($userName){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		if ($result = mysqli_query($this->connection,$sql)){
			$row = mysqli_fetch_row($result);
			$online = $row['5'];
			if ($online == 1)
				return true;
			else
				return false;
		}
		return false;
	}
	public function checkBusy($userName){
		if (!$this->enabled)
			return false;
		$sql = "SELECT * FROM users WHERE userName = '$userName'";
		if ($result = mysqli_query($this->connection,$sql)){
			$row = mysqli_fetch_row($result);
			$busy = $row['6'];
			if ($busy == 1)
				return true;
			else
				return false;
		}
		return false;
	}
}
?>