# Partii_OnOff_LED
ตัวอย่างแอพสั่งเปิดปิด LED อย่างง่ายเชื่อมต่อกับ NETPIE

![alt text](b1.jpg) <br>
![alt text](b2.jpg) <br>
![alt text](b3.png) <br>

ค่าที่นักพัฒนาจำเป็นต้องตั้งค่าให้ถูกต้องจาก NETPIE สำหรับแอพ android คือ <br>
private String appid = ""; //APP_ID <br>
private String key = ""; //KEY <br>
private String secret = ""; //SECRET <br>
	

ค่าที่นักพัฒนาจำเป็นต้องตั้งค่าให้ถูกต้องจาก NETPIE สำหรับ Nodemcu คือ <br>
const char* ssid     = ""; // wifi name <br>
const char* password = ""; // wifi password <br>
#define APPID   "" // NETPIE APP ID <br>
#define KEY     "" // NETPIE KEY <br>
#define SECRET  "" // NETPIE SECRET KEY <br>
	
	
	
ตัวอย่างการใช้งานนี้ผู้ใช้งานสามารถสั่งคำสั่งด้วยการพูดคำสั่งดังต่อไปนี้ <br>
	เปิดไฟสีแดง<br>
	เปิดไฟสีส้ม<br>
	เปิดไฟสีน้ำเงิน<br>
	ปิดไฟสีแดง<br>
	ปิดไฟสีส้ม<br>
	ปิดไฟสีน้ำเงิน<br>
	ปิดไฟทุกดวง<br>
	เปิดไฟทุกดวง<br>
