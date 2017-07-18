#include <MicroGear.h>
#include <ESP8266WiFi.h>
#include <stdlib.h>

// set pin numbers:
#define D0 16     //STATUS LED
#define D1 5     //RED LED
#define D2 4      //ORANGE LED
#define D3 0      //BLUE LED

#define STATUSLED  D0
#define REDLED  D1
#define ORANGELED  D2
#define BULELED  D3

const char* ssid     = ""; // wifi name
const char* password = ""; // wifi password

#define APPID   "" // NETPIE APP ID
#define KEY     "" // NETPIE KEY
#define SECRET  "" // NETPIE SECRET KEY

#define ALIAS   "partiiled" // device name
#define Target "partiiandroid"  // device target

WiFiClient client;
MicroGear microgear(client);

void onMsghandler(char *topic, uint8_t* msg, unsigned int msglen) 
{
  Serial.print("Incoming message --> ");
  Serial.print(topic);
  Serial.print(" : ");
  char strState[msglen];
  for (int i = 0; i < msglen; i++) 
  {
    strState[i] = (char)msg[i];
    Serial.print((char)msg[i]);
  }
  Serial.println();

  String stateStr = String(strState).substring(0, msglen);
  
  if(stateStr == "REDLED_ON") 
  {
    digitalWrite(REDLED, HIGH);
    microgear.chat(Target, "REDLED_ON");
  } 
  else if(stateStr == "ORANGELED_ON") 
  {
    digitalWrite(ORANGELED, HIGH);
    microgear.chat(Target, "ORANGELED_ON");
  }
  else if(stateStr == "BULELED_ON") 
  {
    digitalWrite(BULELED, HIGH);
    microgear.chat(Target, "BULELED_ON");
  }
  else if(stateStr == "REDLED_OFF") 
  {
    digitalWrite(REDLED, LOW);
    microgear.chat(Target, "REDLED_OFF");
  } 
  else if(stateStr == "ORANGELED_OFF") 
  {
    digitalWrite(ORANGELED, LOW);
    microgear.chat(Target, "ORANGELED_OFF");
  }
  else if(stateStr == "BULELED_OFF") 
  {
    digitalWrite(BULELED, LOW);
    microgear.chat(Target, "BULELED_OFF");
  }
  else if(stateStr == "ALLLED_OFF") 
  {
    digitalWrite(REDLED, LOW);
    digitalWrite(ORANGELED, LOW);
    digitalWrite(BULELED, LOW);
    microgear.chat(Target, "ALLLED_OFF");
  }
  else if(stateStr == "ALLLED_ON") 
  {
    digitalWrite(REDLED, HIGH);
    digitalWrite(ORANGELED, HIGH);
    digitalWrite(BULELED, HIGH);
    microgear.chat(Target, "ALLLED_ON");
  }
}

void onConnected(char *attribute, uint8_t* msg, unsigned int msglen) 
{
    Serial.println("Connected to NETPIE...");
    microgear.setAlias(ALIAS);
}

void setup() 
{
    /* Event listener */
    microgear.on(MESSAGE,onMsghandler);
    microgear.on(CONNECTED,onConnected);

    Serial.begin(115200);
    Serial.println("Starting...");

    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) 
    {
       delay(250);
       Serial.print(".");
    }

    Serial.println("WiFi connected");  
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

    microgear.init(KEY,SECRET,ALIAS);
    microgear.connect(APPID);

    pinMode(STATUSLED,OUTPUT);
    pinMode(REDLED,OUTPUT);
    pinMode(ORANGELED,OUTPUT);
    pinMode(BULELED,OUTPUT);

    digitalWrite(STATUSLED, LOW); // On status LED
    digitalWrite(REDLED, LOW);
    digitalWrite(ORANGELED, LOW);
    digitalWrite(BULELED, LOW);
}

void loop() 
{ 
  if(microgear.connected()) 
  {
    microgear.loop();
    //Serial.println("connect...");
  } 
  else 
  {
    Serial.println("connection lost, reconnect...");
    microgear.connect(APPID);
  }
  delay(250);
}
