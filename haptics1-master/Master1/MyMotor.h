#ifndef MyMotor_h
#define MyMotor_h

class MyMotor
{

    String motorId;
    int motorPin;     // the number of the Motor pin
    long onTime;     // milliseconds of on-time
    long offTime;    // milliseconds of off-time

    int vibrationIntensity;

    int motorStatus;

    // These maintain the current state
    int motorState;                 // motorState
    unsigned long previousMillis;   // will store last time motor was updated

    // Constructor - creates a motor with parameters
  public:
    MyMotor(String id, int pin, long on, long off, int status, int vibIntensity)
    {
      motorId = id;
      motorPin = pin;
      pinMode(motorPin, OUTPUT);

      motorStatus = status;
      vibrationIntensity = vibIntensity;

      onTime = on;
      offTime = off;

      motorState = LOW;
      previousMillis = 0;
    }

    void action()
    {

      // check to see if it's time to change the state of the motor

      if (motorStatus == 1) {
        unsigned long currentMillis = millis();

        if ((motorState == HIGH) && (currentMillis - previousMillis >= onTime))
        {
          motorState = LOW;  // Turn it off
          previousMillis = currentMillis;  // Remember the time
          digitalWrite(motorPin, motorState);  // Update the actual motor
        }
        else if ((motorState == LOW) && (currentMillis - previousMillis >= offTime))
        {
          motorState = HIGH;  // turn it on
          previousMillis = currentMillis;   // Remember the time

          int pwm = map(vibrationIntensity, 0, 100, 0, 150); // 168 is max for 3.3V
          analogWrite(motorPin, pwm);   // Update the actual motor
        }
      } else  {
        motorState = LOW;
        digitalWrite(motorPin, motorState);    // Update the actual motor
      }

    }

    void update(int status, int vibIntensity, int on, int off) {
      onTime = on;
      offTime = off;
      motorStatus = status;
      vibrationIntensity = vibIntensity;
    }

    void displayStatus() {
      Serial.print("\n---------");
      Serial.print("\nMOTOR ID:            ");
      Serial.print(motorId);
      Serial.print("\nMOTOR STATUS:        ");
      Serial.print(motorStatus);
      Serial.print("\nVIBRATION INTENSITY: ");
      Serial.print(vibrationIntensity);
      Serial.print("%");
      Serial.print("\nON TIME:             ");
      Serial.print(onTime);
      Serial.print(" ms");
      Serial.print("\nOFF_TIME_0:          ");
      Serial.print(offTime);
      Serial.print(" ms");

    }

};

#endif
