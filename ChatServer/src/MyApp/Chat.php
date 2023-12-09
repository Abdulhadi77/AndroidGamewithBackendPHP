<?php
namespace MyApp;

use Ratchet\MessageComponentInterface;
use Ratchet\ConnectionInterface;

require("DB_Connect.php");

class Chat implements MessageComponentInterface {
	//maybe we need to delete $clients from all the code
    protected $clients;
	protected $clientId;

    public function __construct() {		
        $this->clients = new \SplObjectStorage;
		$this->clientId = array();
    }

    public function onOpen(ConnectionInterface $conn) {
		$this->clients[$conn->resourceId] = $conn;
		$this->clientId[$conn->resourceId] = $conn;
    }

    public function onMessage(ConnectionInterface $from, $msg)
	{
		$msgData = json_decode($msg,true);
		$reponse = array();
		$target = $this->clientId[$from->resourceId];

		$sendReponse = true;
		$db = new DB();
		
		if ($msgData["cmd"] == "updateScore")
		{
			$reponse["cmd"] = "updateScore";			
			if ($db->updateScore($msgData["newScore"],$msgData["userName"]))
				$reponse["reponse"] = true;
			else
				$reponse["reponse"] = false;
		}
		else if ($msgData["cmd"] == "updateEmail")
		{
			$reponse["cmd"] = "updateEmail";
			if ($db->updateEmail($msgData["newEmail"],$msgData["userName"]))
				$reponse["reponse"] = true;
			else
				$reponse["reponse"] = false;
		}
		else if ($msgData["cmd"] == "updatePassword")
		{
			$reponse["cmd"] = "updatePassword";
			if ($db->updatePassword($msgData["newPassword"],$msgData["userName"]))
			{
				$reponse["reponse"] = true;
				$reponse["newPassword"] = $msgData["newPassword"];
			}
			else
				$reponse["reponse"] = false;
		}
		else if ($msgData["cmd"] == "updateUserName")
		{
			$reponse["cmd"] = "updateUserName";
			if ($db->updateUserName($msgData["newUserName"],$msgData["userName"]))
			{
				$reponse["reponse"] = true;
				$reponse["newUserName"] = $msgData["newUserName"];
			}
			else
				$reponse["reponse"] = false;
		}
		else if ($msgData["cmd"] == "loginRequest")
		{
			$reponse["cmd"] = "loginRequest";
			if ($db->searchForLogin($msgData["userName"],$msgData["password"]))
			{
				$reponse["reponse"] = true;
				$db->turnToOnline($msgData["userName"],$from->resourceId);
				$db->updateScore($msgData["score"],$msgData["userName"]);
				//$db->getAllData($msgData["userName"]);
				$reponse["pw"] = $db->getPWForProfile($msgData["userName"]);
				$reponse["userName"] = $msgData["userName"];
				$reponse["email"] = $db->getEmailForProfile($msgData["userName"]);
				$reponse["score"] = $db->getScoreForProfile($msgData["userName"]);
			}
			else
				$reponse["reponse"] = false;
							
		}
		else if ($msgData["cmd"] == "signupRequest")
		{	
			$reponse["cmd"] = "signupReponse";
			//we should check that this user is offline noow ,so he can log in
			if ($db->search($msgData["userName"]))
				$reponse["reponse"] = true;
			else if ($db->create($msgData["userName"],$msgData["email"],$msgData["password"]))
				$reponse["reponse"] = true;
			else
				$reponse["reponse"] = false;
			//turn to online
			if ($reponse["reponse"] == true)
			{
				$db->turnToOnline($msgData["userName"] ,$from->resourceId);
				$db->updateScore($msgData["score"],$msgData["userName"]);
				$reponse["userName"] = $db->getUserNameForProfile($msgData["userName"]);
				$reponse["pw"] = $db->getPWForProfile($msgData["userName"]);
				$reponse["email"] = $db->getEmailForProfile($msgData["userName"]);
				$reponse["score"] = $db->getScoreForProfile($msgData["userName"]);
			}
		}
		else if($msgData["cmd"] == "connectionRequest")
		{
			//we want to check that if the reciever is online & not busy
			//then send to the receiver who want to play with him (user name & id)
			//or send to the sender if the receiver can't play now (maybe he is busy or he is online)
			$userNameReceiver = $msgData["userNameReceiver"];
								
			//receiver is offline
			//$idReceiver = $db->getId($msgData["userNameReceiver"]);
			
			if (!$db->checkOnline($userNameReceiver))
			{
				//$target = $this->clientId[$idReceiver];
				$reponse["receiverIsOffline"] = true;
				$reponse["cmd"] = "connectionReponse";
				$reponse["reponse"] = false;
				$reponse["userNameReceiver"] = $userNameReceiver;
				echo "he is offline ";
			}
			// receiver is busy
			else if ($db->checkBusy($userNameReceiver))
			{
				//$target = $this->clientId[$idReceiver];
				$reponse["receiverIsBusy"] = true;
				$reponse["receiverIsOffline"] = false;
				$reponse["userNameReceiver"] = $userNameReceiver;
				$reponse["cmd"] = "connectionReponse";
				$reponse["reponse"] = false;
				echo "he is busy ";
			}
			else
			{
				echo "he is online and not busy ";
				//receiver is online and not busy
				$reponse["receiverIsOffline"] = false;
				$reponse["receiverIsBusy"] = false;
				$idReceiver = $db->getId($msgData["userNameReceiver"]);
				echo $msgData["userNameReceiver"];
				echo $idReceiver;
				if ($idReceiver)
				{
					echo "Receiver message";
					$target = $this->clientId[$idReceiver];
					$reponse["cmd"] = "connectionRequest";
					$reponse["idSender"] = $from->resourceId;
					$reponse["userNameSender"] = $msgData["userNameSender"];
				}
				else
				{
					//echo "Not AGREEEE";
					$reponse["cmd"] = "connectionReponse";
					$reponse["userNameReceiver"] = $userNameReceiver;
					$reponse["reponse"] = false;
				}
			}								
		}
		else if ($msgData["cmd"] == "connectionReponse")
		{
			//the sender send yes or no with id of the reciever
			//we want to send this msg to the receiver with the id of the sender
			$target = $this->clientId[$msgData["idReceiver"]];
			$reponse["cmd"] = "connectionReponse";
			$reponse["reponse"] = $msgData["reponse"];
			$reponse["idSender"] = $from->resourceId;
			//$userNameReceiver = $db->getUserNameById($from->resourceId);
			if ($msgData["reponse"] == true)
			{
				$db->turnToBusy($msgData["idReceiver"]);//id sender
				$db->turnToBusy($from->resourceId);		//id receiver
			}
			else
			{
				$reponse["receiverIsOffline"] = false;
				$reponse["receiverIsBusy"] = false;
				$reponse["didNotAccept"] = true;
			}					
		}
	
		else if ($msgData["cmd"] == "endGame")
		{
			$sendReponse = false;
			$db->endGame($msgData["userNameOfPlayer1"]);
			$db->endGame($msgData["userNameOfPlayer2"]);
		}
		else if ($msgData["cmd"] == "searchRandomPlayerRequest")
		{
			$reponse["cmd"] = "searchRandomPlayerRequest";
			$userNameForKeepLogin = $msgData["userName"];
			$randomUserName = $db->searchRandomPlayer($userNameForKeepLogin);
			
			
			if ($randomUserName != " ")
			{
				$ScoreOfRandomUserName = $db->getScoreForProfile($randomUserName);
				$reponse["scoreOfRandomPlayer"] = $ScoreOfRandomUserName;
				$reponse["userNameOfRandomPlayer"] = $randomUserName;							
				$reponse["reponse"] = true;
			}
			else
			{
				$reponse["reponse"] = false;
				$reponse["scoreOfRandomPlayer"] = " ";
				$reponse["userNameOfRandomPlayer"] = " ";
			}
		}
		else if ($msgData["cmd"] == "logoutRequest")
		{
			$reponse["cmd"] = "logoutRequest";
			echo $db->updateUserName($msgData["userName"],$msgData["userName"]) . "\n";
			echo $db->updatePassword($msgData["password"],$msgData["userName"]). "\n";								
			echo $msgData["password"]. "\n";
			echo $db->updateEmail($msgData["email"],$msgData["userName"]). "\n";
			echo $db->updateScore($msgData["score"],$msgData["userName"]). "\n";	
			if ($db->turnToOfflineByUserName($msgData["userNameKeepLogin"]))
				$reponse["reponse"] = true;
			else
				$reponse["reponse"] = false;						
		}
		else if ($msgData["cmd"] == "connectionRequestGameOnline")
		{
			echo "Receiver game online message";
			$target = $this->clientId[$msgData["idReceiver"]];
			$reponse["cmd"] = "connectionReponseGameOnline";
			$reponse["btnI"] = $msgData["btnJ"];
			$reponse["btnJ"] = $msgData["btnI"];
			$reponse["userNamePlayerPlayed"] = $msgData["userNamePlayerPlayed"];
			$reponse["endFirstHalf"] = $msgData["endFirstHalf"];
			$reponse["endSecondHalf"] = $msgData["endSecondHalf"];
		}
		else if ($msgData["cmd"] == "connectionRequestEscape")
		{
			$userNameReceiver = $msgData["userNameReceiver"];
			$idReceiver = $db->getId($userNameReceiver);
			if ($idReceiver)
			{
				$target = $this->clientId[$idReceiver];
				$reponse["cmd"] = "connectionRequestEscape";
				$reponse["userNamePlayerEscaped"] = $msgData["userNamePlayerEscaped"];
			}
			else
			{
				//$reponse["cmd"] = "connectionReponseGameOnline";
				$reponse["reponse"] = false;
			}
		}
		
		if ($sendReponse){
			//if i can send (because the receiver's mobile in turn off or if the receiver is not connect to wifi or ......)
				$target->send(json_encode($reponse));
		}
	}

    public function onClose(ConnectionInterface $conn) {
        // connexion fermé, on se débarasse du client
		$db = new DB();
		$db->turnToOffline($conn->resourceId);
        $this->clients->detach($conn);
        echo "L'utilisateur {$conn->resourceId} s'est déconnecté\n";
		
    }

    public function onError(ConnectionInterface $conn, \Exception $e) {
		$db = new DB();
		$db->turnToOffline($conn->resourceId);
        $this->clients->detach($conn);
        echo "Une erreur s'est produite : {$e->getMessage()}\n";
        $conn->close();
    }
	
}

?>