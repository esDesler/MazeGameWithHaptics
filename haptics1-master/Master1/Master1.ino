// M0,1,50,100,100;M1,1,50,100,100;M2,1,50,100,100;M3,1,50,100,100;

#include "MyMotor.h"

#define motor0Pin 5
#define motor1Pin 6
#define motor2Pin 9
#define motor3Pin 10

char delim = ',';
char end_delim = '\n';

MyMotor motor0("M0", motor0Pin, 0, 0, 0, 0);
MyMotor motor1("M1", motor1Pin, 0, 0, 0, 0);
MyMotor motor2("M2", motor2Pin, 0, 0, 0, 0);
MyMotor motor3("M3", motor3Pin, 0, 0, 0, 0);

// Substrings in "String"
String motorId;
String motorStatus;
String vibrationIntensity;
String onTime;
String offTime;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  motor0.displayStatus();
  motor1.displayStatus();
  motor2.displayStatus();
  motor3.displayStatus();
}

void loop() {
  // put your main code here, to run repeatedly:

  motor0.action();
  motor1.action();
  motor2.action();
  motor3.action();

  if (Serial.available())
  {

    // change 2 to count the number of chars left

    String allMotors = Serial.readStringUntil(end_delim);

    int fromIndex = 0;
    int toIndex = 0;
    
    char temp1[200];    
    allMotors.toCharArray(temp1, 200);
    // Serial.println(countChars(temp1, ';'));

    for (int i = 0; i < countChars(temp1, ';'); i++) {

      toIndex = allMotors.indexOf(';', fromIndex);

      String currentMotorString = allMotors.substring(fromIndex, toIndex);

      processMotor(currentMotorString);

      fromIndex = toIndex + 1;

    }
  }
}

void processMotor(String currentMotorString) {

  String subStr = "";
  int fromIndex = 0;
  int toIndex = 0;

  toIndex = currentMotorString.indexOf(',', fromIndex);
  subStr = currentMotorString.substring(fromIndex, toIndex);
  motorId = subStr;
  fromIndex = toIndex + 1;

  toIndex = currentMotorString.indexOf(',', fromIndex);
  subStr = currentMotorString.substring(fromIndex, toIndex);
  motorStatus = subStr;
  fromIndex = toIndex + 1;

  toIndex = currentMotorString.indexOf(',', fromIndex);
  subStr = currentMotorString.substring(fromIndex, toIndex);
  vibrationIntensity = subStr;
  fromIndex = toIndex + 1;

  toIndex = currentMotorString.indexOf(',', fromIndex);
  subStr = currentMotorString.substring(fromIndex, toIndex);
  onTime = subStr;
  fromIndex = toIndex + 1;

  toIndex = currentMotorString.indexOf(',', fromIndex);
  subStr = currentMotorString.substring(fromIndex, toIndex);
  offTime = subStr;
  fromIndex = toIndex + 1;

  if (motorId == "M0")
  {
    motor0.update(motorStatus.toInt(), vibrationIntensity.toInt(), onTime.toInt(), offTime.toInt());
    // motor0.displayStatus();
  } else if (motorId == "M1") {
    motor1.update(motorStatus.toInt(), vibrationIntensity.toInt(), onTime.toInt(), offTime.toInt());
    // motor1.displayStatus();
  } else if (motorId == "M2") {
    motor2.update(motorStatus.toInt(), vibrationIntensity.toInt(), onTime.toInt(), offTime.toInt());
    // motor2.displayStatus();
  } else if (motorId == "M3") {
    motor3.update(motorStatus.toInt(), vibrationIntensity.toInt(), onTime.toInt(), offTime.toInt());
    // motor3.displayStatus();
  }
}

int countChars(char str[], char countThis)
{
  int i, count;
  for (i = 0, count = 0; str[i]; i++)
    count += (str[i] == countThis);
  return count;
}
