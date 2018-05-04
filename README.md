# Overview about Kerberos

## What is Kerberos ? 
Kerberos is an authentication protocol, and at the same time a KDC, that has become very popular. Originally designed at MIT, it is named after the three-headed dog in Greek mythology that guards the gates of Hades. Kerberos has gone through several versions. The general concept is shown in Figure 1 and the detailed interaction is shown in Figure 2. This Kerberos works as a Key Distribution Center (KDC). It provides authentication, key generation, and key distribution.



## Structure of the system 
   	The system below is structured into main 4 packages :
   	1. Database Package : It responsible for storing and getting any Data (arraylist(s)) . Such as users meta data , end servers, ports 
   	2. model Package :  It responsible for a static code that shared between objects . Because we implement don't repeat yourself (d.r.y) principle .
   	3. Services : Here Where the real work coming on. It include a services  classes 
   	4. Test  : The Test App .
   
   **Note :**
   To ensure quality of code and objects thinking , 
  			 we implement some design pattern like singleton , Factory pattern , MVC , Transfer Object pattern ,
    		 Data Access Object and Chain of Responsibility design patterns. 
   	     Also we use AES/CBC/PKCS5Padding Encryption 128-bit encryption algorithm
    
##   Contributors : 
    	1. Jasem Alsadi 
    	3. Mohammed Aman  
     
##   How to read the code  ? 
    	1. Start with the model package , read  each of them But start with the client then server . 
    	2. Then Go to to services package  . Start with service abstract class -> ASService ->TGSService->BobService
    	3. Then Go to database package . Read the port class -> Repository . 
    	4. In Repository class, you will find some methods like giveMe . It just to make the code more controllable , 
    			modular and ensure that one class responsible for storing information  
    	5. Enjoy ;)
        
        
